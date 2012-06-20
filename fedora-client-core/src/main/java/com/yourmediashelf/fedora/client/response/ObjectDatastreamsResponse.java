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

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.GetDatastreams;
import com.yourmediashelf.fedora.client.request.ListDatastreams;
import com.yourmediashelf.fedora.generated.access.ObjectDatastreams;

/**
 * Base class for the {@link ListDatastreams} and {@link GetDatastreams} requests.
 *
 * @author Edwin Shin
 */
abstract public class ObjectDatastreamsResponse extends FedoraResponseImpl {

    private ObjectDatastreams objectDatastreams;

    public ObjectDatastreamsResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    /**
     * Accessor for the pid of the datastreams returned by the request.
     *
     * @return the pid for the parent object of the datastreams
     * @throws FedoraClientException
     */
    public String getPid() throws FedoraClientException {
        return getObjectDatastreams().getPid();
    }

    /**
     * @return the baseURL of the Fedora server that provided the response, e.g.
     *         http://example.org:8080/fedora
     * @throws FedoraClientException
     */
    public String getBaseUrl() throws FedoraClientException {
        return getObjectDatastreams().getBaseURL();
    }

    /**
     *
     * @return the value of the getAsOfDateTime query parameter passed into the
     * original ListDatastreams request.
     * @throws FedoraClientException
     */
    public String getAsOfDateTime() throws FedoraClientException {
        return getObjectDatastreams().getAsOfDateTime();
    }

    protected ObjectDatastreams getObjectDatastreams()
            throws FedoraClientException {
        if (objectDatastreams == null) {
            objectDatastreams =
                    (ObjectDatastreams) unmarshallResponse(ContextPath.Access);
        }
        return objectDatastreams;
    }
}
