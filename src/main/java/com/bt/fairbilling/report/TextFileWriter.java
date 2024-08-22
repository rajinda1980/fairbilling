package com.bt.fairbilling.report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class TextFileWriter implements FileWriter {

    private static final String FILE_OUTPUT_PATH = "output.txt";

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
    public void printReport(Stream<String> lines) throws Exception {
        try {
            Path path = Path.of(FILE_OUTPUT_PATH);
            Files.write(path, (Iterable<String>) lines::iterator, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            throw new Exception("Failed to write report to file : " + e.getMessage());
        }
    }
}
