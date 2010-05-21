package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class ModifyObject extends FedoraMethod {
    private final String pid;

    public ModifyObject(String pid) {
        this.pid = pid;
    }

    @Override
    public FedoraRequest build() {
        return new FedoraRequest(this);
    }

    public ModifyObject label(String label) {
        addQueryParam("label", label);
        return this;
    }

    public ModifyObject ownerId(String ownerId) {
        addQueryParam("ownerId", ownerId);
        return this;
    }

    public ModifyObject logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s", pid);
        return wr.path(path).queryParams(getQueryParams()).put(ClientResponse.class);
    }
}