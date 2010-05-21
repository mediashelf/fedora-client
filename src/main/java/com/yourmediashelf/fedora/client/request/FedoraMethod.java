
package com.yourmediashelf.fedora.client.request;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yourmediashelf.fedora.client.FedoraClient;

/**
 *
 *
 * @author Edwin Shin
 * @version $Id$
 */
public abstract class FedoraMethod {

    private final MultivaluedMap<String, String> queryParams =
            new MultivaluedMapImpl();

    /**
     * Construct the request.
     *
     * @return FedoraRequest representing the FedoraMethod and its properties.
     */
    public FedoraRequest build() {
        return new FedoraRequest(this);
    }

    abstract protected ClientResponse execute(FedoraClient fedora);

    protected void addQueryParam(String key, String value) {
        if (value != null) {
            queryParams.add(key, value);
        }
    }

    protected List<String> getQueryParam(String key) {
        return queryParams.get(key);
    }

    protected String getFirstQueryParam(String key) {
        return queryParams.getFirst(key);
    }

    protected MultivaluedMap<String, String> getQueryParams() {
        return queryParams;
    }
}