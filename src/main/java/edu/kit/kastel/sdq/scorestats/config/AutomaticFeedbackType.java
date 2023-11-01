package edu.kit.kastel.sdq.scorestats.config;

import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroupMatcher;
import edu.kit.kastel.sdq.scorestats.core.assessment.PrefixMatcher;

public enum AutomaticFeedbackType {
    MANDATORY("(MANDATORY)"),
    FUNCTIONAL("(FUNCTIONAL)"),
    MODELLING_CHECK("Modeling-Check");

    private final String prefix;

    AutomaticFeedbackType(String prefix) {
        this.prefix = prefix;
    }

    public FeedbackGroupMatcher matcher() {
        return new PrefixMatcher(this.prefix);
    }
}
