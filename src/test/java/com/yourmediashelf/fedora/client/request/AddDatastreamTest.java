
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;

public class AddDatastreamTest {

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
    public void testDefaultValues() throws Exception {
        String content = "<foo>bar</foo>";
        ClientResponse response =
                fedora.execute(addDatastream(testPid, "baz").content(content)
                        .dsLabel(null).build());
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testManagedContent() throws Exception {
        File f = new File("src/test/resources/21.edit.essay.zip");
        assertTrue(f.exists());
        ClientResponse response =
                fedora.execute(addDatastream(testPid, "MANAGED_DS")
                        .controlGroup("M").content(f).build());
        assertEquals(201, response.getStatus());
    }

    @Test //(expected=FedoraClientException.class)
    public void testMissingContent() {
        try {
            fedora.execute(addDatastream(testPid, "MANAGED_DS")
                        .controlGroup("M").build());
        } catch (FedoraClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
