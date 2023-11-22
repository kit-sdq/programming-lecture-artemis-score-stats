/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.client;

import edu.kit.kastel.sdq.artemis4j.api.ArtemisClientException;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Course;
import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.core.assessment.Assessments;

import java.util.List;

/**
 * A client to interact with the artemis api.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface ArtemisClient<K> {

	void login(String username, String password) throws ArtemisClientException;

	List<Course> loadCourses() throws ArtemisClientException;

	Assessments<K> loadAssessments(Exercise exercise, ExerciseConfig config) throws ArtemisClientException;
}
