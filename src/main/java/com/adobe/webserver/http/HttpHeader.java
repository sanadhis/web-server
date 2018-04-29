package com.adobe.webserver.http;

import com.adobe.webserver.util.Log;

/**
 * HttpHeader class.
 * @author Sanadhi Sutandi
 * @since 29/04/2018
 */
public class HttpHeader {
    private final String POSITION = "HttpHeader";

    private String responseHeaders;
    private final String endHeaders = "\r\n\r\n";

    public HttpHeader(int httpStatusCode, int keepAliveDuration, long responseContentLength) {
        try {
            responseHeaders = "HTTP/1.1" + " " + httpStatusCode + " " 
                                + HttpStatus.getStatusAlias(httpStatusCode) 
                                + "\r\n" + "Content-Type:text/html"
                                + "\r\n" + "Content-Length:" + String.valueOf(responseContentLength)
                                + getConnectionCloseHeader(keepAliveDuration);
        } catch (NullPointerException e) {
            Log.error(POSITION, "cannot resolve status code");
        }
    }

    /**
     * Get the string of complete HTTP response headers'
     */
    public String toString() {
        return responseHeaders + endHeaders;
    }

    /**
     * Append "Connection: Close" header 
     * if client does not include "Keep-alive" header in the request 
     */
    public String getConnectionCloseHeader(int keepAliveDuration) {
        return (keepAliveDuration == -1) ? "\r\nConnection:close" : "";
    }
}