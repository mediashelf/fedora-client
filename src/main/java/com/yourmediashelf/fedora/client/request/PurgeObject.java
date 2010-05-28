package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the PurgeObject method.
 *
 * @author Edwin Shin
 */
public class PurgeObject extends FedoraRequest<PurgeObject> {
    private final String pid;

    public PurgeObject(String pid) {
        this.pid = pid;
    }

    public PurgeObject logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public PurgeObject force(boolean force) {
        addQueryParam("force", Boolean.toString(force));
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        String path = String.format("objects/%s", pid);

        WebResource wr = fedora.resource().path(path).queryParams(getQueryParams());
        return new FedoraResponseImpl(wr.delete(ClientResponse.class));
    }
}