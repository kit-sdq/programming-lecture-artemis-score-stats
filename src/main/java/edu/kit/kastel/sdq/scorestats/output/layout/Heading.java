/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class Heading implements Output {

    private final int indentationLevel;
    private final String text;
    private final Object[] args;

    public Heading(int indentationLevel, String text, Object... args) {
        this.text = text;
        this.indentationLevel = indentationLevel;
        this.args = args;
    }

    @Override
    public String print() {
        StringBuilder builder = new StringBuilder();
        Output.indent(builder, this.indentationLevel);
        builder
                .append(this.text.formatted(this.args))
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        return builder.toString();
    }
}
