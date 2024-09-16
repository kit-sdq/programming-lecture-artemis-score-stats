/* Licensed under EPL-2.0 2024. */
package edu.kit.kastel.sdq.scorestats.core.report.visitors;

import edu.kit.kastel.sdq.artemis4j.grading.Assessment;
import edu.kit.kastel.sdq.scorestats.config.TestResultType;
import edu.kit.kastel.sdq.scorestats.core.assessment.TestResultGroup;

@FunctionalInterface
public interface TestResultGroupResolver {
    TestResultGroup resolve(TestResultType key, Assessment assessment);
}
