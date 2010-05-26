package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.getObjectProfile;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.getObjectXML;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.modifyObject;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.joda.time.DateTime;
import org.junit.Test;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.util.DateUtility;


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
        String lastModifiedDate = response.getEntity(String.class);

        //validate the response
        response = fedora().execute(getObjectXML(testPid).build());
        XpathEngine engine = getXpathEngine("f", "info:fedora/fedora-system:def/foxml#");
        Document objectXML = XMLUnit.buildControlDocument(response.getEntity(String.class));
        String xpath = "/f:digitalObject/f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/model#state']/@VALUE";
        assertEquals("Inactive", engine.evaluate(xpath, objectXML));

        xpath = "/f:digitalObject/f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/view#lastModifiedDate']/@VALUE";
        assertEquals(lastModifiedDate, engine.evaluate(xpath, objectXML));
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        ClientResponse response;
        // get the object's lastModifiedDate
        response = fedora().execute(getObjectXML(testPid).build());
        XpathEngine engine = getXpathEngine("f", "info:fedora/fedora-system:def/foxml#");
        Document objectXML = XMLUnit.buildControlDocument(response.getEntity(String.class));
        String xpath = "/f:digitalObject/f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/view#lastModifiedDate']/@VALUE";
        DateTime lastModifiedDate = DateUtility.parseXSDDateTime(engine.evaluate(xpath, objectXML));

        // create an earlier lastModifiedDate
        String badDate = DateUtility.getXSDDateTime(lastModifiedDate.minusHours(1));
        try {
            fedora().execute(modifyObject(testPid).label("foo").lastModifiedDate(badDate).build());
            fail("modifyObject succeeded, but should have failed with HTTP 409 Conflict");
        } catch (FedoraClientException expected) {
        }
    }
}
