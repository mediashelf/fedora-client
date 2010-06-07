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

import static com.yourmediashelf.fedora.client.FedoraClient.getNextPID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.GetNextPIDResponse;

public class GetNextPIDTest extends BaseFedoraRequestTest {
    @Test
    public void testGetOnePid() throws Exception {
        GetNextPIDResponse response = getNextPID().execute(fedora());
        assertEquals(200, response.getStatus());
        assertNotNull(response.getPid());
    }

    @Test
    public void testGetOneNamespacedPid() throws Exception {
        String namespace = "foo";
        GetNextPIDResponse response = getNextPID().namespace(namespace).execute(fedora());
        assertEquals(200, response.getStatus());
        assertTrue(response.getPid().startsWith(namespace));
    }

    @Test
    public void testGetTwoPids() throws Exception {
        GetNextPIDResponse response = null;
        response = getNextPID().numPIDs(2).execute(fedora()) ;
        assertEquals(200, response.getStatus());
        assertEquals(2, response.getPids().size());
    }

    @Test
    public void testGetFormatHtml() throws Exception {
        GetNextPIDResponse response = null;
        response = new GetNextPID().format("html").execute(fedora());
        try {
            response.getPid();
            fail("Should have failed with format=html");
        } catch(FedoraClientException e) {
            assertTrue(e.getCause() instanceof XPathExpressionException);
        }

    }
}
