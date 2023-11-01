package edu.kit.kastel.sdq.scorestats.config;

import com.beust.jcommander.Parameter;

public class Arguments {

    @Parameter(names = { "-host", "-h" }, description = "Artemis host", required = true)
    public String host;

    @Parameter(names = { "-user", "-u" }, description = "Artemis user name", required = true)
    public String username;

    @Parameter(names = { "-password", "-p" }, description = "Artemis password", password = true, required = true)
    public String password;

    @Parameter(names = { "-inputDir",
            "-i" }, description = "Input directory. Expects the exercise configs and tutorial configs.", required = true)
    public String inputDir;

    @Parameter(names = { "-outputDir", "-o" }, description = "The output directory.", required = true)
    public String outDir;

    @Parameter(names = { "-frequencyCount", "-f" }, description = "How many elements should be listed.")
    public int frequencyCount = 10;

}
