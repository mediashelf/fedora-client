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
 * Builder for the GetObjectXML method.
 *
 * @author Edwin Shin
 * @since 0.0.3
 */
public class GetObjectXML extends FedoraRequest<GetObjectXML> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object, e.g. "demo:1".
     */
    public GetObjectXML(String pid) {
        this.pid = pid;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        WebResource wr = resource();
        String path = String.format("objects/%s/objectXML", pid);

        ClientResponse cr =
                wr.path(path).queryParams(getQueryParams()).get(
                        ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }
}