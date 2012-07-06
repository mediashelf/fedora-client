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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.BatchResponse;
import com.yourmediashelf.fedora.util.DateUtility;

/**
 * <p>Builder for the BatchDeleteDatastreams method.
 * 
 * <p>This provides support for the fedora-batch project, a set of batch API
 * operations on Fedora. BatchDeleteDatastreams should be considered highly 
 * experimental: the API is very likely to change. You have been warned.
 *
 * @author Edwin Shin
 */
public class BatchDeleteDatastreams extends FedoraRequest<BatchDeleteDatastreams> {

    public BatchDeleteDatastreams pids(Collection<String> pids) {
        for (String pid : pids) {
            addQueryParam("pid", pid);
        }
        return this;
    }

    public BatchDeleteDatastreams pids(String... pids) {
        return pids(Arrays.asList(pids));
    }

    public BatchDeleteDatastreams dsids(Collection<String> dsids) {
        for (String dsid : dsids) {
            addQueryParam("dsid", dsid);
        }
        return this;
    }

    public BatchDeleteDatastreams dsids(String... dsids) {
        return dsids(Arrays.asList(dsids));
    }

    /**
     *
     * @param startDT the (inclusive) start date-time stamp of the range. If not
     * specified, this is taken to be the lowest possible value, and thus, the
     * entire version history up to the endDT will be purged.
     * @return this builder
     */
    public BatchDeleteDatastreams startDT(String startDT) {
        addQueryParam("startDT", startDT);
        return this;
    }

    public BatchDeleteDatastreams startDT(Date startDT) {
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
    public BatchDeleteDatastreams endDT(String endDT) {
        addQueryParam("endDT", endDT);
        return this;
    }

    public BatchDeleteDatastreams endDT(Date endDT) {
        addQueryParam("endDT", DateUtility.getXSDDateTime(endDT));
        return this;
    }

    public BatchDeleteDatastreams logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    @Override
    public BatchResponse execute() throws FedoraClientException {
        return (BatchResponse) super.execute();
    }

    @Override
    public BatchResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        String path = String.format("batch/deleteDatastreams");

        WebResource wr =
                resource(fedora).path(path).queryParams(getQueryParams());
        ClientResponse cr = wr.delete(ClientResponse.class);
        return new BatchResponse(cr);
    }
}