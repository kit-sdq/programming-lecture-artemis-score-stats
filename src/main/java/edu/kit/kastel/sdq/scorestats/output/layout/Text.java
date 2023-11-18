/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class Text implements Output {

    private final String text;
    private final Object[] args;

    public Text(String text) {
        this.text = text;
        this.args = null;
    }

    public Text(String text, Object... args) {
        this.text = text;
        this.args = args;
    }

    @Override
    public String print() {
        if (args != null) {
            return this.text.formatted(this.args);
        } else {
            return this.text;
        }
    }
}
