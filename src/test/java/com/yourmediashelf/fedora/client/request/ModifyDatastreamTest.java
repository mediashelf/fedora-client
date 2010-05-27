package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.getDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.getDatastreamDissemination;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.modifyDatastream;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;



public class ModifyDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testModifyDatastreamContent() throws Exception {
        String dsId = "MDFY";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        ClientResponse response = fedora().execute(addDatastream(testPid, dsId).content(content).build());
        assertEquals(201, response.getStatus());

        // verify datastream content before modify
        response = fedora().execute(getDatastreamDissemination(testPid, dsId).build());
        assertEquals(200, response.getStatus());
        assertXMLEqual(content, response.getEntity(String.class));

        // verify datastream properties before modify
        response = fedora().execute(getDatastream(testPid, dsId).format("xml").build());
        assertEquals(200, response.getStatus());
        String datastreamProfile = response.getEntity(String.class);

        assertXpathEvaluatesTo(testPid, "/datastreamProfile/@pid", datastreamProfile);
        assertXpathEvaluatesTo(dsId, "/datastreamProfile/@dsID", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsLabel", datastreamProfile);
        assertXpathEvaluatesTo(String.format("%s.0", dsId), "/datastreamProfile/dsVersionID", datastreamProfile);
        assertXpathExists("/datastreamProfile/dsCreateDate", datastreamProfile);
        assertXpathEvaluatesTo("A", "/datastreamProfile/dsState", datastreamProfile);
        assertXpathEvaluatesTo("text/xml", "/datastreamProfile/dsMIME", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsFormatURI", datastreamProfile);
        assertXpathEvaluatesTo("X", "/datastreamProfile/dsControlGroup", datastreamProfile);
        assertXpathEvaluatesTo("16", "/datastreamProfile/dsSize", datastreamProfile);
        assertXpathEvaluatesTo("true", "/datastreamProfile/dsVersionable", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsInfoType", datastreamProfile);
        assertXpathEvaluatesTo(String.format("%s+%s+%s.0", testPid, dsId, dsId), "/datastreamProfile/dsLocation", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsLocationType", datastreamProfile);
        assertXpathEvaluatesTo("DISABLED", "/datastreamProfile/dsChecksumType", datastreamProfile);
        assertXpathEvaluatesTo("none", "/datastreamProfile/dsChecksum", datastreamProfile);

        // now modify it
        content = "<baz>quux</baz>";
        String newDsLabel = "asdf";
        response = fedora().execute(modifyDatastream(testPid, dsId).content(content).dsLabel(newDsLabel).build());
        assertEquals(200, response.getStatus());

        // verify datastream content after modify
        response = fedora().execute(getDatastreamDissemination(testPid, dsId).build());
        assertEquals(200, response.getStatus());
        assertXMLEqual(content, response.getEntity(String.class));

        // verify datastream properties after modify
        response = fedora().execute(getDatastream(testPid, dsId).format("xml").build());
        assertEquals(200, response.getStatus());
        datastreamProfile = response.getEntity(String.class);

        assertXpathEvaluatesTo(testPid, "/datastreamProfile/@pid", datastreamProfile);
        assertXpathEvaluatesTo(dsId, "/datastreamProfile/@dsID", datastreamProfile);
        assertXpathEvaluatesTo(newDsLabel, "/datastreamProfile/dsLabel", datastreamProfile);
        assertXpathEvaluatesTo(String.format("%s.1", dsId), "/datastreamProfile/dsVersionID", datastreamProfile);
        assertXpathExists("/datastreamProfile/dsCreateDate", datastreamProfile);
        assertXpathEvaluatesTo("A", "/datastreamProfile/dsState", datastreamProfile);
        assertXpathEvaluatesTo("text/xml", "/datastreamProfile/dsMIME", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsFormatURI", datastreamProfile);
        assertXpathEvaluatesTo("X", "/datastreamProfile/dsControlGroup", datastreamProfile);
        assertXpathEvaluatesTo("17", "/datastreamProfile/dsSize", datastreamProfile);
        assertXpathEvaluatesTo("true", "/datastreamProfile/dsVersionable", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsInfoType", datastreamProfile);
        assertXpathEvaluatesTo(String.format("%s+%s+%s.1", testPid, dsId, dsId), "/datastreamProfile/dsLocation", datastreamProfile);
        assertXpathEvaluatesTo("", "/datastreamProfile/dsLocationType", datastreamProfile);
        assertXpathEvaluatesTo("DISABLED", "/datastreamProfile/dsChecksumType", datastreamProfile);
        assertXpathEvaluatesTo("none", "/datastreamProfile/dsChecksum", datastreamProfile);
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        DateTime lastModifiedDate = fedora().getLastModifiedDate(testPid, "DC");
        try {
            fedora().execute(modifyDatastream(testPid, "DC").dsLabel("foo").lastModifiedDate(lastModifiedDate.minusHours(1)).build());
            fail("modifyDatastream succeeded, but should have failed");
        } catch (FedoraClientException expected) {
        }
    }
}
