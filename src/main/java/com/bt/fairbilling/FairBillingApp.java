package com.bt.fairbilling;

import com.bt.fairbilling.processor.FairBillingProcessor;

import java.io.FileNotFoundException;

/**
 * Main class to call the application
 *
 * Important:
 * 1. The log file path should be provided as an argument. Filepath pattern : <path>/<file name>
 * 2. An output file will be generated for valid log entries.
 */
public class FairBillingApp {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("FairBilling Application: Log file is not provided");
            System.exit(1);
        }

        String logFilePath = args[0];
        try {
            FairBillingProcessor processor = new FairBillingProcessor(logFilePath);
            processor.processFile();
            processor.printReport();

        } catch (FileNotFoundException e) {
            System.out.println("File does not exist in the file path. Exception : " + e.getMessage());
            System.exit(1);

        } catch (Exception e) {
            System.out.println("File processing exception : " + e.getMessage());
            System.exit(1);
        }
    }
}
