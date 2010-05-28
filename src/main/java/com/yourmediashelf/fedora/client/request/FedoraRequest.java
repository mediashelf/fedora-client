
package com.yourmediashelf.fedora.client.request;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

/**
 * Base class for Fedora REST API methods.
 *
 * @author Edwin Shin
 */
public abstract class FedoraRequest<T> {

    private final MultivaluedMap<String, String> queryParams =
            new MultivaluedMapImpl();

    abstract public FedoraResponse execute(FedoraClient fedora)
            throws FedoraClientException;

    /**
     * <p>
     * Add an arbitrary query parameter and value to the method.
     * </p>
     * <p>
     * This method is intended as mild future-proofing, allowing fedora-client
     * to be used with future or unreleased versions of Fedora that may support
     * additional or different query parameters than fedora-client is aware of.
     *
     * @param key
     *        the query parameter name
     * @param value
     *        the value
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public T xParam(String key, String value) {
        addQueryParam(key, value);
        return (T) this;
    }

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