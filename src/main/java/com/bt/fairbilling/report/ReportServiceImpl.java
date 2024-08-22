package com.bt.fairbilling.report;

import com.bt.fairbilling.processor.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReportServiceImpl implements ReportService {

    private final FileWriterProxy fileWriterProxy;
    private Map<String, UserSession> userSessionMap = new HashMap<>();

    public ReportServiceImpl(Map<String, UserSession> map) {
        fileWriterProxy = new FileWriterProxy();
        this.userSessionMap = map;
    }

    /**
     * Calling file writer
     * @throws Exception
     */
    @Override
    public void printReport(ReportType type) throws Exception {
        try (Stream<String> lines = userSessionMap.entrySet()
                .stream().map(e -> e.getKey() + " " + e.getValue().getSessionCount() + " "
                        + e.getValue().getTotalSessionTime())) {
            FileWriter writer = fileWriterProxy.getFileWriter(type);

            if (writer == null) {
                throw new RuntimeException("File writer is not found");
            }
            writer.printReport(lines);

        } catch (Exception e) {
            System.out.println("Error in writing to output file. Exception : " + e.getMessage());
            throw new Exception(e);
        }
    }
}
