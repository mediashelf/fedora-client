
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.datastreamProfile.DatastreamProfile;

public class AddDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testNewAddDatastream() throws Exception {
        AddDatastreamResponse response = new AddDatastream(testPid, "foo")
                                        .dsLabel("bar")
                                        .content("<foo>?</foo>").execute(fedora());
        assertEquals(201, response.getStatus());
        DatastreamProfile profile = response.getDatastreamProfile();
        assertNotNull(profile);
        assertEquals("bar", profile.getDsLabel());
    }

    @Test
    public void testDefaultValues() throws Exception {
        String content = "<foo>bar</foo>";
        AddDatastreamResponse response = addDatastream(testPid, "baz")
                                           .content(content)
                                           .dsLabel(null).execute(fedora());
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testManagedContent() throws Exception {
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());
        FedoraResponse response =
            fedora().execute(addDatastream(testPid, "MANAGED_DS")
                        .controlGroup("M").content(f));
        assertEquals(201, response.getStatus());
    }

    @Test(expected=FedoraClientException.class)
    public void testMissingContent() throws Exception {
        fedora().execute(addDatastream(testPid, "MANAGED_DS")
                    .controlGroup("M"));
    }
}
