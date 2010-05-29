package com.yourmediashelf.fedora.client.response;

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;


public class IngestResponse extends FedoraResponseImpl {
    private String pid;
    private final URI location;

    public IngestResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
        location = cr.getLocation();
    }

    public String getPid() {
        if (pid == null) {
            pid = getEntity(String.class);
        }
        return pid;
    }

    public URI getLocation() {
        return location;
    }
}
