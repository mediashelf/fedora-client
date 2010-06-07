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

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.Ingest;

/**
 * A {@link FedoraResponse} for the {@link Ingest} request.
 *
 * @author Edwin Shin
 */
public class IngestResponse extends FedoraResponseImpl {
    private String pid;
    private final URI location;

    public IngestResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
        location = cr.getLocation();
    }

    /**
     *
     * @return the pid of the newly created object.
     */
    public String getPid() {
        if (pid == null) {
            pid = getEntity(String.class);
        }
        return pid;
    }

    /**
     *
     * @return the URI of the newly created resource.
     */
    public URI getLocation() {
        return location;
    }
}
