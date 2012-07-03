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

import java.io.File;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.IngestResponse;

/**
 * Builder for the Ingest method.
 *
 * @author Edwin Shin
 */
public class Ingest extends FedoraRequest<Ingest> {

    private final String pid;

    private Object content;

    /**
     *
     * @param pid the identified to assign or null to use a server-assigned pid
     */
    public Ingest(String pid) {
        this.pid = pid;
    }

    /**
     * Constructor that will use a server-generated pid.
     */
    public Ingest() {
        this(null);
    }

    /**
     * The label of the new object
     *
     * @param label the object label
     * @return this builder
     */
    public Ingest label(String label) {
        addQueryParam("label", label);
        return this;
    }

    /**
     * <p>The XML format of the object to be ingested.</p>
     *
     * <p>One of:</p>
     * <ul>
     *  <li>info:fedora/fedora-system:FOXML-1.1</li>
     *  <li>info:fedora/fedora-system:FOXML-1.0</li>
     *  <li>info:fedora/fedora-system:METSFedoraExt-1.1</li>
     *  <li>info:fedora/fedora-system:METSFedoraExt-1.0</li>
     *  <li>info:fedora/fedora-system:ATOM-1.1</li>
     *  <li>info:fedora/fedora-system:ATOMZip-1.1</li>
     * </ul>
     *
     * @param format the XML format of the object to be ingested.
     * @return this builder
     */
    public Ingest format(String format) {
        addQueryParam("format", format);
        return this;
    }

    /**
     * The encoding of the XML to be ingested.
     *
     * @param encoding
     * @return this builder
     */
    public Ingest encoding(String encoding) {
        addQueryParam("encoding", encoding);
        return this;
    }

    /**
     * The namespace to be used to create a PID for a new empty object;
     * if object XML is included with the request, the namespace parameter is
     * ignored.
     *
     * @param namespace the namespace of the PID to create
     * @return this builder
     */
    public Ingest namespace(String namespace) {
        addQueryParam("namespace", namespace);
        return this;
    }

    /**
     * The id of the user to be listed at the object owner.
     *
     * @param ownerId
     * @return this builder
     */
    public Ingest ownerId(String ownerId) {
        addQueryParam("ownerId", ownerId);
        return this;
    }

    /**
     * <p>A message describing the activity being performed, which will be
     * recorded in the Fedora server log.</p>
     *
     * @param logMessage the log message
     * @return this builder
     */
    public Ingest logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    /**
     * <p>Indicates that the request should not be checked to ensure that the
     * content is XML prior to attempting an ingest.</p>
     *
     * <p>This is provided to allow for client applications which do not
     * indicate the correct Content-Type when submitting a request.</p>
     *
     * <p>If not provided, defaults to false.</p>
     *
     * @param ignoreMime
     * @return this builder
     */
    public Ingest ignoreMime(boolean ignoreMime) {
        addQueryParam("ignoreMime", Boolean.toString(ignoreMime));
        return this;
    }

    /**
     * The InputStream to be ingested as a new object.
     *
     * @param content
     * @return this builder
     */
    public Ingest content(InputStream content) {
        this.content = content;
        return this;
    }

    /**
     * The file to be ingested as a new object.
     *
     * @param content
     * @return this builder
     */
    public Ingest content(File content) {
        this.content = content;
        return this;
    }

    /**
     * The XML to be ingested as a new object.
     *
     * @param content
     * @return this builder
     */
    public Ingest content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public IngestResponse execute() throws FedoraClientException {
        return (IngestResponse) super.execute();
    }

    @Override
    public IngestResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        ClientResponse response = null;
        String path;
        if (pid == null || pid.isEmpty()) {
            path = "objects/new";
        } else {
            path = String.format("objects/%s", pid);
        }

        WebResource wr =
                resource(fedora).path(path).queryParams(getQueryParams());

        if (content == null) {
            response = wr.post(ClientResponse.class);
        } else if (content instanceof String) {
            response =
                    wr.type(MediaType.TEXT_XML_TYPE).post(ClientResponse.class,
                            content);
        } else if (content instanceof InputStream) {
            response =
                    wr.type(MediaType.TEXT_XML_TYPE).post(ClientResponse.class,
                            (InputStream) content);
        } else if (content instanceof File) {
            File f = (File) content;
            MediaType mimeType = MediaType.TEXT_XML_TYPE;
            // FCREPO-1070 (https://jira.duraspace.org/browse/FCREPO-1070):
            //   requires that we do *not* set the mimeType to application/zip
            //if (getQueryParam("format").contains(
            //        "info:fedora/fedora-system:ATOMZip-1.1")) {
            //    mimeType = MediaType.valueOf("application/zip");
            //}
            response = wr.type(mimeType).post(ClientResponse.class, f);
        } else {
            throw new IllegalArgumentException("unknown request content type");
        }
        return new IngestResponse(response);
    }
}