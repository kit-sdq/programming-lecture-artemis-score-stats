/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.client.FeedbackType;
import edu.kit.kastel.sdq.artemis4j.grading.TestResult;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public final class PrefixMatcher implements TestResultGroupMatcher {

    private final String regex;

    public PrefixMatcher(String regex) {
        this.regex = regex;
    }

    /**
     * Returns {@code true} if {@link TestResult#getTestName()} begins with the
     * prefix.
     *
     * @param testResult the test result
     * @return {@code true} if {@link TestResult#getTestName()} begins with the
     *         prefix
     */
    @Override
    public boolean matches(TestResult testResult) {
        if (testResult.getFeedbackType() == FeedbackType.MANUAL_UNREFERENCED) {
            throw new IllegalArgumentException();
        }

        return testResult.getTestName().matches(this.regex);
    }
}
