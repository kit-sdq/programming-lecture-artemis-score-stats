/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

/**
 * A report visitor calculating the average score.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class ScoreAverage implements ReportAverageVisitor<Assessment> {
    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public double summand(Assessment value, ReportData data) {
        return value.calculateTotalPoints();
    }

    @Override
    public double max(ReportData data) {
        return data.exercise().getMaxPoints();
    }

}
