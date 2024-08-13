/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.scorestats.config.TestResultType;
import edu.kit.kastel.sdq.scorestats.core.assessment.TestResultGroup;
import edu.kit.kastel.sdq.scorestats.core.report.Report.ReportData;
import edu.kit.kastel.sdq.scorestats.core.report.ReportCountVisitor;

/**
 * A report visitor counting the number of passed tests in a
 * {@link TestResultGroup} of a given key.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class TestResultGroupPassedCount implements ReportCountVisitor<Assessment> {
    private final TestResultType key;
    private final TestResultGroupResolver groupResolver;

    public TestResultGroupPassedCount(TestResultType key, TestResultGroupResolver groupResolver) {
        this.key = key;
        this.groupResolver = groupResolver;
    }

    @Override
    public Iterable<Assessment> iterable(ReportData data) {
        return data.selectedAssessments();
    }

    @Override
    public boolean count(Assessment assessment) {
        TestResultGroup feedbackGroup = this.groupResolver.resolve(this.key, assessment);
        return feedbackGroup.hasPassed();
    }
}
