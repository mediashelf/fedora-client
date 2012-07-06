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

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.multipart.BodyPart;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.BatchResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

@Ignore("Batch API is not included by default in Fedora")
public class BatchDeleteDatastreamsIT extends BaseFedoraRequestIT {

    @Test
    public void testBatchDeleteDatastreams() throws Exception {
        String[] dsids = {"batchDS1", "batchDS2"};
        String content = "<foo>bar</foo>";
        FedoraResponse adr;
        for (String dsid : dsids) {
            adr = addDatastream(testPid, dsid).content(content).execute();
            assertEquals(201, adr.getStatus());
        }

        // now delete it
        BatchResponse response =
                new BatchDeleteDatastreams().pids(testPid).dsids(dsids)
                        .logMessage("testBatchDeleteDatasteams")
                        .execute();
        assertEquals(200, response.getStatus());
        
        List<BodyPart> bps = response.getBodyParts();
        for (BodyPart bp : bps) {
            System.out.println(bp.getEntityAs(String.class));
            bp.cleanup();
        }
        response.close();
    }

    @Override
    public void testNoDefaultClientRequest() throws FedoraClientException {
    }
}
