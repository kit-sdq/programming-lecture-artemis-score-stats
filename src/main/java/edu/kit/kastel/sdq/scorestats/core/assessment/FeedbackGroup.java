/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.FeedbackType;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;

/**
 * A group of automatic {@link Feedback feedbacks}. If a {@link Feedback}
 * belongs to this group is determined by the given
 * {@link FeedbackGroupMatcher}.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class FeedbackGroup {

    private final Submission submission;
    private final FeedbackGroupMatcher matcher;
    private final List<Feedback> passedFeedbacks;
    private final List<Feedback> failedFeedbacks;

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

        if (feedback.getPositive() != null && feedback.getPositive()) {
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
