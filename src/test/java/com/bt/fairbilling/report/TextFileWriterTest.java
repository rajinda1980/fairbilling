package com.bt.fairbilling.report;

import com.bt.fairbilling.TestConstants;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class TextFileWriterTest {
    private TextFileWriter textFileWriter;

    @BeforeEach
    public void setUp() {
        textFileWriter = new TextFileWriter();
    }

    @AfterEach
    public void cleanUp() throws Exception{
        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @DisplayName("Report should be generated if input is provided")
    @Test
    public void shouldGenerateReportForValidInput() throws Exception {
        List<String> list = List.of("ALICE99 4 240", "CHARLIE 3 37");
        textFileWriter.printReport(list.stream());

        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);
        Assertions.assertTrue(Files.exists(path));
        String content = Files.readString(path);
        Assertions.assertTrue(content.contains("ALICE99 4 240"));
        Assertions.assertTrue(content.contains("CHARLIE 3 37"));
    }

    @DisplayName("The report should be overwritten with new data if the old report still exist")
    @Test
    public void shouldOverwriteOldFileIfExists() throws Exception {
        List<String> oldList = List.of("ALICE99 4 240", "CHARLIE 3 37");
        textFileWriter.printReport(oldList.stream());
        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);

        Assertions.assertTrue(Files.exists(path));

        List<String> newList = List.of("JOHN 1 50", "EMMALI 8 542", "TONY 5 360");
        textFileWriter.printReport(newList.stream());
        Assertions.assertTrue(Files.exists(path));
        String content = Files.readString(path);
        Assertions.assertTrue(content.contains("JOHN 1 50"));
        Assertions.assertTrue(content.contains("EMMALI 8 542"));
        Assertions.assertTrue(content.contains("TONY 5 360"));
    }

    @DisplayName("An empty report should be generated if the stream is empty")
    @Test
    public void shouldGenerateAnEmptyReport() throws Exception {
        textFileWriter.printReport(Stream.empty());
        Path path = Paths.get(TestConstants.OUTPUT_FILE_PATH);
        Assertions.assertTrue(Files.exists(path));
        String content = Files.readString(path);
        Assertions.assertTrue(content.isEmpty());
    }

    @DisplayName("Exceptions should be captured and thrown for any failures")
    @Test
    public void shouldCaptureException() throws Exception {
        Exception exception = Assertions.assertThrows(Exception.class, () -> textFileWriter.printReport(null));
        Assertions.assertTrue(exception.getMessage().contains("Failed to write the report to the file"));
    }
}
