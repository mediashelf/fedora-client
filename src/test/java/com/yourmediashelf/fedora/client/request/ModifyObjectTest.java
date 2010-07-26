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

import static com.yourmediashelf.fedora.client.FedoraClient.getObjectProfile;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetObjectProfileResponse;
import com.yourmediashelf.fedora.util.DateUtility;

/**
 *
 *
 * @author Edwin Shin
 */
public class ModifyObjectTest extends BaseFedoraRequestTest {

    @Test
    public void testModifyObjectLabel() throws Exception {
        String modifiedLabel = "A modified label";
        FedoraResponse response = modifyObject(testPid).label(modifiedLabel).logMessage("testModifyObjectLabel()").execute(fedora());
        assertEquals(200, response.getStatus());

        GetObjectProfileResponse getOP = getObjectProfile(testPid).format("xml").execute(fedora());

        //test the response
        assertEquals(modifiedLabel, getOP.getLabel());
    }

    @Test
    public void testModifyObjectLabelWithXParam() throws Exception {
        String modifiedLabel = "Nobody expects the Spanish Inquisition";
        FedoraResponse response = modifyObject(testPid).xParam("label", modifiedLabel).logMessage("testModifyObjectLabelWithXParam()").execute(fedora());
        assertEquals(200, response.getStatus());

        GetObjectProfileResponse getOP = getObjectProfile(testPid).xParam("format", "xml").execute(fedora());

        //test the response
        assertEquals(modifiedLabel, getOP.getLabel());
    }

    @Test
    public void testModifyObjectState() throws Exception {
        FedoraResponse response = modifyObject(testPid).state("I").logMessage("testModifyObjectState()").execute(fedora());
        assertEquals(200, response.getStatus());
        String lastModifiedDate = response.getEntity(String.class);

        //validate the response
        GetObjectProfileResponse getOP = getObjectProfile(testPid).execute(fedora());
        assertEquals("I", getOP.getState());
        assertEquals(lastModifiedDate, DateUtility.getXSDDateTime(getOP.getLastModifiedDate()));
    }

    @Test
    public void testOptimisticLocking() throws Exception {
        DateTime lastModifiedDate = new DateTime(fedora().getLastModifiedDate(testPid));

        try {
            modifyObject(testPid).label("foo")
                .lastModifiedDate(lastModifiedDate.minusHours(1))
                .logMessage("testOptimisticLocking(): this request should fail")
                .execute(fedora());
            fail("modifyObject succeeded, but should have failed with HTTP 409 Conflict");
        } catch (FedoraClientException expected) {
            assertEquals(409, expected.getStatus());
        }
    }
}
