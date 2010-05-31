
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * <p>Builder for the GetDatastreamHistory method.</p>
 *
 * @author Edwin Shin
 * @since 0.0.3
 */
public class GetDatastreamHistory
        extends FedoraRequest<GetDatastreamHistory> {

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
     * <p>The format of the response. Defaults to "xml".</p>
     *
     * <p>The Fedora REST API default is "html", but
     * fedora-client will set "xml" as the default in order to parse the
     * response. If "html" is selected, the caller is responsible for parsing
     * the raw HTTP response as most of the FedoraResponse convenience methods
     * rely on an XML response.</p>
     *
     * @param format "html" or "xml". Defaults to "xml".
     * @return this builder
     */
    public GetDatastreamHistory format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        // default to xml for the format, so we can parse the results
        if (getFirstQueryParam("format") == null) {
            addQueryParam("format", "xml");
        }
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams/%s/versions", pid, dsId);

        return new FedoraResponseImpl(wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class));
    }
}