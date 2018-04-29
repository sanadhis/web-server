package com.adobe.webserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log class.
 * Format: DATE : LOG_TYPE CODE_POSITION - LOG_MESSAGES
 * @author Sanadhi Sutandi
 * @since 29/04/2018
 */
public class Log {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Info log.
     */
    public static void info(String codePosition, String logMessage) {
        System.out.println(getCurrentTime() + " : " + "__INFO__ " + codePosition + " - " + logMessage);
    }

    /**
     * Error log.
     */
    public static void error(String codePosition, String logMessage) {
        System.out.println(getCurrentTime() + " : " + "__ERROR__ " + codePosition + " - " + logMessage);
    }

    /**
     * Get current time as string.
     */
    public static String getCurrentTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }
}