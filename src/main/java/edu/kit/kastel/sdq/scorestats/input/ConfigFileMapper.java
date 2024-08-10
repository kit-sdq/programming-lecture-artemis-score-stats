/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.input;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.kastel.sdq.artemis4j.api.artemis.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.config.ExerciseConfig;
import edu.kit.kastel.sdq.scorestats.input.ConfigFileParser.ConfigFileParserException;

/**
 * Maps config files in a directory to their respective exercise.
 *
 * @author Moritz Hertler
 * @version 1.0
 */
public class ConfigFileMapper {

    /**
     * The last file in the given directory containing the short name of a given
     * exercise is mapped to that exercise.
     *
     * The matching is case-insensitive.
     *
     * If an exercise has no matching config file, the map will contain {@code null}
     * for that exercise.
     *
     * @param exercises the exercises
     * @param directory the directory containing the config files
     * @return the map of exercises to exercise configs
     * @throws ConfigFileParserException if a config file could not be parsed
     */
    public Map<Exercise, ExerciseConfig> mapConfigFiles(List<Exercise> exercises, File directory) throws ConfigFileParserException {
        if (directory != null && !directory.isDirectory()) {
            throw new IllegalArgumentException("File must be a directory.");
        }

        ConfigFileParser parser = new ConfigFileParser();

        File[] files = directory == null ? new File[] {} : directory.listFiles();

        Map<Exercise, ExerciseConfig> map = new HashMap<>();
        for (Exercise exercise : exercises) {
            map.put(exercise, null);
            for (File file : files) {
                if (!this.matchesExercise(file, exercise)) {
                    continue;
                }

                ExerciseConfig exerciseConfig = parser.parse(exercise, file);
                map.put(exercise, exerciseConfig);
            }
        }

        return map;
    }

    private boolean matchesExercise(File file, Exercise exercise) {
        String fileName = file.getName().toLowerCase();
        String exerciseName = exercise.getShortName().toLowerCase();
        return fileName.contains(exerciseName);
    }
}
