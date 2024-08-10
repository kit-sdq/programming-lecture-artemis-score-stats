/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public interface Output {

    String INDENTATION = " ";
    int INDENTATION_SIZE = 2;

    String print();

    static StringBuilder indent(StringBuilder builder, int indentationLevel) {
        builder.append(INDENTATION.repeat(INDENTATION_SIZE).repeat(Math.max(0, indentationLevel)));
        return builder;
    }
}
