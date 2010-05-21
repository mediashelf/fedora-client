
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.getDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.custommonkey.xmlunit.HTMLDocumentBuilder;
import org.custommonkey.xmlunit.TolerantSaxDocumentBuilder;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraCredentials;

public class GetDatastreamTest {

    private static FedoraCredentials credentials;

    private FedoraClient fedora;

    private static String testPid = "test-rest:1";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String baseUrl = System.getProperty("fedora.test.baseUrl");
        String username = System.getProperty("fedora.test.username");
        String password = System.getProperty("fedora.test.password");
        credentials =
                new FedoraCredentials(new URL(baseUrl), username, password);
    }

    @Before
    public void setUp() throws Exception {
        fedora = new FedoraClient(credentials);
        fedora.execute(ingest(testPid).build());
    }

    @After
    public void tearDown() throws Exception {
        fedora.execute(purgeObject(testPid).build());
    }

    @Test
    public void testGetDatastreamAsHtml() throws Exception {
        ClientResponse response = null;

        response = fedora.execute(getDatastream(testPid, "DC").build());
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);

        assertXpathEvaluatesTo("Datastream Profile HTML Presentation",
                               "/html/head/title",
                               buildTestDocument(result));
    }

    @Test
    public void testGetDatastreamAsXml() throws Exception {
        ClientResponse response = null;

        response = fedora.execute(getDatastream(testPid, "DC").format("xml").build());
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
