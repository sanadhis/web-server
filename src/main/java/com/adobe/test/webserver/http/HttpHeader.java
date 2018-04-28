package com.adobe.test.webserver.http;

import com.adobe.test.webserver.util.Log;

public class HttpHeader{
    private final String POSITION = "HttpHeader";
    private String headers;
    private String end_headers;

    public HttpHeader(int statusCode){
        try{
            headers = "HTTP/1.1" +
                            statusCode +
                            HttpStatus.getStatus(statusCode) +
                            "\r\n" + 
                            "Content-Type: text/html\r\n"; 
        }
        catch(NullPointerException e){
            Log.error(POSITION, "cannot resolve status code");
        }
        end_headers = "\r\n\r\n";
    }
    
    public String toString(){
        return headers + end_headers;
    }
}