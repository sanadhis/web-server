package com.adobe.test.webserver.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;

import com.adobe.test.webserver.util.Log;

public class HttpResponse{
    private final String POSITION = "HttpResponse";
    private String path;
    private String webDirectory;
    private Hashtable headers;

    private final String projectPath = System.getProperty("user.dir");

    public HttpResponse(String path, Hashtable headers, String webDirectory){
        this.path = path;
        this.webDirectory = projectPath + "/" + webDirectory;
        this.headers = headers;
    }

    public void writeTo(OutputStream output){
        String pathToFile = webDirectory + "/"  + path;
        int statusCode = 200; // default
        File f = new File(pathToFile);
        
        if(!f.exists() || f.isDirectory()) { 
            pathToFile = webDirectory + "/notfound.html";
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writeHttpHeadersTo(writer, statusCode);
            Log.info(POSITION, "opening " + pathToFile);

            InputStream in = Files.newInputStream(Paths.get(pathToFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            writer.flush();
        }
        catch(IOException e){
            Log.error(POSITION, "file does not exist");
        }   
    }

    public void writeHttpHeadersTo(BufferedWriter writer, int statusCode) throws IOException{
        HttpHeader headers = new HttpHeader(statusCode);
        writer.write(headers.toString());
    }
}