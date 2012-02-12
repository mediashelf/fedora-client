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
 * Builder for the PurgeRelationship Method.
 *
 * @author Edwin Shin
 */
public class PurgeRelationship extends RelationshipsRequest {
    public PurgeRelationship(String subject) {
        super(subject);
    }

    public PurgeRelationship subject(String subject) {
        addQueryParam("subject", subject);
        return this;
    }

    public PurgeRelationship predicate(String predicate) {
        addQueryParam("predicate", predicate);
        return this;
    }

    /**
     * The object of the relationship.
     * 
     * <p>Unless otherwise indicated with the now-deprecated 
     * {@link #isLiteral(boolean)} method, the object will be treated as a 
     * resource (i.e. a URI, not as a literal).
     * 
     * @param object the object of the relationship
     * @return this builder
     */
    public PurgeRelationship object(String object) {
        addQueryParam("object", object);
        return this;
    }
    
    /**
     * The object of the relationship as a plain literal.
     * 
     * @param object the object of the relationship
     * @param isLiteral whether or not the object is a literal.
     * @return this builder
     */
    public PurgeRelationship object(String object, boolean isLiteral) {
        addQueryParam("object", object);
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }
    
    /**
     * The object of the relationship as a datatyped literal. 
     * 
     * @param object the object of the relationship
     * @param datatype The URI of the XML Schema Datatype of the object literal, 
     * e.g. "http://www.w3.org/2001/XMLSchema#dateTime".
     * @return this builder
     */
    public PurgeRelationship object(String object, String datatype) {
        addQueryParam("object", object);
        addQueryParam("isLiteral", Boolean.toString(true));
        addQueryParam("datatype", datatype);
        return this;
    }

    /**
     * 
     * @param isLiteral
     * @return this builder
     * @deprecated use {@link #object(String, boolean)}
     */
    public PurgeRelationship isLiteral(boolean isLiteral) {
        addQueryParam("isLiteral", Boolean.toString(isLiteral));
        return this;
    }

    /**
     * 
     * @param datatype
     * @return this builder
     * @deprecated use {@link #object(String, String)}
     */
    public PurgeRelationship datatype(String datatype) {
        addQueryParam("datatype", datatype);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/relationships", pid);

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).delete(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }

}