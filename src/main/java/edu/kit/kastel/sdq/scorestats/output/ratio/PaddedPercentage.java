/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.ratio;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class PaddedPercentage extends RatioOutput {

    private static final String ILLEGAL_RATIO_ERROR = "Error: The numerator %d is bigger than the denominator %d.";

    private static final int MAX_WIDTH = 4;

    public PaddedPercentage(int numerator, int denominator) {
        super(numerator, denominator, 0);

        if (numerator > denominator) {
            throw new IllegalArgumentException(ILLEGAL_RATIO_ERROR.formatted(numerator, denominator));
        }
    }

    public PaddedPercentage(double numerator, int denominator) {
        super(numerator, denominator, 0);

        if (numerator > denominator) {
            throw new IllegalArgumentException(ILLEGAL_RATIO_ERROR.formatted(numerator, denominator));
        }
    }

    @Override
    public String print() {
        String output = Math.round(this.numerator / this.denominator * 100) + "%";
        int paddingWidth = MAX_WIDTH - output.length();
        String padding = "";
        for (int i = 0; i < paddingWidth; i++) {
            padding += " ";
        }
        return output + padding;
    }
}
