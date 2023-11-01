package edu.kit.kastel.sdq.scorestats.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
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
import edu.kit.kastel.sdq.scorestats.cli.dialogue.OptionDialogue;
import edu.kit.kastel.sdq.scorestats.cli.dialogue.OptionsDialogue;
import edu.kit.kastel.sdq.scorestats.config.Arguments;
import edu.kit.kastel.sdq.scorestats.config.AutomaticFeedbackType;
import edu.kit.kastel.sdq.scorestats.config.AutomaticFeedbackTypeAssessmentFactory;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.core.client.Artemis4JArtemisClient;
import edu.kit.kastel.sdq.scorestats.core.client.ArtemisClient;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileMapper;
import edu.kit.kastel.sdq.scorestats.input.StudentsFileParser;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileMapper.ConfigFileNotFoundException;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileParser.ConfigFileParserException;
import edu.kit.kastel.sdq.scorestats.input.StudentsFileParser.StudentFileParserException;

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

		try (Scanner scanner = new Scanner(System.in)) {

			OptionDialogue<Course> courseDialogue = new OptionDialogue<>(
					scanner,
					"Please select the course:",
					courses.stream()
							.collect(Collectors.toMap(Course::getShortName, item -> item)));
			Course course = courseDialogue.prompt();

			List<Exercise> exercises;
			try {
				exercises = course.getExercises();
			} catch (ArtemisClientException e) {
				System.err.println("Error loading exercises.");
				System.err.println(e.getMessage());
				return;
			}
			OptionsDialogue<Exercise> exerciseDialogue = new OptionsDialogue<>(scanner,
					"Please select one or more exercises (separated by comma).",
					exercises.stream()
							.collect(Collectors.toMap(Exercise::getShortName, item -> item)));
			List<Exercise> selectedExercises = exerciseDialogue.prompt();

			File inputFolder = new File(arguments.inputDir);
			try {
				if (!inputFolder.isDirectory()) {
					System.err.println("Input directory '%s' is not a directory.".formatted(arguments.inputDir));
					return;
				}
			} catch (SecurityException e) {
				System.err.println("Could not access the directory '%s'".formatted(arguments.inputDir));
				return;
			}

			File outputFolder = new File(arguments.outDir);
			try {
				outputFolder.mkdirs();
			} catch (SecurityException e) {
				System.err.println("Could not access the directory '%s'".formatted(arguments.outDir));
				return;
			}

			File[] files = inputFolder.listFiles();

			Map<Exercise, ExerciseConfig> configs;
			try {
				configs = new ConfigFileMapper().mapConfigFiles(selectedExercises, inputFolder);
			} catch (ConfigFileNotFoundException e) {
				return;
			} catch (ConfigFileParserException e) {
				return;
			}

			File tutorialsDir = null;
			for (File file : files) {
				if (file.isDirectory() && file.getName().equals("tutorials")) {
					tutorialsDir = file;
				}
			}
			if (tutorialsDir == null) {
				System.err.println("Did not find a tutorials folder. Quitting...");
				return;
			}

			StudentsFileParser parser = new StudentsFileParser();
			Map<String, Set<String>> tutorials = new HashMap<>();
			for (File file : tutorialsDir.listFiles()) {
				Set<String> students;
				try {
					students = parser.parse(file);
				} catch (FileNotFoundException e) {
					System.err.println("Error");
					return;
				} catch (StudentFileParserException e) {
					return;
				}

				tutorials.put(file.getName(), students);
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
						.createReport(course, exercise, config, assessments, null)
						.writeToFile(new File(outputFolder, course.getShortName()));

				for (Map.Entry<String, Set<String>> tutorial : tutorials.entrySet()) {
					System.out.println("Creating report for: '%s'".formatted(tutorial.getKey()));
					new ReportBuilder()
							.createReport(course, exercise, config, assessments, tutorial.getValue())
							.writeToFile(new File(outputFolder, tutorial.getKey()));

				}
			}

			System.out.println("Finished!");
		}
	}
}
