package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraCredentials;



public class PurgeObjectTest {
    private static FedoraCredentials credentials;
    private FedoraClient fedora;

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
    public void testPurgeObject() throws Exception {
        ClientResponse response = null;
        String pid = null;

        // first, ingest object
        response = fedora.execute(ingest(null).build());
        assertEquals(201, response.getStatus());
        pid = response.getEntity(String.class);

        // now delete it
        response = fedora.execute(purgeObject(pid).build());
        assertEquals(204, response.getStatus());
    }
}
