package com.bt.fairbilling.processor;

import java.util.Map;

/**
 * This is an interface for data processing
 */
public interface DataProcessor {
    void processFile() throws Exception;
    Map<String, UserSession> getUserSessionMap() throws Exception;
}
