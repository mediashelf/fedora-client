
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.getDatastream;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;

import org.custommonkey.xmlunit.HTMLDocumentBuilder;
import org.custommonkey.xmlunit.TolerantSaxDocumentBuilder;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;

public class GetDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testGetDatastreamAsHtml() throws Exception {
        ClientResponse response = null;

        response = fedora().execute(getDatastream(testPid, "DC").build());
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);

        assertXpathEvaluatesTo("Datastream Profile HTML Presentation",
                               "/html/head/title",
                               buildTestDocument(result));
    }

    @Test
    public void testGetDatastreamAsXml() throws Exception {
        ClientResponse response = null;

        response = fedora().execute(getDatastream(testPid, "DC").format("xml").build());
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);

        assertXpathExists(String.format("/datastreamProfile[@pid='%s']", testPid),
                          result);
    }

    private Document buildTestDocument(String html) throws Exception {
        TolerantSaxDocumentBuilder tolerantSaxDocumentBuilder =
                new TolerantSaxDocumentBuilder(XMLUnit.newTestParser());
        HTMLDocumentBuilder htmlDocumentBuilder =
                new HTMLDocumentBuilder(tolerantSaxDocumentBuilder);
        return htmlDocumentBuilder.parse(html);
    }
}
