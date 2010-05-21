package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.modifyDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraCredentials;



public class ModifyDatastreamTest {
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
        fedora.execute(ingest(testPid).build());
    }

    @After
    public void tearDown() throws Exception {
        fedora.execute(purgeObject(testPid).build());
    }

    @Test
    public void testModifyDatastream() throws Exception {
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        ClientResponse response = fedora.execute(addDatastream(testPid, "baz").content(content).dsLabel(null).build());
        assertEquals(201, response.getStatus());

        // now modify it
        content = "<bar>baz</bar>";
        response = fedora.execute(modifyDatastream(testPid, "baz").content(content).dsLabel(null).build());
        assertEquals(201, response.getStatus());
    }
}
