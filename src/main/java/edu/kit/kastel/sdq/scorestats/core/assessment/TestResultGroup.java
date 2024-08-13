/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kit.kastel.sdq.artemis4j.client.FeedbackType;
import edu.kit.kastel.sdq.artemis4j.grading.ProgrammingSubmission;
import edu.kit.kastel.sdq.artemis4j.grading.TestResult;

/**
 * A group of automatic {@link TestResult test results}. Whether a
 * {@link TestResult} belongs to this group is determined by the given
 * {@link TestResultGroupMatcher}.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class TestResultGroup {
    private final ProgrammingSubmission submission;
    private final TestResultGroupMatcher matcher;
    private final List<TestResult> passedFeedbacks;
    private final List<TestResult> failedFeedbacks;

    public TestResultGroup(TestResultGroupMatcher matcher, ProgrammingSubmission submission) {
        this.submission = submission;
        this.matcher = matcher;
        this.passedFeedbacks = new ArrayList<>();
        this.failedFeedbacks = new ArrayList<>();
    }

    private void addTestResult(TestResult result) {
        if (result.getFeedbackType() != FeedbackType.AUTOMATIC || !this.matcher.matches(result)) {
            return;
        }

        // TODO: should TestResult#getDto be public? Should one create a clean method
        // for the isPositive?
        if (result.getDto().positive() != null && result.getDto().positive()) {
            this.passedFeedbacks.add(result);
        } else {
            this.failedFeedbacks.add(result);
        }

    }

    public void addMatchingTestResults(Iterable<? extends TestResult> results) {
        for (TestResult testResult : results) {
            this.addTestResult(testResult);
        }
    }

    public int getFeedbackCount() {
        return this.passedFeedbacks.size() + this.failedFeedbacks.size();
    }

    public int getPassedFeedbacksCount() {
        return this.passedFeedbacks.size();
    }

    public List<TestResult> getFailedFeedbacks() {
        return Collections.unmodifiableList(this.failedFeedbacks);
    }

    public boolean hasPassed() {
        return !this.submission.isBuildFailed() && this.failedFeedbacks.isEmpty();
    }
}
