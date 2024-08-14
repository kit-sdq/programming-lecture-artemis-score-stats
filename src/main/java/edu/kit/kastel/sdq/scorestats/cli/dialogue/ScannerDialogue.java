/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.cli.dialogue;

import java.util.Scanner;

/**
 * A CLI dialogue using a {@link Scanner}.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public abstract class ScannerDialogue<T> implements Dialogue<T> {
    protected final Scanner scanner;

    public ScannerDialogue(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public abstract T prompt();

    protected void print(String s) {
        System.out.println(s);
    }
}
