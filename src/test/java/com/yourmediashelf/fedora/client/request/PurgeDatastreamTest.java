package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeDatastream;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;



public class PurgeDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testPurgeDatastream() throws Exception {
        String content = "<foo>bar</foo>";
        FedoraResponse response =
                fedora().execute(addDatastream(testPid, "baz").content(content)
                        .dsLabel(null));
        assertEquals(201, response.getStatus());

        // now delete it
        response = fedora().execute(purgeDatastream(testPid, "baz"));
        assertEquals(204, response.getStatus());
    }
}
