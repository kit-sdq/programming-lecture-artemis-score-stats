package edu.kit.kastel.sdq.scorestats.output.layout;

import edu.kit.kastel.sdq.scorestats.output.Output;

public class LineSeparator implements Output {

    @Override
    public String print() {
        return System.lineSeparator();
    }

}
