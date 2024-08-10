/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.cli.dialogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * A cli dialogue asking the user to select exactly one option.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class OptionDialogue<T> extends ScannerDialogue<T> {

    private final String text;
    private final Map<String, T> options;

    public OptionDialogue(Scanner scanner, String text, Map<String, T> options) {
        super(scanner);
        this.text = text;
        this.options = options;
    }

    @Override
    public T prompt() {
        List<Map.Entry<String, T>> optionLabels = new ArrayList<>(this.options.entrySet());

        this.print(text);
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        for (int i = 0; i < optionLabels.size(); i++) {
            String label = optionLabels.get(i).getKey();
            joiner.add("  (" + (i + 1) + ") " + label);
        }

        this.print(joiner.toString());

        while (true) {
            String input = this.scanner.nextLine();
            int selection;
            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                this.print("Please enter a valid integer.");
                continue;
            }

            if (selection < 1 || selection > optionLabels.size()) {
                this.print("Please enter a integer that matches one of the options.");
                continue;
            }

            return optionLabels.get(selection - 1).getValue();
        }
    }

}
