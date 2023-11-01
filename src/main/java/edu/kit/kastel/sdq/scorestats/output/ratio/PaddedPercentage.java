package edu.kit.kastel.sdq.scorestats.output.ratio;

public class PaddedPercentage extends RatioOutput {

    private static final int MAX_WIDTH = 4;

    public PaddedPercentage(int numerator, int denominator) {
        super(numerator, denominator, 0);

        if (numerator > denominator) {
            throw new IllegalArgumentException();
        }
    }

    public PaddedPercentage(double numerator, int denominator) {
        super(numerator, denominator, 0);

        if (numerator > denominator) {
            throw new IllegalArgumentException();
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
