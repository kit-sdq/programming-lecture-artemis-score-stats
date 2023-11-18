/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public interface FeedbackGroupMatcher {
    boolean matches(Feedback feedback);
}
