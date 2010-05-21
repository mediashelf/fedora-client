
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class ListDatastreams
        extends FedoraMethod {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public ListDatastreams(String pid) {
        this.pid = pid;
    }

    public ListDatastreams asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    public ListDatastreams format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams", pid);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}