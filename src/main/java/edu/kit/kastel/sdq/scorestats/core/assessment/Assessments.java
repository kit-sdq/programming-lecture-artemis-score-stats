/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.List;
import java.util.Map;

/**
 * @param <K> see {@link Assessment}
 * @author Moritz Hertler
 * @version 1.0
 */
public record Assessments<K>(
		List<String> skippedStudents,
		Map<String, Assessment<K>> assessments) {
}
