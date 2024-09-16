/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.List;
import java.util.Map;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public record Assessments(List<String> skippedStudents, Map<String, Assessment> assessments) {
}
