/* Licensed under EPL-2.0 2023. */
package edu.kit.kastel.sdq.scorestats.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Moritz Hertler
 * @version 1.0
 */
public class FileWriter {

    private static final String CHARSET = "UTF-8";

    private Output output;
    private File file;

    public FileWriter(Output output, File file) {
        this.output = output;
        this.file = file;
    }

    public void write() throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file), CHARSET));
        w.write(this.output.print());
        w.close();
    }
}
