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

import static com.yourmediashelf.fedora.util.DateUtility.getXSDDateTime;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;

/**
 * Builder for the ModifyDatastream method.
 *
 * @author Edwin Shin
 */
public class ModifyDatastream extends FedoraRequest<ModifyDatastream> {

    private final String pid;

    private final String dsId;

    private Object content;

    private String mimeType;

    public ModifyDatastream(String pid, String dsId) {
        if (pid == null || pid.isEmpty()) {
            throw new IllegalArgumentException("pid cannot be null or empty");
        }
        if (dsId == null || dsId.isEmpty()) {
            throw new IllegalArgumentException("dsId cannot be null or empty");
        }
        this.pid = pid;
        this.dsId = dsId;
    }

    public ModifyDatastream altIDs(List<String> altIDs) {
        for (String altID : altIDs) {
            addQueryParam("altIDs", altID);
        }
        return this;
    }

    public ModifyDatastream checksum(String checksum) {
        addQueryParam("checksum", checksum);
        return this;
    }

    /**
     * The algorithm used to compute the checksum.
     *
     * @param checksumType One of DEFAULT, DISABLED, MD5, SHA-1, SHA-256,
     * SHA-385, SHA-512
     * @return this builder
     */
    public ModifyDatastream checksumType(String checksumType) {
        addQueryParam("checksumType", checksumType);
        return this;
    }

    public ModifyDatastream controlGroup(String controlGroup) {
        addQueryParam("controlGroup", controlGroup);
        return this;
    }

    public ModifyDatastream dsLabel(String dsLabel) {
        addQueryParam("dsLabel", dsLabel);
        return this;
    }

    public ModifyDatastream dsLocation(String dsLocation) {
        addQueryParam("dsLocation", dsLocation);
        return this;
    }

    /**
     * One of "A", "I", "D" (*A*ctive, *I*nactive, *D*eleted)
     *
     * @param dsState
     * @return this builder
     */
    public ModifyDatastream dsState(String dsState) {
        addQueryParam("dsState", dsState);
        return this;
    }

    public ModifyDatastream formatURI(String formatURI) {
        addQueryParam("formatURI", formatURI);
        return this;
    }

    public ModifyDatastream ignoreContent(boolean ignoreContent) {
        addQueryParam("ignoreContent", Boolean.toString(ignoreContent));
        return this;
    }

    /**
     * Convenience method for {@link #lastModifiedDate(String)}.
     *
     * @param lastModifiedDate
     * @return this builder
     */
    public ModifyDatastream lastModifiedDate(DateTime lastModifiedDate) {
        return lastModifiedDate(getXSDDateTime(lastModifiedDate));
    }

    /**
     * Convenience method for {@link #lastModifiedDate(String)}.
     *
     * @param lastModifiedDate
     * @return this builder
     */
    public ModifyDatastream lastModifiedDate(Date lastModifiedDate) {
        return lastModifiedDate(getXSDDateTime(lastModifiedDate));
    }

    /**
     * <p>If provided, the server will use the supplied lastModifedDate to
     * prevent concurrent modifications, only performing the request if the
     * datastream has not been modified since the request-provided
     * lastModifiedDate. Otherwise, the request will fail with an HTTP 409
     * Conflict.</p>
     *
     * <p>Typical usage would be to get the lastModifiedDate of a datastream
     * before modification, and then to pass that date as part of the subsequent
     * modify request, which would then only succeed if the datastream has not
     * been already modified since.</p>
     *
     * <p>Supported against Fedora 3.4.0 and later (with earlier versions, this
     * parameter is ignored).</p>
     *
     * @param lastModifiedDate an xsd:dateTime string, e.g. 2001-12-31T12:50:01.000Z
     * @return this builder
     */
    public ModifyDatastream lastModifiedDate(String lastModifiedDate) {
        addQueryParam("lastModifiedDate", lastModifiedDate);
        return this;
    }

    public ModifyDatastream logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    /**
     * Set the mime type of the datastream.
     * 
     * <p>If not set, fedora-client will make a best-guess based on the actual 
     * content.
     * 
     * @param mimeType the mime type of the datastream.
     * @return this builder
     */
    public ModifyDatastream mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    /**
     * Enable versioning of the datastream.
     *
     * @param versionable
     * @return this builder
     */
    public ModifyDatastream versionable(boolean versionable) {
        addQueryParam("versionable", Boolean.toString(versionable));
        return this;
    }

    public ModifyDatastream content(File content) {
        this.content = content;
        return this;
    }

    public ModifyDatastream content(InputStream content) {
        this.content = content;
        return this;
    }

    public ModifyDatastream content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public ModifyDatastreamResponse execute() throws FedoraClientException {
        return (ModifyDatastreamResponse) super.execute();
    }

    @Override
    public ModifyDatastreamResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        WebResource wr = resource(fedora);
        String path = String.format("objects/%s/datastreams/%s", pid, dsId);
        wr = wr.path(path).queryParams(getQueryParams());

        ClientResponse response = null;
        if (content == null) {
            response = wr.put(ClientResponse.class);
        } else if (content instanceof String) {
            if (mimeType == null) {
                mimeType = MediaType.TEXT_XML_TYPE.toString();
            }
            response = wr.type(mimeType).put(ClientResponse.class, content);
        } else if (content instanceof InputStream) {
            response = wr.type(mimeType).put(ClientResponse.class, content);
        } else if (content instanceof File) {
            File f = (File) content;
            if (mimeType == null) {
                mimeType = fedora.getMimeType(f);
            }
            response = wr.type(mimeType).put(ClientResponse.class, f);
        } else {
            throw new IllegalArgumentException("unknown request content type");
        }
        return new ModifyDatastreamResponse(response);
    }

}
