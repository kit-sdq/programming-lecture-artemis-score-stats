package edu.kit.kastel.sdq.scorestats.core.assessment;

import java.util.List;
import java.util.Map;

public record Assessments<K>(
		List<String> skippedStudents,
		Map<String, Assessment<K>> assessments) {
}
