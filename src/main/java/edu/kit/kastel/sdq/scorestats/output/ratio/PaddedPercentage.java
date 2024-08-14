/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.output.ratio;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class PaddedPercentage extends RatioOutput {

    private static final int MAX_WIDTH = 4;

    public PaddedPercentage(int numerator, int denominator) {
        super(numerator, denominator, 0);
    }

    public PaddedPercentage(double numerator, int denominator) {
        super(numerator, denominator, 0);
    }

    @Override
    public String print() {
        String output;
        if (this.denominator < 1) {
            output = "- %";
        } else {
            output = Math.round(this.numerator / this.denominator * 100) + "%";
        }
        int paddingWidth = MAX_WIDTH - output.length();
        return output + " ".repeat(Math.max(0, paddingWidth));
    }
}
