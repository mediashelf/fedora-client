package com.yourmediashelf.fedora.client;

import static com.yourmediashelf.fedora.client.FedoraClient.getNextPID;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yourmediashelf.fedora.client.request.AddRelationship;
import com.yourmediashelf.fedora.client.request.Ingest;
import com.yourmediashelf.fedora.client.request.PurgeObject;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.IngestResponse;


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
	    // create the object
	    String pid = getNextPID().namespace("test").execute(client).getPid();
	    IngestResponse response = new Ingest(pid).execute(client);
	    assertEquals(pid, response.getPid());

	    // create rels-ext with a new cmodel
	    String cmodel = "test:cmodel";

	    FedoraResponse addRel = new AddRelationship(pid)
    	            .predicate("info:fedora/fedora-system:def/model#hasModel")
    	            .object(cmodel).execute(client);
	    assertEquals(200, addRel.getStatus());

	    // cleanup
	    FedoraResponse purge = new PurgeObject(pid).execute(client);
	    assertEquals(204, purge.getStatus());
	}
}
