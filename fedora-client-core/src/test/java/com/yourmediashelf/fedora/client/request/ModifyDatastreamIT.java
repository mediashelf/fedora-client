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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.DatastreamProfileResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamResponse;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

public class ModifyDatastreamIT
        extends BaseFedoraRequestIT {

    @Test
    public void testModifyDatastreamContent() throws Exception {
        String dsId = "testModifyDatastreamContent";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        FedoraResponse response;
        response =
                addDatastream(testPid, dsId).content(content).execute();
        assertEquals(201, response.getStatus());

        // verify datastream content before modify
        response = getDatastreamDissemination(testPid, dsId).execute();
        assertEquals(200, response.getStatus());
        assertXMLEqual(content, response.getEntity(String.class));

        // verify datastream properties before modify
        DatastreamProfileResponse dspResponse;
        dspResponse =
                getDatastream(testPid, dsId).format("xml").execute();
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
        assertEquals(String.format("%s+%s+%s.0", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());

        // now modify the content (and label, while we're at it)
        content = "<baz>quux</baz>";
        String newDsLabel = "asdf";
        response =
                modifyDatastream(testPid, dsId).content(content)
                        .dsLabel(newDsLabel).execute();
        assertEquals(200, response.getStatus());

        // verify datastream content after modify
        response = getDatastreamDissemination(testPid, dsId).execute();
        assertEquals(200, response.getStatus());
        assertXMLEqual(content, response.getEntity(String.class));

        // verify datastream properties after modify
        dspResponse =
                getDatastream(testPid, dsId).format("xml").execute();
        assertEquals(200, dspResponse.getStatus());
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
        assertEquals(String.format("%s+%s+%s.1", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        DateTime lastModifiedDate =
                new DateTime(fedora.getLastModifiedDate(testPid, "DC"));
        try {
            modifyDatastream(testPid, "DC").dsLabel("testOptimisticLocking")
                    .lastModifiedDate(lastModifiedDate.minusHours(1))
                    .execute();
            fail("modifyDatastream succeeded, but should have failed");
        } catch (FedoraClientException expected) {
            assertEquals(409, expected.getStatus());
        }
    }

    @Test
    public void testNullAndEmptyPidAndDsId() throws Exception {
        String dsId = "testNullAndEmptyPidAndDsId";
        try {
            modifyDatastream(null, dsId).logMessage("test null pid").execute();
            fail("Null pid should throw an IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        try {
            modifyDatastream("", dsId).logMessage("test empty pid").execute();
            fail("Empty pid should throw an IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        try {
            modifyDatastream(null, null).logMessage("test null pid & dsid").execute();
            fail("Null pid & dsId should throw an IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        try {
            modifyDatastream("", "").logMessage("test empty pid & dsid").execute();
            fail("Empty pid & dsId should throw an IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        try {
            modifyDatastream(testPid, null).logMessage("test null dsid").execute();
            fail("Null dsId should throw an IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        try {
            modifyDatastream(testPid, "").logMessage("test empty dsid").execute();
            fail("Empty dsId should throw an IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    /**
     * @throws Exception
     * @see "http://www.fedora-commons.org/jira/browse/FCREPO-608"
     */
    @Test
    public void testModifyState() throws Exception {
        String dsId = "testModifyState";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        FedoraResponse response;
        response =
                addDatastream(testPid, dsId).content(content).execute();
        assertEquals(201, response.getStatus());

        // verify datastream properties before modify
        GetDatastreamResponse gdResponse;
        gdResponse =
                getDatastream(testPid, dsId).format("xml").execute();
        assertEquals(200, gdResponse.getStatus());
        DatastreamProfile profile = gdResponse.getDatastreamProfile();
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
        assertEquals(String.format("%s+%s+%s.0", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());

        // now modify the state
        String newDsState = "I";
        response =
                modifyDatastream(testPid, dsId).dsState(newDsState)
                        .execute();
        assertEquals(200, response.getStatus());

        // verify datastream properties after modify
        gdResponse = getDatastream(testPid, dsId).execute();
        assertEquals(200, response.getStatus());
        profile = gdResponse.getDatastreamProfile();

        assertEquals(testPid, profile.getPid());
        assertEquals(dsId, profile.getDsID());
        assertEquals("", profile.getDsLabel());
        assertEquals(String.format("%s.1", dsId), profile.getDsVersionID());
        assertNotNull(profile.getDsCreateDate());
        assertEquals(newDsState, profile.getDsState());
        assertEquals("text/xml", profile.getDsMIME());
        assertEquals("", profile.getDsFormatURI());
        assertEquals("X", profile.getDsControlGroup());
        assertEquals(16, profile.getDsSize().intValue());
        assertEquals("true", profile.getDsVersionable());
        assertEquals("", profile.getDsInfoType());
        assertEquals(String.format("%s+%s+%s.1", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());

        // now modify the label
        String newDsLabel = "testModifyState";
        response =
                modifyDatastream(testPid, dsId).dsLabel(newDsLabel)
                        .execute();
        assertEquals(200, response.getStatus());

        // verify datastream properties after modify
        gdResponse = getDatastream(testPid, dsId).execute();
        assertEquals(200, response.getStatus());
        profile = gdResponse.getDatastreamProfile();

        assertEquals(testPid, profile.getPid());
        assertEquals(dsId, profile.getDsID());
        assertEquals(newDsLabel, profile.getDsLabel());
        assertEquals(String.format("%s.2", dsId), profile.getDsVersionID());
        assertNotNull(profile.getDsCreateDate());
        assertEquals(newDsState, profile.getDsState());
        assertEquals("text/xml", profile.getDsMIME());
        assertEquals("", profile.getDsFormatURI());
        assertEquals("X", profile.getDsControlGroup());
        assertEquals(16, profile.getDsSize().intValue());
        assertEquals("true", profile.getDsVersionable());
        assertEquals("", profile.getDsInfoType());
        assertEquals(String.format("%s+%s+%s.2", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());
    }

    @Test
    /**
     * Verifies setting versionable property.
     *
     * @see "http://www.fedora-commons.org/jira/browse/FCREPO-699"
     */
    public void testModifyVersionable() throws Exception {
        String dsId = "testModifyVersionable";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        FedoraResponse response;
        response =
                addDatastream(testPid, dsId).content(content).execute();
        assertEquals(201, response.getStatus());

        // verify datastream properties before modify
        DatastreamProfileResponse dspResponse;
        dspResponse =
                getDatastream(testPid, dsId).format("xml").execute();
        assertEquals(200, dspResponse.getStatus());
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
        assertEquals(String.format("%s+%s+%s.0", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());

        // now modify versionable
        boolean newVersionable = false;
        ModifyDatastream modifyDS = modifyDatastream(testPid, dsId)
            .versionable(newVersionable)
            .logMessage("set versionable = " + newVersionable);
        List<String> versionable = modifyDS.getQueryParam("versionable");
        assertEquals(1, versionable.size());
        assertEquals(Boolean.toString(newVersionable), versionable.get(0));
        response = modifyDS.execute();
        assertEquals(200, response.getStatus());

        // verify datastream properties after modify
        dspResponse = getDatastream(testPid, dsId).execute();
        assertEquals(200, response.getStatus());
        profile = dspResponse.getDatastreamProfile();

        assertEquals(testPid, profile.getPid());
        assertEquals(dsId, profile.getDsID());
        assertEquals("", profile.getDsLabel());
        assertEquals(String.format("%s.1", dsId), profile.getDsVersionID());
        assertNotNull(profile.getDsCreateDate());
        assertEquals("A", profile.getDsState());
        assertEquals("text/xml", profile.getDsMIME());
        assertEquals("", profile.getDsFormatURI());
        assertEquals("X", profile.getDsControlGroup());
        assertEquals(16, profile.getDsSize().intValue());
        assertEquals(Boolean.toString(newVersionable), profile
                .getDsVersionable());
        assertEquals("", profile.getDsInfoType());
        assertEquals(String.format("%s+%s+%s.1", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());

        // empty modify operation
        response =
                modifyDatastream(testPid, dsId)
                        .logMessage("don't actually modify anything")
                        .execute();
        assertEquals(200, response.getStatus());

        // verify datastream properties after modify
        dspResponse = getDatastream(testPid, dsId).execute();
        assertEquals(200, response.getStatus());
        profile = dspResponse.getDatastreamProfile();

        assertEquals(testPid, profile.getPid());
        assertEquals(dsId, profile.getDsID());
        assertEquals("", profile.getDsLabel());
        assertEquals(String.format("%s.2", dsId), profile.getDsVersionID());
        assertNotNull(profile.getDsCreateDate());
        assertEquals("A", profile.getDsState());
        assertEquals("text/xml", profile.getDsMIME());
        assertEquals("", profile.getDsFormatURI());
        assertEquals("X", profile.getDsControlGroup());
        assertEquals(16, profile.getDsSize().intValue());
        assertEquals(Boolean.toString(newVersionable), profile
                .getDsVersionable());
        assertEquals("", profile.getDsInfoType());
        assertEquals(String.format("%s+%s+%s.2", testPid, dsId, dsId), profile
                .getDsLocation());
        assertEquals("", profile.getDsLocationType());
        assertEquals("DISABLED", profile.getDsChecksumType());
        assertEquals("none", profile.getDsChecksum());
    }
    
    /**
     * Test modifying a managed RELS-EXT datastream.
     * 
     * <p>This test will fail against Fedora 3.5 and earlier as described in 
     * <a href="https://jira.duraspace.org/browse/FCREPO-1045">FCREPO-1045</a>.
     * @throws Exception
     */
    @Test
	public void testModifyManagedRelsExt() throws Exception {
		String dsId = "RELS-EXT";

		AddDatastreamResponse adr = addDatastream(testPid, dsId)
				.controlGroup("M")
				.versionable(false)
				.content(
						"<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">"
								+ "<rdf:Description rdf:about=\"info:fedora/"
								+ testPid + "\" /></rdf:RDF>").execute();

		assertEquals(201, adr.getStatus());

		ModifyDatastreamResponse mdr = modifyDatastream(testPid, dsId).dsLabel(
				"a test label").logMessage("testModifyManagedRelsExt").execute();
		assertEquals(200, mdr.getStatus());

		// The fix for FCREPO-1045 skips validation if the dsLocation is
		// unchanged or copy://. Want to ensure that updating the content of an 
		// unversioned RELS-EXT still triggers validation.
		mdr = modifyDatastream(testPid, dsId).versionable(false)
				.logMessage("testModifyManagedRelsExt setting versionable false")
				.execute();
		GetDatastreamResponse gdr = getDatastream(testPid, dsId).execute();
        DatastreamProfile dsp = gdr.getDatastreamProfile();
        assertFalse(Boolean.parseBoolean(dsp.getDsVersionable()));
		
		try {
			modifyDatastream(testPid, dsId)
				.content("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">"
						+ "<rdf:Description rdf:about=\"info:fedora/"
						+ "AnInvalidPID\" /></rdf:RDF>")
				.logMessage("testModifyManagedRelsExt request that should fail")
				.execute();
			fail("Should have thrown an exception because of an invalid RELS-EXT");
		} catch (FedoraClientException e) {
		}
	}
}
