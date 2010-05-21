package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraCredentials;



public class IngestTest {
    private static FedoraCredentials credentials;
    private FedoraClient fedora;
    private static String testPid = "test-rest:1";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String baseUrl = System.getProperty("fedora.test.baseUrl");
        String username = System.getProperty("fedora.test.username");
        String password = System.getProperty("fedora.test.password");
        credentials = new FedoraCredentials(new URL(baseUrl), username, password);
    }

    @Before
    public void setUp() throws Exception {
        fedora = new FedoraClient(credentials);
    }

    @Test
    public void testIngest() throws Exception {
        ClientResponse response = null;
        URI location = null;
        String pid = null;

        // empty object, no pid, no namespace
        response = fedora.execute(ingest(null).build());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/"));
        pid = response.getEntity(String.class);
        fedora.execute(purgeObject(pid).build());

        // empty object, with namespace, but no pid
        response = fedora.execute(ingest(null).namespace("foospace").build());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/foospace"));
        pid = response.getEntity(String.class);
        fedora.execute(purgeObject(pid).build());

        // empty object, with pid
        response = fedora.execute(ingest(testPid).build());
        assertEquals(201, response.getStatus());
        pid = response.getEntity(String.class);
        assertEquals(testPid, pid);
        fedora.execute(purgeObject(testPid).build());
    }
}
