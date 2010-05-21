
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addRelationship;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraCredentials;

public class AddRelationshipTest {

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
    public void testAddRelationship() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "urn:foo/한";

        response = fedora.execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).build());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddLiteral() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "你好";

        response = fedora.execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).build());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddTypedLiteral() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "1970-01-01T00:00:00Z";
        String datatype = "http://www.w3.org/2001/XMLSchema#dateTime";

        response = fedora.execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).datatype(datatype).build());
        assertEquals(200, response.getStatus());
    }
}
