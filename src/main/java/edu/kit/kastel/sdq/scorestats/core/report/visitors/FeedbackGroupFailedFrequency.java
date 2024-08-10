/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportFrequencyVisitor;

/**
 * A report visitor calculating the failure count of each feedback of the
 * {@link FeedbackGroup} with the given key.
 *
 * @param <K> the key of the {@link FeedbackGroup}
 * @author Moritz Hertler
 * @version 1.0
 */
public class FeedbackGroupFailedFrequency<K> implements ReportFrequencyVisitor<K, Assessment<K>, String> {

    private final K key;

    public FeedbackGroupFailedFrequency(K key) {
        this.key = key;
    }

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public List<String> count(Assessment<K> value) {
        FeedbackGroup feedbackGroup = value.getFeedbackGroup(this.key);
        return feedbackGroup.getFailedFeedbacks().stream().map(Feedback::getTestName).toList();
    }

    @Override
    public int max(ReportData<K> data) {
        return data.selectedAssessments().size();
    }

}
