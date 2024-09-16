/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.output.report;

import edu.kit.kastel.sdq.artemis4j.grading.penalty.MistakeType;
import edu.kit.kastel.sdq.scorestats.core.report.Report.FrequencyResult;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class MistakeTypeFrequencyList extends FrequencyList<MistakeType> {

    private static final String FORMAT = "%s [%s]";

    public MistakeTypeFrequencyList(int indentationLevel, FrequencyResult<MistakeType> result, int itemsCount) {
        super(indentationLevel, result, itemsCount);
    }

    @Override
    protected String getLabel(MistakeType item) {
        return FORMAT.formatted(item.getButtonText(), item.getId());
    }
}
