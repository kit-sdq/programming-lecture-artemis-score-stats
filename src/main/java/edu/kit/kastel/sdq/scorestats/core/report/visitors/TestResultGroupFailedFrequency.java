/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import java.util.List;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.artemis4j.grading.TestResult;
import edu.kit.kastel.sdq.scorestats.config.TestResultType;
import edu.kit.kastel.sdq.scorestats.core.assessment.TestResultGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportFrequencyVisitor;

/**
 * A report visitor calculating the failure count of each feedback of the
 * {@link TestResultGroup} with the given key.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class TestResultGroupFailedFrequency implements ReportFrequencyVisitor<Assessment, String> {
    private final TestResultType key;
    private final TestResultGroupResolver groupResolver;

    public TestResultGroupFailedFrequency(TestResultType key, TestResultGroupResolver groupResolver) {
        this.key = key;
        this.groupResolver = groupResolver;
    }

    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public List<String> count(Assessment assessment) {
        TestResultGroup feedbackGroup = this.groupResolver.resolve(this.key, assessment);
        return feedbackGroup.getFailedFeedbacks().stream().map(TestResult::getTestName).toList();
    }

    @Override
    public int max(ReportData data) {
        return data.selectedAssessments().size();
    }

}
