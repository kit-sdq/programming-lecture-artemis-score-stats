/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.report;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import edu.kit.kastel.sdq.artemis4j.api.grading.IAnnotation;
import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class CustomPenaltyList implements Output {

	private static final String FORMAT = "%.1fP %s";

	private final List<IAnnotation> annotations;
	private final int itemsCount;
	private final int indentationLevel;

	public CustomPenaltyList(int indentationLevel, List<IAnnotation> annotations) {
		this.annotations = annotations;
		this.itemsCount = annotations.size();
		this.indentationLevel = indentationLevel;
	}

	public CustomPenaltyList(int indentationLevel, List<IAnnotation> annotations, int itemsCount) {
		this.annotations = annotations;
		this.itemsCount = itemsCount;
		this.indentationLevel = indentationLevel;
	}

	@Override
	public String print() {
		this.annotations.sort(Comparator.comparing((IAnnotation a) -> a.getCustomPenalty().get()).thenComparing((IAnnotation a) -> a.getCustomMessage().get()));
		Map<String, AtomicInteger> annotationOutput = new LinkedHashMap<>();
		for (var annotation : annotations) {
			String representation = String.format(Locale.US, FORMAT, annotation.getCustomPenalty().get(), annotation.getCustomMessage().get()).trim();
			representation = String.join(" ", representation.lines().toArray(String[]::new)).replace("\\s+", " ");
			annotationOutput.computeIfAbsent(representation, r -> new AtomicInteger(0)).addAndGet(1);
		}

		StringBuilder builder = new StringBuilder();
		int itemCount = 0;
		for (var representation : annotationOutput.entrySet()) {
			if (itemCount > this.itemsCount) {
				break;
			}
			Output.indent(builder, this.indentationLevel);
			builder.append(representation.getValue().get()).append("x: ").append(representation.getKey());
			builder.append(System.lineSeparator());
			itemCount++;
		}
		return builder.toString();
	}

}
