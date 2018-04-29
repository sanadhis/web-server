package com.adobe.webserver;

import com.adobe.webserver.http.HttpServer;
import com.adobe.webserver.util.Log;

/**
 * Main class.
 * @author Sanadhi Sutandi
 * @since 29/04/2018
 */
public class Main {
    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        if (isCompleted(args)) {
            final HttpServer server;
            int serverPort = 4433; //default server port

            try {
                serverPort = Integer.parseInt(args[0]);
                Log.info("Main", "Starting HTTP Server on port " + args[0] + " with web directory at " + args[1]);
            } catch (NumberFormatException e) {
                Log.info("Main", "Starting HTTP Server on default port 4433 with web directory at " + args[1]);
            } finally {
                server = new HttpServer(serverPort, args[1]); //assume 2nd argument is always correct
                server.run();
            }
        } else {
            System.out.println("Argument missmatch, pass: <port> <web_directory>");
        }
    }

    /**
     * Check if passed arguments satisfy the
     */
    public static boolean isCompleted(String[] args) {
        return args.length == 2;
    }
}
