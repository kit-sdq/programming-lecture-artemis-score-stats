package edu.kit.kastel.sdq.scorestats.output.report;

import edu.kit.kastel.sdq.scorestats.core.report.Report.FrequencyResult;

public class FeedbackFrequencyList extends FrequencyList<String> {

    public FeedbackFrequencyList(int indentationLevel, FrequencyResult<String> result) {
        super(indentationLevel, result);
    }

    public FeedbackFrequencyList(int indentationLevel, FrequencyResult<String> result, int itemsCount) {
        super(indentationLevel, result, itemsCount);
    }

    @Override
    protected String getLabel(String item) {
        return item;
    }
}
