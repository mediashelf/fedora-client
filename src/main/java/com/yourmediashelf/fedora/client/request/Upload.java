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
 * @author Edwin Shin
 */
public class Upload
        extends FedoraRequest<Upload> {

    private final File file;

    /**
     * @param file
     *        the file to upload
     */
    public Upload(File file) {
        this.file = file;
    }

    @Override
    public UploadResponse execute(FedoraClient fedora) throws FedoraClientException {
        ClientResponse response = null;
        WebResource wr = fedora.resource();
        String path = String.format("management/upload");

        MediaType mediaType = MediaType.valueOf(fedora.getMimeType(file));
        MultiPart multiPart = new FormDataMultiPart().bodyPart(new FileDataBodyPart("file", file, mediaType));

        response = wr.path(path).queryParams(getQueryParams()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
        return new UploadResponse(response);
    }
}