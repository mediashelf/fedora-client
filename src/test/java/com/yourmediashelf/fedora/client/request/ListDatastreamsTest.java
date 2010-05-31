package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.listDatastreams;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.ListDatastreamsResponse;



public class ListDatastreamsTest extends FedoraMethodBaseTest {

    @Test
    public void testListDatastreams() throws Exception {
        String dsId = "MDFY";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        addDatastream(testPid, dsId).content(content).execute(fedora());

        // list datastreams
        ListDatastreamsResponse response = listDatastreams(testPid).execute(fedora());
        assertEquals(200, response.getStatus());
        System.out.println(response.getEntity(String.class));
        /*
        List<DatastreamType> datastreams = response.getDatastreams();
        boolean found = false;
        for (DatastreamType d : datastreams) {
            if (d.getDsid().equals(dsId)) {
                found = true;
            }
        }
        assertTrue(found);
        */
    }
}
