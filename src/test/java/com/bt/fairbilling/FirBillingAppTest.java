package com.bt.fairbilling;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FirBillingAppTest {

    @AfterEach
    void cleanUp() throws Exception {
        Path filePath = Paths.get(TestConstants.OUTPUT_FILE_PATH);
        Files.delete(filePath);
        System.out.println("File is deleted");
    }

    /**
     * Value map for generating valid file
     * @return Argument list
     */
    static Stream<Arguments> shouldGenerateReport() {
        return Stream.of(
                Arguments.of(TestConstants.VALID_LOG_FILE_PATH),     // Generating report with correct information from a valid log file
                Arguments.of(TestConstants.VALID_INVALID_LOG_FILE_PATH),     // Generating report with correct information from a valid log file, which contains invalid records as well
                Arguments.of(TestConstants.CAMEL_LETTER_LOG_FILE_PATH)  // Generate report if the username contains both uppercase and lowercase letters
        );
    }

    @ParameterizedTest
    @MethodSource
    public void shouldGenerateReport(String sourceFile) throws Exception {
        FairBillingApp.main(new String[] {sourceFile});
        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(TestConstants.OUTPUT_FILE_PATH))){
            String line;
            List<String> output = new ArrayList<>();

            while (null != (line = reader.readLine())) {
                output.add(line);
            }

            Assertions.assertEquals(2, output.size());
            Assertions.assertTrue(output.contains("ALICE99 4 240"));
            Assertions.assertTrue(output.contains("CHARLIE 3 37"));

            String aliceRecord =
                    output.stream().filter(s -> s.startsWith("ALICE99")).findFirst()
                            .orElseThrow(() -> new AssertionError("Expected record not found"));
            String[] aliceParts = aliceRecord.split(" ");
            Assertions.assertEquals("4", aliceParts[1]);
            Assertions.assertEquals("240", aliceParts[2]);

            String charlieRecord =
                    output.stream().filter(s -> s.startsWith("CHARLIE")).findFirst()
                            .orElseThrow(() -> new AssertionError("Expected record not found"));
            String[] charlieParts = charlieRecord.split(" ");
            Assertions.assertEquals("3", charlieParts[1]);
            Assertions.assertEquals("37", charlieParts[2]);
        }
    }

    @DisplayName("Generating an empty report if there are no records that match the criteria")
    @Test
    public void shouldGenerateEmptyReportForEmptyFile() throws Exception {
        FairBillingApp.main(new String[]{TestConstants.EMPTY_LOG_FILE_PATH});
        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);
        String content = Files.readString(path);
        Assertions.assertTrue(content.isEmpty());
    }

    @DisplayName("Generating report with correct information form valid log file which contains a single record")
    @Test
    public void shouldGenerateReportForSingleLogRecord() throws Exception {
        FairBillingApp.main(new String[] {TestConstants.VALID_LOG_SINGLE_RECORD_FILE_PATH});
        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(TestConstants.OUTPUT_FILE_PATH))) {
            String line;
            List<String> output = new ArrayList<>();

            while (null != (line = reader.readLine())) {
                output.add(line);
            }

            Assertions.assertEquals(1, output.size());
            Assertions.assertTrue(output.contains("ALICE99 1 0"));
        }
    }
}
