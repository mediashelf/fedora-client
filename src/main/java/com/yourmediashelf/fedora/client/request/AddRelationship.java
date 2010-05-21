package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class AddRelationship extends FedoraMethod {
    private final String pid;

    public AddRelationship(String pid) {
        this.pid = pid;
    }

    public AddRelationship subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public AddRelationship predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    public AddRelationship object(String object) {
        addQueryParam("object", object);
        return this;
    }

    public AddRelationship isLiteral(boolean isLiteral) {
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }

    public AddRelationship datatype(String datatype) {
        addQueryParam("datatype", datatype);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships/new", pid);

        return wr.path(path).queryParams(getQueryParams()).post(ClientResponse.class);
    }

}