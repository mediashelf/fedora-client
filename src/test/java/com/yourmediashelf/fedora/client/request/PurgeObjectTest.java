package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeObject;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.IngestResponse;



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
        IngestResponse response = null;
        String pid = null;

        // first, ingest object
        response = ingest(null).execute(fedora());
        assertEquals(201, response.getStatus());
        pid = response.getPid();

        // now delete it
        FedoraResponse purge = purgeObject(pid).execute(fedora());
        assertEquals(200, purge.getStatus());
    }
}
