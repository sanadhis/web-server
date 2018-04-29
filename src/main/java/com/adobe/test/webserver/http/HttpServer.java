package com.adobe.test.webserver.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.adobe.test.webserver.util.Log;

public class HttpServer implements Runnable {
    private final int port;
    private final String webDirectory;

    private final String POSITION = "HTTP SERVER";

    public HttpServer(int port, String webDirectory) {
        this.port = port;
        this.webDirectory = webDirectory;
    }

    public void run() {
        ServerSocket socket = null;
        ThreadPoolExecutor threadPool = null;
        BlockingQueue<Runnable> pool = null;

        try {
            socket = new ServerSocket(port);
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
                threadPool.shutdown();
                Log.error(POSITION, "cannot accept new connection");
            } catch (Exception e) {
                Log.error(POSITION, "hard crash, exiting..");
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            Log.error(POSITION, "cannot close socket");
        }
    }
}