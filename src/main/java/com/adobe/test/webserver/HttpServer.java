package com.adobe.test.webserver;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer implements Runnable{
    private final int port;
    private final String webDirectory;
    
    private ServerSocket socket;

    public HttpServer(int port, String webDirectory){
        this.port = port;
        this.webDirectory = webDirectory;
    }

    public void run() {
        try{
            socket = new ServerSocket(port);
        }
        catch(IOException e){
            // error here
        }

        while(true){
            try{

            }
            catch(IOException e){
                //
            }
        }
    }
}