
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

/**
 * Builder for the GetObjectXML method.
 *
 * @author Edwin Shin
 * @since 0.0.3
 */
public class GetObjectXML
        extends FedoraMethod {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object, e.g. "demo:1".
     */
    public GetObjectXML(String pid) {
        this.pid = pid;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/objectXML", pid);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }
}