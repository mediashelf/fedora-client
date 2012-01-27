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

package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.findObjects;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FindObjectsResponse;

public class FindObjectsIT extends BaseFedoraRequestIT {

	@Test
	public void testFindObjects() throws Exception {
		FindObjectsResponse response = null;

		response = findObjects().pid().terms("Content Model Object for Service Definition Objects")
				.execute(fedora());
		assertEquals(200, response.getStatus());

		List<String> pids = response.getPids();
		assertEquals(1, pids.size());
		assertFalse(response.hasNext());
	}

	@Test
	public void testResumeFindObjects() throws Exception {
		FindObjectsResponse response = null;

		response = findObjects().pid().maxResults(3).query("pid~fedora-system:*")
				.execute(fedora());
		assertEquals(200, response.getStatus());

		List<String> pids = response.getPids();

		assertEquals(3, pids.size());
		assertTrue(response.hasNext());
		response = findObjects().pid().sessionToken(response.getToken())
				.maxResults(3).resultFormat("xml").execute(fedora());

		pids = response.getPids();

		assertEquals(1, pids.size());
		assertFalse(response.hasNext());
	}
	
	@Test
	public void testFindByQuery() throws Exception {
		FindObjectsResponse response = null;

		response = findObjects().pid().title().query("pid=" + testPid).execute(fedora());
		assertEquals(200, response.getStatus());
		assertEquals(1, response.getPids().size());
	}

	@Test
	public void testFindDCTitle() throws Exception {
		FindObjectsResponse response = null;

		response = findObjects().pid().title().query("pid~fedora-system:*").execute(fedora());
		assertEquals(200, response.getStatus());
		List<String> pids = response.getPids();
		assertEquals(4, pids.size());

		List<String> titles;
		for (String pid : pids) {
			titles = response.getObjectField(pid, "title");
			assertEquals(1, titles.size());
		}
	}

	@Test
	public void testFindInvalidField() throws Exception {
		FindObjectsResponse response = null;

		response = findObjects().pid().identifier().query("pid~fedora-system:*")
				.execute(fedora());
		assertEquals(200, response.getStatus());
		List<String> pids = response.getPids();
		assertEquals(4, pids.size());

		List<String> identifiers;
		for (String pid : pids) {
			identifiers = response.getObjectField(pid, "InvalidField");
			assertEquals(0, identifiers.size());
		}
	}
}
