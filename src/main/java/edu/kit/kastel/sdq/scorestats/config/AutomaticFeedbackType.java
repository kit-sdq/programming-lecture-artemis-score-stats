/* Licensed under EPL-2.0 2024. */
package edu.kit.kastel.sdq.scorestats.config;

import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroupMatcher;
import edu.kit.kastel.sdq.scorestats.core.assessment.PrefixMatcher;

public enum AutomaticFeedbackType {
	MANDATORY("^\\(?MANDATORY\\)?.*"),
	FUNCTIONAL("^\\(?FUNCTIONAL\\)?.*"),
	MODELING_CHECK("^(Graded )?Modeling-Check.*"),
	OPTIONAL_CHECK("^\\(?OPTIONAL\\)?.*");

	private final String regex;

	AutomaticFeedbackType(String regex) {
		this.regex = regex;
	}

	public FeedbackGroupMatcher matcher() {
		return new PrefixMatcher(this.regex);
	}
}
