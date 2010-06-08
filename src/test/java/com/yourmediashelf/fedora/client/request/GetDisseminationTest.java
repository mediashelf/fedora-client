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

import static com.yourmediashelf.fedora.client.FedoraClient.getDissemination;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class GetDisseminationTest extends BaseFedoraRequestTest {

    @Test
    public void testGetDissemination() throws Exception {
        FedoraResponse response = null;
        //fedora-system:FedoraObject-3.0/methods/fedora-system:3/viewDublinCore

        String objectPid = "fedora-system:FedoraObject-3.0";
        String sdefPid = "fedora-system:3";
        String method = "viewDublinCore";
        response = getDissemination(objectPid, sdefPid, method).execute(fedora());
        assertEquals(200, response.getStatus());
    }
}
