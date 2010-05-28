package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

public class PurgeDatastream extends FedoraRequest<PurgeDatastream> {
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
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        String path = String.format("objects/%s/datastreams/%s", pid, dsId);

        WebResource wr = fedora.resource().path(path).queryParams(getQueryParams());
        ClientResponse cr = wr.delete(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }
}