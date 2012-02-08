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

import java.util.Date;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.PurgeDatastreamResponse;
import com.yourmediashelf.fedora.util.DateUtility;

/**
 * Builder for the PurgeDatastream method.
 *
 * @author Edwin Shin
 */
public class PurgeDatastream extends FedoraRequest<PurgeDatastream> {
    private final String pid;
    private final String dsId;

    public PurgeDatastream(String pid, String dsId) {
        this.pid = pid;
        this.dsId = dsId;
    }

    public PurgeDatastream logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    /**
     *
     * @param startDT the (inclusive) start date-time stamp of the range. If not
     * specified, this is taken to be the lowest possible value, and thus, the
     * entire version history up to the endDT will be purged.
     * @return this builder
     */
    public PurgeDatastream startDT(String startDT) {
        addQueryParam("startDT", startDT);
        return this;
    }

    public PurgeDatastream startDT(Date startDT) {
        addQueryParam("startDT", DateUtility.getXSDDateTime(startDT));
        return this;
    }

    /**
     *
     * @param endDT the (inclusive) ending date-time stamp of the range. If not
     * specified, this is taken to be the greatest possible value, and thus, the
     * entire version history back to the startDT will be purged.
     * @return this builder
     */
    public PurgeDatastream endDT(String endDT) {
        addQueryParam("endDT", endDT);
        return this;
    }

    public PurgeDatastream endDT(Date endDT) {
        addQueryParam("endDT", DateUtility.getXSDDateTime(endDT));
        return this;
    }
    
    @Override
    public PurgeDatastreamResponse execute() throws FedoraClientException {
        return (PurgeDatastreamResponse)super.execute();
    }

    @Override
    public PurgeDatastreamResponse execute(FedoraClient fedora) throws FedoraClientException {
        String path = String.format("objects/%s/datastreams/%s", pid, dsId);

        WebResource wr = fedora.resource().path(path).queryParams(getQueryParams());
        ClientResponse cr = wr.delete(ClientResponse.class);
        return new PurgeDatastreamResponse(cr);
    }
}