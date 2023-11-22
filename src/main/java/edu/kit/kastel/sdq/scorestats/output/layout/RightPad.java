/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class RightPad implements Output {

	private final Output output;
	private final int indentationLevel;
	private final int width;

	public RightPad(int indentationLevel, int width, Output output) {
		this.indentationLevel = indentationLevel;
		this.width = width;
		this.output = output;
	}

	@Override
	public String print() {
		StringBuilder builder = new StringBuilder();
		Output.indent(builder, this.indentationLevel);

		String s = this.output.print();
		builder.append(s);
		int paddingWidth = this.width - s.length();
		builder.append(" ".repeat(Math.max(0, paddingWidth)));
		return builder.toString();
	}
}
