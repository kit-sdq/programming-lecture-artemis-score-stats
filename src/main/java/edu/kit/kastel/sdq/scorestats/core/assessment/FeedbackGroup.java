package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.FeedbackType;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;

public class FeedbackGroup {

    private Submission submission;
    private FeedbackGroupMatcher matcher;
    private List<Feedback> passedFeedbacks;
    private List<Feedback> failedFeedbacks;

    public FeedbackGroup(FeedbackGroupMatcher matcher, Submission submission) {
        this.submission = submission;
        this.matcher = matcher;
        this.passedFeedbacks = new ArrayList<>();
        this.failedFeedbacks = new ArrayList<>();
    }

    public boolean addFeedback(Feedback feedback) {
        if (feedback.getFeedbackType() != FeedbackType.AUTOMATIC) {
            return false;
        }

        if (!matcher.matches(feedback)) {
            return false;
        }

        if (feedback.getPositive()) {
            this.passedFeedbacks.add(feedback);
        } else {
            this.failedFeedbacks.add(feedback);
        }

        return true;
    }

    public void addMatchingFeedbacks(Collection<Feedback> feedbacks) {
        for (Feedback feedback : feedbacks) {
            this.addFeedback(feedback);
        }
    }

    public int getFeedbackCount() {
        return this.passedFeedbacks.size() + this.failedFeedbacks.size();
    }

    public int getPassedFeedbacksCount() {
        return this.passedFeedbacks.size();
    }

    public List<Feedback> getPassedFeedbacks() {
        return Collections.unmodifiableList(this.passedFeedbacks);
    }

    public int getFailedFeedbacksCount() {
        return this.failedFeedbacks.size();
    }

    public List<Feedback> getFailedFeedbacks() {
        return Collections.unmodifiableList(this.failedFeedbacks);
    }

    public boolean hasPassed() {
        return !this.submission.isBuildFailed() && this.failedFeedbacks.size() == 0;
    }
}
