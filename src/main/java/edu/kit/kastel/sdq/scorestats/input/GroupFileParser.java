/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Parser for group files.
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class GroupFileParser {

    private static final String COMMENT = "#";

    public Set<String> parse(File file) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
        }
        return this.parse(lines);
    }

    public Set<String> parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return Set.of();
        }

        Set<String> students = new HashSet<>(lines);
        for (String line : lines) {
            if (line.isBlank() || this.isComment(line)) {
                continue;
            }

            students.add(line.trim());
        }

        return students;
    }

    private boolean isComment(String line) {
        return line.trim().startsWith(COMMENT);
    }
}
