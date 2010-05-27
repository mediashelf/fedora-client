package com.yourmediashelf.fedora.client;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addDatastream;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.ingest;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.purgeObject;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;


/**
 * This is an integration test which requires a running Fedora server.
 *
 * @author Edwin Shin
 *
 */
public class FedoraClientTest {
	private static FedoraCredentials credentials;
	private FedoraClient client;
	//private static String testPid = "test-rest:1";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String baseUrl = System.getProperty("fedora.test.baseUrl");
		String username = System.getProperty("fedora.test.username");
		String password = System.getProperty("fedora.test.password");
		credentials = new FedoraCredentials(new URL(baseUrl), username, password);
	}

	@Before
	public void setUp() throws Exception {
		client = new FedoraClient(credentials);
	}

	@Test
	public void testGetMimeType() throws Exception {
	    File f = new File("src/test/resources/foo.xml");
	    assertEquals("text/xml", client.getMimeType(f));
	}

	@Test
	public void testObjectCreationWithRelsExt() throws Exception {
	    String pid = client.getNextPid("test");
	    String cmodel = "test:cmodel";
	    String relsExt = new RelsExt.Builder(pid)
            .addCModel(cmodel)
            .build().toString();
	    ClientResponse response = client.execute(ingest(pid).build());
	    assertEquals(pid, response.getEntity(String.class));
	    response = client.execute(addDatastream(pid, "RELS-EXT").content(relsExt).build());
	    assertEquals(201, response.getStatus());

	    // cleanup
	    response = client.execute(purgeObject(pid).build());
	    assertEquals(204, response.getStatus());
	}
}
