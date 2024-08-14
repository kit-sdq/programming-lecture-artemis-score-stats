/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.output.ratio;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class PercentageBar extends RatioOutput {

    // width in number of characters
    private static final int WIDTH = 10;
    private static final String FILLED = "■";
    private static final String EMPTY = "□";

    public PercentageBar(int numerator, int denominator) {
        super(numerator, denominator, 0);
    }

    public PercentageBar(double numerator, int denominator) {
        super(numerator, denominator, 0);
    }

    @Override
    public String print() {
        long filledCount = Math.min(Math.round(this.numerator / this.denominator * WIDTH), WIDTH);
        long emptyCount = WIDTH - filledCount;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < filledCount; i++) {
            builder.append(FILLED);
        }
        for (int i = 0; i < emptyCount; i++) {
            builder.append(EMPTY);
        }

        return builder.toString();
    }
}
