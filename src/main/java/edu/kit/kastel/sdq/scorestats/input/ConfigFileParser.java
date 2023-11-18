/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.input;

import java.io.File;
import java.io.IOException;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.artemis4j.grading.config.GradingConfig;
import edu.kit.kastel.sdq.artemis4j.grading.config.JsonFileConfig;

/**
 * A parser for config files.
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class ConfigFileParser {

    public ExerciseConfig parse(Exercise exercise, File file) throws ConfigFileParserException {

        GradingConfig config = new JsonFileConfig(file);

        ExerciseConfig exerciseConfig;
        try {
            exerciseConfig = config.getExerciseConfig(exercise);
        } catch (IOException e) {
            throw new ConfigFileParserException(ConfigFileParserError.PARSING_FAILED, exercise, file);
        }

        if (!exerciseConfig.getAllowedExercises().contains(exercise.getExerciseId())) {
            throw new ConfigFileParserException(ConfigFileParserError.NOT_ALLOWED, exercise, file);
        }

        return exerciseConfig;
    }

    public class ConfigFileParserException extends Exception {
        private ConfigFileParserError error;
        private Exercise exercise;
        private File file;

        ConfigFileParserException(ConfigFileParserError error, Exercise exercise, File file) {
            this.error = error;
            this.exercise = exercise;
            this.file = file;
        }

        public ConfigFileParserError getError() {
            return this.error;
        }

        public Exercise getExercise() {
            return this.exercise;
        }

        public File getFile() {
            return this.file;
        }
    }

    public enum ConfigFileParserError {
        PARSING_FAILED,
        NOT_ALLOWED;
    }
}
