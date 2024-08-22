package com.bt.fairbilling.report;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileWriterProxyTest {
    private FileWriterProxy fileWriterProxy;

    @BeforeEach
    public void setUp() {
        fileWriterProxy = new FileWriterProxy();
    }

    @DisplayName("Should return a file writer instance for the correct report type")
    @Test
    public void shouldReturnFileWriterForCorrectReportType() {
        FileWriter fileWriter = fileWriterProxy.getFileWriter(ReportType.TXT);
        Assertions.assertNotNull(fileWriter);
        Assertions.assertTrue(fileWriter instanceof TextFileWriter);
    }

    @DisplayName("Should return null for an invalid report type")
    @Test
    public void shouldReturnNullForInvalidReportType() {
        FileWriter fileWriter = fileWriterProxy.getFileWriter(null);
        Assertions.assertNull(fileWriter);
    }
}
