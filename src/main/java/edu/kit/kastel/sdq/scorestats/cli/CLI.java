/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import edu.kit.kastel.sdq.artemis4j.ArtemisClientException;
import edu.kit.kastel.sdq.artemis4j.ArtemisNetworkException;
import edu.kit.kastel.sdq.artemis4j.client.ArtemisInstance;
import edu.kit.kastel.sdq.artemis4j.grading.ArtemisConnection;
import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.artemis4j.grading.Course;
import edu.kit.kastel.sdq.artemis4j.grading.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.ProgrammingExercise;
import edu.kit.kastel.sdq.artemis4j.grading.ProgrammingSubmission;
import edu.kit.kastel.sdq.artemis4j.grading.metajson.AnnotationMappingException;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.GradingConfig;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.InvalidGradingConfigException;
import edu.kit.kastel.sdq.scorestats.cli.arguments.Arguments;
import edu.kit.kastel.sdq.scorestats.cli.dialogue.OptionDialogue;
import edu.kit.kastel.sdq.scorestats.cli.dialogue.OptionsDialogue;
import edu.kit.kastel.sdq.scorestats.config.ReportBuilder;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileMapper;
import edu.kit.kastel.sdq.scorestats.input.GroupFileParser;
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

        var artemis = new ArtemisInstance(arguments.host);

        ArtemisConnection connection;
        try {
            connection = ArtemisConnection.connectWithUsernamePassword(artemis, arguments.username, arguments.password);
        } catch (ArtemisNetworkException e) {
            logger.error("Failed to login!");
            logger.error(e.getMessage(), e);
            return;
        }

        List<Course> courses;
        try {
            courses = new ArrayList<>(connection.getCourses());
        } catch (ArtemisClientException e) {
            logger.error("Failed to load courses!");
            logger.error(e.getMessage(), e);
            return;
        }
        courses.sort(Comparator.comparing(Course::getId));

        try (Scanner scanner = new Scanner(System.in)) {
            Course course = courses.getFirst();
            // only prompt if there is more than one course to select from
            if (courses.size() > 1) {
                OptionDialogue<Course> courseDialogue = new OptionDialogue<>(scanner, "Please select the course:",
                        courses.stream().collect(Collectors.toMap(Course::getShortName, item -> item, (i1, i2) -> null, LinkedHashMap::new)));
                course = courseDialogue.prompt();
            }

            List<ProgrammingExercise> exercises;
            try {
                exercises = new ArrayList<>(course.getProgrammingExercises());
            } catch (ArtemisClientException e) {
                logger.error("Error loading exercises.");
                logger.error(e.getMessage());
                return;
            }
            exercises.sort(Comparator.comparing(ProgrammingExercise::getId));
            OptionsDialogue<ProgrammingExercise> exerciseDialogue = new OptionsDialogue<>(scanner, "Please select one or more exercises (separated by comma).",
                    exercises.stream().collect(Collectors.toMap(Exercise::getShortName, item -> item, (i1, i2) -> null, LinkedHashMap::new)));

            List<ProgrammingExercise> selectedExercises = exerciseDialogue.prompt();

            try {
                arguments.outDir.mkdirs();
            } catch (SecurityException e) {
                logger.error("Could not access the directory '%s'".formatted(arguments.outDir));
                return;
            }

            Map<ProgrammingExercise, GradingConfig> configs = new HashMap<>();
            try {
                configs = new ConfigFileMapper().mapConfigFiles(selectedExercises, arguments.configsDir);
            } catch (InvalidGradingConfigException e) {
                logger.error("Failed to parse config file: %s".formatted(e.getMessage()));
            } catch (IOException e) {
                logger.error("Error while reading config files.");
                logger.error(e.getMessage());
                return;
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

            for (var entry : configs.entrySet()) {
                ProgrammingExercise exercise = entry.getKey();
                GradingConfig config = entry.getValue();

                logger.info(" -------------------- %s --------------------".formatted(exercise.getShortName()));

                logger.info("Loading data...");
                Assessments assessments;
                try {
                    assessments = this.loadAssessments(config, exercise);
                } catch (ArtemisNetworkException | AnnotationMappingException e) {
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

    private Assessments loadAssessments(GradingConfig config, ProgrammingExercise exercise) throws ArtemisNetworkException, AnnotationMappingException {
        Collection<ProgrammingSubmission> submissions = new ArrayList<>(exercise.fetchSubmissions(0, false));

        if (exercise.hasSecondCorrectionRound()) {
            submissions.addAll(exercise.fetchSubmissions(1, false));
        }

        Map<String, Assessment> assessments = HashMap.newHashMap(submissions.size());
        List<String> skippedStudents = new ArrayList<>();

        for (ProgrammingSubmission submission : submissions) {
            String studentId = submission.getParticipantIdentifier();
            Assessment assessment = submission.openAssessment(config).orElse(null);
            if (assessment == null) {
                skippedStudents.add(studentId);
                continue;
            }

            if (assessments.containsKey(studentId)) {
                logger.error("Something went wrong: The student id %s occurred multiple times.".formatted(studentId));
            }

            assessments.put(studentId, assessment);
        }

        return new Assessments(skippedStudents, assessments);
    }
}
