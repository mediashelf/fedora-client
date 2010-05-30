package com.yourmediashelf.fedora.client.response;

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.Ingest;

/**
 * A {@link FedoraResponse} for the {@link Ingest} request.
 *
 * @author Edwin Shin
 */
public class IngestResponse extends FedoraResponseImpl {
    private String pid;
    private final URI location;

    public IngestResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
        location = cr.getLocation();
    }

    /**
     *
     * @return the pid of the newly created object.
     */
    public String getPid() {
        if (pid == null) {
            pid = getEntity(String.class);
        }
        return pid;
    }

    /**
     *
     * @return the URI of the newly created resource.
     */
    public URI getLocation() {
        return location;
    }
}
