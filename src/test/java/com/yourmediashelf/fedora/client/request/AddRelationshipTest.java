
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addRelationship;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

public class AddRelationshipTest extends FedoraMethodBaseTest {

    @Test
    public void testAddRelationship() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "urn:foo/한";

        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).build());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddLiteral() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "你好";

        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).build());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddTypedLiteral() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "1970-01-01T00:00:00Z";
        String datatype = "http://www.w3.org/2001/XMLSchema#dateTime";

        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).datatype(datatype).build());
        assertEquals(200, response.getStatus());
    }
}
