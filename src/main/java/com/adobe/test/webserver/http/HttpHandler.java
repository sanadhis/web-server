package com.adobe.test.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.adobe.test.webserver.util.Log;

public class HttpHandler implements Runnable{
    private final String POSITION = "HttpHandler";
    private Socket socket;
    private String webDirectory;

    public HttpHandler(Socket socket, String webDirectory){
        this.socket = socket;
        this.webDirectory = webDirectory;
    }

    public void run(){
        InputStream input = null;
        OutputStream output = null;

        try{
            input = socket.getInputStream();
            output = socket.getOutputStream();
            HttpRequestParser httpRequest = null;

            try{
                httpRequest = new HttpRequestParser(input);
            }
            catch(Exception e){
                Log.error(POSITION, "cannot parse incoming request");
            }

            if (httpRequest.parseRequest() == 200){
                HttpResponse httpResponse = new HttpResponse(httpRequest.getRequestURL(), httpRequest.getHeaders(), webDirectory);
                httpResponse.writeTo(output);
            }
            else{
                Log.error(POSITION, "cannot accept non-GET request");
            }
        }
        catch(Exception e){
            Log.error(POSITION, "cannot accept non-GET request");
        }
        finally{
            try{
                input.close();
                output.close();
            }
            catch(IOException e){
                Log.error(POSITION, "cannot close inputstream or outputstream");
            }
        }
    }
}