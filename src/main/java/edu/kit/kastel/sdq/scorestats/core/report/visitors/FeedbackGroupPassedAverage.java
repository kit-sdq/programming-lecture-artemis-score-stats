/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

/**
 * A report visitor calculating the average number of passed feedbacks (i.e.
 * tests) in a {@link FeedbackGroup} of a given key.
 *
 * @param <K> the key of the {@link FeedbackGroup}
 * @author Moritz Hertler
 * @version 1.0
 */
public class FeedbackGroupPassedAverage<K> implements ReportAverageVisitor<K, Assessment<K>> {

    private final K key;

    public FeedbackGroupPassedAverage(K key) {
        this.key = key;
    }

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public double summand(Assessment<K> value, ReportData<K> data) {
        FeedbackGroup feedbackGroup = value.getFeedbackGroup(this.key);
        return feedbackGroup.getPassedFeedbacksCount();
    }

    @Override
    public double max(ReportData<K> data) {
        if (data.selectedAssessments().isEmpty()) {
            return 0;
        }

        for (Assessment<K> assessment : data.selectedAssessments()) {
            if (!assessment.getSubmission().isBuildFailed()) {
                return assessment.getFeedbackGroup(this.key).getFeedbackCount();
            }
        }
        return 0;
    }
}
