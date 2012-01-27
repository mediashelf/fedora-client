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
 * Builder for the GetRelationships method.
 *
 * @author Edwin Shin
 */
public class GetRelationships extends FedoraRequest<GetRelationships> {
    private final String pid;

    public GetRelationships(String pid) {
        this.pid = pid;
    }

    /**
     * The format of the response. Default is rdf/xml.
     *
     * @param format one of "rdf/xml", "n-triples", "turtle", or "sparql".
     * @return this builder
     */
    public GetRelationships format(String format) {
        addQueryParam("format", format);
        return this;
    }

    public GetRelationships subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public GetRelationships predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships", pid);

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}