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
 * @author Sanadhi Sutandi
 * @since 29/04/2018
 */
public class HttpResponse {
    private final String POSITION = "HttpResponse";
    private final String currentDirPath = System.getProperty("user.dir"); //current directory

    private String requestedFile;
    private String webDirectoryPath;

    public HttpResponse(String requestedFile, String webDirectory) {
        this.requestedFile = requestedFile;
        this.webDirectoryPath = currentDirPath + "/" + webDirectory;
    }

    public void writeTo(OutputStream output, int keepAliveDuration) {
        String pathToRequestedFile = webDirectoryPath + requestedFile;
        int httpStatusCode = 200; // default status code
        File f = new File(pathToRequestedFile);
        long contentLength = 0;

        // check if requested file is exists or not, if not, return "notfound.html"
        if (!f.exists() || f.isDirectory()) {
            Log.info(POSITION, "file does not exist, render notfound.html");
            pathToRequestedFile = webDirectoryPath + "/notfound.html";
            f = new File(pathToRequestedFile);
            httpStatusCode = 404;
        } 

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            contentLength = f.length(); //get the length of response's content
            
            //write header first
            writeHttpHeadersTo(writer, httpStatusCode, keepAliveDuration, contentLength);
            
            Log.info(POSITION, "opening " + pathToRequestedFile);

            InputStream in = Files.newInputStream(Paths.get(pathToRequestedFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            //write body response
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

            //close buffered reader and buffered writer
            reader.close();
            writer.flush();
        } catch (IOException e) {
            Log.error(POSITION, "file does not exist or cannot write to output stream");
        }
    }

    /**
     * write the http response headers to the writer
     */
    public void writeHttpHeadersTo(BufferedWriter responseWriter, int httpStatusCode, 
                                    int keepAliveDuration, long contentLength) throws IOException {
        HttpHeader completeHeadersResponse = new HttpHeader(httpStatusCode, keepAliveDuration, contentLength);
        responseWriter.write(completeHeadersResponse.toString());
    }
}