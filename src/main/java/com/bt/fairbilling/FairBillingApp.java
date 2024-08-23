package com.bt.fairbilling;

import com.bt.fairbilling.processor.*;
import com.bt.fairbilling.report.ReportService;
import com.bt.fairbilling.report.ReportServiceImpl;
import com.bt.fairbilling.report.ReportType;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

/**
 * Main class to call the application
 *
 * Important:
 * 1. The log file path should be provided as an argument. Filepath pattern : <path>/<file name>
 * 2. An output file will be generated for valid log entries.
 */
public class FairBillingApp {

    public static final Logger LOGGER = Logger.getLogger(FairBillingApp.class.getName());

    public static void main(String[] args) {
        if (args.length != 1) {
            LOGGER.severe("FairBilling Application: Log file is not provided");
            System.exit(1);
        }
        process(args);
    }

    private static void process(String[] args) {
        String logFilePath = args[0];
        LOGGER.info("Log file is received. File path : " + logFilePath);
        try {
            DataProcessor processor = new DataProcessorImpl(logFilePath);
            processor.processFile();

            ReportService writer = new ReportServiceImpl(processor.getUserSessionMap());
            writer.printReport(ReportType.TXT);

        } catch (FileNotFoundException e) {
            LOGGER.severe("File processing exception : " + e.getMessage());

        } catch (Exception e) {
            LOGGER.severe("File processing exception : " + e.getMessage());
        }
    }

}
