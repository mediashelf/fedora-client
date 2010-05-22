package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.modifyDatastream;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;



public class ModifyDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testModifyDatastream() throws Exception {
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        ClientResponse response = fedora().execute(addDatastream(testPid, "baz").content(content).dsLabel(null).build());
        assertEquals(201, response.getStatus());

        // now modify it
        content = "<bar>baz</bar>";
        response = fedora().execute(modifyDatastream(testPid, "baz").content(content).dsLabel(null).build());
        assertEquals(200, response.getStatus());
    }
}
