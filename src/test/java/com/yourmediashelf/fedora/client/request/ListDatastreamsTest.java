package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.listDatastreams;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.ListDatastreamsResponse;
import com.yourmediashelf.fedora.client.response.objectDatastreams.DatastreamType;



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
        assertEquals(testPid, response.getPid());
        // response baseUrl is appending a /
        String baseUrl = getCredentials().getBaseUrl().toString() + "/";
        assertEquals(baseUrl, response.getBaseUrl());
        assertNull(response.getAsOfDateTime());

        List<DatastreamType> datastreams = response.getDatastreams();
        boolean found = false;
        for (DatastreamType d : datastreams) {
            if (d.getDsid().equals(dsId)) {
                found = true;
            }
        }
        assertTrue("Expected to find dsId among the list of datastreams", found);
    }
}
