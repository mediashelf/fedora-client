
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

/**
 * <p>Builder for the AddDatastream method.</p>
 *
 * @author Edwin Shin
 * @since 0.0.3
 */
public class GetDatastreamHistory
        extends FedoraMethod<GetDatastreamHistory> {

    private final String pid;
    private final String dsId;

    /**
     * @param pid
     *        persistent identifier of the digital object
     * @param dsId datastream identifier
     */
    public GetDatastreamHistory(String pid, String dsId) {
        this.pid = pid;
        this.dsId = dsId;
    }

    /**
     * Specify the format of the response.
     *
     * @param format response format, one of "html" or "xml".
     * @return this builder
     */
    public GetDatastreamHistory format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams/%s/versions", pid, dsId);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }
}