
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

public class GetNextPID
        extends FedoraRequest<GetNextPID> {

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
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/nextPID");

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).post(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}