/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.scorestats.config.TestResultType;
import edu.kit.kastel.sdq.scorestats.core.assessment.TestResultGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportAverageVisitor;

/**
 * A report visitor calculating the average number of passed tests in a
 * {@link TestResultGroup} of a given key.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class TestResultGroupPassedAverage implements ReportAverageVisitor<Assessment> {
    private final TestResultType key;
    private final TestResultGroupResolver groupResolver;

    public TestResultGroupPassedAverage(TestResultType key, TestResultGroupResolver groupResolver) {
        this.key = key;
        this.groupResolver = groupResolver;
    }

    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public double summand(Assessment assessment, ReportData data) {
        TestResultGroup testResultGroup = this.groupResolver.resolve(this.key, assessment);
        return testResultGroup.getPassedFeedbacksCount();
    }

    @Override
    public double max(ReportData data) {
        if (data.selectedAssessments().isEmpty()) {
            return 0.0;
        }

        for (Assessment assessment : data.selectedAssessments()) {
            if (!assessment.getSubmission().isBuildFailed()) {
                return this.groupResolver.resolve(this.key, assessment).getFeedbackCount();
            }
        }
        return 0.0;
    }
}
