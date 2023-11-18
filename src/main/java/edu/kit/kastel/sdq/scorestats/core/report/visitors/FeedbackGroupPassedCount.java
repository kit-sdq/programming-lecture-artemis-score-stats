/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportCountVisitor;

/**
 * A report visitor counting the number of passed feedbacks (i.e.
 * tests) in a {@link FeedbackGroup} of a given key.
 * 
 * @param <K> the key of the {@link FeedbackGroup}
 * @author Moritz Hertler
 * @version 1.0
 */
public class FeedbackGroupPassedCount<K> implements ReportCountVisitor<K, Assessment<K>> {

    private final K key;

    public FeedbackGroupPassedCount(K key) {
        this.key = key;
    }

    @Override
    public Iterable<Assessment<K>> iterable(ReportData<K> data) {
        return data.selectedAssessments();
    }

    @Override
    public boolean count(Assessment<K> value) {
        FeedbackGroup feedbackGroup = value.getFeedbackGroup(this.key);
        return feedbackGroup.hasPassed();
    }
}
