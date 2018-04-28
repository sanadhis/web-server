package com.adobe.test.webserver;

import java.io.InputStream;

public class HttpRequest{

    private boolean validity;

    public HttpRequest(InputStream input){

    }
    
    public boolean isValid(){
        return validity;
    }
}