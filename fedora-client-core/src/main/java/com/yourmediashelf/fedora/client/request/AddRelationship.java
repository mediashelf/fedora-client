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
 * Builder for the AddRelationship method.
 *
 * @author Edwin Shin
 */
public class AddRelationship
        extends FedoraRequest<AddRelationship> {

    private final String pid;

    public AddRelationship(String pid) {
        this.pid = pid;
    }

    public AddRelationship subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public AddRelationship predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    public AddRelationship object(String object) {
        addQueryParam("object", object);
        return this;
    }

    /**
     * Indicate whether the object is a literal. If omitted, defaults to false.
     *
     * @param isLiteral indicate whether the object is a literal.
     * @return this builder
     */
    public AddRelationship isLiteral(boolean isLiteral) {
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }

    /**
     * The URI of the XML Schema Datatype of the object literal, e.g.
     * "http://www.w3.org/2001/XMLSchema#dateTime".
     *
     * @param datatype the datatype of the literal
     * @return this builder
     */
    public AddRelationship datatype(String datatype) {
        addQueryParam("datatype", datatype);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships/new", pid);

        return new FedoraResponseImpl(wr.path(path)
                .queryParams(getQueryParams()).post(ClientResponse.class));
    }

}