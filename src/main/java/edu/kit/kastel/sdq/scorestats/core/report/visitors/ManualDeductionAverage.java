/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.grading.IRatingGroup;
import edu.kit.kastel.sdq.artemis4j.grading.artemis.AnnotationMapper;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

/**
 * A report visitor calculating the average manual deduction.
 * 
 * @param <K> see {@link Assessment}
 * @author Moritz Hertler
 * @version 1.0
 */
public class ManualDeductionAverage<K> implements ReportAverageVisitor<K, Assessment<K>> {

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public double summand(Assessment<K> value, ReportData<K> data) {
        List<IRatingGroup> ratingGroups = data.config().getIRatingGroups();
        AnnotationMapper mapper = new AnnotationMapper(
                data.exercise(),
                value.getSubmission(),
                value.getAnnotations(),
                ratingGroups,
                null, null);
        double sum = 0;
        for (IRatingGroup ratingGroup : ratingGroups) {
            sum += mapper.calculatePointsForRatingGroup(ratingGroup).points();
        }
        return Math.abs(sum);
    }

    @Override
    public double max(ReportData<K> data) {
        double sum = 0;
        for (IRatingGroup group : data.config().getIRatingGroups()) {
            sum += group.getRange().first();

        }
        return Math.abs(sum);
    }
}
