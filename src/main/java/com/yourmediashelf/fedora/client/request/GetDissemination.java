
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class GetDissemination
        extends FedoraMethod {

    private final String pid;
    private final String sdefPid;
    private final String method;

    /**
     *
     * @param pid persistent identifier of the digital object
     * @param sdefPid persistent identifier of the sDef defining the methods
     * @param method method to invoke
     */
    public GetDissemination(String pid, String sdefPid, String method) {
        this.pid = pid;
        this.sdefPid = sdefPid;
        this.method = method;
    }

    /**
     * Add a parameter required by the method.
     *
     * @param key
     * @param value
     * @return
     */
    public GetDissemination methodParam(String key, String value) {
        addQueryParam("key", value);
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/methods/%s/%s", pid, sdefPid, method);

        return wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
    }

}