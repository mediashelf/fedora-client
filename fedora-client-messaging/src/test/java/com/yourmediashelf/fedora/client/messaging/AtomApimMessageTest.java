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
/**
 * 
 */
package com.yourmediashelf.fedora.client.messaging;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yourmediashelf.fedora.util.DateUtility;


/**
 * <p>Test parsing of Fedora API-M messages.
 * 
 * @author Edwin Shin
 *
 */
public class AtomApimMessageTest {

    private static AtomApimMessage ingest;

    private static AtomApimMessage addDatastream;

    private static AtomApimMessage modifyDatastreamByValue;

    private static AtomApimMessage purgeDatastream;

    private static AtomApimMessage purgeObject;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ingest = getResourceAsMessage("ingest.xml");
        addDatastream = getResourceAsMessage("addDatastream.xml");
        modifyDatastreamByValue =
                getResourceAsMessage("modifyDatastreamByValue.xml");
        purgeDatastream = getResourceAsMessage("purgeDatastream.xml");
        purgeObject = getResourceAsMessage("purgeObject.xml");
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getBaseUrl()}.
     */
    @Test
    public void testGetBaseUrl() {
        String baseUrl = "http://localhost:8080/fedora";
        assertEquals(baseUrl, ingest.getBaseUrl());
        assertEquals(baseUrl, addDatastream.getBaseUrl());
        assertEquals(baseUrl, modifyDatastreamByValue.getBaseUrl());
        assertEquals(baseUrl, purgeDatastream.getBaseUrl());
        assertEquals(baseUrl, purgeObject.getBaseUrl());
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getDate()}.
     */
    @Test
    public void testGetDate() {
        assertEquals("2012-08-30T09:19:39.01Z", DateUtility
                .getXSDDateTime(ingest.getDate()));
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getMethodName()}.
     */
    @Test
    public void testGetMethodName() {
        assertEquals("ingest", ingest.getMethodName());
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getPID()}.
     */
    @Test
    public void testGetPID() {
        assertEquals("test-rest:1", ingest.getPID());
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getAuthor()}.
     */
    @Test
    public void testGetAuthor() {
        assertEquals("fedoraAdmin", ingest.getAuthor());
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getFormat()}.
     */
    @Test
    public void testGetFormat() {
        assertEquals("info:fedora/fedora-system:ATOM-APIM-1.0", ingest
                .getFormat());
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getServerVersion()}.
     */
    @Test
    public void testGetServerVersion() {
        assertEquals("3.6.1-SNAPSHOT", ingest.getServerVersion());
    }

    /**
     * Test method for {@link com.yourmediashelf.fedora.client.messaging.AtomApimMessage#getReturnVal()}.
     */
    @Test
    public void testGetReturnVal() {
        assertEquals("test-rest:1", ingest.getReturnVal());
    }

    /**
     * Convenience method that returns an {@link AtomApimMessage} from a File.
     * 
     * @param filename name of the File, e.g. "foo.xml" (assumed to be in src/test/resources).
     * The File must be a Fedora API-M message serialized as an Atom Entry.
     * @return an AtomApimMessage that represents the file
     * @throws IOException
     */
    private static AtomApimMessage getResourceAsMessage(String filename)
            throws IOException {
        return new AtomApimMessage(FileUtils.readFileToString(new File(
                "src/test/resources/" + filename)));
    }

}
