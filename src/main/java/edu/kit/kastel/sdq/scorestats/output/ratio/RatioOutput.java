/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.ratio;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public abstract class RatioOutput implements Output {

	protected final double numerator;
	protected final double denominator;

	protected int decimalPlaces;

	protected RatioOutput(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.decimalPlaces = 0;
	}

	protected RatioOutput(double numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.decimalPlaces = 1;
	}

	protected RatioOutput(double numerator, int denominator, int decimalPlaces) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.decimalPlaces = decimalPlaces;
	}

	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

}
