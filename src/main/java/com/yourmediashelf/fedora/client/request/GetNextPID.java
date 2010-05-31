
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

    /**
     * <p>The format of the response. Defaults to "xml".</p>
     *
     * <p>The Fedora REST API default is "html", but
     * fedora-client will set "xml" as the default in order to parse the
     * response. If "html" is selected, the caller is responsible for parsing
     * the raw HTTP response as most of the FedoraResponse convenience methods
     * rely on an XML response.</p>
     *
     * @param format The response format, either "xml" or "html"
     * @return this builder
     */
    public GetNextPID format(String format) {
        addQueryParam("format", format);
        return this;
    }

    public GetNextPID namespace(String namespace) {
        addQueryParam("namespace", namespace);
        return this;
    }

    public GetNextPID numPIDs(int numPIDs) {
        addQueryParam("numPIDs", String.valueOf(numPIDs));
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