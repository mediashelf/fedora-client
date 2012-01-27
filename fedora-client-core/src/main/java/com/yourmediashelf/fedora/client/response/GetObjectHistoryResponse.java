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
import com.yourmediashelf.fedora.client.request.GetObjectProfile;
import com.yourmediashelf.fedora.generated.access.FedoraObjectHistory;

/**
 * A {@link FedoraResponse} for the {@link GetObjectProfile} request.
 *
 * @author Edwin Shin
 */
public class GetObjectHistoryResponse
        extends FedoraResponseImpl {

    private FedoraObjectHistory objectHistory;

    public GetObjectHistoryResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    public FedoraObjectHistory getObjectHistory() throws FedoraClientException {
        if (objectHistory == null) {
        	objectHistory =
                (FedoraObjectHistory) unmarshallResponse(ContextPath.Access);
        }
        return objectHistory;
    }
    
    /**
     * Accessor for the pid of the ObjectHistory response.
     *
     * @return the pid
     * @throws FedoraClientException
     */
    public String getPid() throws FedoraClientException {
    	return getObjectHistory().getPid();
    }
    
    /**
     * Accessor for the objectChangeDate property of the ObjectHistory response.
     * 
     * Dates are returned as Strings in the following form: 2011-03-14T17:56:54.522Z
     *
     * @return List of objectChangeDates
     * @throws FedoraClientException
     */
    public List<String> getObjectChangeDate() throws FedoraClientException {
    	return getObjectHistory().getObjectChangeDate();
    }
}
