package com.bt.fairbilling.processor;

import java.time.LocalTime;

public class SessionRecord {
    private final String username;
    private final LocalTime localTime;
    private final SessionType sessionType;

    public SessionRecord(String username, LocalTime time, SessionType type) {
        this.username = username;
        this.localTime = time;
        this.sessionType = type;
    }

    public String getUsername() { return username; }
    public LocalTime getLocalTime() { return localTime; }
    public SessionType getSessionType() { return sessionType; }
}
