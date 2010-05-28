package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.util.DateUtility.getXSDDateTime;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;


public class ModifyDatastream extends FedoraRequest<ModifyDatastream> {
    private final String pid;
    private final String dsId;
    private Object content;

    public ModifyDatastream(String pid, String dsId) {
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

    public ModifyDatastream lastModifiedDate(DateTime lastModifiedDate) {
        addQueryParam("lastModifiedDate", getXSDDateTime(lastModifiedDate));
        return this;
    }

    public ModifyDatastream lastModifiedDate(String lastModifiedDate) {
        addQueryParam("lastModifiedDate", lastModifiedDate);
        return this;
    }

    public ModifyDatastream logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public ModifyDatastream versionable(boolean versionable) {
        addQueryParam("versionable", Boolean.toString(versionable));
        return this;
    }

    public ModifyDatastream content(File content) {
        this.content = content;
        return this;
    }

    public ModifyDatastream content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path;
        if (dsId == null) {
            path = String.format("objects/%s/datastreams/", pid);
        } else {
            path = String.format("objects/%s/datastreams/%s", pid, dsId);
        }
        wr = wr.path(path).queryParams(getQueryParams());

        ClientResponse response = null;
        if (content == null) {
            response = wr.put(ClientResponse.class);
        } else if (content instanceof String) {
            response = wr.type(MediaType.TEXT_XML_TYPE).put(ClientResponse.class,
                                                             content);
        } else if (content instanceof File) {
            File f = (File) content;
            String mimeType = fedora.getMimeType(f);
            response = wr.type(mimeType).put(ClientResponse.class, f);
        } else {
            throw new IllegalArgumentException("unknown request content type");
        }
        return new FedoraResponseImpl(response);
    }

}
