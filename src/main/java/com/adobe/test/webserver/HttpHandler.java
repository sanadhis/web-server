package com.adobe.test.webserver;

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

            HttpRequest httpRequest = new HttpRequest(request);

            if (httpRequest.isValid()){
                HttpResponse httpResponse = new HttpResponse(httpRequest);
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