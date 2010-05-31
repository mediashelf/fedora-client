package com.yourmediashelf.fedora.client.response;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.PurgeDatastream;

/**
 * A {@link FedoraResponse} for the {@link PurgeDatastream} request.
 *
 * @author Edwin Shin
 */
public class PurgeDatastreamResponse extends FedoraResponseImpl {
    private List<String> purgedDates;
    private final ObjectMapper mapper;

    public PurgeDatastreamResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
        mapper = new ObjectMapper();
    }

    /**
     *
     * @return the dates of the purged datastream versions
     * @throws FedoraClientException
     */
    public List<String> getPurgedDates() throws FedoraClientException {
        if (purgedDates == null) {
            try {
                purgedDates = mapper.readValue(getEntity(String.class),
                                               TypeFactory
                                               .collectionType(ArrayList.class,
                                                               String.class));
            } catch (Exception e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
        }
        return purgedDates;
    }
}
