package edu.kit.kastel.sdq.scorestats;

import edu.kit.kastel.sdq.scorestats.cli.CLI;

public class Application {
    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run(args);
    }
}
