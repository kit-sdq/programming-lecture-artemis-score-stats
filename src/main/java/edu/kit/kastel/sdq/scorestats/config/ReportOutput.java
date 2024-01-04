/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.config;

import java.util.List;
import java.util.Objects;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.artemis4j.api.grading.IMistakeType;
import edu.kit.kastel.sdq.scorestats.cli.arguments.Arguments;
import edu.kit.kastel.sdq.scorestats.core.Ratio;
import edu.kit.kastel.sdq.scorestats.core.report.Report.FrequencyResult;
import edu.kit.kastel.sdq.scorestats.output.Output;
import edu.kit.kastel.sdq.scorestats.output.layout.Divider;
import edu.kit.kastel.sdq.scorestats.output.layout.Document;
import edu.kit.kastel.sdq.scorestats.output.layout.Heading;
import edu.kit.kastel.sdq.scorestats.output.layout.LineSeparator;
import edu.kit.kastel.sdq.scorestats.output.ratio.RatioRow;
import edu.kit.kastel.sdq.scorestats.output.report.CustomPenaltyList;
import edu.kit.kastel.sdq.scorestats.output.report.FeedbackFrequencyList;
import edu.kit.kastel.sdq.scorestats.output.report.MistakeTypeFrequencyList;
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
	private final Ratio averagePassedOptionalChecks;
	private final Ratio averageManualDeduction;
	private final FrequencyResult<String> mandatoryFrequency;
	private final FrequencyResult<String> functionalFrequency;
	private final FrequencyResult<String> modellingFrequency;
	private final FrequencyResult<String> optionalFrequency;
	private final FrequencyResult<IMistakeType> annotationsFrequencyPerSubmission;
	private final FrequencyResult<IMistakeType> annotationsFrequencyPerAnnotations;
	private final List<IAnnotation> customAnnotations;
	private final Arguments arguments;

	public ReportOutput(Arguments arguments, Course course, Exercise exercise, Ratio participation, Ratio mandatoryPassed, Ratio averageScore,
			Ratio averagePassedFunctional, Ratio averagePassedModellingChecks, Ratio averagePassedOptionalChecks, Ratio averageManualDeduction,
			FrequencyResult<String> mandatoryFrequency, FrequencyResult<String> functionalFrequency, FrequencyResult<String> modellingFrequency,
			FrequencyResult<String> optionalFrequency, FrequencyResult<IMistakeType> annotationsFrequencyPerSubmission,
			FrequencyResult<IMistakeType> annotationsFrequencyPerAnnotations, List<IAnnotation> customAnnotations) {
		this.arguments = Objects.requireNonNull(arguments);
		this.course = Objects.requireNonNull(course);
		this.exercise = Objects.requireNonNull(exercise);
		this.participation = participation;
		this.mandatoryPassed = mandatoryPassed;
		this.averageScore = averageScore;
		this.averagePassedFunctional = averagePassedFunctional;
		this.averagePassedModellingChecks = averagePassedModellingChecks;
		this.averagePassedOptionalChecks = averagePassedOptionalChecks;
		this.averageManualDeduction = averageManualDeduction;
		this.mandatoryFrequency = mandatoryFrequency;
		this.functionalFrequency = functionalFrequency;
		this.modellingFrequency = modellingFrequency;
		this.optionalFrequency = optionalFrequency;
		this.annotationsFrequencyPerSubmission = annotationsFrequencyPerSubmission;
		this.annotationsFrequencyPerAnnotations = annotationsFrequencyPerAnnotations;
		this.customAnnotations = customAnnotations;
	}

	@Override
	public String print() {
		Document document = new Document(new LineSeparator(), new Heading(1, this.course.getTitle()), new Divider(), new LineSeparator(),

				new Heading(1, this.exercise.getTitle()), new Heading(1, "ZUSAMMENFASSUNG"));

		if (this.participation != null) {
			document.append(new SummaryLine("Teilnahmen", 1, new RatioRow((int) this.participation.numerator(), (int) this.participation.denominator()))

			);
		}

		if (this.mandatoryPassed != null) {
			document.append(
					new SummaryLine("Mandatory bestanden", 1, new RatioRow((int) this.mandatoryPassed.numerator(), (int) this.mandatoryPassed.denominator())));
		}

		if (this.averageScore != null) {
			document.append(new SummaryLine("Ø Punktzahl", 1, new RatioRow(this.averageScore.numerator(), (int) this.averageScore.denominator())));
		}

		if (this.averagePassedFunctional != null) {
			document.append(new SummaryLine("Ø bestandene functional Tests", 1,
					new RatioRow(this.averagePassedFunctional.numerator(), (int) this.averagePassedFunctional.denominator())));
		}

		if (this.averagePassedModellingChecks != null) {
			document.append(new SummaryLine("Ø bestandene Modeling-Checks", 1,
					new RatioRow(this.averagePassedModellingChecks.numerator(), (int) this.averagePassedModellingChecks.denominator())));
		}

		if (this.averagePassedOptionalChecks != null) {
			document.append(new SummaryLine("Ø bestandene Optional-Checks", 1,
					new RatioRow(this.averagePassedOptionalChecks.numerator(), (int) this.averagePassedOptionalChecks.denominator())));
		}

		document.append(new LineSeparator());

		if (this.averageManualDeduction != null) {
			document.append(new SummaryLine("Ø manueller Abzug", 1,
					new RatioRow(this.averageManualDeduction.numerator(), (int) this.averageManualDeduction.denominator())), new LineSeparator());
		}

		document.append(new LineSeparator());

		if (this.mandatoryFrequency != null) {
			document.append(new Heading(1, "HÄUFIG FEHLGESCHLAGENE MANDATORY TESTS"), new FeedbackFrequencyList(1, this.mandatoryFrequency),
					new LineSeparator());
		}

		if (this.functionalFrequency != null) {
			document.append(new Heading(1, "HÄUFIG FEHLGESCHLAGENE FUNCTIONAL TESTS"), new FeedbackFrequencyList(1, this.functionalFrequency),
					new LineSeparator());
		}

		if (this.modellingFrequency != null) {
			document.append(new Heading(1, "HÄUFIG FEHLGESCHLAGENE MODELING-CHECKS"),
					new FeedbackFrequencyList(1, this.modellingFrequency, this.arguments.outputLimit), new LineSeparator());
		}

		if (this.optionalFrequency != null) {
			document.append(new Heading(1, "HÄUFIG FEHLGESCHLAGENE OPTIONAL-CHECKS"),
					new FeedbackFrequencyList(1, this.optionalFrequency, this.arguments.outputLimit), new LineSeparator());
		}

		if (this.annotationsFrequencyPerSubmission != null) {
			document.append(new Heading(1, "HÄUFIGE KORREKTUR ANMERKUNGEN (mind. eine Anmerkung pro Abgabe)"),
					new MistakeTypeFrequencyList(1, this.annotationsFrequencyPerSubmission, this.arguments.outputLimit), new LineSeparator());

		}

		if (this.annotationsFrequencyPerAnnotations != null) {
			document.append(new Heading(1, "HÄUFIGE KORREKTUR ANMERKUNGEN (alle Anmerkungen)"),
					new MistakeTypeFrequencyList(1, this.annotationsFrequencyPerAnnotations, this.arguments.outputLimit), new LineSeparator());
		}
		if (this.customAnnotations != null) {
			document.append(new Heading(1, "INDIVIDUELLE KOMMENTARE"), new CustomPenaltyList(1, this.customAnnotations), new LineSeparator());
		}

		return document.print();
	}
}
