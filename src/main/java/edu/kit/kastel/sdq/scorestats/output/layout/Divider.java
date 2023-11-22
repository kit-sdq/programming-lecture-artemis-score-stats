/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class Divider implements Output {

	private static final int DEFAULT_WIDTH = 100;
	private static final char CHARACTER = '-';

	private final int width;

	public Divider() {
		this.width = DEFAULT_WIDTH;
	}

	public Divider(int width) {
		this.width = width;
	}

	@Override
	public String print() {
		return String.valueOf(CHARACTER).repeat(Math.max(0, this.width)) + System.lineSeparator();
	}
}
