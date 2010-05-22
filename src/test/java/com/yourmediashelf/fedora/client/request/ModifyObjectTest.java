package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.getObjectProfile;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.getObjectXML;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.modifyObject;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.junit.Test;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;



public class ModifyObjectTest extends FedoraMethodBaseTest {

    @Test
    public void testModifyObjectLabel() throws Exception {
        String modifiedLabel = "A modified label";
        ClientResponse response = fedora().execute(modifyObject(testPid).label(modifiedLabel).build());
        assertEquals(200, response.getStatus());

        response = fedora().execute(getObjectProfile(testPid).format("xml").build());

        //test the response
        String objectProfile = response.getEntity(String.class);
        assertXpathEvaluatesTo(modifiedLabel, "/objectProfile/objLabel", objectProfile);
    }

    @Test
    public void testModifyObjectState() throws Exception {
        ClientResponse response = fedora().execute(modifyObject(testPid).state("I").build());
        assertEquals(200, response.getStatus());

        //test the response
        response = fedora().execute(getObjectXML(testPid).build());
        XpathEngine engine = getXpathEngine("f", "info:fedora/fedora-system:def/foxml#");
        Document objectXML = XMLUnit.buildControlDocument(response.getEntity(String.class));
        String xpath = "/f:digitalObject/f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/model#state']/@VALUE";
        assertEquals("Inactive", engine.evaluate(xpath, objectXML));
    }
}
