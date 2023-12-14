/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import edu.kit.kastel.sdq.artemis4j.api.ArtemisClientException;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.cli.arguments.Arguments;
import edu.kit.kastel.sdq.scorestats.cli.dialogue.OptionDialogue;
import edu.kit.kastel.sdq.scorestats.cli.dialogue.OptionsDialogue;
import edu.kit.kastel.sdq.scorestats.config.AutomaticFeedbackType;
import edu.kit.kastel.sdq.scorestats.config.AutomaticFeedbackTypeAssessmentFactory;
import edu.kit.kastel.sdq.scorestats.config.ReportBuilder;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.core.client.Artemis4JArtemisClient;
import edu.kit.kastel.sdq.scorestats.core.client.ArtemisClient;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileMapper;
import edu.kit.kastel.sdq.scorestats.input.GroupFileParser;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileParser.ConfigFileParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLI {
	private static final Logger logger = LoggerFactory.getLogger(CLI.class);

	public void run(String[] args) {

		Arguments arguments = new Arguments();
		try {
			JCommander.newBuilder().addObject(arguments).build().parse(args);
		} catch (ParameterException e) {
			e.getJCommander().usage();
			return;
		}

		ArtemisClient<AutomaticFeedbackType> client = new Artemis4JArtemisClient<>(arguments.host, new AutomaticFeedbackTypeAssessmentFactory());
		try {
			client.login(arguments.username, arguments.password);
		} catch (ArtemisClientException e) {
			logger.error("Failed to login!");
			logger.error(e.getMessage(), e);
			return;
		}

		List<Course> courses;
		try {
			courses = client.loadCourses();
		} catch (ArtemisClientException e) {
			logger.error("Failed to load courses!");
			logger.error(e.getMessage(), e);
			return;
		}
		courses.sort(Comparator.comparing(Course::getCourseId));

		try (Scanner scanner = new Scanner(System.in)) {
			Course course = courses.get(0);
			// only prompt if there is more than one course to select from
			if (courses.size() > 1) {
				OptionDialogue<Course> courseDialogue = new OptionDialogue<>(scanner, "Please select the course:",
					courses.stream().collect(Collectors.toMap(Course::getShortName, item -> item, (i1, i2) -> null, LinkedHashMap::new)));
				course = courseDialogue.prompt();
			}

			List<Exercise> exercises;
			try {
				exercises = course.getExercises();
			} catch (ArtemisClientException e) {
				logger.error("Error loading exercises.");
				logger.error(e.getMessage());
				return;
			}
			exercises.sort(Comparator.comparing(Exercise::getExerciseId));
			OptionsDialogue<Exercise> exerciseDialogue = new OptionsDialogue<>(scanner, "Please select one or more exercises (separated by comma).",
					exercises.stream().collect(Collectors.toMap(Exercise::getShortName, item -> item, (i1, i2) -> null, LinkedHashMap::new)));

			List<Exercise> selectedExercises = exerciseDialogue.prompt();

			try {
				arguments.outDir.mkdirs();
			} catch (SecurityException e) {
				logger.error("Could not access the directory '%s'".formatted(arguments.outDir));
				return;
			}

			Map<Exercise, ExerciseConfig> configs;
			try {
				configs = new ConfigFileMapper().mapConfigFiles(selectedExercises, arguments.configsDir);
			} catch (ConfigFileParserException e) {
				switch (e.getError()) {
				case PARSING_FAILED:
					logger.error("Failed to parse config file '%s':".formatted(e.getFile().getName()));
					return;
				case NOT_ALLOWED:
					logger.error("The config file '%s' is not allowed for the exercise '%s':".formatted(e.getFile().getName(), e.getExercise().getShortName()));
					return;
				default:
					return;
				}
			}

			GroupFileParser parser = new GroupFileParser();
			Map<String, Set<String>> groups = new HashMap<>();
			File[] groupFiles = arguments.groupsDir == null ? new File[0] : arguments.groupsDir.listFiles();
			for (File file : groupFiles) {
				Set<String> students;
				try {
					students = parser.parse(file);
				} catch (FileNotFoundException e) {
					continue;
				}

				groups.put(file.getName().replaceFirst("[.][^.]+$", ""), students);
			}

			for (Map.Entry<Exercise, ExerciseConfig> entry : configs.entrySet()) {

				Exercise exercise = entry.getKey();
				ExerciseConfig config = entry.getValue();

				logger.info(" -------------------- %s --------------------".formatted(exercise.getShortName()));

				logger.info("Loading data...");
				Assessments<AutomaticFeedbackType> assessments;
				try {
					assessments = client.loadAssessments(exercise, config);
				} catch (ArtemisClientException e) {
					logger.error("Error while loading data.");
					logger.error(e.getMessage());
					return;
				}

				logger.info("Creating course report...");

				new ReportBuilder().createReport(arguments, course, exercise, config, assessments, null)
						.writeToFile(new File(arguments.outDir, course.getShortName() + "_" + exercise.getShortName() + ".txt"));

				for (Map.Entry<String, Set<String>> tutorial : groups.entrySet()) {
					logger.info("Creating report for: '%s'".formatted(tutorial.getKey()));
					new ReportBuilder().createReport(arguments, course, exercise, config, assessments, tutorial.getValue())
							.writeToFile(new File(arguments.outDir, tutorial.getKey() + "_" + exercise.getShortName() + ".txt"));

				}
			}

			logger.info("Finished!");
		}
	}
}
