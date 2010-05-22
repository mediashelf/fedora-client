package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeDatastream;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;



public class PurgeDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testPurgeDatastream() throws Exception {
        String content = "<foo>bar</foo>";
        ClientResponse response =
                fedora().execute(addDatastream(testPid, "baz").content(content)
                        .dsLabel(null).build());
        assertEquals(201, response.getStatus());

        // now delete it
        response = fedora().execute(purgeDatastream(testPid, "baz").build());
        assertEquals(204, response.getStatus());
    }
}
