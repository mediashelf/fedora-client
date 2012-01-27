/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.yourmediashelf.fedora.client;

import static com.yourmediashelf.fedora.client.FedoraClient.getNextPID;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yourmediashelf.fedora.client.request.AddDatastream;
import com.yourmediashelf.fedora.client.request.Ingest;
import com.yourmediashelf.fedora.client.request.PurgeObject;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.IngestResponse;


/**
 * This is an integration test which requires a running Fedora server.
 *
 * @author Edwin Shin
 *
 */
public class FedoraClientIT {
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
		String debug = System.getProperty("test.debug");
		client.debug(Boolean.parseBoolean(debug));
	}

	@Test
	public void testGetMimeType() throws Exception {
	    File f = new File("src/test/resources/testGetMimeType.xml");
	    assertEquals("text/xml", client.getMimeType(f));
	}

	@Test
	public void testObjectCreationWithRelsExt() throws Exception {
	    // create the object
	    String pid = getNextPID().namespace("test").execute(client).getPid();
	    IngestResponse response = new Ingest(pid).execute(client);
	    assertEquals(pid, response.getPid());

	    // Create RELS-EXT using RelsExt.Builder
	    String cmodel = "test:cmodel";
	    String relsExt = new RelsExt.Builder(pid)
                               .addCModel(cmodel).build().toString();

	    AddDatastreamResponse addDS = new AddDatastream(pid, "RELS-EXT")
	                                        .content(relsExt).execute(client);
	    assertEquals(201, addDS.getStatus());

	    // cleanup
	    FedoraResponse purge = new PurgeObject(pid).execute(client);
	    assertEquals(200, purge.getStatus());
	}
}
