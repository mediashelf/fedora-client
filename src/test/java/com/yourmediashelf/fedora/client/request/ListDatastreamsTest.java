package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.listDatastreams;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;



public class ListDatastreamsTest extends FedoraMethodBaseTest {

    @Test
    public void testListDatastreams() throws Exception {
        String dsId = "MDFY";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        ClientResponse response = fedora().execute(addDatastream(testPid, dsId).content(content).build());
        assertEquals(201, response.getStatus());

        // list datastreams
        response = fedora().execute(listDatastreams(testPid).format("xml").build());
        assertEquals(200, response.getStatus());
        System.out.println("listDS: " + response.getEntity(String.class));

    }
}
