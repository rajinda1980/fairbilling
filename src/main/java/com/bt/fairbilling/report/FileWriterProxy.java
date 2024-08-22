package com.bt.fairbilling.report;

public class FileWriterProxy {
    private FileWriter fileWriter;

    public FileWriter getFileWriter(ReportType reportType) {
        if (null == reportType) {
            return null;
        }
        fileWriter =
                switch (reportType) {
                    case TXT -> new TextFileWriter();
                    default -> null;
                };
        return fileWriter;
    }
}
