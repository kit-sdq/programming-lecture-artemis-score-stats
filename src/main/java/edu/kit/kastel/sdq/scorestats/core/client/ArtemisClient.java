package edu.kit.kastel.sdq.scorestats.core.client;

import java.util.List;

import edu.kit.kastel.sdq.artemis4j.api.ArtemisClientException;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.api.artemis.ExerciseStats;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;

public interface ArtemisClient<K> {

	public void login(String username, String password) throws ArtemisClientException;

	public boolean isReady();

	public List<Course> loadCourses() throws ArtemisClientException;

	public ExerciseStats loadStats(Exercise exercise) throws ArtemisClientException;

	public Assessments<K> loadAssessments(Exercise exercise, ExerciseConfig config)
			throws ArtemisClientException ;
}
