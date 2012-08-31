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


import java.util.Arrays;
import java.util.Collection;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.BatchResponse;

/**
 * <p>Builder for the BatchGetDatastreams method.
 * 
 * <p>BatchGetDatastreams is not natively supported by Fedora. This is an 
 * proof-of-concept implementation of a batch request and currently requires 
 * manual patching of Fedora to support this request.
 * 
 * <p>BatchGetDatastreams issues a single client request for each of the 
 * requested datastreams of each of the requested pids and returns the 
 * combined result as a multipart/mixed response.
 * 
 * @see <a href="https://github.com/mediashelf/fedora-batch">https://github.com/mediashelf/fedora-batch</a>
 *
 * @author Edwin Shin
 */
public class BatchGetDatastreams extends FedoraRequest<BatchGetDatastreams> {

    /**
     * 
     * @param pids The pids to fetch datastreams from
     * @return this builder
     */
    public BatchGetDatastreams pids(Collection<String> pids) {
        for (String pid : pids) {
            addQueryParam("pid", pid);
        }
        return this;
    }
    
    /**
     * <p>Convenience method for {@link #pids(Collection)}.
     * 
     * @param pids The pids to fetch datastreams from
     * @return this builder
     */
    public BatchGetDatastreams pids(String... pids) {
        return pids(Arrays.asList(pids));
    }
    
    /**
     * 
     * @param dsids The datastream ids that will be requested of each object
     * @return this builder
     */
    public BatchGetDatastreams dsids(Collection<String> dsids) {
        for (String dsid : dsids) {
            addQueryParam("dsid", dsid);
        }
        return this;
    }
    
    /**
     * <p>Convenience method for {@link #dsids(Collection)}.
     * 
     * @param dsids The datastream ids that will be requested of each object
     * @return this builder
     */
    public BatchGetDatastreams dsids(String... dsids) {
        return dsids(Arrays.asList(dsids));
    }

    public BatchGetDatastreams asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    public BatchGetDatastreams download(boolean download) {
        addQueryParam("download", Boolean.toString(download));
        return this;
    }

    @Override
    public BatchResponse execute() throws FedoraClientException {
        return (BatchResponse) super.execute();
    }

    @Override
    public BatchResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        WebResource wr = resource(fedora);
        String path = "batch/getDatastreams";

        return new BatchResponse(wr.path(path).queryParams(getQueryParams())
                .get(ClientResponse.class));
    }

}