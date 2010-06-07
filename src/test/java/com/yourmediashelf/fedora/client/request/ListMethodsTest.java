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

import static com.yourmediashelf.fedora.client.FedoraClient.listMethods;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.ListMethodsResponse;
import com.yourmediashelf.fedora.generated.access.ObjectMethods;

public class ListMethodsTest extends BaseFedoraRequestTest {
    @Test
    public void testListMethods() throws Exception {
        ListMethodsResponse response = listMethods(testPid).execute(fedora());
        ObjectMethods methods = response.getObjectMethods();
        assertEquals(testPid, methods.getPid());
        //System.out.println(response.getEntity(String.class));
    }
}
