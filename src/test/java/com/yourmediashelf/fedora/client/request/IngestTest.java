package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yourmediashelf.fedora.client.response.IngestResponse;



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
            fedora().execute(purgeObject(pid));
        }
    }

    @Test
    public void testIngestDefault() throws Exception {
        IngestResponse response = null;
        URI location = null;

        // empty object, no pid, no namespace
        response = ingest(null).execute(fedora());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/"));
        pid = response.getPid();
    }

    @Test
    public void testIngestWithNamespace() throws Exception {
        IngestResponse response = null;
        URI location = null;

        // empty object, with namespace, but no pid
        response = ingest(null).namespace("foospace").execute(fedora());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/foospace"));
        pid = response.getPid();
    }

    @Test
    public void testIngestWithPid() throws Exception {
        IngestResponse response = null;

        // empty object, with pid
        response = ingest(testPid).execute(fedora());
        assertEquals(201, response.getStatus());
        pid = response.getPid();
        assertEquals(testPid, pid);
    }
}
