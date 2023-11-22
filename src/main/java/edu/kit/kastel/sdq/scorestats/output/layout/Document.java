/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class Document implements Output {

	private final List<Output> outputs;

	public Document(Output... outputs) {
		this.outputs = new ArrayList<>();
		this.outputs.addAll(Arrays.asList(outputs));
	}

	public void append(Output... output) {
		this.outputs.addAll(Arrays.asList(output));
	}

	@Override
	public String print() {
		StringBuilder builder = new StringBuilder();

		for (Output output : this.outputs) {
			builder.append(output.print());
		}

		return builder.toString();
	}
}
