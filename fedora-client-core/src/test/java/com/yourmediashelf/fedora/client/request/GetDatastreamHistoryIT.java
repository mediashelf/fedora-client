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
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastreamHistory;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamHistoryResponse;

public class GetDatastreamHistoryIT extends BaseFedoraRequestIT {

    @Test
    public void testGetDatastreamHistory() throws Exception {        
        String dsId = "testGetDatastreamHistory";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        FedoraResponse response;
        response =
                addDatastream(testPid, dsId).content(content).versionable(true)
                .execute();
        assertEquals(201, response.getStatus());
        
        GetDatastreamHistoryResponse gdhResponse = null;
        
        gdhResponse = getDatastreamHistory(testPid, dsId).execute();
        assertEquals(1, gdhResponse.getDatastreamProfile().getDatastreamProfile().size());
        
        for (int i = 0; i < 3; i++) {
            modifyDatastream(testPid, dsId).dsLabel("modification: " + i).execute();
        }
        
        gdhResponse = getDatastreamHistory(testPid, dsId).execute();
        assertEquals(4, gdhResponse.getDatastreamProfile().getDatastreamProfile().size());
    }
}
