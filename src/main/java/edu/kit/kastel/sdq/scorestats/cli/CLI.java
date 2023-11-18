package edu.kit.kastel.sdq.scorestats.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
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

public class CLI {
	public void run(String[] args) {

		Arguments arguments = new Arguments();
		try {
			JCommander.newBuilder()
					.addObject(arguments)
					.build()
					.parse(args);
		} catch (ParameterException e) {
			e.getJCommander().usage();
			return;
		}

		ArtemisClient<AutomaticFeedbackType> client = new Artemis4JArtemisClient<>(arguments.host,
				new AutomaticFeedbackTypeAssessmentFactory());
		try {
			client.login(arguments.username, arguments.password);
		} catch (ArtemisClientException e) {
			System.err.println("Failed to login!");
			System.err.println(e.getMessage());
			return;
		}

		List<Course> courses;
		try {
			courses = client.loadCourses();
		} catch (ArtemisClientException e) {
			System.err.println("Failed to load courses!");
			System.err.println(e.getMessage());
			return;
		}
		Collections.sort(courses, Comparator.comparing(Course::getCourseId));

		try (Scanner scanner = new Scanner(System.in)) {

			OptionDialogue<Course> courseDialogue = new OptionDialogue<>(
					scanner,
					"Please select the course:",
					courses.stream()
							.collect(Collectors.toMap(Course::getShortName, item -> item, (i1, i2) -> null,
									LinkedHashMap::new)));
			Course course = courseDialogue.prompt();

			List<Exercise> exercises;
			try {
				exercises = course.getExercises();
			} catch (ArtemisClientException e) {
				System.err.println("Error loading exercises.");
				System.err.println(e.getMessage());
				return;
			}
			Collections.sort(exercises, Comparator.comparing(Exercise::getExerciseId));
			OptionsDialogue<Exercise> exerciseDialogue = new OptionsDialogue<>(scanner,
					"Please select one or more exercises (separated by comma).",
					exercises.stream()
							.collect(Collectors.toMap(Exercise::getShortName, item -> item, (i1, i2) -> null,
									LinkedHashMap::new)));

			List<Exercise> selectedExercises = exerciseDialogue.prompt();

			try {
				arguments.outDir.mkdirs();
			} catch (SecurityException e) {
				System.err.println("Could not access the directory '%s'".formatted(arguments.outDir));
				return;
			}

			Map<Exercise, ExerciseConfig> configs;
			try {
				configs = new ConfigFileMapper().mapConfigFiles(selectedExercises, arguments.configsDir);
			} catch (ConfigFileParserException e) {
				switch (e.getError()) {
					case PARSING_FAILED:
						System.err.println(
								"Failed to parse config file '%s':".formatted(e.getFile().getName()));
						return;
					case NOT_ALLOWED:
						System.err.println(
								"The config file '%s' is not allowed for the exercise '%s':"
										.formatted(e.getFile().getName(), e.getExercise().getShortName()));
						return;
					default:
						return;
				}
			}

			GroupFileParser parser = new GroupFileParser();
			Map<String, Set<String>> groups = new HashMap<>();
			File[] groupFiles = arguments.groupsDir == null ? new File[] {} : arguments.groupsDir.listFiles();
			for (File file : groupFiles) {
				Set<String> students;
				try {
					students = parser.parse(file);
				} catch (FileNotFoundException e) {
					continue;
				}

				groups.put(file.getName(), students);
			}

			for (Map.Entry<Exercise, ExerciseConfig> entry : configs.entrySet()) {

				Exercise exercise = entry.getKey();
				ExerciseConfig config = entry.getValue();

				System.out.println(" -------------------- %s --------------------".formatted(exercise.getShortName()));

				System.out.println("Loading data...");
				Assessments<AutomaticFeedbackType> assessments;
				try {
					assessments = client.loadAssessments(exercise, config);
				} catch (ArtemisClientException e) {
					System.err.println("Error while loading data.");
					System.err.println(e.getMessage());
					return;
				}

				System.out.println("Creating course report...");

				new ReportBuilder()
						.createReport(arguments, course, exercise, config, assessments, null)
						.writeToFile(new File(arguments.outDir, course.getShortName() + ".txt"));

				for (Map.Entry<String, Set<String>> tutorial : groups.entrySet()) {
					System.out.println("Creating report for: '%s'".formatted(tutorial.getKey()));
					new ReportBuilder()
							.createReport(arguments, course, exercise, config, assessments, tutorial.getValue())
							.writeToFile(new File(arguments.outDir, tutorial.getKey()));

				}
			}

			System.out.println("Finished!");
		}
	}
}
