package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

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
    public double n(ReportData<K> data) {
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
