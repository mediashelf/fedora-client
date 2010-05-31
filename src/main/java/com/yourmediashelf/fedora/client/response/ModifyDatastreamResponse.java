package com.yourmediashelf.fedora.client.response;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.ModifyDatastream;

/**
 * A {@link FedoraResponse} for the {@link ModifyDatastream} request.
 *
 * @author Edwin Shin
 */
public class ModifyDatastreamResponse
        extends DatastreamProfileResponse {

    public ModifyDatastreamResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }
}
