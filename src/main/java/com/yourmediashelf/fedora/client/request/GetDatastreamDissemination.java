
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetDatastreamDissemination
        extends FedoraMethod<GetDatastreamDissemination> {

    private final String pid;
    private final String dsId;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public GetDatastreamDissemination(String pid, String dsId) {
        this.pid = pid;
        this.dsId = dsId;
    }

    public GetDatastreamDissemination asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    public GetDatastreamDissemination format(String format) {
        addQueryParam("format", format);
        return this;
    }

    public GetDatastreamDissemination download(boolean download) {
        addQueryParam("download", Boolean.toString(download));
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams/%s/content", pid, dsId);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}