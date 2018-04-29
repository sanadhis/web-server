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

/**
 * HttpResponse class.
 * created by Sanadhi Sutandi on 29/04/2018.
 */
public class HttpResponse {
    private final String POSITION = "HttpResponse";

    private String requestedFile;
    private String webDirectoryPath;

    private final String projectPath = System.getProperty("user.dir");

    public HttpResponse(String requestedFile, String webDirectory) {
        this.requestedFile = requestedFile;
        this.webDirectoryPath = projectPath + "/" + webDirectory;
    }

    public void writeTo(OutputStream output, int keepAliveDuration) {
        String pathToFile = webDirectoryPath + requestedFile;
        int httpStatusCode = 200; // default
        File f = new File(pathToFile);

        if (!f.exists() || f.isDirectory()) {
            Log.info(POSITION, "file does not exist, render notfound.html");
            pathToFile = webDirectoryPath + "/notfound.html";
            httpStatusCode = 404;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writeHttpHeadersTo(writer, httpStatusCode, keepAliveDuration);
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
            Log.error(POSITION, "file does not exist or cannot write to output stream");
        }
    }

    public void writeHttpHeadersTo(BufferedWriter responseWriter, int httpStatusCode, int keepAliveDuration) throws IOException {
        HttpHeader completeHeadersResponse = new HttpHeader(httpStatusCode, keepAliveDuration);
        responseWriter.write(completeHeadersResponse.toString());
    }
}