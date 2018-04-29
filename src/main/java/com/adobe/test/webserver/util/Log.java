package com.adobe.test.webserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void info(String position, String message) {
        System.out.println(getCurrentTime() + " : " + "__INFO__ " + position + " - " + message);
    }

    public static void error(String position, String message) {
        System.out.println(getCurrentTime() + " : " + "__ERROR__ " + position + " - " + message);
    }

    public static String getCurrentTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }
}