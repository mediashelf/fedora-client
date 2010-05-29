
package com.yourmediashelf.fedora.client.response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.listDatastreams.DatastreamType;
import com.yourmediashelf.fedora.client.response.listDatastreams.ObjectDatastreams;

public class ListDatastreamsResponse
        extends FedoraResponseImpl {

    private ObjectDatastreams objectDatastreams;

    public ListDatastreamsResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    /**
     * The List of requested datastreams.
     * <p>
     * If the ListDatastreams request explicitly set format=html, this method
     * call will fail.
     * </p>
     *
     * @return the List of datastreams
     * @throws FedoraClientException
     */
    public List<DatastreamType> getDatastreams() throws FedoraClientException {
        return getObjectDatastreams().getDatastream();
    }

    /**
     * Accessor for the pid of the datastreams returned by the request.
     *
     * @return the pid for the parent object of the datastreams
     * @throws FedoraClientException
     */
    public String getPid() throws FedoraClientException {
        return getObjectDatastreams().getPid();
    }

    /**
     * @return the baseURL of the Fedora server that provided the response, e.g.
     *         http://example.org:8080/fedora
     * @throws FedoraClientException
     */
    public String getBaseUrl() throws FedoraClientException {
        return getObjectDatastreams().getBaseURL();
    }

    /**
     *
     * @return the value of the getAsOfDateTime query parameter passed into the
     * original ListDatastreams request.
     * @throws FedoraClientException
     */
    public String getAsOfDateTime() throws FedoraClientException {
        return getObjectDatastreams().getAsOfDateTime();
    }

    private ObjectDatastreams getObjectDatastreams() throws FedoraClientException {
        if (objectDatastreams == null) {
            try {
                JAXBContext context = JAXBContext
                        .newInstance("com.yourmediashelf.fedora.client.response.listDatastreams");
                Unmarshaller unmarshaller = context.createUnmarshaller();
                objectDatastreams =
                        (ObjectDatastreams) unmarshaller
                                .unmarshal(new BufferedReader(new InputStreamReader(getEntityInputStream())));
            } catch (JAXBException e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
        }
        return objectDatastreams;
    }
}
