package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetRelationships extends FedoraMethod<GetRelationships> {
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
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships", pid);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}