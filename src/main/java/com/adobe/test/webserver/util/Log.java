package com.adobe.test.webserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log{
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final Date date = new Date();
    private static final String currentTime = dateFormat.format(date); 

    public static void info(String position, String message){
        System.out.println(currentTime + " : " + "__INFO__ " + position + " - " + message);
    }

    public static void error(String position, String message){
        System.out.println(currentTime + " : " + "__ERROR__ " + position + " - " + message);
    }
}