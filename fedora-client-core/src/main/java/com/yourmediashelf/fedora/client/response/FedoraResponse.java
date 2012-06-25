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

package com.yourmediashelf.fedora.client.response;

import java.io.InputStream;

import com.yourmediashelf.fedora.client.request.FedoraRequest;

/**
 * <p>A wrapper for the HTTP response to a {@link FedoraRequest}.</p>
 *
 * <p>Request-specific FedoraResponse implementations should be generally
 * preferred as they provide convenience methods specific to the kind of
 * response. For example, {@link GetNextPIDResponse} provides a convenience
 * method that parses the HTTP response an returns a List of the requested
 * pids.</p>
 *
 * <p>Note that {@link #getEntity(Class)} and {@link #getEntityInputStream()}
 * provide access to the underlying response entity, if desired. However, using
 * these methods and the convenience methods that parse the response entity are
 * mutually exclusive, as either will close the underlying InputStream once
 * accessed.</p>
 *
 * <p>Implementations of FedoraResponse MUST throw a FedoraClientException if
 * the HTTP status code of the response is >= 400.</p>
 *
 * @author Edwin Shin
 */
public interface FedoraResponse {

    /**
     *
     * @return the HTTP Status Code of the response.
     */
    public int getStatus();

    /**
     * <p>Gets the raw response entity.</p>
     *
     * @return the entity response
     */
    public InputStream getEntityInputStream();

    /**
     * <p>Gets the raw response entity.</p>
     *
     * @return the entity response
     */
    public <T> T getEntity(Class<T> c);

    /**
     * <p>Close the response.
     * <p>The entity input stream is closed;
     */
    public void close();

}
