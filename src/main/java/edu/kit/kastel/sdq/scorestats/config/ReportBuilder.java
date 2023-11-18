package edu.kit.kastel.sdq.scorestats.config;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.cli.arguments.Arguments;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;
import edu.kit.kastel.sdq.scorestats.core.report.Report;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.MistakeTypeFrequencyPerAnnotation;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.MistakeTypeFrequencyPerSubmission;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.CustomPenaltyAnnotationList;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.FeedbackGroupFailedFrequency;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.FeedbackGroupPassedAverage;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.FeedbackGroupPassedCount;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.ManualDeductionAverage;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.ParticipationReport;
import edu.kit.kastel.sdq.scorestats.core.report.visitors.ScoreAverage;
import edu.kit.kastel.sdq.scorestats.output.FileWriter;
import edu.kit.kastel.sdq.scorestats.output.Output;

public class ReportBuilder {

        private Output output;

        public ReportBuilder createReport(
                        Arguments arguments,
                        Course course,
                        Exercise exercise,
                        ExerciseConfig config,
                        Assessments<AutomaticFeedbackType> assessments,
                        Set<String> students) {

                Report<AutomaticFeedbackType> report = new Report<>(
                                course,
                                exercise,
                                config,
                                assessments,
                                students);

                ReportOutput output = new ReportOutput(
                                arguments,
                                course,
                                exercise,
                                report.accept(new ParticipationReport<>()),
                                report.count(new FeedbackGroupPassedCount<AutomaticFeedbackType>(
                                                AutomaticFeedbackType.MANDATORY)),
                                report.average(new ScoreAverage<>()),
                                report.average(
                                                new FeedbackGroupPassedAverage<AutomaticFeedbackType>(
                                                                AutomaticFeedbackType.FUNCTIONAL)),
                                report.average(
                                                new FeedbackGroupPassedAverage<AutomaticFeedbackType>(
                                                                AutomaticFeedbackType.MODELLING_CHECK)),
                                config == null ? null : report.average(new ManualDeductionAverage<>()),
                                report.frequency(
                                                new FeedbackGroupFailedFrequency<AutomaticFeedbackType>(
                                                                AutomaticFeedbackType.FUNCTIONAL)),
                                report.frequency(
                                                new FeedbackGroupFailedFrequency<AutomaticFeedbackType>(
                                                                AutomaticFeedbackType.MODELLING_CHECK)),
                                config == null ? null : report.frequency(new MistakeTypeFrequencyPerSubmission<>()),
                                config == null ? null : report.frequency(new MistakeTypeFrequencyPerAnnotation<>()),
                                config == null ? null : report.list(new CustomPenaltyAnnotationList<>()));

                this.output = output;
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
                        System.err.println("Error, could not write to file: %s".formatted(file.getAbsolutePath()));
                }
        }
}
