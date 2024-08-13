/* Licensed under EPL-2.0 2024. */
package edu.kit.kastel.sdq.scorestats.config;

import edu.kit.kastel.sdq.scorestats.core.assessment.PrefixMatcher;
import edu.kit.kastel.sdq.scorestats.core.assessment.TestResultGroupMatcher;

public enum TestResultType {
    MANDATORY("^\\(?(?i)MANDATORY(?-i)\\)?.*"), FUNCTIONAL("^\\(?(?i)FUNCTIONAL(?-i)\\)?.*"), MODELING_CHECK("^((?i)Graded )?Modeling-Check(?-i).*"),
    OPTIONAL_CHECK("^\\(?(?i)OPTIONAL(?-i)\\)?.*");

    private final String regex;

    TestResultType(String regex) {
        this.regex = regex;
    }

    public TestResultGroupMatcher matcher() {
        return new PrefixMatcher(this.regex);
    }
}
