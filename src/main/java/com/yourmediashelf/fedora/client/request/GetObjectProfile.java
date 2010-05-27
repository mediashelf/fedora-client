
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetObjectProfile
        extends FedoraMethod<GetObjectProfile> {

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
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s", pid);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}