/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.cli.arguments;

import java.io.File;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Verifies that the specified path is an existing directory.
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class ExistingDirectory implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        File dir = new File(value);
        try {
            if (!dir.exists()) {
                throw new ParameterException("The directory '%s' does not exist.".formatted(value));
            }
            if (!dir.isDirectory()) {
                throw new ParameterException("The directory '%s' is not a directory.".formatted(value));
            }
        } catch (SecurityException e) {
            throw new ParameterException("Could not access the directory '%s'.".formatted(value));
        }
    }
}
