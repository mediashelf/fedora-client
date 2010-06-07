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
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the Export method.
 *
 * @author Edwin Shin
 *
 */
public class Export
        extends FedoraRequest<Export> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public Export(String pid) {
        this.pid = pid;
    }

    /**
     *
     * @param format The XML format to export. Defaults to
     *               "info:fedora/fedora-system:FOXML-1.1".
     * @return this builder
     */
    public Export format(String format) {
        addQueryParam("format", format);
        return this;
    }

    /**
     *
     * @param context The export context, which determines how datastream URLs
     *        and content are represented. One of "public", "migrate", "archive".
     *        Defaults to "public".
     * @return this builder
     */
    public Export context(String context) {
        addQueryParam("context", context);
        return this;
    }

    /**
     *
     * @param encoding The preferred encoding of the exported XML. Defaults to
     *                 "UTF-8".
     * @return this builder
     */
    public Export encoding(String encoding) {
        addQueryParam("encoding", encoding);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/export", pid);

        return new FedoraResponseImpl(wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class));
    }

}