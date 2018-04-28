package com.adobe.test.webserver;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    public static final String projectPath = System.getProperty("user.dir");
    public static final String basePath = projectPath + "/resources";
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/html" media type.
     *
     * @return InputStream that will be returned as a text/html response.
     */
    @GET
    @Path("{filename}")
    @Produces(MediaType.TEXT_HTML)
    public InputStream getResponse(@PathParam("filename") String filename) {
        if(filename.equals("index.html")){
            try{
                Thread.sleep(5000);
            }
            catch(InterruptedException e){
                // pass
            }
        }
        System.out.println(filename);
        try{
            File f = new File(basePath + "/" + filename);
            return new FileInputStream(f);
        }
        catch(FileNotFoundException e){
            String response = "<body><h1>File not found</h1></body>";
            InputStream stream  = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            return stream;
        }
    }
}
