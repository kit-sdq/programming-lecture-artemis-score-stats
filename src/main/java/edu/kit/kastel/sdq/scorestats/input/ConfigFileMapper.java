/* Licensed under EPL-2.0 2023-2024. */
package edu.kit.kastel.sdq.scorestats.input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import edu.kit.kastel.sdq.artemis4j.grading.Exercise;
import edu.kit.kastel.sdq.artemis4j.grading.ProgrammingExercise;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.GradingConfig;
import edu.kit.kastel.sdq.artemis4j.grading.penalty.InvalidGradingConfigException;

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
     * @throws InvalidGradingConfigException if a config file could not be parsed
     * @throws IOException                   if it failed to read the config file
     */
    public Map<ProgrammingExercise, GradingConfig> mapConfigFiles(Iterable<? extends ProgrammingExercise> exercises, File directory)
            throws IOException, InvalidGradingConfigException {
        if (directory != null && !directory.isDirectory()) {
            throw new IllegalArgumentException("File must be a directory.");
        }

        File[] files = directory == null ? new File[] {} : directory.listFiles();

        Map<ProgrammingExercise, GradingConfig> map = new HashMap<>();
        for (ProgrammingExercise exercise : exercises) {
            map.put(exercise, null);
            for (File file : files) {
                if (!this.matchesExercise(file, exercise)) {
                    continue;
                }

                map.put(exercise, GradingConfig.readFromString(Files.readString(file.toPath()), exercise));
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
