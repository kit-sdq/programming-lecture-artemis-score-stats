/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.cli.dialogue;

/**
 * A CLI dialogue.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public interface Dialogue<T> {
    T prompt();
}
