
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addRelationship;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.getRelationships;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class GetRelationshipsTest extends FedoraMethodBaseTest {

    @Test
    public void testGetAllRelationships() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);

        response = fedora().execute(getRelationships(testPid).subject(subject).build());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetRelationships() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "你好";

        // first add a relationship
        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).build());
        assertEquals(200, response.getStatus());

        // now get it
        response = fedora().execute(getRelationships(testPid).subject(subject).predicate(predicate).build());
        assertEquals(200, response.getStatus());
    }
}
