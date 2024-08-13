/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.RatingGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

/**
 * A report visitor calculating the average manual deduction.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class ManualDeductionAverage implements ReportAverageVisitor<Assessment> {

    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public double summand(Assessment assessment, ReportData data) {
        double sum = 0.0;
        for (RatingGroup ratingGroup : data.config().getRatingGroups()) {
            sum += assessment.calculatePointsForRatingGroup(ratingGroup).score();
        }
        return Math.abs(sum);
    }

    @Override
    public double max(ReportData data) {
        double sum = 0.0;
        for (RatingGroup group : data.config().getRatingGroups()) {
            sum += group.getMinPenalty();

        }
        return Math.abs(sum);
    }
}
