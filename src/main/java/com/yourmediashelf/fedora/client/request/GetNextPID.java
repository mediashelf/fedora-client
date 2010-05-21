
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetNextPID
        extends FedoraMethod {

    public GetNextPID() {
    }

    public GetNextPID numPIDs(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    public GetNextPID namespace(String namespace) {
        addQueryParam("namespace", namespace);
        return this;
    }

    public GetNextPID format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/nextPID");

        return wr.path(path).queryParams(getQueryParams()).post(ClientResponse.class);
    }

}