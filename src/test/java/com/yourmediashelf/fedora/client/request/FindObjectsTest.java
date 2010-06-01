
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.findObjects;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class FindObjectsTest extends BaseFedoraRequestTest {

    @Test
    public void testFindObjects() throws Exception {
        FedoraResponse response = null;

        response = findObjects().pid(true).terms("*").resultFormat("xml").execute(fedora());
        assertEquals(200, response.getStatus());
    }
}
