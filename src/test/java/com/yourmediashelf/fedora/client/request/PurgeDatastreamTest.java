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
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeDatastream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;
import com.yourmediashelf.fedora.client.response.PurgeDatastreamResponse;
import com.yourmediashelf.fedora.util.DateUtility;

public class PurgeDatastreamTest
        extends BaseFedoraRequestTest {

    @Test
    public void testPurgeDatastream() throws Exception {
        String dsid = "testPurgeDatastream";
        String content = "<foo>bar</foo>";
        FedoraResponse response =
                addDatastream(testPid, dsid).content(content).dsLabel(null)
                        .execute(fedora());
        assertEquals(201, response.getStatus());

        // now delete it
        response = fedora().execute(purgeDatastream(testPid, dsid));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testPurgeVersion() throws Exception {
        String dsid = "testPurgeVersion";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse addResponse =
                addDatastream(testPid, dsid).content(content).logMessage("").execute(fedora());
        assertEquals(201, addResponse.getStatus());
        Date originalDate = addResponse.getLastModifiedDate();

        ModifyDatastreamResponse modifyResponse;
        modifyResponse =
                modifyDatastream(testPid, dsid).dsLabel("1").execute(fedora());
        Date modify1 = modifyResponse.getLastModifiedDate();

        modifyResponse =
                modifyDatastream(testPid, dsid).dsLabel("2").execute(fedora());
        Date modify2 = modifyResponse.getLastModifiedDate();

        // purge only the first modified version
        PurgeDatastreamResponse purge =
                purgeDatastream(testPid, dsid).startDT(modify1).endDT(modify1)
                        .logMessage("purge only 1").execute(fedora());
        List<String> purgedDates = purge.getPurgedDates();
        assertEquals(DateUtility.getXSDDateTime(modify1), purgedDates.get(0));
        assertEquals(1, purgedDates.size());

        // purge everything
        purge = purgeDatastream(testPid, dsid).logMessage("purge everything").execute(fedora());
        purgedDates = purge.getPurgedDates();
        assertEquals(2, purgedDates.size());
        Collections.sort(purgedDates);
        assertEquals("purgedDates: " + purgedDates, DateUtility.getXSDDateTime(originalDate), purgedDates
                .get(0));
        assertEquals("purgedDates: " + purgedDates, DateUtility.getXSDDateTime(modify2), purgedDates.get(1));

        try {
            getDatastream(testPid, dsid).execute(fedora());
            fail("Datastream should have been deleted");
        } catch (FedoraClientException e) {
            assertEquals(404, e.getStatus());
        }
    }

    @Test
    public void testOutOfRangeDates() throws Exception {
        String dsid = "testOutOfRangeDates";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse addResponse =
                addDatastream(testPid, dsid).content(content).execute(fedora());
        assertEquals(201, addResponse.getStatus());
        Date originalDate = addResponse.getLastModifiedDate();

        Date future = new DateTime(originalDate).plusHours(1).toDate();
        PurgeDatastreamResponse purge = purgeDatastream(testPid, dsid)
                                          .startDT(future)
                                          .endDT(future).execute(fedora());
        assertEquals(0, purge.getPurgedDates().size());

        purge = purgeDatastream(testPid, dsid)
                  .startDT(future).execute(fedora());
        assertEquals(0, purge.getPurgedDates().size());

        Date past = new DateTime(originalDate).minusHours(1).toDate();
        purge = purgeDatastream(testPid, dsid)
                  .endDT(past).execute(fedora());
        assertEquals(0, purge.getPurgedDates().size());
    }

    @Test
    public void testMalformedDates() throws Exception {
        String dsid = "testMalformedDates";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse addResponse =
                addDatastream(testPid, dsid).content(content).execute(fedora());
        assertEquals(201, addResponse.getStatus());

        try {
            purgeDatastream(testPid, dsid).startDT("1999")
                .logMessage("test bad purgeDatastream request").execute(fedora());
            fail("purge with invalid date should have failed.");
        } catch(FedoraClientException expected) {
            assertEquals(400, expected.getStatus());
        }
    }
}
