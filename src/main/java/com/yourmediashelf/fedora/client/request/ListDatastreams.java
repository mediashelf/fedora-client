
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the ListDatastreams method.
 *
 * @author Edwin Shin
 */
public class ListDatastreams
        extends FedoraRequest<ListDatastreams> {

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
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams", pid);

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}