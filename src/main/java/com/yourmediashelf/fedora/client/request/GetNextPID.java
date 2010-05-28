
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.GetNextPIDResponse;

/**
 * Builder for the GetNextPID method.
 *
 * @author Edwin Shin
 */
public class GetNextPID
        extends FedoraRequest<GetNextPID> {

    public GetNextPID() {
    }

    public GetNextPID numPIDs(int numPIDs) {
        addQueryParam("numPIDs", String.valueOf(numPIDs));
        return this;
    }

    public GetNextPID namespace(String namespace) {
        addQueryParam("namespace", namespace);
        return this;
    }

    @Override
    public GetNextPIDResponse execute(FedoraClient fedora) throws FedoraClientException {
        // default to xml for the format, so we can parse the results
        if (getFirstQueryParam("format") == null) {
            addQueryParam("format", "xml");
        }

        WebResource wr = fedora.resource();
        String path = String.format("objects/nextPID");

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).post(ClientResponse.class);
        return new GetNextPIDResponse(cr);
    }

}