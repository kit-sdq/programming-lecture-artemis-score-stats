/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.FeedbackType;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public final class PrefixMatcher implements FeedbackGroupMatcher {

	private final String prefix;

	public PrefixMatcher(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Returns {@code true} if {@link Feedback#getTestName()} begins with the prefix.
	 * 
	 * @param feedback the feedback
	 * @return {@code true} if {@link Feedback#getTestName()} begins with the prefix
	 */
	@Override
	public boolean matches(Feedback feedback) {
		if (feedback.getFeedbackType() == FeedbackType.MANUAL_UNREFERENCED || !feedback.isTest()) {
			throw new IllegalArgumentException();
		}

		return feedback.getTestName().startsWith(this.prefix);
	}
}
