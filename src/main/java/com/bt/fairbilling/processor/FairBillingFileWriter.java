package com.bt.fairbilling.processor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FairBillingFileWriter {

    private static final String FILE_OUTPUT_PATH = "output.txt";
    private Stream<String> lines;

    public FairBillingFileWriter(Stream<String> lines) {
        this.lines = lines;
    }

    /**
     * Write the session object details for each user to a text file.
     *
     * File content pattern:
     * <USER> <SESSION COUNT> <<TOTAL SESSION TIME IN SECONDS>
     * Ex:
     * ALICE99 4 240
     * CHARLIE 3 37
     *
     * @throws Exception
     */
    public void printReport() throws Exception {
        Path path = Path.of(FILE_OUTPUT_PATH);
        Files.write(path, (Iterable<String>) lines::iterator, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
