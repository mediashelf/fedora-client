
package com.yourmediashelf.fedora.client.response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.GetObjectProfile;
import com.yourmediashelf.fedora.client.response.objectProfile.ObjectProfile;
import com.yourmediashelf.fedora.util.DateUtility;

/**
 * A {@link FedoraResponse} for the {@link GetObjectProfile} request.
 *
 * @author Edwin Shin
 */
public class GetObjectProfileResponse
        extends FedoraResponseImpl {

    private ObjectProfile objectProfile;

    public GetObjectProfileResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    public Date getCreateDate() throws FedoraClientException {
        return DateUtility.parseXSDDateTime(getObjectProfile()
                                            .getObjCreateDate()
                                            .toXMLFormat()).toDate();
    }

    public String getLabel() throws FedoraClientException {
        return getObjectProfile().getObjLabel();
    }

    /**
     *
     * @return the object's lastModifiedDate
     * @throws FedoraClientException
     */
    public Date getLastModifiedDate() throws FedoraClientException {
        return DateUtility.parseXSDDateTime(getObjectProfile()
                                            .getObjLastModDate()
                                            .toXMLFormat()).toDate();
    }

    public String getOwnerId() throws FedoraClientException {
        return getObjectProfile().getObjOwnerId();
    }

    /**
     * Accessor for the pid of the object returned by the request.
     *
     * @return the pid
     * @throws FedoraClientException
     */
    public String getPid() throws FedoraClientException {
        return getObjectProfile().getPid();
    }

    public String getState() throws FedoraClientException {
        return getObjectProfile().getObjState();
    }

    public ObjectProfile getObjectProfile() throws FedoraClientException {
        if (objectProfile == null) {
            try {
                JAXBContext context = JAXBContext
                        .newInstance("com.yourmediashelf.fedora.client.response.objectProfile");
                Unmarshaller unmarshaller = context.createUnmarshaller();
                objectProfile =
                        (ObjectProfile) unmarshaller
                                .unmarshal(new BufferedReader(new InputStreamReader(getEntityInputStream())));
            } catch (JAXBException e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
        }
        return objectProfile;
    }
}
