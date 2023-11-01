package edu.kit.kastel.sdq.scorestats.output.ratio;

public class PercentageBar extends RatioOutput {

	// width in number of characters
	private static final int WIDTH = 10;
	private static final String FILLED = "■";
	private static final String EMPTY = "□";

	public PercentageBar(int numerator, int denominator) {
		super(numerator, denominator, 0);

		if (numerator > denominator) {
			throw new IllegalArgumentException("The numerator can't be larger as the denominator!");
		}
	}

	public PercentageBar(double numerator, int denominator) {
		super(numerator, denominator, 0);

		if (numerator > denominator) {
			throw new IllegalArgumentException("The numerator can't be larger as the denominator!");
		}
	}

	@Override
	public String print() {
		long filledCount = Math.round(this.numerator / this.denominator * WIDTH);
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
