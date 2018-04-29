package com.adobe.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.adobe.webserver.util.Log;

/**
 * HttpHandler class.
 * created by Sanadhi Sutandi on 29/04/2018.
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

        for (;;) {
            try {
                streamOfInput = serverSocket.getInputStream();
                streamOfoutput = serverSocket.getOutputStream();
                HttpRequestParser httpRequest = null;
                int keepaliveDuration = -1; //default assume keepalive is not enabled

                try {
                    httpRequest = new HttpRequestParser(streamOfInput);
                } catch (Exception e) {
                    Log.error(POSITION, "cannot parse incoming request");
                }

                if (isValidRequest(httpRequest.parseRequest())) {
                    if (acceptKeepAlive) {
                        try {
                            keepaliveDuration = Integer.parseInt(httpRequest.getHeader("keep-alive"));
                        } catch (NumberFormatException e) {
                            keepaliveDuration = -1;
                        }
                    }

                    HttpResponse httpResponse = new HttpResponse(httpRequest.getRequestURL(), webDirectory);
                    httpResponse.writeTo(streamOfoutput, keepaliveDuration);
                    if (acceptKeepAlive && keepaliveDuration != -1) {
                        serverSocket.setKeepAlive(true);
                        serverSocket.setSoTimeout(keepaliveDuration * 1000);
                        acceptKeepAlive = false;
                        keepaliveDuration = 0;
                        continue;
                    } else if (keepaliveDuration == 0) {
                        continue;
                    } else {
                        break;
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

        try {
            streamOfInput.close();
            streamOfoutput.close();
        } catch (IOException e) {
            Log.info(POSITION, "cannot close streamOfInput and streamOfoutput, already closed");
        }
    }

    public boolean isValidRequest(int code){
        return code == 200;
    }

}