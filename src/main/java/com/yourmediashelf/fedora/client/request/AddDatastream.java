
package com.yourmediashelf.fedora.client.request;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;

public class AddDatastream
        extends FedoraMethod {

    private final String pid;

    private final String dsId;

    private Object content;

    /**
     * @param pid
     *        persistent identifier of the digital object
     * @param dsId
     *        datastream identifier
     */
    public AddDatastream(String pid, String dsId) {
        this.pid = pid;
        this.dsId = dsId;
    }

    public AddDatastream altIDs(List<String> altIDs) {
        for (String altID : altIDs) {
            addQueryParam("altIDs", altID);
        }
        return this;
    }

    public AddDatastream checksum(String checksum) {
        addQueryParam("checksum", checksum);
        return this;
    }

    public AddDatastream checksumType(String checksumType) {
        addQueryParam("checksumType", checksumType);
        return this;
    }

    /**
     * @param controlGroup
     *        one of "X", "M", "R", or "E" (Inline *X*ML, *M*anaged Content,
     *        *R*edirect, or *E*xternal Referenced)
     * @return
     */
    public AddDatastream controlGroup(String controlGroup) {
        addQueryParam("controlGroup", controlGroup);
        return this;
    }

    public AddDatastream dsLabel(String dsLabel) {
        addQueryParam("dsLabel", dsLabel);
        return this;
    }

    /**
     * @param dsLocation
     *        location of managed or external datastream content
     * @return
     */
    public AddDatastream dsLocation(String dsLocation) {
        addQueryParam("dsLocation", dsLocation);
        return this;
    }

    /**
     * @param dsState
     *        one of "A", "I", "D" (*A*ctive, *I*nactive, *D*eleted)
     * @return
     */
    public AddDatastream dsState(String dsState) {
        addQueryParam("dsState", dsState);
        return this;
    }

    public AddDatastream formatURI(String formatURI) {
        addQueryParam("formatURI", formatURI);
        return this;
    }

    public AddDatastream logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public AddDatastream mimeType(String mimeType) {
        addQueryParam("mimeType", mimeType);
        return this;
    }

    public AddDatastream versionable(boolean versionable) {
        addQueryParam("versionable", Boolean.toString(versionable));
        return this;
    }

    /**
     * @param content the datastream content (XML)
     * @return
     */
    public AddDatastream content(String content) {
        this.content = content;
        return this;
    }

    /**
     * @param content the datastream content (File)
     * @return
     */
    public AddDatastream content(File content) {
        this.content = content;
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/datastreams/%s", pid, dsId);

        wr = wr.path(path).queryParams(getQueryParams());

        ClientResponse response = null;
        String mimeType = getFirstQueryParam("mimeType");
        MediaType mediaType = null;
        if (content == null) {
            response = wr.post(ClientResponse.class);
        } else if (content instanceof String) {
            if (mimeType == null) {
                mediaType = MediaType.TEXT_XML_TYPE;
            } else {
                mediaType = MediaType.valueOf(mimeType);
            }
            response = wr.type(mediaType).post(ClientResponse.class, content);
        } else if (content instanceof File) {
            File f = (File) content;
            if (mimeType == null) {
                mediaType = MediaType.valueOf(fedora.getMimeType(f));
            } else {
                mediaType = MediaType.valueOf(mimeType);
            }
            response = wr.type(mediaType).post(ClientResponse.class, f);
        } else {
            throw new IllegalArgumentException("unknown request content type");
        }
        return response;
    }

}