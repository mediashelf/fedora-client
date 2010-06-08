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

import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yourmediashelf.fedora.client.response.IngestResponse;



public class IngestTest extends BaseFedoraRequestTest {

    private String pid;

    @Override
    @Before
    public void setUp() throws Exception {
        // do nothing
    }

    @Override
    @After
    public void tearDown() throws Exception {
        if (pid != null) {
            purgeObject(pid).logMessage("purging IngestTest object").execute(fedora());
        }
    }

    @Test
    public void testIngestDefault() throws Exception {
        IngestResponse response = null;
        URI location = null;

        // empty object, no pid, no namespace
        response = ingest().logMessage("testIngestDefault").execute(fedora());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/"));
        pid = response.getPid();
    }

    @Test
    public void testIngestWithNamespace() throws Exception {
        IngestResponse response = null;
        URI location = null;

        // empty object, with namespace, but no pid
        response = ingest().namespace("foospace").logMessage("testIngestWithNamespace").execute(fedora());
        assertEquals(201, response.getStatus());
        location = response.getLocation();
        assertTrue(location.toString().contains("/objects/foospace"));
        pid = response.getPid();
    }

    @Test
    public void testIngestWithPid() throws Exception {
        IngestResponse response = null;

        // empty object, with pid
        response = ingest(testPid).logMessage("testIngestWithPid").execute(fedora());
        assertEquals(201, response.getStatus());
        pid = response.getPid();
        assertEquals(testPid, pid);
    }
}
