package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastreamDissemination;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.DatastreamProfileResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.datastreamProfile.DatastreamProfile;



public class ModifyDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testModifyDatastreamContent() throws Exception {
        String dsId = "MDFY";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        FedoraResponse response;
        response = addDatastream(testPid, dsId).content(content).execute(fedora());
        assertEquals(201, response.getStatus());

        // verify datastream content before modify
        response = getDatastreamDissemination(testPid, dsId).execute(fedora());
        assertEquals(200, response.getStatus());
        assertXMLEqual(content, response.getEntity(String.class));

        // verify datastream properties before modify

        DatastreamProfileResponse dspResponse;
        dspResponse = getDatastream(testPid, dsId).format("xml").execute(fedora());
        assertEquals(200, response.getStatus());
        DatastreamProfile profile = dspResponse.getDatastreamProfile();
        assertEquals(testPid, profile.getPid());
        assertEquals(dsId, profile.getDsID());
        assertEquals("", profile.getDsLabel());
        assertEquals(String.format("%s.0", dsId), profile.getDsVersionID());
        assertNotNull(profile.getDsCreateDate());
        assertEquals("A", profile.getDsState());
        assertEquals("text/xml", profile.getDsMIME());
        assertEquals("", profile.getDsFormatURI());
        assertEquals("X", profile.getDsControlGroup());
        assertEquals(16, profile.getDsSize().intValue());
        assertEquals("true", profile.getDsVersionable());
        assertEquals("", profile.getDsInfoType());
        assertEquals(String.format("%s+%s+%s.0", testPid, dsId, dsId), profile.getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());

        // now modify it
        content = "<baz>quux</baz>";
        String newDsLabel = "asdf";
        response = modifyDatastream(testPid, dsId).content(content).dsLabel(newDsLabel).execute(fedora());
        assertEquals(200, response.getStatus());

        // verify datastream content after modify
        response = getDatastreamDissemination(testPid, dsId).execute(fedora());
        assertEquals(200, response.getStatus());
        assertXMLEqual(content, response.getEntity(String.class));

        // verify datastream properties after modify
        dspResponse = getDatastream(testPid, dsId).format("xml").execute(fedora());
        assertEquals(200, response.getStatus());
        profile = dspResponse.getDatastreamProfile();

        assertEquals(testPid, profile.getPid());
        assertEquals(dsId, profile.getDsID());
        assertEquals(newDsLabel, profile.getDsLabel());
        assertEquals(String.format("%s.1", dsId), profile.getDsVersionID());
        assertNotNull(profile.getDsCreateDate());
        assertEquals("A", profile.getDsState());
        assertEquals("text/xml", profile.getDsMIME());
        assertEquals("", profile.getDsFormatURI());
        assertEquals("X", profile.getDsControlGroup());
        assertEquals(17, profile.getDsSize().intValue());
        assertEquals("true", profile.getDsVersionable());
        assertEquals("", profile.getDsInfoType());
        assertEquals(String.format("%s+%s+%s.1", testPid, dsId, dsId), profile.getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        DateTime lastModifiedDate = new DateTime(fedora().getLastModifiedDate(testPid, "DC"));
        try {
            modifyDatastream(testPid, "DC").dsLabel("foo").lastModifiedDate(lastModifiedDate.minusHours(1)).execute(fedora());
            fail("modifyDatastream succeeded, but should have failed");
        } catch (FedoraClientException expected) {
            assertEquals(409, expected.getStatus());
        }
    }

    @Test
    public void testNullAndEmptyPidAndDsId() throws Exception {
        String dsId = "test";
        try {
            modifyDatastream(null, dsId).execute(fedora());
            modifyDatastream("", dsId).execute(fedora());
            modifyDatastream(null, null).execute(fedora());
            modifyDatastream("", "").execute(fedora());
            modifyDatastream(testPid, null).execute(fedora());
            modifyDatastream(testPid, "").execute(fedora());
            fail("Null or empty pids or dsIds should throw an exception");
        } catch(IllegalArgumentException expected) {

        }
    }
}
