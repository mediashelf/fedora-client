
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.getDatastream;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;

import org.custommonkey.xmlunit.HTMLDocumentBuilder;
import org.custommonkey.xmlunit.TolerantSaxDocumentBuilder;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;

import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class GetDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testGetDatastreamAsHtml() throws Exception {
        FedoraResponse response = null;

        response = fedora().execute(getDatastream(testPid, "DC"));
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);

        assertXpathEvaluatesTo("Datastream Profile HTML Presentation",
                               "/html/head/title",
                               buildTestDocument(result));
    }

    @Test
    public void testGetDatastreamAsXml() throws Exception {
        FedoraResponse response = null;

        response = fedora().execute(getDatastream(testPid, "DC").format("xml"));
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
