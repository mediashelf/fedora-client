
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
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

public class AddDatastreamTest extends BaseFedoraRequestTest {

    @Test
    public void testNewAddDatastream() throws Exception {
        String dsid = "testNewAddDatastream";
        AddDatastreamResponse response = new AddDatastream(testPid, dsid)
                                        .dsLabel("bar")
                                        .content("<foo>?</foo>").execute(fedora());
        assertEquals(201, response.getStatus());
        String expectedLocation = String.format("%s/objects/%s/datastreams/%s",
                                                getCredentials().getBaseUrl().toString(),
                                                testPid,
                                                dsid);
        assertEquals(expectedLocation, response.getLocation().toString());
        DatastreamProfile profile = response.getDatastreamProfile();
        assertNotNull(profile);
        assertEquals("bar", profile.getDsLabel());
        assertEquals(0, profile.getDsAltID().size());
    }

    @Test
    public void testDefaultValues() throws Exception {
        String dsid = "testDefaultValues";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse response = addDatastream(testPid, dsid)
                                           .content(content)
                                           .dsLabel(null).execute(fedora());
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testManagedContent() throws Exception {
        String dsid = "MANAGED_DS";
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());
        FedoraResponse response =
            fedora().execute(addDatastream(testPid, dsid)
                        .controlGroup("M").content(f));
        assertEquals(201, response.getStatus());
    }

    @Test(expected=FedoraClientException.class)
    public void testMissingManagedContent() throws Exception {
        String dsid = "testMissingManagedContent";
        fedora().execute(addDatastream(testPid, dsid)
                    .controlGroup("M"));
    }

    @Test
    public void testAddByLocation() throws Exception {
        String dsid = "testAddByLocation";
        String dsLocation = String.format("%s/objects/%s/datastreams/DC/content",
                                          getCredentials().getBaseUrl().toString(), testPid);
        addDatastream(testPid, dsid).dsLocation(dsLocation).controlGroup("R").execute(fedora());
    }
}
