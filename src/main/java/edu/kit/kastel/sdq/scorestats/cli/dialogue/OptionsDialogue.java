/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.cli.dialogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * A CLI dialogue asking the user to select at least one option.
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class OptionsDialogue<T> extends ScannerDialogue<List<T>> {

	private static final String DELIMITER = ",";

	private final String text;
	private final Map<String, T> options;

	public OptionsDialogue(Scanner scanner, String text, Map<String, T> options) {
		super(scanner);
		this.text = text;
		this.options = options;
	}

	@Override
	public List<T> prompt() {
		List<Map.Entry<String, T>> optionLabels = new ArrayList<>(this.options.entrySet());

		this.print(text);
		StringJoiner joiner = new StringJoiner(System.lineSeparator());
		for (int i = 0; i < optionLabels.size(); i++) {
			String label = optionLabels.get(i).getKey();
			joiner.add("  (" + (i + 1) + ") " + label);
		}

		this.print(joiner.toString());
		List<T> elements;

		outer: while (true) {
			elements = new ArrayList<>();

			String input = this.scanner.nextLine();
			String[] split = input.split(DELIMITER);

			for (String s : split) {
				int selection;
				try {
					selection = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					this.print("'%s' is not a valid integer.".formatted(s));
					continue outer;
				}

				if (selection < 1 || selection > optionLabels.size()) {
					this.print("'%d' does not match one of the options.".formatted(selection));
					continue outer;
				}

				elements.add(optionLabels.get(selection - 1).getValue());
			}

			return elements;
		}
	}
}
