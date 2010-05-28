
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the GetObjectHistory method.
 *
 * @author Edwin Shin
 */
public class GetObjectHistory
        extends FedoraRequest<GetObjectHistory> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public GetObjectHistory(String pid) {
        this.pid = pid;
    }

    /**
     *
     * @param format "html" or "xml". Defaults to "html".
     * @return this builder
     */
    public GetObjectHistory format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/versions", pid);

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}