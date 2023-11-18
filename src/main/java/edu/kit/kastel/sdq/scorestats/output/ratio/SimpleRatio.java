/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.ratio;

import java.util.Locale;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class SimpleRatio extends RatioOutput {

    private static final String FORMAT_FORMAT = "%%%sf/%%%sf";

    public SimpleRatio(int numerator, int denominator) {
        super(numerator, denominator);
    }

    public SimpleRatio(double numerator, int denominator) {
        super(numerator, denominator);
    }

    public SimpleRatio(double numerator, int denominator, int decimalPlaces) {
        super(numerator, denominator, decimalPlaces);
    }

    @Override
    public String print() {
        String floatFormat = "." + this.decimalPlaces;
        String format = FORMAT_FORMAT.formatted(floatFormat, floatFormat);
        return String.format(Locale.US, format, this.numerator, this.denominator);
    }
}
