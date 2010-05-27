
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.getNextPID;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.junit.Test;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;

public class GetNextPIDTest extends FedoraMethodBaseTest {
    @Test
    public void testGetOnePid() throws Exception {
        ClientResponse response = null;

        response = fedora().execute(getNextPID().format("xml").build());
        assertEquals(200, response.getStatus());
        String nextPid = response.getEntity(String.class);
        assertXpathExists("/pidList/pid", nextPid);
    }

    @Test
    public void testGetOneNamespacedPid() throws Exception {
        ClientResponse response = null;
        String namespace = "foo";
        response = fedora().execute(getNextPID().namespace(namespace).format("xml").build());
        assertEquals(200, response.getStatus());

        Document nextPid = XMLUnit.buildControlDocument(response.getEntity(String.class));

        XpathEngine engine = getXpathEngine(null);
        String pid = engine.evaluate("/pidList/pid", nextPid);
        assertTrue(pid.startsWith(namespace));
    }
}
