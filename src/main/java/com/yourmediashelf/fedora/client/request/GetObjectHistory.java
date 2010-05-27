
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetObjectHistory
        extends FedoraMethod<GetObjectHistory> {

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
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/versions", pid);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}