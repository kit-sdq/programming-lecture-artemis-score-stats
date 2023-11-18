/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public interface Output {

    public static final String INDENTATION = " ";
    public static final int INDENTATION_SIZE = 2;

    String print();

    public static StringBuilder indent(StringBuilder builder, int indentationLevel) {
        for (int i = 0; i < indentationLevel; i++) {
            for (int j = 0; j < INDENTATION_SIZE; j++) {
                builder.append(INDENTATION);
            }
        }
        return builder;
    }
}
