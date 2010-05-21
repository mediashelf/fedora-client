package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class PurgeRelationship extends FedoraMethod {
    private final String pid;

    public PurgeRelationship(String pid) {
        this.pid = pid;
    }

    public PurgeRelationship subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public PurgeRelationship predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    public PurgeRelationship object(String object) {
        addQueryParam("object", object);
        return this;
    }

    public PurgeRelationship isLiteral(boolean isLiteral) {
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }

    public PurgeRelationship datatype(String datatype) {
        addQueryParam("datatype", datatype);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships", pid);

        return wr.path(path).queryParams(getQueryParams()).delete(ClientResponse.class);
    }

}