package com.bt.fairbilling.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FairBillingProcessor {


    private static final String STATUS_START = "Start";
    private static final String STATUS_END = "End";

    private final String filePath;
    private Map<String, UserSession> userSessionMap = new HashMap<>();

    public FairBillingProcessor(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Read the log file, generate a session object for each user, and store it in the map.
     * Map<User, Session Object>
     *
     * @throws Exception
     */
    public void processFile() throws Exception {
        LocalTime initialTime = null;
        LocalTime endTime = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while (null != (line = reader.readLine())) {
                SessionRecord sessionRecord = SessionParser.getSessionRecord(line);
                if (null == sessionRecord) {
                    continue;
                }

                // For initial time
                if (null == initialTime || sessionRecord.getLocalTime().isBefore(initialTime)) {
                    initialTime = sessionRecord.getLocalTime();
                }

                // For end time
                if (null == endTime || sessionRecord.getLocalTime().isAfter(endTime)) {
                    endTime = sessionRecord.getLocalTime();
                }

                // New session for the user
                userSessionMap.putIfAbsent(sessionRecord.getUsername(), new UserSession());

                if (STATUS_START.equalsIgnoreCase(sessionRecord.getSessionType().name())) {
                    userSessionMap.get(sessionRecord.getUsername()).setStartSession(sessionRecord.getLocalTime());

                } else if (STATUS_END.equalsIgnoreCase(sessionRecord.getSessionType().name())) {
                    userSessionMap.get(sessionRecord.getUsername()).setEndSession(sessionRecord.getLocalTime(), initialTime);
                }
            }

            // For remaining sessions
            for (Map.Entry<String, UserSession> e : userSessionMap.entrySet()) {
                e.getValue().calculateRemainingSessionTime(endTime);
            }
        }
    }

    /**
     * Calling file writer
     * @throws Exception
     */
    public void printReport() throws Exception {
        FairBillingFileWriter writer;

        try (Stream<String> lines = userSessionMap.entrySet()
                                        .stream().map(e -> e.getKey() + " " + e.getValue().getSessionCount() + " "
                                                                + e.getValue().getTotalSessionTime())) {
            writer = new FairBillingFileWriter(lines);
            writer.printReport();

        } catch (Exception e) {
            System.out.println("Error in writing to output file. Exception : " + e.getMessage());
            throw new Exception(e);
        }
    }
}
