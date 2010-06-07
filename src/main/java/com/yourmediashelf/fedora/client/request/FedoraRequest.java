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

import javax.ws.rs.core.MultivaluedMap;

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

    private final MultivaluedMap<String, String> queryParams =
            new MultivaluedMapImpl();

    /**
     * <p>Execute this request using the supplied FedoraClient instance.</p>
     *
     * @param fedora an instance of FedoraClient
     * @return the response
     * @throws FedoraClientException if the HTTP status code of the response is
     * >= 400.
     */
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