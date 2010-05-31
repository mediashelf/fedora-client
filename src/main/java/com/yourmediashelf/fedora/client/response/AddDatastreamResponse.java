package com.yourmediashelf.fedora.client.response;

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.AddDatastream;

/**
 * A {@link FedoraResponse} for the {@link AddDatastream} request.
 *
 * @author Edwin Shin
 */
public class AddDatastreamResponse
        extends DatastreamProfileResponse {
    private final URI location;

    public AddDatastreamResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
        location = cr.getLocation();
    }

    public URI getLocation() {
        return location;
    }
}
