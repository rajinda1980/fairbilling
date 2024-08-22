package com.bt.fairbilling.report;

import java.util.stream.Stream;

public interface FileWriter {
    void printReport(Stream<String> lines) throws Exception;
}
