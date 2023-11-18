/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.kastel.sdq.artemis4j.api.ArtemisClientException;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.api.artemis.ExerciseStats;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Feedback;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Result;
import edu.kit.kastel.sdq.artemis4j.api.artemis.assessment.Submission;
import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.artemis4j.client.RestClientManager;
import edu.kit.kastel.sdq.artemis4j.grading.artemis.AnnotationDeserializer;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessment;
import edu.kit.kastel.sdq.scorestats.core.assessment.AssessmentFactory;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;

/**
 * An {@link ArtemisClient} using artemis4j.
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class Artemis4JArtemisClient<K> implements ArtemisClient<K> {

	private final String hostname;
	private final AssessmentFactory<K> assessmentFactory;
	private RestClientManager client;

	public Artemis4JArtemisClient(String hostname, AssessmentFactory<K> assessmentFactory) {
		this.hostname = hostname;
		this.assessmentFactory = assessmentFactory;
	}

	public void login(String username, String password) throws ArtemisClientException {
		this.client = new RestClientManager(this.hostname, username, password);
		this.client.login();
	}

	public boolean isReady() {
		return this.client != null && this.client.isReady();
	}

	public List<Course> loadCourses() throws ArtemisClientException {
		return this.client.getCourseArtemisClient().getCourses();
	}

	public ExerciseStats loadStats(Exercise exercise) throws ArtemisClientException {
		return this.client.getAssessmentArtemisClient().getStats(exercise);
	}

	public Assessments<K> loadAssessments(Exercise exercise, ExerciseConfig config)
			throws ArtemisClientException {

		List<Submission> submissions = this.client.getSubmissionArtemisClient().getSubmissions(exercise);

		AnnotationDeserializer deserializer = null;
		if (config != null) {
			deserializer = new AnnotationDeserializer(config.getIMistakeTypes());
		}

		Map<String, Assessment<K>> assessments = new HashMap<>(submissions.size());

		List<String> skippedStudents = new ArrayList<>();

		for (Submission submission : submissions) {
			Result result = submission.getLatestResult();
			List<Feedback> feedbacks = this.client.getAssessmentArtemisClient()
					.getFeedbacks(submission, result);
			feedbacks.forEach(Feedback::init);
			boolean success = loadDetailText(result, feedbacks);
			if (!success) {
				skippedStudents.add(submission.getParticipantIdentifier());
				continue;
			}

			List<IAnnotation> annotations = List.of();
			if (config != null) {
				try {
					annotations = deserializer.deserialize(feedbacks);
				} catch (IOException e) {
					throw new ArtemisClientException("Could not parse manual feedback", e);
				}
			}

			String id = submission.getParticipantIdentifier();
			Assessment<K> assessment = this.assessmentFactory.createAssessment(submission);
			assessment.init(submission, feedbacks, annotations);

			if (assessments.containsKey(id)) {
				System.err.println("Something went wrong: Duplicate student id");
			}

			assessments.put(id, assessment);
		}

		return new Assessments<K>(skippedStudents, assessments);
	}

	private boolean loadDetailText(Result result, List<Feedback> feedbacks) {
		for (Feedback feedback : feedbacks) {
			if (!feedback.hasLongFeedbackText()) {
				continue;
			}

			String longText;
			try {
				longText = this.client.getAssessmentArtemisClient().getLongFeedback(result.id, feedback);
			} catch (ArtemisClientException e) {
				return false;
			}

			feedback.setDetailTextComplete(longText);
		}

		return true;
	}

}
