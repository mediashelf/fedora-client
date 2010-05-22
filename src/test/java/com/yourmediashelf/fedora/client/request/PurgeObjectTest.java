package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;



public class PurgeObjectTest extends FedoraMethodBaseTest {

    @Override
    @Before
    public void setUp() throws Exception {
        // do nothing
    }

    @Override
    @After
    public void tearDown() throws Exception {
        // do nothing
    }

    @Test
    public void testPurgeObject() throws Exception {
        ClientResponse response = null;
        String pid = null;

        // first, ingest object
        response = fedora().execute(ingest(null).build());
        assertEquals(201, response.getStatus());
        pid = response.getEntity(String.class);

        // now delete it
        response = fedora().execute(purgeObject(pid).build());
        assertEquals(204, response.getStatus());
    }
}
