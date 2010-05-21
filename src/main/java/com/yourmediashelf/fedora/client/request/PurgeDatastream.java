package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class PurgeDatastream extends FedoraMethod {
    private final String pid;
    private final String dsId;

    public PurgeDatastream(String pid, String dsId) {
        this.pid = pid;
        this.dsId = dsId;
    }

    public PurgeDatastream logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public PurgeDatastream startDT(String startDT) {
        addQueryParam("startDT", startDT);
        return this;
    }

    public PurgeDatastream endDT(String endDT) {
        addQueryParam("endDT", endDT);
        return this;
    }

    public PurgeDatastream force(boolean force) {
        addQueryParam("force", Boolean.toString(force));
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        String path = String.format("objects/%s/datastreams/%s", pid, dsId);

        WebResource wr = fedora.resource().path(path).queryParams(getQueryParams());
        return wr.delete(ClientResponse.class);
    }
}