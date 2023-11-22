/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.cli.arguments;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.validators.PositiveInteger;

/**
 * The arguments.
 * 
 * @author Moritz Hertler
 * @version 1.0
 */
public class Arguments {

	@Parameter(names = { "-host", "-h" }, description = "Artemis host", required = true)
	public String host;

	@Parameter(names = { "-user", "-u" }, description = "Artemis user name", required = true)
	public String username;

	@Parameter(names = { "-password", "-p" }, description = "Artemis password", password = true, required = true)
	public String password;

	@Parameter(names = { "-output", "-o" }, description = "The output directory.", converter = FileConverter.class)
	public File outDir = new File("./stats");

	@Parameter(names = { "-groups",
			"-g" }, description = "The directory containing the group files. If no directory is specified, or if the directory is empty, only one report about all submissions will be generated.", converter = FileConverter.class, validateWith = ExistingDirectory.class)
	public File groupsDir;

	@Parameter(names = { "-configs",
			"-c" }, description = "The directory containing the grading config files. A config file must contain the short name of its corresponding exercise. For exercises without a config file all stats related to manual assessments will not be generated.", converter = FileConverter.class, validateWith = ExistingDirectory.class)
	public File configsDir;

	@Parameter(names = { "-outputLimit",
			"-l" }, description = "How many elements should at most be listed. This setting will be respected by all stats where the entirety of the result is not particularly significant.", validateWith = PositiveInteger.class)
	public int outputLimit = 0;
}
