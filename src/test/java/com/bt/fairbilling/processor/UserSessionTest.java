package com.bt.fairbilling.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class UserSessionTest {
    private UserSession userSession;
    private LocalTime initialTime;

    @BeforeEach
    void setUp() {
        userSession = new UserSession();
        initialTime = LocalTime.of(15, 0, 0);
    }

    @DisplayName("Session count and total session time should be updated when setting start and end sessions")
    @Test
    public void shouldUpdateWhenSetStartAndEndSession() {
        LocalTime startTime = initialTime.plusSeconds(5);
        LocalTime endTime = initialTime.plusSeconds(45);
        userSession.setStartSession(startTime);
        userSession.setEndSession(endTime, initialTime);
        Assertions.assertEquals(40, userSession.getTotalSessionTime());
        Assertions.assertEquals(1, userSession.getSessionCount());
    }

    @DisplayName("Session count and total session time should be updated even without setting a start session")
    @Test
    public void shouldUpdateWithoutStartSession() {
        LocalTime endTime = initialTime.plusSeconds(50);
        userSession.setEndSession(endTime, initialTime);
        Assertions.assertEquals(50, userSession.getTotalSessionTime());
        Assertions.assertEquals(1, userSession.getSessionCount());
    }

    @DisplayName("Session count and total session time should be updated for remaining session times")
    @Test
    public void shouldUpdateWithRemainingSessionTime() {
        LocalTime start1 = initialTime.plusSeconds(15);
        LocalTime start2 = initialTime.plusSeconds(25);
        LocalTime endTime = initialTime.plusSeconds(50);

        userSession.setStartSession(start1);
        userSession.setStartSession(start2);
        userSession.calculateRemainingSessionTime(endTime);

        Assertions.assertEquals(60, userSession.getTotalSessionTime());
        Assertions.assertEquals(2, userSession.getSessionCount());
    }

    @DisplayName("Session count and total session time should be updated for multiple sessions")
    @Test
    public void shouldUpdateWithMultipleSessions() {
        LocalTime start1 = initialTime.plusSeconds(5);
        LocalTime end1 = initialTime.plusSeconds(15);
        LocalTime start2 = initialTime.plusSeconds(30);
        LocalTime end2 = initialTime.plusSeconds(50);

        userSession.setStartSession(start1);
        userSession.setEndSession(end1, initialTime);
        userSession.setStartSession(start2);
        userSession.setEndSession(end2, initialTime);

        Assertions.assertEquals(30, userSession.getTotalSessionTime());
        Assertions.assertEquals(2, userSession.getSessionCount());
    }

    @DisplayName("Session count and total session time should be updated even if the stack is empty")
    @Test
    public void shouldUpdateEvenStackIsEmpty() {
        LocalTime time = initialTime.plusSeconds(20);
        userSession.calculateRemainingSessionTime(time);
        Assertions.assertEquals(0, userSession.getTotalSessionTime());
        Assertions.assertEquals(0, userSession.getSessionCount());
    }
}
