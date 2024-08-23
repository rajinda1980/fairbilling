package com.bt.fairbilling.processor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class SessionParser {

    private static final Logger LOGGER = Logger.getLogger(SessionParser.class.getName());
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
                LOGGER.severe("Session record could not be generated. Given record : " + logLine + ",  Exception : " + e.getMessage());
                return null;
            }

        } else {
            return null;
        }
    }
}
