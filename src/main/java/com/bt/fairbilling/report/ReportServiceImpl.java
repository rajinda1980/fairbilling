package com.bt.fairbilling.report;

import com.bt.fairbilling.processor.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class.getName());

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
                LOGGER.severe("File writer is not found for the specified report type " + type);
                throw new RuntimeException("FFile writer is not found for the specified report type " + type);
            }
            writer.printReport(lines);

        } catch (Exception e) {
            LOGGER.severe("Error in writing to the output file. Exception : " + e.getMessage());
            throw new Exception(e);
        }
    }
}
