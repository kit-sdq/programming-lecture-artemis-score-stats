package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

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
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.width; i++) {
            builder.append(CHARACTER);
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}
