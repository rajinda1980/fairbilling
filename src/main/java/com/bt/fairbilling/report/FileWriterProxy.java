package com.bt.fairbilling.report;

import java.util.logging.Logger;

public class FileWriterProxy {

    private static final Logger LOGGER = Logger.getLogger(FileWriterProxy.class.getName());
    private FileWriter fileWriter;

    public FileWriter getFileWriter(ReportType reportType) {
        if (null == reportType) {
            LOGGER.severe("Report type is missing");
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
