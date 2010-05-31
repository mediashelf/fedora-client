package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the GetRelationships method.
 *
 * @author Edwin Shin
 */
public class GetRelationships extends FedoraRequest<GetRelationships> {
    private final String pid;

    public GetRelationships(String pid) {
        this.pid = pid;
    }

    /**
     * The format of the response. Default is rdf/xml.
     *
     * @param format one of "rdf/xml", "n-triples", "turtle", or "sparql".
     * @return this builder
     */
    public GetRelationships format(String format) {
        addQueryParam("format", format);
        return this;
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