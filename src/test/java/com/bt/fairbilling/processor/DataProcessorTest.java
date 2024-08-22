package com.bt.fairbilling.processor;

import com.bt.fairbilling.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.time.LocalTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

public class DataProcessorTest {

    @DisplayName("Update the map with correct log entries.")
    @Test
    public void shouldProcessTheLogFile() throws Exception {
        DataProcessor fairBillingProcessor = new DataProcessorImpl(TestConstants.VALID_LOG_FILE_4_FILE_PATH);

        try (MockedStatic<SessionParser> mockSessionParser = Mockito.mockStatic(SessionParser.class))    {
            extractSessionRecords(mockSessionParser);

            fairBillingProcessor.processFile();

            @SuppressWarnings("unchecked")
            Map<String, UserSession> sessionMap = fairBillingProcessor.getUserSessionMap();

            Assertions.assertTrue(sessionMap.size() == 2);

            // Verify ALICE99 record
            Assertions.assertEquals(1, sessionMap.get("ALICE99").getSessionCount());
            Assertions.assertEquals(31, sessionMap.get("ALICE99").getTotalSessionTime());
            // Verify CHARLIE record
            Assertions.assertEquals(1, sessionMap.get("CHARLIE").getSessionCount());
            Assertions.assertEquals(53, sessionMap.get("CHARLIE").getTotalSessionTime());
        }
    }

    private void extractSessionRecords(MockedStatic<SessionParser> mockSessionParser) {
        mockSessionParser.when(() -> SessionParser.getSessionRecord("14:02:03 ALICE99 Start"))
                .thenReturn(new SessionRecord("ALICE99", LocalTime.parse("14:02:03", TestConstants.LOG_TIME_FORMATTER), SessionType.valueOf("START")));
        mockSessionParser.when(() -> SessionParser.getSessionRecord("14:02:05 CHARLIE Start"))
                .thenReturn(new SessionRecord("CHARLIE", LocalTime.parse("14:02:05", TestConstants.LOG_TIME_FORMATTER), SessionType.valueOf("START")));
        mockSessionParser.when(() -> SessionParser.getSessionRecord("14:02:34 alice99    end"))
                .thenReturn(new SessionRecord("ALICE99", LocalTime.parse("14:02:34", TestConstants.LOG_TIME_FORMATTER), SessionType.valueOf("END")));
        mockSessionParser.when(() -> SessionParser.getSessionRecord("14:02:25 | CHARLIE End"))
                .thenReturn(null);
        mockSessionParser.when(() -> SessionParser.getSessionRecord("14:02:58   charlie   END"))
                .thenReturn(new SessionRecord("CHARLIE", LocalTime.parse("14:02:58", TestConstants.LOG_TIME_FORMATTER), SessionType.valueOf("END")));
    }

    @DisplayName("Test for exceptions if the file path is invalid")
    @Test
    public void shouldThrownExceptionForInvalidFilePath() {
        DataProcessor fairBillingProcessor = new DataProcessorImpl("invalid_file_path.log");
        FileNotFoundException exception =
                Assertions.assertThrows(FileNotFoundException.class, () -> fairBillingProcessor.processFile());
        Assertions.assertTrue(exception.getMessage().contains("File not found. Please check the file path"));
    }

    @DisplayName("Test for any exceptions thrown by the method due to unexpected behavior")
    @Test
    public void shouldThrownGeneralException() throws Exception {
        DataProcessor fairBillingProcessor = new DataProcessorImpl(TestConstants.VALID_LOG_FILE_4_FILE_PATH);
        try (MockedStatic<SessionParser> mockSessionParser = Mockito.mockStatic(SessionParser.class)){
            BufferedReader reader = new BufferedReader(new StringReader("Invalid content"));
            mockSessionParser.when(() -> SessionParser.getSessionRecord(anyString())).thenThrow(new RuntimeException("Data mapping error"));
            Exception exception = Assertions.assertThrows(Exception.class, () -> fairBillingProcessor.processFile());
            Assertions.assertTrue(exception.getMessage().contains("UNKNOWN EXCEPTION : Data mapping error"));
        }
    }

}
