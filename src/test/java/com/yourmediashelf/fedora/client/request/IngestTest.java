package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;



public class IngestTest extends FedoraMethodBaseTest {

    private String pid;

    @Override
    @Before
    public void setUp() throws Exception {
        // do nothing
    }

    @Override
    @After
    public void tearDown() throws Exception {
        if (pid != null) {
            fedora().execute(purgeObject(pid).build());
        }
    }

    @Test
    public void testIngestDefault() throws Exception {
        ClientResponse response = null;
        URI location = null;

        // empty object, no pid, no namespace
        response = fedora().execute(ingest(null).build());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/"));
        pid = response.getEntity(String.class);
    }

    @Test
    public void testIngestWithNamespace() throws Exception {
        ClientResponse response = null;
        URI location = null;

        // empty object, with namespace, but no pid
        response = fedora().execute(ingest(null).namespace("foospace").build());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/foospace"));
        pid = response.getEntity(String.class);
    }

    @Test
    public void testIngestWithPid() throws Exception {
        ClientResponse response = null;

        // empty object, with pid
        response = fedora().execute(ingest(testPid).build());
        assertEquals(201, response.getStatus());
        pid = response.getEntity(String.class);
        assertEquals(testPid, pid);
    }
}
