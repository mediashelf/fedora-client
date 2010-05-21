package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class PurgeObject extends FedoraMethod {
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
    protected ClientResponse execute(FedoraClient fedora) {
        String path = String.format("objects/%s", pid);

        WebResource wr = fedora.resource().path(path).queryParams(getQueryParams());
        return wr.delete(ClientResponse.class);
    }
}