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
import com.yourmediashelf.fedora.client.response.ValidateResponse;

/**
 * Builder for the Validate method.
 *
 * @author Edwin Shin
 */
public class Validate extends FedoraRequest<Validate> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public Validate(String pid) {
        this.pid = pid;
    }

    /**
    *
    * @param asOfDateTime yyyy-MM-dd or yyyy-MM-ddTHH:mm:ssZ
    * @return this builder
    */
    public Validate asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    @Override
    public ValidateResponse execute() throws FedoraClientException {
        return (ValidateResponse) super.execute();
    }

    @Override
    public ValidateResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        WebResource wr = resource();
        String path = String.format("objects/%s/validate", pid);

        return new ValidateResponse(wr.path(path).queryParams(getQueryParams())
                .get(ClientResponse.class));
    }

}