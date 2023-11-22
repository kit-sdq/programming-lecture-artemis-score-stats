/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats;

import edu.kit.kastel.sdq.scorestats.cli.CLI;

public final class Application {
	public static void main(String[] args) {
		CLI cli = new CLI();
		cli.run(args);
	}
}
