package com.yourmediashelf.fedora.client.response;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.GetDatastream;

/**
 * A {@link FedoraResponse} for the {@link GetDatastream} request.
 *
 * @author Edwin Shin
 */
public class GetDatastreamResponse
        extends DatastreamProfileResponse {

    public GetDatastreamResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }
}
