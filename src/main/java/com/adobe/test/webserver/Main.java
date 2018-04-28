package com.adobe.test.webserver;

import com.adobe.test.webserver.http.HttpServer;
import com.adobe.test.webserver.util.Log;
/**
 * Main class.
 *
 */
public class Main {
    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        if (isCompleted(args)){
            final HttpServer server = new HttpServer(Integer.parseInt(args[0]), args[1]);
            Log.info("Main", "Starting HTTP Server");
            server.run();
        }
        else{
            System.out.println("Argument missmatch, pass: <port> <web_directory>");
        }
    }

    public static boolean isCompleted(String[] args){
        return args.length == 2;
    }
}

