package com.bt.fairbilling.processor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Stack;

public class UserSession {
    private final Stack<LocalTime> sessionTimeStack = new Stack<>();
    private int totalSessionTime;
    private int sessionCount;

    public int getTotalSessionTime() {
        return totalSessionTime;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setStartSession(LocalTime startTime) {
        sessionTimeStack.push(startTime);
    }

    public void setEndSession(LocalTime logTime, LocalTime initialTime) {
        if (!sessionTimeStack.isEmpty()) {
            sessionCount++;
            LocalTime stackTime = sessionTimeStack.pop();
            totalSessionTime += Duration.between(stackTime, logTime).getSeconds();
        } else {
            // Session has been ended without start time reference
            sessionCount++;
            totalSessionTime += Duration.between(initialTime, logTime).getSeconds();
        }
    }

    // Session has been started but no ending
    public void calculateRemainingSessionTime(LocalTime endTime) {
        if (!sessionTimeStack.isEmpty()) {
            while (!sessionTimeStack.isEmpty()) {
                sessionCount++;
                LocalTime stackTime = sessionTimeStack.pop();
                totalSessionTime += Duration.between(stackTime, endTime).getSeconds();
            }
        }
    }
}
