package edu.kit.kastel.sdq.scorestats.output.ratio;

import edu.kit.kastel.sdq.scorestats.output.Output;

public class RatioRow implements Output {

    private static final String DELIMITER = " ";

    private PercentageBar percentageBar;
    private PaddedPercentage paddedPercentage;
    private SimpleRatio simpleRatio;

    public RatioRow(int numerator, int denominator) {
        this.percentageBar = new PercentageBar(numerator, denominator);
        this.paddedPercentage = new PaddedPercentage(numerator, denominator);
        this.simpleRatio = new SimpleRatio(numerator, denominator);
    }

    public RatioRow(double numerator, int denominator) {
        this.percentageBar = new PercentageBar(numerator, denominator);
        this.paddedPercentage = new PaddedPercentage(numerator, denominator);
        this.simpleRatio = new SimpleRatio(numerator, denominator);
    }

    public RatioRow(double numerator, int denominator, int decimalPlaces) {
        this.percentageBar = new PercentageBar(numerator, denominator);
        this.paddedPercentage = new PaddedPercentage(numerator, denominator);
        this.simpleRatio = new SimpleRatio(numerator, denominator, decimalPlaces);
    }

    @Override
    public String print() {
        return this.percentageBar.print() + DELIMITER +
                this.paddedPercentage.print() + DELIMITER +
                this.simpleRatio.print();
    }
}
