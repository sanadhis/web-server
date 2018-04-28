package com.adobe.test.webserver;

import com.adobe.test.webserver.HttpServer;
/**
 * Main class.
 *
 */
public class Main {
    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        if (isCompleted(args)){
            final HttpServer server = new HttpServer(Integer.parseInt(args[0]), args[1]);
            server.run();
        }
        else{
            System.out.println("Argument missmatch: Use ");
        }
    }

    public static boolean isCompleted(String[] args){
        return args.length == 2;
    }
}

