package com.adobe.webserver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.adobe.webserver.http.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MyResourceTest {

    private WebTarget target;
    private Thread testserver;

    @Before
    public void setUp() throws Exception {
        // start the server
        testserver = new Thread(new HttpServer(9092, "resources"));
        testserver.start();

        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target("http://localhost:9092/");
    }

    @After
    public void tearDown() throws Exception {
        testserver.stop();
    }

    /**
     * Test to see that the 200 status code is sent in the response.
     */
    @Test
    public void testGetIt() {
        int statusCode1 = target.path("index.html").request().get().getStatusInfo().getStatusCode();
        int statusCode2 = target.path("blabla.html").request().get().getStatusInfo().getStatusCode();

        assertEquals(200, statusCode1);
        assertEquals(404, statusCode2);
    }
}
