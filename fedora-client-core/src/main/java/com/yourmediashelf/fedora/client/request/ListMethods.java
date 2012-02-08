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
import com.yourmediashelf.fedora.client.response.ListMethodsResponse;

/**
 * Builder for the ListMethods method.
 *
 * @author Edwin Shin
 */
public class ListMethods
        extends FedoraRequest<ListMethods> {

    private final String pid;
    private final String sdefPid;

    /**
    *
    * @param pid persistent identifier of the digital object
    * @param sdefPid persistent identifier of the SDef defining the methods
    */
   public ListMethods(String pid, String sdefPid) {
       this.pid = pid;
       this.sdefPid = sdefPid;
   }

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public ListMethods(String pid) {
        this(pid, null);
    }

    public ListMethods asOfDateTime(String asOfDateTime) {
        addQueryParam("asOfDateTime", asOfDateTime);
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
    public ListMethods format(String format) {
        addQueryParam("format", format);
        return this;
    }

    @Override
    public ListMethodsResponse execute() throws FedoraClientException {
        return (ListMethodsResponse)super.execute();
    }
    
    @Override
    public ListMethodsResponse execute(FedoraClient fedora) throws FedoraClientException {
        // default to xml for the format, so we can parse the results
        if (getFirstQueryParam("format") == null) {
            addQueryParam("format", "xml");
        }
        WebResource wr = fedora.resource();
        String path;
        if (sdefPid == null) {
            path = String.format("objects/%s/methods", pid);
        } else {
            path = String.format("objects/%s/methods%s", pid, sdefPid);
        }

        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class);
        return new ListMethodsResponse(cr);
    }

}