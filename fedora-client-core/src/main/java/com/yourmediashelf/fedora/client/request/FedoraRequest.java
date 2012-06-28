/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.yourmediashelf.fedora.client.request;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

/**
 * Base class for Fedora REST API requests.
 *
 * @author Edwin Shin
 */
public abstract class FedoraRequest<T> {

    private static FedoraClient DEFAULT_CLIENT;

    private final MultivaluedMap<String, String> queryParams =
            new MultivaluedMapImpl();

    /*
     * HTTP headers for use with this request
     */
    private MultivaluedMap<String, String> headers = new MultivaluedMapImpl();

    /**
     * <p>Executes this request against the {@link #DEFAULT_CLIENT}
     * 
     * @return the FedoraResponse to this request
     * @throws FedoraClientException if {@link #DEFAULT_CLIENT} is <code>null</code>
     */
    public FedoraResponse execute() throws FedoraClientException {
        if (DEFAULT_CLIENT == null) {
            throw new FedoraClientException("No default FedoraClient was set.");
        }
        return execute(DEFAULT_CLIENT);
    }

    /**
     * Set the instance of {@link FedoraClient} that all {@link FedoraRequest}s
     * should use by default.
     * 
     * @param client the instance of FedoraClient that requests should use by default
     */
    public static void setDefaultClient(FedoraClient client) {
        DEFAULT_CLIENT = client;
    }

    /**
     * Return boolean indicating whether {@link #DEFAULT_CLIENT} has been set.
     * 
     * @return boolean
     */
    public static boolean isDefaultClientSet() {
        return (DEFAULT_CLIENT != null);
    }

    /**
     * <p>Execute this request using the supplied FedoraClient instance.</p>
     *
     * @param fedora an instance of FedoraClient
     * @return the response
     * @throws FedoraClientException if the HTTP status code of the response is
     * >= 400.
     */
    abstract public <F extends FedoraResponse> FedoraResponse execute(
            FedoraClient fedora) throws FedoraClientException;

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
    
    /**
     * Retrieves the current HTTP headers set on this request.
     * 
     * @return A {@link MultivaluedMap<String, String>} of HTTP headers.
     */
    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the current HTTP headers on this request. This overwrites
     * any headers already set.
     * 
     * @param headers A {@link MultivaluedMap<String, String>} of HTTP headers.
     */
    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Adds an HTTP header to this request. This does not overwrite
     * any headers of the same name already set: it adds to them.
     * 
     * @param key The name of the HTTP header to set.
     * @param value The value of the HTTP header to set.
     */
    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    /**
     * Removes all HTTP headers of a specified name to this request.
     * 
     * @param key The name of the HTTP header to remove.
     */
    public void removeHeader(String key) {
        headers.remove(key);
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

    /**
     * <p>Returns a <code>WebResource</code> as supplied by the
     * {@link #DEFAULT_CLIENT} using <code>FedoraClient.resource()</code>.
     * Also adds any headers as found via {@link getHeaders()} to the resource.
     * </p>
     * 
     * @return WebResource
     * @throws FedoraClientException if {@link #DEFAULT_CLIENT} is <code>null</code>
     */
    protected WebResource resource() throws FedoraClientException {
        if (DEFAULT_CLIENT == null) {
            throw new FedoraClientException("No default FedoraClient was set.");
        }
        return resource(DEFAULT_CLIENT);
    }

    /**
     * <p>Returns a <code>WebResource</code> as supplied by the
     * {@link #DEFAULT_CLIENT} using <code>FedoraClient.resource()</code>.
     * Also adds any headers as found via {@link getHeaders()} to the resource.
     * </p>
     * 
     * @param path the path to use for this resource
     * @return WebResource
     * @throws FedoraClientException if {@link #DEFAULT_CLIENT} is <code>null</code>
     */
    protected WebResource resource(String path) throws FedoraClientException {
        if (DEFAULT_CLIENT == null) {
            throw new FedoraClientException("No default FedoraClient was set.");
        }
        return resource(DEFAULT_CLIENT, path);
    }

    /**
     * <p>Returns a <code>WebResource</code> as supplied by the
     * <code>FedoraClient</code> fc using <code>FedoraClient.resource()</code>.
     * Also adds any headers as found via {@link getHeaders()} to the resource.
     * </p>
     * 
     * @param fc the FedoraClient from which to get this resource
     * @return WebResource
     */
    protected WebResource resource(FedoraClient fc) {
        WebResource resource = fc.resource();
        for (Entry<String, List<String>> entry : getHeaders().entrySet()) {
            for (String value : entry.getValue()) {
                resource.header(entry.getKey(), value);
            }
        }
        return resource;
    }

    /**
     * <p>Returns a <code>WebResource</code> as supplied by the
     * <code>FedoraClient</code> fc using <code>FedoraClient.resource()</code>.
     * Also adds any headers as found via {@link getHeaders()} to the resource.
     * </p>
     * 
     * @param fc the FedoraClient from which to get this resource
     * @param path the path to use for this resource
     * @return WebResource
     */
    protected WebResource resource(FedoraClient fc, String path) {
        WebResource resource = fc.resource().path(path);
        for (Entry<String, List<String>> entry : getHeaders().entrySet()) {
            for (String value : entry.getValue()) {
                resource.header(entry.getKey(), value);
            }
        }
        return resource;
    }
}