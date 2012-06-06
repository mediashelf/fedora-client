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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the PurgeObject method.
 *
 * @author Edwin Shin
 */
public class PurgeObject extends FedoraRequest<PurgeObject> {

    private final String pid;

    /**
     *
     * @param pid the identifier of the object to delete.
     */
    public PurgeObject(String pid) {
        this.pid = pid;
    }

    public PurgeObject logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        String path = String.format("objects/%s", pid);

        WebResource wr =
                resource(fedora).path(path).queryParams(getQueryParams());
        return new FedoraResponseImpl(wr.delete(ClientResponse.class));
    }
}