package edu.kit.kastel.sdq.scorestats.output.report;

import edu.kit.kastel.sdq.artemis4j.api.grading.IMistakeType;
import edu.kit.kastel.sdq.scorestats.core.report.Report.FrequencyResult;

public class AnnotationFrequencyList extends FrequencyList<IMistakeType> {

    private static final String FORMAT = "%s [%s]";

    public AnnotationFrequencyList(int indentationLevel, FrequencyResult<IMistakeType> result) {
        super(indentationLevel, result);
    }

    public AnnotationFrequencyList(int indentationLevel, FrequencyResult<IMistakeType> result, int itemsCount) {
        super(indentationLevel, result, itemsCount);
    }

    @Override
    protected String getLabel(IMistakeType item) {
        return FORMAT.formatted(item.getButtonText(null), item.getIdentifier());
    }
}
