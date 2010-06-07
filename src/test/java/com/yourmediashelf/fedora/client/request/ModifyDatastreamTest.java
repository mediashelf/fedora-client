/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;



public class ModifyDatastreamTest extends BaseFedoraRequestTest {

    @Test
    public void testModifyDatastreamContent() throws Exception {
        String dsId = "testModifyDatastreamContent";
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
            modifyDatastream(testPid, "DC").dsLabel("testOptimisticLocking").lastModifiedDate(lastModifiedDate.minusHours(1)).execute(fedora());
            fail("modifyDatastream succeeded, but should have failed");
        } catch (FedoraClientException expected) {
            assertEquals(409, expected.getStatus());
        }
    }

    @Test
    public void testNullAndEmptyPidAndDsId() throws Exception {
        String dsId = "testNullAndEmptyPidAndDsId";
        try {
            modifyDatastream(null, dsId).logMessage("test null pid").execute(fedora());
            modifyDatastream("", dsId).logMessage("test empty pid").execute(fedora());
            modifyDatastream(null, null).logMessage("test null pid & dsid").execute(fedora());
            modifyDatastream("", "").logMessage("test empty pid & dsid").execute(fedora());
            modifyDatastream(testPid, null).logMessage("test null dsid").execute(fedora());
            modifyDatastream(testPid, "").logMessage("test empty dsid").execute(fedora());
            fail("Null or empty pids or dsIds should throw an exception");
        } catch(IllegalArgumentException expected) {

        }
    }
}
