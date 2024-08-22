package com.bt.fairbilling.processor;

import com.bt.fairbilling.TestConstants;
import com.bt.fairbilling.processor.SessionParser;
import com.bt.fairbilling.processor.SessionRecord;
import com.bt.fairbilling.processor.SessionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.stream.Stream;

public class SessionParserTest {

    /**
     * Value map for successful session records
     * @return Argument list
     */
    static Stream<Arguments> shouldReturnSessionRecord() {
        String logLine1 = "14:02:03 ALICE99 Start";
        SessionRecord sessionRecord1 =
                new SessionRecord("ALICE99", LocalTime.parse("14:02:03", TestConstants.LOG_TIME_FORMATTER), SessionType.START);

        String logLine2 = "14:02:03 Alice99 Start";
        SessionRecord sessionRecord2 =
                new SessionRecord("ALICE99", LocalTime.parse("14:02:03", TestConstants.LOG_TIME_FORMATTER), SessionType.START);

        String logLine3 = "14:02:03 alice99 Start";
        SessionRecord sessionRecord3 =
                new SessionRecord("ALICE99", LocalTime.parse("14:02:03", TestConstants.LOG_TIME_FORMATTER), SessionType.START);

        String logLine4 = "14:02:03 aliCE99 eND";
        SessionRecord sessionRecord4 =
                new SessionRecord("ALICE99", LocalTime.parse("14:02:03", TestConstants.LOG_TIME_FORMATTER), SessionType.END);

        String logLine5 = "14:02:03       aliCE99      end";
        SessionRecord sessionRecord5 =
                new SessionRecord("ALICE99", LocalTime.parse("14:02:03", TestConstants.LOG_TIME_FORMATTER), SessionType.END);

        String logLine6 = "21:08:23       aliCE99      end";
        SessionRecord sessionRecord6 =
                new SessionRecord("ALICE99", LocalTime.parse("21:08:23", TestConstants.LOG_TIME_FORMATTER), SessionType.END);

        String logLine7 = "00:00:00       aliCE99      end";
        SessionRecord sessionRecord7 =
                new SessionRecord("ALICE99", LocalTime.parse("00:00:00", TestConstants.LOG_TIME_FORMATTER), SessionType.END);

        String logLine8 = "23:59:59       aliCE99      end";
        SessionRecord sessionRecord8 =
                new SessionRecord("ALICE99", LocalTime.parse("23:59:59", TestConstants.LOG_TIME_FORMATTER), SessionType.END);

        return Stream.of(
                Arguments.of(logLine1, sessionRecord1),
                Arguments.of(logLine2, sessionRecord2),
                Arguments.of(logLine3, sessionRecord3),
                Arguments.of(logLine4, sessionRecord4),
                Arguments.of(logLine5, sessionRecord5),
                Arguments.of(logLine6, sessionRecord6),
                Arguments.of(logLine7, sessionRecord7),
                Arguments.of(logLine8, sessionRecord8)
        );
    }

    @DisplayName("Get session record for valid log record")
    @ParameterizedTest
    @MethodSource
    public void shouldReturnSessionRecord(String logLine, SessionRecord record) {
        SessionParser sp = new SessionParser();
        SessionRecord sessionRecord = SessionParser.getSessionRecord(logLine);

        Assertions.assertEquals(record.getUsername(), sessionRecord.getUsername());
        Assertions.assertEquals(record.getLocalTime(), sessionRecord.getLocalTime());
        Assertions.assertEquals(record.getSessionType(), sessionRecord.getSessionType());
    }

    /**
     * Value map for invalid log lines
     * @return Argument list
     */
    static Stream<Arguments> shouldReturnNullForInvalidLogLines() {
        String line1 = "14:02:03 ALICE99 Start HTTPStatus";
        String line2 = "14:02:03 ALICE99 St";
        String line3 = "14:02:03 ALICE99 Start @@";
        String line4 = "14:02:03 ALICE99";
        String line5 = "14-02-03 ALICE99 Start";
        String line6 = "2024:08:21 ALICE99 Start";
        String line7 = "24:00:01 ALICE99 Start";
        String line8 = "-00:00:01 ALICE99 Start";
        String line9 = "14:02:03 | ALICE99 Start";
        String line10 = "14:02_03  ALICE99 Start";

        return Stream.of(
                Arguments.of(line1),
                Arguments.of(line2),
                Arguments.of(line3),
                Arguments.of(line4),
                Arguments.of(line5),
                Arguments.of(line6),
                Arguments.of(line7),
                Arguments.of(line8),
                Arguments.of(line9),
                Arguments.of(line10)
        );
    }

    @DisplayName("Should return null for invalid log lines")
    @ParameterizedTest
    @MethodSource
    public void shouldReturnNullForInvalidLogLines(String logLine) {
        SessionRecord sr = SessionParser.getSessionRecord(logLine);
        Assertions.assertNull(sr);
    }
}
