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
import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.UploadResponse;

/**
 * Builder for the Upload method.
 *
 * <p>The Fedora REST API upload method was introduced in Fedora 3.4</p>
 *
 * @author Edwin Shin
 * @see "http://www.fedora-commons.org/jira/browse/FCREPO-687"
 */
public class Upload extends FedoraRequest<Upload> {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(this.getClass());

    private final File file;

    /**
     * @param file
     *        the file to upload
     */
    public Upload(File file) {
        this.file = file;
    }

    @Override
    public UploadResponse execute() throws FedoraClientException {
        return (UploadResponse) super.execute();
    }

    @Override
    public UploadResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        ClientResponse response = null;
        String path = String.format("upload");
        WebResource wr = resource().path(path);

        MediaType mediaType = MediaType.valueOf(fedora.getMimeType(file));
        MultiPart multiPart =
                new FormDataMultiPart().bodyPart(new FileDataBodyPart("file",
                        file, mediaType));

        // Check for a 302 (expected if baseUrl is http but Fedora is configured
        // to require SSL
        response = wr.head();
        if (response.getStatus() == 302) {
            URI newLocation = response.getLocation();
            logger.warn("302 status for upload request: " + newLocation);
            wr = resource(newLocation.toString());
        }

        response =
                wr.queryParams(getQueryParams()).type(
                        MediaType.MULTIPART_FORM_DATA_TYPE).post(
                        ClientResponse.class, multiPart);
        return new UploadResponse(response);
    }
}