package com.adobe.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.adobe.webserver.util.Log;

/**
 * HttpHandler class.
 * @author Sanadhi Sutandi
 * @since 29/04/2018
 */
public class HttpHandler implements Runnable {
    private final String POSITION = "HttpHandler";

    private Socket serverSocket;
    private String webDirectory;

    public HttpHandler(Socket serverSocket, String webDirectory) {
        this.serverSocket = serverSocket;
        this.webDirectory = webDirectory;
    }

    public void run() {
        InputStream streamOfInput = null;
        OutputStream streamOfoutput = null;
        boolean acceptKeepAlive = true;
        int keepaliveDuration = -1; //default assume keepalive is not enabled

        //looping forever allows the implementation of keepalive with uncertain duration for the persistency
        for (;;) {
            try {
                streamOfInput = serverSocket.getInputStream();
                streamOfoutput = serverSocket.getOutputStream();
                HttpRequestParser httpRequest = null;

                //parsing incoming stream input
                try {
                    httpRequest = new HttpRequestParser(streamOfInput);
                } catch (Exception e) {
                    Log.error(POSITION, "cannot parse incoming request");
                }

                if (isValidRequest(httpRequest.parseRequest())) {
                    //accept only keep alive header in the first request of the corresponding connection
                    if (acceptKeepAlive) {
                        try {
                            keepaliveDuration = Integer.parseInt(httpRequest.getHeader("keep-alive"));
                        } catch (NumberFormatException e) {
                            keepaliveDuration = -1;
                        }
                    }

                    //write http response to client regardless of keep-alive presence
                    HttpResponse httpResponse = new HttpResponse(httpRequest.getRequestURL(), webDirectory);
                    httpResponse.writeTo(streamOfoutput, keepaliveDuration);
                    
                    //process the first request in a connection if and only if it contains "keep-alive" in request header
                    if (acceptKeepAlive && keepaliveDuration != -1) {
                        //use socket timeout for keepalive extension
                        serverSocket.setKeepAlive(true);
                        serverSocket.setSoTimeout(keepaliveDuration * 1000); //convert seconds to milliseconds
                        acceptKeepAlive = false;
                        keepaliveDuration = 0;
                        continue; //continue the loop
                    } else if (keepaliveDuration == 0) {
                        continue; //continue the loop
                    } else {
                        break; //this is invoked only when keep alive is not requested/enabled (-1)
                    }
                } else {
                    Log.error(POSITION, "cannot process non-GET HTTP request");
                    break;
                }
            } catch (IOException e) {
                Log.info(POSITION, "cannot process streamOfInput and streamOfoutput, socket is already closed");
                break;
            }
        }

        //close resources
        try {
            streamOfInput.close();
            streamOfoutput.close();
        } catch (IOException e) {
            Log.info(POSITION, "cannot close streamOfInput and streamOfoutput, already closed");
        }
    }

    /**
     * Check if client request is a valid HTTP request or not
     * 200 indicate it is a valid request
     * Other codes imply they are not.
     */
    public boolean isValidRequest(int code){
        return code == 200;
    }

}