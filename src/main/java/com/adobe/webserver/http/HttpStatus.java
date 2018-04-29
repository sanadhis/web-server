package com.adobe.webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * HttpStatus class.
 * created by Sanadhi Sutandi on 29/04/2018.
 */
public class HttpStatus {
    private static final Map<Integer, String> statusMap;
    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();
        aMap.put(200, "OK");
        aMap.put(404, "Not Found");
        statusMap = Collections.unmodifiableMap(aMap);
    }

    public static String getStatusAlias(int statusCode) throws NullPointerException {
        return statusMap.get(statusCode);
    }
}