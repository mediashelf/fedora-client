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
import com.yourmediashelf.fedora.client.response.ListDatastreamsResponse;

/**
 * Builder for the ListDatastreams method.
 *
 * @author Edwin Shin
 */
public class ListDatastreams extends FedoraRequest<ListDatastreams> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public ListDatastreams(String pid) {
        this.pid = pid;
    }

    /**
     * Indicates that the result should be relative to the digital object as it 
     * existed on the given date.
     * 
     * @param asOfDateTime datetime string as yyyy-MM-dd or yyyy-MM-ddTHH:mm:ssZ
     * @return this builder
     */
    public ListDatastreams asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
        return this;
    }

    /**
     * @param dsState
     *            one of "A", "I", "D" (*A*ctive, *I*nactive, *D*eleted)
     * @return this builder
     */
    public ListDatastreams dsState(String dsState) {
        addQueryParam("dsState", dsState);
        return this;
    }

    /**
     * <p>The format of the response. Defaults to "xml".</p>
     *
     * <p>The Fedora REST API default is "html", but
     * fedora-client will set "xml" as the default in order to parse the
     * response. If "html" is selected, the caller is responsible for parsing
     * the raw HTTP response as most of the FedoraResponse convenience methods
     * rely on an XML response.</p>
     *
     * @param format The response format, either "xml" or "html"
     * @return this builder
     */
    public ListDatastreams format(String format) {
        addQueryParam("format", format);
        return this;
    }

    /**
     * Verifies that the Datastream content has not changed since the checksum 
     * was initially computed. If {@link #asOfDateTime(String) asOfDateTime} is 
     * null, Fedora will use the most recent version.
     * 
     * @param validateChecksum
     * @return this builder
     */
    public ListDatastreams validateChecksum(boolean validateChecksum) {
        addQueryParam("validateChecksum", Boolean.toString(validateChecksum));
        return this;
    }

    @Override
    public ListDatastreamsResponse execute() throws FedoraClientException {
        return (ListDatastreamsResponse) super.execute();
    }

    @Override
    public ListDatastreamsResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        // default to xml for the format, so we can parse the results
        if (getFirstQueryParam("format") == null) {
            addQueryParam("format", "xml");
        }

        WebResource wr = resource(fedora);
        String path = String.format("objects/%s/datastreams", pid);

        ClientResponse cr =
                wr.path(path).queryParams(getQueryParams()).get(
                        ClientResponse.class);
        return new ListDatastreamsResponse(cr);
    }

}