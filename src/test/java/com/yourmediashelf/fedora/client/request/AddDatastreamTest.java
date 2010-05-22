
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;

public class AddDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testDefaultValues() throws Exception {
        String content = "<foo>bar</foo>";
        ClientResponse response =
                fedora().execute(addDatastream(testPid, "baz").content(content)
                        .dsLabel(null).build());
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testManagedContent() throws Exception {
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());
        ClientResponse response =
            fedora().execute(addDatastream(testPid, "MANAGED_DS")
                        .controlGroup("M").content(f).build());
        assertEquals(201, response.getStatus());
    }

    @Test(expected=FedoraClientException.class)
    public void testMissingContent() throws Exception {
        fedora().execute(addDatastream(testPid, "MANAGED_DS")
                    .controlGroup("M").build());
    }
}
