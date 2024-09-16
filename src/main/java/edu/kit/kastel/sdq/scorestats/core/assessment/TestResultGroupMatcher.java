/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.core.assessment;

import edu.kit.kastel.sdq.artemis4j.grading.TestResult;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
@FunctionalInterface
public interface TestResultGroupMatcher {
    boolean matches(TestResult testResult);
}
