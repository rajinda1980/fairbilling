package com.bt.fairbilling.report;

/**
 * This is an interface for report generation
 */
public interface ReportService {
    void printReport(ReportType type) throws Exception;
}
