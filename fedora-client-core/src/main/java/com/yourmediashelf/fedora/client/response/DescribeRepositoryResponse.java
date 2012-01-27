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

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.DescribeRepository;
import com.yourmediashelf.fedora.generated.access.FedoraRepository;

/**
 * <p>A {@link FedoraResponse} for the {@link DescribeRepository} request.</p>
 *
 * @author Edwin Shin
 */
public class DescribeRepositoryResponse
        extends FedoraResponseImpl {
    private FedoraRepository repositoryInfo;

    public DescribeRepositoryResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }

    public String getRepositoryVersion() throws FedoraClientException {
        return getRepositoryInfo().getRepositoryVersion();
    }

    /**
     * Get the repository description. Note this method will fail if the request
     * specified xml=false rather than true.
     *
     * @return the Repository Info
     * @throws FedoraClientException
     */
    public FedoraRepository getRepositoryInfo() throws FedoraClientException {
        if (repositoryInfo == null) {
        	repositoryInfo = (FedoraRepository) unmarshallResponse(ContextPath.Access);
        }
        return repositoryInfo;
    }
}
