package com.bt.fairbilling.processor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SessionParser {

    private static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static SessionRecord getSessionRecord(String logLine) {
        String[] parts = logLine.split("\\s+");

        if (3 == parts.length) {
            try {
                LocalTime time = LocalTime.parse(parts[0], LOG_TIME_FORMATTER);
                String username = parts[1].toUpperCase();
                SessionType type = SessionType.valueOf(parts[2].toUpperCase());
                return new SessionRecord(username, time, type);

            } catch (Exception e) {
                System.err.println("Session record cannot be generated. Exception : " + e.getMessage());
                return null;
            }

        } else {
            return null;
        }
    }
}
