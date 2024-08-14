/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.FeedbackType;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public final class PrefixMatcher implements FeedbackGroupMatcher {

    private final String regex;

    public PrefixMatcher(String regex) {
        this.regex = regex;
    }

    /**
     * Returns {@code true} if {@link Feedback#getTestName()} begins with the
     * prefix.
     *
     * @param feedback the feedback
     * @return {@code true} if {@link Feedback#getTestName()} begins with the prefix
     */
    @Override
    public boolean matches(Feedback feedback) {
        if (feedback.getFeedbackType() == FeedbackType.MANUAL_UNREFERENCED || !feedback.isTest()) {
            throw new IllegalArgumentException();
        }

        return feedback.getTestName().matches(this.regex);
    }
}
