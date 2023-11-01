package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportFrequencyVisitor;

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
        return feedbackGroup.getFailedFeedbacks().stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    @Override
    public int n(ReportData<K> data) {
        return data.selectedAssessments().size();
    }

}
