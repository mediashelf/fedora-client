
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetDatastream
        extends FedoraMethod {

    private final String pid;
    private final String dsId;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public GetDatastream(String pid, String dsId) {
        this.pid = pid;
        this.dsId = dsId;
    }

    public GetDatastream asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    public GetDatastream format(String format) {
        addQueryParam("format", format);
        return this;
    }

    public GetDatastream validateChecksum(boolean validateChecksum) {
        addQueryParam("validateChecksum", Boolean.toString(validateChecksum));
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams/%s", pid, dsId);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}