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
        FedoraResponse response = modifyObject(testPid).label(modifiedLabel).execute(fedora());
        assertEquals(200, response.getStatus());

        GetObjectProfileResponse getOP = getObjectProfile(testPid).format("xml").execute(fedora());

        //test the response
        assertEquals(modifiedLabel, getOP.getLabel());
    }

    @Test
    public void testModifyObjectLabelWithXParam() throws Exception {
        String modifiedLabel = "Nobody expects the Spanish Inquisition";
        FedoraResponse response = modifyObject(testPid).xParam("label", modifiedLabel).execute(fedora());
        assertEquals(200, response.getStatus());

        GetObjectProfileResponse getOP = getObjectProfile(testPid).xParam("format", "xml").execute(fedora());

        //test the response
        assertEquals(modifiedLabel, getOP.getLabel());
    }

    @Test
    public void testModifyObjectState() throws Exception {
        FedoraResponse response = modifyObject(testPid).state("I").execute(fedora());
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
            fedora().execute(modifyObject(testPid).label("foo").lastModifiedDate(lastModifiedDate.minusHours(1)));
            fail("modifyObject succeeded, but should have failed with HTTP 409 Conflict");
        } catch (FedoraClientException expected) {
            assertEquals(409, expected.getStatus());
        }
    }
}
