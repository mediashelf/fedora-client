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
import com.yourmediashelf.fedora.client.request.Validate;
import com.yourmediashelf.fedora.generated.management.Validation;

/**
 * A {@link FedoraResponse} for the {@link Validate} request.
 *
 * @author Edwin Shin
 */
public class ValidateResponse
        extends FedoraResponseImpl {
	
	private Validation validation;

    public ValidateResponse(ClientResponse cr) throws FedoraClientException {
        super(cr);
    }
    
    public Validation getValidation() throws FedoraClientException {
        if (validation == null) {
        	validation =
                (Validation) unmarshallResponse(ContextPath.Access);
        }
        return validation;
    }
    
    /**
     * Convenience method that returns the pid of the object.
     *
     * @return the pid of the object
     * @throws FedoraClientException
     */
    public String getPid() throws FedoraClientException {
    	return getValidation().getPid();
    }
    
    /**
     * Convenience method that returns the request's dateTime.
     *
     * @return the dateTime of the request
     * @throws FedoraClientException
     */
    public String getAsOfDateTime() throws FedoraClientException {
    	return getValidation().getAsOfDateTime().toXMLFormat();
    }
    
    /**
     * Convenience method that returns true if the object is valid with 
     * respect to its content model(s).
     * 
     * @return true iff the object is valid with respect to its ContentModel(s)
     * @throws FedoraClientException
     */
    public boolean isValid() throws FedoraClientException {
    	return getValidation().isValid();
    }
}
