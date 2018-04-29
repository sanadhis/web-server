package com.adobe.webserver.http;

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

import com.adobe.webserver.util.Log;

public class HttpResponse {
    private final String POSITION = "HttpResponse";
    private String path;
    private String webDirectory;

    private final String projectPath = System.getProperty("user.dir");

    public HttpResponse(String path, String webDirectory) {
        this.path = path;
        this.webDirectory = projectPath + "/" + webDirectory;
    }

    public void writeTo(OutputStream output, int keepAlive) {
        String pathToFile = webDirectory + path;
        int statusCode = 200; // default
        File f = new File(pathToFile);

        if (!f.exists() || f.isDirectory()) {
            pathToFile = webDirectory + "/notfound.html";
            statusCode = 404;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writeHttpHeadersTo(writer, statusCode, keepAlive);
            Log.info(POSITION, "opening " + pathToFile);

            InputStream in = Files.newInputStream(Paths.get(pathToFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
            reader.close();
            writer.flush();
        } catch (IOException e) {
            Log.error(POSITION, "file does not exist");
        }
    }

    public void writeHttpHeadersTo(BufferedWriter writer, int statusCode, int keepAlive) throws IOException {
        HttpHeader headers = new HttpHeader(statusCode, keepAlive);
        writer.write(headers.toString());
    }
}