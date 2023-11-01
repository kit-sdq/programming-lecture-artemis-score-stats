package edu.kit.kastel.sdq.scorestats.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class StudentsFileParser {

    private static final String COMMENT = "#";

    public Set<String> parse(File file) throws FileNotFoundException, StudentFileParserException {
        List<String> lines = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
        }
        return this.parse(lines);
    }

    public Set<String> parse(List<String> lines) throws StudentFileParserException {
        if (lines == null || lines.isEmpty()) {
            throw new StudentFileParserException(StudentFileParserError.EMPTY_FILE);
        }

        Set<String> students = new HashSet<>(lines);
        for (String line : lines) {
            if (line.isBlank() || this.isComment(line)) {
                continue;
            }

            students.add(line.trim());
        }

        if (students.isEmpty()) {
            throw new StudentFileParserException(StudentFileParserError.NO_STUDENTS);
        }

        return students;
    }

    private boolean isComment(String line) {
        return line.trim().startsWith(COMMENT);
    }

    public class StudentFileParserException extends Exception {
        private StudentFileParserError error;

        StudentFileParserException(StudentFileParserError error) {
            this.error = error;
        }

        public StudentFileParserError getError() {
            return this.error;
        }
    }

    public enum StudentFileParserError {
        FILE_NOT_FOUND,
        EMPTY_FILE,
        NO_STUDENTS;
    }
}
