package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;

public interface AssessmentFactory<K> {
    Assessment<K> createAssessment(Submission submission);
}
