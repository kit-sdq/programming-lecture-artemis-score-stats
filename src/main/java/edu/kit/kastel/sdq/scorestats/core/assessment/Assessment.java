/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;
import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;

/**
 * The automatic or semi-automatic assessment of the latest submission.
 * 
 * This is an abstraction over {@link IAnnotation annotations} and
 * {@link Feedback feedbacks} using {@link FeedbackGroup FeedbackGroups}.
 * 
 * @param <K> the type of key used to map the {@link FeedbackGroup
 *            FeedbackGroups}
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class Assessment<K> {

    private Submission submission;
    private List<IAnnotation> annotations;
    private final Map<K, FeedbackGroup> feedbackGroups;

    public Assessment() {
        this.feedbackGroups = new HashMap<>();
    }

    public void init(Submission submission, List<Feedback> feedbacks, List<IAnnotation> annotations) {
        this.submission = submission;
        this.annotations = new ArrayList<>(annotations);
        for (FeedbackGroup feedbackGroup : this.feedbackGroups.values()) {
            feedbackGroup.addMatchingFeedbacks(feedbacks);
        }
    }

    public double getScore() {
        return this.submission.getLatestResult().score;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public List<IAnnotation> getAnnotations() {
        return Collections.unmodifiableList(this.annotations);
    }

    public void addFeedbackGroup(K key, FeedbackGroup feedbackGroup) {
        this.feedbackGroups.put(key, feedbackGroup);
    }

    public FeedbackGroup getFeedbackGroup(K key) {
        return this.feedbackGroups.get(key);
    }
}
