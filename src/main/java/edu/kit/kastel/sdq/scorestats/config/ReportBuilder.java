/* Licensed under EPL-2.0 2023. */
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportBuilder {
	private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
	private Output output;

	public ReportBuilder createReport(Arguments arguments, Course course, Exercise exercise, ExerciseConfig config,
			Assessments<AutomaticFeedbackType> assessments, Set<String> students) {

		Report<AutomaticFeedbackType> report = new Report<>(course, exercise, config, assessments, students);

		this.output = new ReportOutput(arguments, course, exercise, report.accept(new ParticipationReport<>()),
				report.count(new FeedbackGroupPassedCount<>(AutomaticFeedbackType.MANDATORY)), //
				report.average(new ScoreAverage<>()), //
				report.average(new FeedbackGroupPassedAverage<>(AutomaticFeedbackType.FUNCTIONAL)), //
				report.average(new FeedbackGroupPassedAverage<>(AutomaticFeedbackType.MODELLING_CHECK)), //
				config == null ? null : report.average(new ManualDeductionAverage<>()), //

				report.frequency(new FeedbackGroupFailedFrequency<>(AutomaticFeedbackType.MANDATORY)), //
				report.frequency(new FeedbackGroupFailedFrequency<>(AutomaticFeedbackType.FUNCTIONAL)), //
				report.frequency(new FeedbackGroupFailedFrequency<>(AutomaticFeedbackType.MODELLING_CHECK)), //

				config == null ? null : report.frequency(new MistakeTypeFrequencyPerSubmission<>()), //
				config == null ? null : report.frequency(new MistakeTypeFrequencyPerAnnotation<>()), //
				config == null ? null : report.list(new CustomPenaltyAnnotationList<>()));//
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
