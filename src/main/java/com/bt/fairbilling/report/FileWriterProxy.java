package com.bt.fairbilling.report;

public class FileWriterProxy {
    private FileWriter fairBillingWriter;

    public FileWriter getFileWriter(ReportType reportType) {
        fairBillingWriter =
                switch (reportType) {
                    case TXT -> new TextFileWriter();
                    default -> null;
                };
        return fairBillingWriter;
    }
}
