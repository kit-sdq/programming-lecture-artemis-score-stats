package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.FeedbackType;

public class PrefixMatcher implements FeedbackGroupMatcher{
    
    private final String prefix;

    public PrefixMatcher(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public boolean matches(Feedback feedback) {
       if (feedback.getFeedbackType() == FeedbackType.MANUAL_UNREFERENCED) {
            throw new IllegalArgumentException();
        }

        return feedback.getText().startsWith(this.prefix);
    }
}
