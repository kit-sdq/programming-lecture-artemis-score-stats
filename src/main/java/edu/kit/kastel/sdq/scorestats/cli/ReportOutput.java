package edu.kit.kastel.sdq.scorestats.cli;

import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.artemis4j.api.grading.IMistakeType;
import edu.kit.kastel.sdq.scorestats.config.Arguments;
import edu.kit.kastel.sdq.scorestats.core.Ratio;
import edu.kit.kastel.sdq.scorestats.core.report.Report.FrequencyResult;
import edu.kit.kastel.sdq.scorestats.output.Output;
import edu.kit.kastel.sdq.scorestats.output.layout.Divider;
import edu.kit.kastel.sdq.scorestats.output.layout.Document;
import edu.kit.kastel.sdq.scorestats.output.layout.Heading;
import edu.kit.kastel.sdq.scorestats.output.layout.LineSeparator;
import edu.kit.kastel.sdq.scorestats.output.ratio.RatioRow;
import edu.kit.kastel.sdq.scorestats.output.report.AnnotationFrequencyList;
import edu.kit.kastel.sdq.scorestats.output.report.CustomPenaltyList;
import edu.kit.kastel.sdq.scorestats.output.report.FeedbackFrequencyList;
import edu.kit.kastel.sdq.scorestats.output.report.SummaryLine;

public class ReportOutput implements Output {

        public static final int COLUMN_WIDTH = 35;

        private final Course course;
        private final Exercise exercise;
        private final Ratio participation;
        private final Ratio mandatoryPassed;
        private final Ratio averageScore;
        private final Ratio averagePassedFunctional;
        private final Ratio averagePassedModellingChecks;
        private final Ratio averageManualDeduction;
        private final FrequencyResult<String> functionalFrequency;
        private final FrequencyResult<String> modellingFrequency;
        private final FrequencyResult<IMistakeType> annotationsFrequencyPerSubmission;
        private final FrequencyResult<IMistakeType> annotationsFrequencyPerAnnotations;
        private final List<IAnnotation> customAnnotations;
        private Arguments arguments;

        public ReportOutput(
                        Course course,
                        Exercise exercise,
                        Ratio participation,
                        Ratio mandatoryPassed,
                        Ratio averageScore,
                        Ratio averagePassedFunctional,
                        Ratio averagePassedModellingChecks,
                        Ratio averageManualDeduction,
                        FrequencyResult<String> functionalFrequency,
                        FrequencyResult<String> modellingFrequency,
                        FrequencyResult<IMistakeType> annotationsFrequencyPerSubmission,
                        FrequencyResult<IMistakeType> annotationsFrequencyPerAnnotations,
                        List<IAnnotation> customAnnotations,
                        Arguments arguments) {
                this.course = course;
                this.exercise = exercise;
                this.participation = participation;
                this.mandatoryPassed = mandatoryPassed;
                this.averageScore = averageScore;
                this.averagePassedFunctional = averagePassedFunctional;
                this.averagePassedModellingChecks = averagePassedModellingChecks;
                this.averageManualDeduction = averageManualDeduction;
                this.functionalFrequency = functionalFrequency;
                this.modellingFrequency = modellingFrequency;
                this.annotationsFrequencyPerSubmission = annotationsFrequencyPerSubmission;
                this.annotationsFrequencyPerAnnotations = annotationsFrequencyPerAnnotations;
                this.customAnnotations = customAnnotations;
                this.arguments = arguments;
        }

        @Override
        public String print() {
                Document document = new Document(
                                new LineSeparator(),
                                new Heading(1, this.course.getTitle()),
                                new Divider(),
                                new LineSeparator(),

                                new Heading(1, this.exercise.getTitle()),
                                new Heading(1, "ZUSAMMENFASSUNG"),
                                new SummaryLine("Teilnahmen",
                                                1,
                                                new RatioRow((int) this.participation.numerator(),
                                                                (int) this.participation.denominator())),
                                new SummaryLine("Mandatory bestanden",
                                                1,
                                                new RatioRow((int) this.mandatoryPassed.numerator(),
                                                                (int) this.mandatoryPassed.denominator())),
                                new SummaryLine("Ø Punktzahl",
                                                1,
                                                // TODO use double denominator
                                                new RatioRow(this.averageScore.numerator(),
                                                                (int) this.averageScore.denominator())),
                                new SummaryLine("Ø bestandene functional Tests",
                                                1,
                                                new RatioRow(this.averagePassedFunctional.numerator(),
                                                                (int) this.averagePassedFunctional.denominator())),
                                new SummaryLine("Ø bestandene Modelling-Checks",
                                                1,
                                                new RatioRow(this.averagePassedModellingChecks.numerator(),
                                                                (int) this.averagePassedModellingChecks.denominator())),
                                new LineSeparator(),

                                new SummaryLine("Ø manueller Abzug",
                                                1,
                                                new RatioRow(this.averageManualDeduction.numerator(),
                                                                // TODO use double denominator
                                                                (int) this.averageManualDeduction.denominator())),
                                new LineSeparator(),
                                new LineSeparator(),

                                new Heading(1, "HÄUFIG FEHLGESCHLAGENE FUNCTIONAL TESTS"),
                                new FeedbackFrequencyList(1, this.functionalFrequency),
                                new LineSeparator(),

                                new Heading(1, "HÄUFIG FEHLGESCHLAGENE MODELLING-CHECKS"),
                                new FeedbackFrequencyList(1, this.modellingFrequency, this.arguments.frequencyCount),
                                new LineSeparator(),

                                new Heading(1, "HÄUFIGE KORREKTUR ANMERKUNGEN (mind. eine Anmerkung pro Abgabe)"),
                                new AnnotationFrequencyList(1, this.annotationsFrequencyPerSubmission,
                                                this.arguments.frequencyCount),
                                new LineSeparator(),

                                new Heading(1, "HÄUFIGE KORREKTUR ANMERKUNGEN (alle Anmerkungen)"),
                                new AnnotationFrequencyList(1, this.annotationsFrequencyPerAnnotations,
                                                this.arguments.frequencyCount),
                                new LineSeparator(),

                                new Heading(1, "INDIVIDUELLE KOMMENTARE"),
                                new CustomPenaltyList(1, this.customAnnotations),
                                new LineSeparator()

                );
                return document.print();
        }
}
