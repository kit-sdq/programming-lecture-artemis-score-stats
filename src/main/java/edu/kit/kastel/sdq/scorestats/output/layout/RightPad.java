package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

public class RightPad implements Output {

    private final Output output;
    private final int indentationLevel;
    private final int width;

    public RightPad(int indentationLevel, int width, Output output) {
        this.indentationLevel = indentationLevel;
        this.width = width;
        this.output = output;
    }

    @Override
    public String print() {
        StringBuilder builder = new StringBuilder();
        Output.indent(builder, this.indentationLevel);

        String s = this.output.print();
        int paddingWidth = this.width - s.length();
        builder.append(s);
        for (int i = 0; i < paddingWidth; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

}
