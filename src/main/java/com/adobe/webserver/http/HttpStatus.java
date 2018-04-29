package com.adobe.webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * HttpStatus class.
 * @author Sanadhi Sutandi
 * @since 29/04/2018
 */
public class HttpStatus {
    private static final Map<Integer, String> statusMap;
    //only support either 200 or 404 status code
    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();
        aMap.put(200, "OK");
        aMap.put(404, "Not Found");
        statusMap = Collections.unmodifiableMap(aMap);
    }

    /**
     * Get Status code definition
     * e.g. 200 -> "OK"
     */
    public static String getStatusAlias(int httpStatusCode) throws NullPointerException {
        return statusMap.get(httpStatusCode);
    }
}