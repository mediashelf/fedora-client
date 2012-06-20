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
import com.yourmediashelf.fedora.client.response.DescribeRepositoryResponse;

/**
 * Builder for the DescribeRepository method.
 * 
 * <p>
 * As there is currently no REST-API implementation of DescribeRepository, this
 * request will be executed using the API-A-Lite interface.
 * 
 * @author Edwin Shin
 */
public class DescribeRepository extends FedoraRequest<DescribeRepository> {

    /**
     * <p>
     * Format the response as XML. Defaults to "true".
     * </p>
     * 
     * @param xml
     *            Defaults to true.
     * @return this builder
     */
    public DescribeRepository xml(boolean xml) {
        addQueryParam("xml", Boolean.toString(xml));
        return this;
    }

    @Override
    public DescribeRepositoryResponse execute() throws FedoraClientException {
        return (DescribeRepositoryResponse) super.execute();
    }

    @Override
    public DescribeRepositoryResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        // default to xml for the format, so we can parse the results
        if (getFirstQueryParam("xml") == null) {
            addQueryParam("xml", "true");
        }

        WebResource wr =
                resource(fedora, "describe").queryParams(getQueryParams());
        return new DescribeRepositoryResponse(wr.get(ClientResponse.class));
    }

}