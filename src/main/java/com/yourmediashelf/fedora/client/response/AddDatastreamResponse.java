package com.yourmediashelf.fedora.client.response;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.AddDatastream;

/**
 * A {@link FedoraResponse} for the {@link AddDatastream} request.
 *
 * @author Edwin Shin
 */
public class AddDatastreamResponse
        extends FedoraResponseImpl {

    public AddDatastreamResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }

    //TODO convenience methods that parse the response datastreamProfile

}
