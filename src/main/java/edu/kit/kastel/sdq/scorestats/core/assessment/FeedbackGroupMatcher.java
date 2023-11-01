package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;

public interface FeedbackGroupMatcher {
    boolean matches(Feedback feedback);
}
