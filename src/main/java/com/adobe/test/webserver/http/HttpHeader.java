package com.adobe.test.webserver.http;

import com.adobe.test.webserver.util.Log;

public class HttpHeader {
    private final String POSITION = "HttpHeader";
    private String headers;
    private String end_headers;

    public HttpHeader(int statusCode, int keepAlive) {
        try {
            headers = "HTTP/1.1" + statusCode + HttpStatus.getStatus(statusCode) + "\r\n" + "Content-Type: text/html"
                    + getConnectionCloseHeader(keepAlive) + "\r\n";
        } catch (NullPointerException e) {
            Log.error(POSITION, "cannot resolve status code");
        }
        end_headers = "\r\n\r\n";
    }

    public String toString() {
        return headers + end_headers;
    }

    public String getConnectionCloseHeader(int keepAlive) {
        return keepAlive == 0 ? "\r\nConnection: close" : "";
    }
}