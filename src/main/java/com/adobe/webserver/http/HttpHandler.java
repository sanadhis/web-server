package com.adobe.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.adobe.webserver.util.Log;

public class HttpHandler implements Runnable {
    private final String POSITION = "HttpHandler";
    private Socket socket;
    private String webDirectory;

    public HttpHandler(Socket socket, String webDirectory) {
        this.socket = socket;
        this.webDirectory = webDirectory;
    }

    public void run() {
        InputStream input = null;
        OutputStream output = null;
        boolean acceptKeepAlive = true;

        for (;;) {
            try {
                input = socket.getInputStream();
                output = socket.getOutputStream();
                HttpRequestParser httpRequest = null;
                int keepaliveDuration = -1; //default assume keepalive is not enabled

                try {
                    httpRequest = new HttpRequestParser(input);
                } catch (Exception e) {
                    Log.error(POSITION, "cannot parse incoming request");
                }

                if (httpRequest.parseRequest() == 200) {
                    if (acceptKeepAlive) {
                        try {
                            keepaliveDuration = Integer.parseInt(httpRequest.getHeader("keep-alive"));
                        } catch (NumberFormatException e) {
                            keepaliveDuration = -1;
                        }
                    }

                    HttpResponse httpResponse = new HttpResponse(httpRequest.getRequestURL(), webDirectory);
                    Log.info(POSITION, "keepalive duration " + keepaliveDuration);
                    httpResponse.writeTo(output, keepaliveDuration);
                    if (acceptKeepAlive && keepaliveDuration != -1) {
                        Log.info(POSITION, "here");
                        socket.setKeepAlive(true);
                        socket.setSoTimeout(keepaliveDuration * 1000);
                        acceptKeepAlive = false;
                        keepaliveDuration = 0;
                        continue;
                    } else if (keepaliveDuration == 0) {
                        continue;
                    } else {
                        break;
                    }
                } else {
                    Log.error(POSITION, "cannot accept non-GET request");
                    break;
                }
            } catch (IOException e) {
                Log.error(POSITION, "cannot process input and output stream, socket is closed");
                break;
            }
        }

        try {
            input.close();
            output.close();
        } catch (IOException e) {
            Log.error(POSITION, "cannot close input and output stream, already closed");
        }
    }

}