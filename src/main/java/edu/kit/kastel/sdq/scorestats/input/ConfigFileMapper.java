package edu.kit.kastel.sdq.scorestats.input;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileParser.ConfigFileParserException;

public class ConfigFileMapper {

    public Map<Exercise, ExerciseConfig> mapConfigFiles(List<Exercise> exercises, File directory)
            throws ConfigFileNotFoundException, ConfigFileParserException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("File must be a directory.");
        }

        ConfigFileParser parser = new ConfigFileParser();

        File[] files = directory.listFiles();

        Map<Exercise, ExerciseConfig> map = new HashMap<>();
        for (Exercise exercise : exercises) {
            for (File file : files) {
                if (!this.matchesExercise(file, exercise)) {
                    continue;
                }

                ExerciseConfig exerciseConfig = parser.parse(exercise, file);
                map.put(exercise, exerciseConfig);
            }

            if (!map.containsKey(exercise)) {
                throw new ConfigFileNotFoundException(exercise);
            }
        }

        return map;
    }

    private boolean matchesExercise(File file, Exercise exercise) {
        String fileName = file.getName().toLowerCase();
        String exerciseName = exercise.getShortName().toLowerCase();
        return fileName.contains(exerciseName);
    }

    public class ConfigFileNotFoundException extends Exception {
        private Exercise exercise;

        ConfigFileNotFoundException(Exercise exercise) {
            this.exercise = exercise;
        }

        public Exercise getExercise() {
            return this.exercise;
        }
    }
}
