package com.yourmediashelf.fedora.client.request;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;


public class ModifyDatastream extends FedoraMethod {
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

    public ModifyDatastream logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public ModifyDatastream versionable(boolean versionable) {
        addQueryParam("versionable", Boolean.toString(versionable));
        return this;
    }

    public ModifyDatastream content(Object content) {
        this.content = content;
        return this;
    }

    @Override
    protected ClientResponse execute(FedoraClient fedora) {
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
            response = wr.post(ClientResponse.class);
        } else if (content instanceof String) {
            response = wr.type(MediaType.TEXT_XML_TYPE).post(ClientResponse.class,
                                                             content);
        } else if (content instanceof File) {
            File f = (File) content;
            String mimeType = fedora.getMimeType(f);
            response = wr.type(mimeType).post(ClientResponse.class, f);
        } else {
            throw new IllegalArgumentException("unknown request content type");
        }
        return response;
    }

}
