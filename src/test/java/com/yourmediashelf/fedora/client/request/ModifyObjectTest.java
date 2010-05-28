package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.getObjectProfile;
import static com.yourmediashelf.fedora.client.FedoraClient.getObjectXML;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyObject;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.joda.time.DateTime;
import org.junit.Test;
import org.w3c.dom.Document;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

/**
 *
 *
 * @author Edwin Shin
 */
public class ModifyObjectTest extends FedoraMethodBaseTest {

    @Test
    public void testModifyObjectLabel() throws Exception {
        String modifiedLabel = "A modified label";
        FedoraResponse response = fedora().execute(modifyObject(testPid).label(modifiedLabel));
        assertEquals(200, response.getStatus());

        response = fedora().execute(getObjectProfile(testPid).format("xml"));

        //test the response
        String objectProfile = response.getEntity(String.class);
        assertXpathEvaluatesTo(modifiedLabel, "/objectProfile/objLabel", objectProfile);
    }

    @Test
    public void testModifyObjectLabelWithXParam() throws Exception {
        String modifiedLabel = "Nobody expects the Spanish Inquisition";
        FedoraResponse response = fedora().execute(modifyObject(testPid).xParam("label", modifiedLabel));
        assertEquals(200, response.getStatus());

        response = fedora().execute(getObjectProfile(testPid).xParam("format", "xml"));

        //test the response
        String objectProfile = response.getEntity(String.class);
        assertXpathEvaluatesTo(modifiedLabel, "/objectProfile/objLabel", objectProfile);
    }

    @Test
    public void testModifyObjectState() throws Exception {
        FedoraResponse response = fedora().execute(modifyObject(testPid).state("I"));
        assertEquals(200, response.getStatus());
        String lastModifiedDate = response.getEntity(String.class);

        //validate the response
        response = fedora().execute(getObjectXML(testPid));
        XpathEngine engine = getXpathEngine("f", "info:fedora/fedora-system:def/foxml#");
        Document objectXML = XMLUnit.buildControlDocument(response.getEntity(String.class));
        String xpath = "/f:digitalObject/f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/model#state']/@VALUE";
        assertEquals("Inactive", engine.evaluate(xpath, objectXML));

        xpath = "/f:digitalObject/f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/view#lastModifiedDate']/@VALUE";
        assertEquals(lastModifiedDate, engine.evaluate(xpath, objectXML));
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        DateTime lastModifiedDate = fedora().getLastModifiedDate(testPid);

        try {
            fedora().execute(modifyObject(testPid).label("foo").lastModifiedDate(lastModifiedDate.minusHours(1)));
            fail("modifyObject succeeded, but should have failed with HTTP 409 Conflict");
        } catch (FedoraClientException expected) {
        }
    }
}
