package com.yourmediashelf.fedora.client.response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;
import com.yourmediashelf.fedora.util.DateUtility;

/**
 * <p>A {@link FedoraResponse} for requests that return a datastreamProfile.</p>
 *
 * <p>This includes AddDatastream, GetDatastream, and ModifyDatastream.<p>
 *
 * @author Edwin Shin
 */
public class DatastreamProfileResponse
        extends FedoraResponseImpl {
    private DatastreamProfile datastreamProfile;

    public DatastreamProfileResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }

    /**
     * Get the last modified date of the datastream.
     *
     * <p>Note this method will fail if the request specified format=html
     * rather than xml.</p>
     *
     * @return the lastModifiedDate of the datastream
     * @throws FedoraClientException
     */
    public Date getLastModifiedDate() throws FedoraClientException {
        return DateUtility.parseXSDDateTime(getDatastreamProfile()
                                            .getDsCreateDate()
                                            .toXMLFormat()).toDate();
    }

    /**
     * Get the datastreamProfile. Note this method will fail if the request
     * specified format=html rather than xml.
     *
     * @return the DatastreamProfile
     * @throws FedoraClientException
     */
    public DatastreamProfile getDatastreamProfile() throws FedoraClientException {
        if (datastreamProfile == null) {
            try {
                JAXBContext context = JAXBContext
                        .newInstance("com.yourmediashelf.fedora.generated.management");
                Unmarshaller unmarshaller = context.createUnmarshaller();
                datastreamProfile =
                        (DatastreamProfile) unmarshaller
                                .unmarshal(new BufferedReader(new InputStreamReader(getEntityInputStream())));
            } catch (JAXBException e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
        }
        return datastreamProfile;
    }
}
