package com.adobe.webserver.http;

import com.adobe.webserver.util.Log;

/**
 * HttpHeader class.
 * created by Sanadhi Sutandi on 29/04/2018.
 */
public class HttpHeader {
    private final String POSITION = "HttpHeader";

    private String responseHeaders;
    private final String endHeaders = "\r\n\r\n";

    public HttpHeader(int statusCode, int keepAlive) {
        try {
            responseHeaders = "HTTP/1.1" + " " + statusCode + " " + HttpStatus.getStatusAlias(statusCode) + "\r\n" + "Content-Type:text/html"
                    + getConnectionCloseHeader(keepAlive);
        } catch (NullPointerException e) {
            Log.error(POSITION, "cannot resolve status code");
        }
    }

    public String toString() {
        return responseHeaders + endHeaders;
    }

    public String getConnectionCloseHeader(int keepAlive) {
        return keepAlive == 0 ? "\r\nConnection:close" : "";
    }
}