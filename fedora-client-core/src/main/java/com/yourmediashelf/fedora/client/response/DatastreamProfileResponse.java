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

import java.util.Date;

import javax.xml.bind.JAXBElement;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;
import com.yourmediashelf.fedora.util.DateUtility;

/**
 * <p>A {@link FedoraResponse} for requests that return a datastreamProfile.</p>
 *
 * <p>This includes AddDatastream, GetDatastream, and ModifyDatastream.<p>
 *
 * @author Edwin Shin
 */
public class DatastreamProfileResponse extends FedoraResponseImpl {

    private DatastreamProfile datastreamProfile;

    public DatastreamProfileResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    public boolean isChecksumValid() throws FedoraClientException {
        return Boolean
                .parseBoolean(getDatastreamProfile().getDsChecksumValid());
    }

    /**
     * Get the last modified date of the datastream.
     *
     * <p>Note this method will fail if the request specified format=html
     * rather than xml.</p>
     *
     * @return the lastModifiedDate of the datastream
     * @throws FedoraClientException
     */
    public Date getLastModifiedDate() throws FedoraClientException {
        return DateUtility.parseXSDDateTime(
                getDatastreamProfile().getDsCreateDate().toXMLFormat())
                .toDate();
    }

    /**
     * Get the datastreamProfile. Note this method will fail if the request
     * specified format=html rather than xml.
     *
     * @return the DatastreamProfile
     * @throws FedoraClientException
     */
    public DatastreamProfile getDatastreamProfile()
            throws FedoraClientException {
        if (datastreamProfile == null) {

            // FIXME: after the changes to listDatastreams.xsd to support GetDatastreams,
            // this now throws: 
            // javax.xml.bind.JAXBElement cannot be cast to com.yourmediashelf.fedora.generated.management.DatastreamProfile
            //
            //datastreamProfile =
            //        (DatastreamProfile) unmarshallResponse(ContextPath.Management);

            JAXBElement<DatastreamProfile> root =
                    (JAXBElement<DatastreamProfile>) unmarshallResponse(ContextPath.Management);
            datastreamProfile = root.getValue();

        }
        return datastreamProfile;
    }
}
