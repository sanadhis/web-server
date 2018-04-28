package com.adobe.test.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpHandler implements Runnable{
    private Socket socket;

    public HttpHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        InputStream request;
        OutputStream response;

        try{
            request = socket.getInputStream();
            response = socket.getOutputStream();
            HttpParser httpRequest;

            try{
                httpRequest = new HttpParser(request);
            }
            catch(IOException e){

            }

            if (httpRequest.parseRequest() == 200){
                HttpResponse httpResponse = new HttpResponse(httpRequest.getRequestURL(), httpRequest.getHeaders());
                response.write(httpResponse.toByte());
            }
            else{
                //not http
            }
        }
        catch(Exception e){
            //error
        }
    }
}