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
import static com.yourmediashelf.fedora.client.FedoraClient.upload;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamResponse;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;
import com.yourmediashelf.fedora.util.ChecksumUtility;
import com.yourmediashelf.fedora.util.XmlSerializer;

public class AddDatastreamIT
        extends BaseFedoraRequestIT {
    
    @Test
    public void testNewAddDatastream() throws Exception {
        String dsid = "testNewAddDatastream";
        AddDatastreamResponse response =
                new AddDatastream(testPid, dsid).dsLabel("bar")
                        .content("<foo>?</foo>").execute();
        assertEquals(201, response.getStatus());
        String expectedLocation =
                String.format("%s/objects/%s/datastreams/%s", getCredentials()
                        .getBaseUrl().toString(), testPid, dsid);
        assertEquals(expectedLocation, response.getLocation().toString());
        DatastreamProfile profile = response.getDatastreamProfile();
        assertNotNull(profile);
        assertEquals("bar", profile.getDsLabel());
        assertEquals(0, profile.getDsAltID().size());
    }

    @Test
    public void testManagedContent() throws Exception {
        String dsid = "testManagedContent";
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());
        FedoraResponse response =
                addDatastream(testPid, dsid).controlGroup("M").content(f)
                        .execute();
        assertEquals(201, response.getStatus());
    }

    @Test(expected = FedoraClientException.class)
    public void testMissingManagedContent() throws Exception {
        String dsid = "testMissingManagedContent";
        addDatastream(testPid, dsid).controlGroup("M").execute();
    }

    @Test
    public void testAddByLocation() throws Exception {
        String dsid = "testAddByLocation";
        String dsLocation =
                String.format("%s/objects/%s/datastreams/DC/content",
                              getCredentials().getBaseUrl().toString(),
                              testPid);
        AddDatastreamResponse response =
                addDatastream(testPid, dsid).dsLocation(dsLocation)
                        .controlGroup("R").execute();
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testDefaultValues() throws Exception {
        String dsid = "testDefaultValues";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse response =
                addDatastream(testPid, dsid).content(content)
                        .logMessage("testDefaultValues").execute();
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testAltIds() throws Exception {
        String dsid = "testAltIds";
        String content = "<test>altIds</test>";
        List<String> altIds = Arrays.asList("foo", "bar", "baz", "quuz");
        AddDatastreamResponse response =
                addDatastream(testPid, dsid).altIDs(altIds).content(content)
                        .execute();
        assertEquals(201, response.getStatus());
        assertTrue(response.getDatastreamProfile().getDsAltID()
                .containsAll(altIds));
    }

    @Test
    public void testDsLabel() throws Exception {
        String dsid = "testDsLabel";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse response =
                addDatastream(testPid, dsid).content(content)
                        .dsLabel("testDsLabel").execute();
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testDsState() throws Exception {
        String[] states = {"A", "I", "D"};
        String content = "<foo>bar</foo>";
        AddDatastreamResponse response = null;
        for (String state : states) {
            response =
                    addDatastream(testPid, state).content(content)
                            .dsState(state)
                            .logMessage("testDsState with state: " + state)
                            .execute();
            assertEquals(201, response.getStatus());
        }
    }

    /**
     * Ignored pending resolution of https://jira.duraspace.org/browse/FCREPO-190
     * @throws Exception
     */
    @Ignore
    @Test
    public void testInlineChecksum() throws Exception {
        String dsid = "testChecksum";
        String content = "        <foo>bar</foo>      ";
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        XmlSerializer.canonicalize(content, bout);
        content = bout.toString("UTF-8");
        String[] types = {"MD5"};
        String value;

        AddDatastreamResponse response = null;

        for (String type : types) {
            dsid = dsid + type;
            value = ChecksumUtility.checksum(type, content);
            response =
                    addDatastream(testPid, dsid).content(content)
                            .checksumType(type).checksum(value).logMessage(type
                                    + ": " + value).execute();
            assertEquals(201, response.getStatus());
        }
    }

    @Test
    public void testManagedChecksum() throws Exception {
        FedoraResponse response = null;
        String dsid = "testManagedChecksum";
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());

        String[] types = {"MD5", "SHA-1"};
        String checksum;

        for (String type : types) {
            dsid = dsid + type;
            checksum = ChecksumUtility.checksum(type, new FileInputStream(f));
            response = addDatastream(testPid, dsid).controlGroup("M").content(f)
                    .checksumType(type).checksum(checksum).execute();
            assertEquals(201, response.getStatus());
            GetDatastreamResponse r = getDatastream(testPid, dsid).validateChecksum(true).execute();
            assertTrue(r.isChecksumValid());
        }
    }

    @Test
    public void testManagedChecksumByLocation() throws Exception {
        FedoraResponse response = null;
        String dsid = "testManagedChecksumByLocation";
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());

        String dsLocation = upload(f).execute().getUploadLocation();
        assertTrue("unexpected value for dsLocation: \"" + dsLocation + "\"",
                   dsLocation.startsWith("uploaded://"));

        String[] types = {"MD5", "SHA-1"};
        String checksum;

        for (String type : types) {
            dsid = dsid + type;
            checksum = ChecksumUtility.checksum(type, new FileInputStream(f));
            response = addDatastream(testPid, dsid).controlGroup("M").dsLocation(dsLocation)
                    .checksumType(type).checksum(checksum).execute();
            assertEquals(201, response.getStatus());
            GetDatastreamResponse r = getDatastream(testPid, dsid).validateChecksum(true).execute();
            assertTrue(r.isChecksumValid());
        }
    }

    @Ignore
    @Test
    public void testManagedChecksumByHttpLocation() throws Exception {
        FedoraResponse response = null;
        String dsid = "testManagedChecksumByHttpLocation";

        URL baseUrl = getCredentials().getBaseUrl();
        String dsLocation = String.format("%s://%s:%s/tomcat.gif",
                                          baseUrl.getProtocol(),
                                          baseUrl.getHost(),
                                          baseUrl.getPort());

        response = addDatastream(testPid, dsid).controlGroup("M").dsLocation(dsLocation)
                .checksumType("MD5").checksum("b8ebd781db53b856efa8e873fa4d2f6e").execute();
        assertEquals(201, response.getStatus());
        GetDatastreamResponse r = getDatastream(testPid, dsid).validateChecksum(true).execute();
        assertTrue(r.isChecksumValid());
    }
}
