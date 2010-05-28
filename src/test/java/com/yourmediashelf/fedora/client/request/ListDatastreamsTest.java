package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.listDatastreams;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;



public class ListDatastreamsTest extends FedoraMethodBaseTest {

    @Test
    public void testListDatastreams() throws Exception {
        String dsId = "MDFY";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        FedoraResponse response = fedora().execute(addDatastream(testPid, dsId).content(content));
        assertEquals(201, response.getStatus());

        // list datastreams
        response = fedora().execute(listDatastreams(testPid).format("xml"));
        assertEquals(200, response.getStatus());
        System.out.println("listDS: " + response.getEntity(String.class));
    }
}
