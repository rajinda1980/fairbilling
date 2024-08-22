package com.bt.fairbilling.report;

import com.bt.fairbilling.processor.UserSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReportServiceImplTest {

    private ReportServiceImpl reportService;
    private FileWriterProxy fileWriterProxy;
    private FileWriter fileWriter;
    Map<String, UserSession> userSessionMap;
    ReportType type;

    @BeforeEach
    void setUp() {
        type = ReportType.TXT;
        fileWriterProxy = Mockito.mock(FileWriterProxy.class);
        fileWriter = Mockito.mock(FileWriter.class);

        userSessionMap = new HashMap<>();
        userSessionMap.put("ALICE99", mockUserSession(4, 240));
        userSessionMap.put("CHARLIE", mockUserSession(3, 37));

        reportService = new ReportServiceImpl(userSessionMap);
        injectFileWriterProxy(reportService, "fileWriterProxy", fileWriterProxy);
    }

    private UserSession mockUserSession(int sessionCount, int totalSessionTime) {
        UserSession userSession = Mockito.mock(UserSession.class);
        Mockito.when(userSession.getSessionCount()).thenReturn(sessionCount);
        Mockito.when(userSession.getTotalSessionTime()).thenReturn(totalSessionTime);
        return userSession;
    }

    private void injectFileWriterProxy(Object target, String fieldName, Object fieldValue) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, fieldValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Generate the report if the session map contains values")
    @Test
    public void shouldCallProxyAndWriter() throws Exception {
        Mockito.when(fileWriterProxy.getFileWriter(type)).thenReturn(fileWriter);
        reportService.printReport(type);
        Mockito.verify(fileWriterProxy, Mockito.times(1)).getFileWriter(type);
        Mockito.verify(fileWriter, Mockito.times(1)).printReport(any(Stream.class));
    }

    @DisplayName("Throw an exception if the proxy does not return a file writer")
    @Test
    public void shouldThrowExceptionIfFileWriterIsNull() throws Exception {
        Mockito.when(fileWriterProxy.getFileWriter(any(ReportType.class))).thenReturn(null);
        Exception exception = Assertions.assertThrows(Exception.class, () -> reportService.printReport(type));
        Assertions.assertTrue(exception.getMessage().contains("File writer is not found"));
    }

    @DisplayName("Throw an exception if report generation is interrupted")
    @Test
    public void shouldThrowExceptionIfReportGenerationIsInterrupted() throws Exception {
        Mockito.when(fileWriterProxy.getFileWriter(type)).thenReturn(fileWriter);
        Mockito.doThrow(new Exception("Failed to write report")).when(fileWriter).printReport(any(Stream.class));
        Exception exception = Assertions.assertThrows(Exception.class, () -> reportService.printReport(type));
        Assertions.assertTrue(exception.getMessage().contains("Failed to write report"));
    }
}
