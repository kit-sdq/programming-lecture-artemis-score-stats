/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.config;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.artemis4j.grading.Course;
import edu.kit.kastel.sdq.artemis4j.grading.ProgrammingExercise;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.GradingConfig;
import edu.kit.kastel.sdq.scorestats.cli.arguments.Arguments;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.core.assessment.TestResultGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.CustomPenaltyAnnotationList;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.ManualDeductionAverage;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.MistakeTypeFrequencyPerAnnotation;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.MistakeTypeFrequencyPerSubmission;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.ParticipationReport;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.ScoreAverage;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.TestResultGroupFailedFrequency;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.TestResultGroupPassedAverage;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.TestResultGroupPassedCount;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.TestResultGroupResolver;
import edu.kit.kastel.sdq.scorestats.output.FileWriter;
import edu.kit.kastel.sdq.scorestats.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
    private Output output;

    public ReportBuilder createReport(Arguments arguments, Course course, ProgrammingExercise exercise, GradingConfig config, Assessments assessments,
            Set<String> students) {

        Report report = new Report(course, exercise, config, assessments, students);

        Map<Assessment, Map<TestResultType, TestResultGroup>> storedGroups = new IdentityHashMap<>();
        TestResultGroupResolver groupResolver = (key, assessment) -> {
            Map<TestResultType, TestResultGroup> groups = storedGroups.computeIfAbsent(assessment, k -> new EnumMap<>(TestResultType.class));

            return groups.computeIfAbsent(key, k -> {
                TestResultGroup group = new TestResultGroup(key.matcher(), assessment.getSubmission());
                group.addMatchingTestResults(assessment.getTestResults());
                return group;
            });
        };
        this.output = new ReportOutput(arguments, course, exercise, report.accept(new ParticipationReport()),
                report.count(new TestResultGroupPassedCount(TestResultType.MANDATORY, groupResolver)), //
                report.average(new ScoreAverage()), //
                report.average(new TestResultGroupPassedAverage(TestResultType.FUNCTIONAL, groupResolver)), //
                report.average(new TestResultGroupPassedAverage(TestResultType.MODELING_CHECK, groupResolver)), //
                report.average(new TestResultGroupPassedAverage(TestResultType.OPTIONAL_CHECK, groupResolver)), //
                config == null ? null : report.average(new ManualDeductionAverage()), //

                report.frequency(new TestResultGroupFailedFrequency(TestResultType.MANDATORY, groupResolver)), //
                report.frequency(new TestResultGroupFailedFrequency(TestResultType.FUNCTIONAL, groupResolver)), //
                report.frequency(new TestResultGroupFailedFrequency(TestResultType.MODELING_CHECK, groupResolver)), //
                report.frequency(new TestResultGroupFailedFrequency(TestResultType.OPTIONAL_CHECK, groupResolver)), //

                config == null ? null : report.frequency(new MistakeTypeFrequencyPerSubmission()), //
                config == null ? null : report.frequency(new MistakeTypeFrequencyPerAnnotation()), //
                config == null ? null : report.list(new CustomPenaltyAnnotationList()));//
        return this;
    }

    public Output getOutput() {
        return this.output;
    }

    public void writeToFile(File file) {
        FileWriter writer = new FileWriter(this.output, file);
        try {
            writer.write();
        } catch (IOException e) {
            logger.error(String.format("Error, could not write to file: %s%n", file.getAbsolutePath()));
        }
    }
}
