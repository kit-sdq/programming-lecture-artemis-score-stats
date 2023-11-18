/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;

/**
 * An {@link Assessment} factory.
 * 
 * @param <K> see {@link Assessment}
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public interface AssessmentFactory<K> {
    Assessment<K> createAssessment(Submission submission);
}
