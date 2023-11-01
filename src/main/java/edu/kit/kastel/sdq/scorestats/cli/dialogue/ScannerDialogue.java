package edu.kit.kastel.sdq.scorestats.cli.dialogue;

import java.util.Scanner;

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
