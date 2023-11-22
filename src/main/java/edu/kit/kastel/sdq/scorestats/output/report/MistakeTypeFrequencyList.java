/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.report;

import edu.kit.kastel.sdq.artemis4j.api.grading.IMistakeType;
import edu.kit.kastel.sdq.scorestats.core.report.Report.FrequencyResult;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class MistakeTypeFrequencyList extends FrequencyList<IMistakeType> {

	private static final String FORMAT = "%s [%s]";

	public MistakeTypeFrequencyList(int indentationLevel, FrequencyResult<IMistakeType> result, int itemsCount) {
		super(indentationLevel, result, itemsCount);
	}

	@Override
	protected String getLabel(IMistakeType item) {
		return FORMAT.formatted(item.getButtonText(null), item.getIdentifier());
	}
}
