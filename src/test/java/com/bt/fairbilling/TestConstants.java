package com.bt.fairbilling;

import java.time.format.DateTimeFormatter;

public final class TestConstants {

    public static final String VALID_LOG_FILE_PATH = "src/test/resources/logfile_valid.log";
    public static final String VALID_INVALID_LOG_FILE_PATH = "src/test/resources/logfile_valid_invalid.log";
    public static final String CAMEL_LETTER_LOG_FILE_PATH = "src/test/resources/logfile_camel_case.log";
    public static final String EMPTY_LOG_FILE_PATH = "src/test/resources/logfile_empty.log";
    public static final String VALID_LOG_FILE_4_FILE_PATH = "src/test/resources/logfile_valid5.log";
    public static final String VALID_LOG_SINGLE_RECORD_FILE_PATH = "src/test/resources/logfile_single_record.log";

    public static final String OUTPUT_FILE_PATH = "output.txt";
    public static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private TestConstants() {}
}
