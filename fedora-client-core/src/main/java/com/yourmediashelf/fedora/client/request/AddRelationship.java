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
public class AddRelationship extends RelationshipsRequest {

    public AddRelationship(String subject) {
        super(subject);
    }

    /**
     * The subject of the relationship. If <code>null</code>, defaults to the
     * URI form of the constructor-provided pid, e.g.
     * <code>info:fedora/demo:1</code>.
     * 
     * @param subject
     *            the subject of the relationship
     * @return this builder
     * @deprecated use constructor
     */
    public AddRelationship subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public AddRelationship predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    /**
     * Add the object of the relationship.
     * 
     * <p>
     * Unless otherwise indicated with the now-deprecated
     * {@link #isLiteral(boolean)} method, the object will be added as a
     * resource (i.e. a URI, not as a literal).
     * 
     * @param object
     *            the object of the relationship
     * @return this builder
     */
    public AddRelationship object(String object) {
        addQueryParam("object", object);
        return this;
    }

    /**
     * Add the object of the relationship as a plain literal.
     * 
     * @param object
     *            the object of the relationship
     * @param isLiteral
     *            whether or not the object is a literal.
     * @return this builder
     */
    public AddRelationship object(String object, boolean isLiteral) {
        addQueryParam("object", object);
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }

    /**
     * Add the object of the relationship as a datatyped literal.
     * 
     * @param object
     *            the object of the relationship
     * @param datatype
     *            The URI of the XML Schema Datatype of the object literal, e.g.
     *            "http://www.w3.org/2001/XMLSchema#dateTime".
     * @return this builder
     */
    public AddRelationship object(String object, String datatype) {
        addQueryParam("object", object);
        addQueryParam("isLiteral", Boolean.toString(true));
        addQueryParam("datatype", datatype);
        return this;
    }

    /**
     * Indicate whether the object is a literal. If omitted, defaults to false.
     * 
     * @param isLiteral
     *            indicate whether the object is a literal.
     * @return this builder
     * @deprecated use {@link #object(String, boolean)}
     */
    public AddRelationship isLiteral(boolean isLiteral) {
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }

    /**
     * The URI of the XML Schema Datatype of the object literal, e.g.
     * "http://www.w3.org/2001/XMLSchema#dateTime".
     * 
     * @param datatype
     *            the datatype of the literal
     * @return this builder
     * @deprecated use {@link #object(String, String)}
     */
    public AddRelationship datatype(String datatype) {
        addQueryParam("datatype", datatype);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        WebResource wr =
                resource(fedora,
                        String.format("objects/%s/relationships/new", pid))
                        .queryParams(getQueryParams());

        return new FedoraResponseImpl(wr.post(ClientResponse.class));
    }

}