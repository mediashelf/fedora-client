package com.yourmediashelf.fedora.client.request;

import java.io.File;

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

    public Ingest(String pid) {
        this.pid = pid;
    }

    public Ingest label(String label) {
        addQueryParam("label", label);
        return this;
    }

    public Ingest format(String format) {
        addQueryParam("format", format);
        return this;
    }

    public Ingest encoding(String encoding) {
        addQueryParam("encoding", encoding);
        return this;
    }

    public Ingest namespace(String namespace) {
        addQueryParam("namespace", namespace);
        return this;
    }

    public Ingest ownerId(String ownerId) {
        addQueryParam("ownerId", ownerId);
        return this;
    }

    public Ingest logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public Ingest ignoreMime(String ignoreMime) {
        addQueryParam("ignoreMime", ignoreMime);
        return this;
    }

    public Ingest content(Object content) {
        this.content = content;
        return this;
    }

    @Override
    public IngestResponse execute(FedoraClient fedora) throws FedoraClientException {
        ClientResponse response = null;
        String path;
        if (pid == null || pid.isEmpty()) {
            path = "objects/new";
        } else {
            path = String.format("objects/%s", pid);
        }

        WebResource wr = fedora.resource().path(path).queryParams(getQueryParams());

        if (content == null) {
            response = wr.post(ClientResponse.class);
        } else if (content instanceof String) {
            response =
                    wr.type(MediaType.TEXT_XML_TYPE).post(ClientResponse.class,
                                                          content);
        } else if (content instanceof File) {
            File f = (File) content;
            response =
                    wr.type(MediaType.TEXT_XML_TYPE).post(ClientResponse.class,
                                                          f);
        } else {
            throw new IllegalArgumentException("unknown request content type");
        }
        return new IngestResponse(response);
    }
}