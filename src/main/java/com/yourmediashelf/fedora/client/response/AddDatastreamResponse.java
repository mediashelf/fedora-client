package com.yourmediashelf.fedora.client.response;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;


public class AddDatastreamResponse
        extends FedoraResponseImpl {

    public AddDatastreamResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }


}
