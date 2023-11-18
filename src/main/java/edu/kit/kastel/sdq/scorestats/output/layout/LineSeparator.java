/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class LineSeparator implements Output {

    @Override
    public String print() {
        return System.lineSeparator();
    }
}
