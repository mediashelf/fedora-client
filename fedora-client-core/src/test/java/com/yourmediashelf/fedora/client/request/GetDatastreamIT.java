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

import static com.yourmediashelf.fedora.client.FedoraClient.getDatastream;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;

import org.custommonkey.xmlunit.HTMLDocumentBuilder;
import org.custommonkey.xmlunit.TolerantSaxDocumentBuilder;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;

import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamResponse;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

/**
 * Integration tests for GetDatastream
 *
 * @author Edwin Shin
 *
 */
public class GetDatastreamIT extends BaseFedoraRequestIT {

    @Test
    public void testGetDatastreamAsHtml() throws Exception {
        FedoraResponse response = null;

        response = getDatastream(testPid, "DC").format("html").execute(fedora());
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);

        assertXpathEvaluatesTo("Datastream Profile HTML Presentation",
                               "/html/head/title",
                               buildTestDocument(result));
    }

    @Test
    public void testGetDatastreamAsXml() throws Exception {
        GetDatastreamResponse response = null;

        response = getDatastream(testPid, "DC").format("xml").execute(fedora());
        assertEquals(200, response.getStatus());

        DatastreamProfile profile = response.getDatastreamProfile();
        assertEquals(testPid, profile.getPid());
        assertEquals("DC", profile.getDsID());
    }
    
    @Test
    public void testGetDatastreamAsOfDateTimeAsXML() throws Exception {
    	GetDatastreamResponse response = null;

        response = getDatastream(testPid, "DC").format("xml").asOfDateTime("9999-01-01T00:01:04.567Z").execute(fedora());
        DatastreamProfile profile = response.getDatastreamProfile();
        assertEquals(testPid, profile.getPid());
        assertEquals("DC", profile.getDsID());
    }

    /**
     * Test retrieving the dsState of a datastream.
     *
     * @throws Exception
     */
    @Test
    public void testGetDatastreamState() throws Exception {
    	GetDatastreamResponse response = null;

        response = getDatastream(testPid, "DC").format("xml").execute(fedora());
        assertEquals(200, response.getStatus());

        String dsState = response.getDatastreamProfile().getDsState();
        assertEquals("A", dsState);
    }

    /**
     * Helper method that returns an XHTML document, allowing xmlunit assertions
     * to be made against badly formed HTML source.
     *
     * @param html HTML that may not be well-formed
     * @return a DOM document that has converted the 'plain' HTML source into valid 'XHTML' 
     * @throws Exception
     */
    private Document buildTestDocument(String html) throws Exception {
        TolerantSaxDocumentBuilder tolerantSaxDocumentBuilder =
                new TolerantSaxDocumentBuilder(XMLUnit.newTestParser());
        HTMLDocumentBuilder htmlDocumentBuilder =
                new HTMLDocumentBuilder(tolerantSaxDocumentBuilder);
        return htmlDocumentBuilder.parse(html);
    }
}
