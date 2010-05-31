package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeDatastream;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;
import com.yourmediashelf.fedora.client.response.PurgeDatastreamResponse;
import com.yourmediashelf.fedora.util.DateUtility;



public class PurgeDatastreamTest extends FedoraMethodBaseTest {

    @Test
    public void testPurgeDatastream() throws Exception {
        String content = "<foo>bar</foo>";
        FedoraResponse response =
                addDatastream(testPid, "baz").content(content)
                        .dsLabel(null).execute(fedora());
        assertEquals(201, response.getStatus());

        // now delete it
        response = fedora().execute(purgeDatastream(testPid, "baz"));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testPurgeVersion() throws Exception {
        String dsid = "quux";
        String content = "<foo>bar</foo>";
        AddDatastreamResponse addResponse =
                addDatastream(testPid, dsid).content(content).execute(fedora());
        assertEquals(201, addResponse.getStatus());
        Date originalDate = addResponse.getLastModifiedDate();

        ModifyDatastreamResponse modifyResponse;
        modifyResponse = modifyDatastream(testPid, dsid).dsLabel("1").execute(fedora());
        Date modify1 = modifyResponse.getLastModifiedDate();

        modifyResponse = modifyDatastream(testPid, dsid).dsLabel("2").execute(fedora());
        Date modify2 = modifyResponse.getLastModifiedDate();

        PurgeDatastreamResponse purge = purgeDatastream(testPid, dsid).startDT(modify1).endDT(modify1).execute(fedora());
        List<String> purgedDates = purge.getPurgedDates();
        assertEquals(1, purgedDates.size());
        assertEquals(DateUtility.getXSDDateTime(modify1), purgedDates.get(0));

        purge = purgeDatastream(testPid, dsid).execute(fedora());
        purgedDates = purge.getPurgedDates();
        assertEquals(2, purgedDates.size());
        Collections.sort(purgedDates);
        assertEquals(DateUtility.getXSDDateTime(originalDate), purgedDates.get(0));
        assertEquals(DateUtility.getXSDDateTime(modify2), purgedDates.get(1));
    }
}
