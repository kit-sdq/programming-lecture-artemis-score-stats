package edu.kit.kastel.sdq.scorestats.config;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.AssessmentFactory;
import edu.kit.kastel.sdq.scorestats.core.assessment.FeedbackGroup;

public class AutomaticFeedbackTypeAssessmentFactory implements AssessmentFactory<AutomaticFeedbackType> {

    @Override
    public Assessment<AutomaticFeedbackType> createAssessment(Submission submission) {
        Assessment<AutomaticFeedbackType> assessment = new Assessment<>();
        for (AutomaticFeedbackType type : AutomaticFeedbackType.values()) {
            assessment.addFeedbackGroup(type, new FeedbackGroup(type.matcher(), submission));
        }
        return assessment;
    }
}
