package com.bt.fairbilling.processor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DataProcessorImpl implements DataProcessor {


    private static final String STATUS_START = "Start";
    private static final String STATUS_END = "End";

    private final String filePath;
    private Map<String, UserSession> userSessionMap = new HashMap<>();

    public DataProcessorImpl(String filePath) {
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

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found. Please check the file path");

        } catch (Exception e) {
            throw new Exception("UNKNOWN EXCEPTION : " + e.getMessage());
        }
    }

    public Map<String, UserSession> getUserSessionMap() throws Exception {
        return userSessionMap;
    }
}
