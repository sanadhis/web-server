package com.adobe.webserver.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.adobe.webserver.util.Log;

/**
 * HttpServer class.
 * created by Sanadhi Sutandi on 29/04/2018.
 */
public class HttpServer implements Runnable {
    private final String POSITION = "HTTP SERVER";
    
    private final int serverPort;
    private final String webDirectory;

    public HttpServer(int serverPort, String webDirectory) {
        this.serverPort = serverPort;
        this.webDirectory = webDirectory;
    }

    public void run() {
        ServerSocket socket = null;
        ThreadPoolExecutor threadPool = null;
        BlockingQueue<Runnable> pool = null;

        try {
            socket = new ServerSocket(serverPort);
            pool = new LinkedBlockingQueue<Runnable>();
            threadPool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, pool);
        } catch (IOException e) {
            Log.error(POSITION, "cannot bind socket");
            System.exit(1);
        }

        while (true) {
            try {
                threadPool.execute(new Thread(new HttpHandler(socket.accept(), webDirectory)));
            } catch (IOException e) {
                Log.error(POSITION, "cannot accept new connection");
                break;
            } catch (Exception e) {
                Log.error(POSITION, "hard crash, exiting..");
                break;
            }
        }

        threadPool.shutdown();
        try {
            socket.close();
        } catch (IOException e) {
            Log.error(POSITION, "cannot close socket");
        }
    }
}