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

import java.util.List;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.GetDatastreams;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

/**
 * A {@link FedoraResponse} for the {@link GetDatastreams} request.
 *
 * @author Edwin Shin
 */
public class GetDatastreamsResponse extends ObjectDatastreamsResponse {

    public GetDatastreamsResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    /**
     * The List of requested datastream profiles.
     * <p>
     * If the ListDatastreams request explicitly set format=html, this method
     * call will fail.
     * </p>
     *
     * @return the List of datastream profiles
     * @throws FedoraClientException
     */
    public List<DatastreamProfile> getDatastreamProfiles()
            throws FedoraClientException {
        return getObjectDatastreams().getDatastreamProfile();
    }
}
