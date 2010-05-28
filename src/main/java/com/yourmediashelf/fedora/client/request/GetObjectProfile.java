
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the GetObjectProfile method.
 *
 * @author Edwin Shin
 */
public class GetObjectProfile
        extends FedoraRequest<GetObjectProfile> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public GetObjectProfile(String pid) {
        this.pid = pid;
    }

    /**
     *
     * @param asOfDateTime yyyy-MM-dd or yyyy-MM-ddTHH:mm:ssZ
     * @return this builder
     */
    public GetObjectProfile asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    /**
     *
     * @param format "html" or "xml". Defaults to "html".
     * @return this builder
     */
    public GetObjectProfile format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s", pid);

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}