package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

public class GetRelationships extends FedoraRequest<GetRelationships> {
    private final String pid;

    public GetRelationships(String pid) {
        this.pid = pid;
    }

    public GetRelationships subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public GetRelationships predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships", pid);

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}