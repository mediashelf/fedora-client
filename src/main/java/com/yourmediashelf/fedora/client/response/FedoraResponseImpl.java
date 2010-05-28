package com.yourmediashelf.fedora.client.response;

import java.io.InputStream;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;


public class FedoraResponseImpl implements FedoraResponse {
    private final ClientResponse cr;
    private final int status;

    public FedoraResponseImpl(ClientResponse cr) throws FedoraClientException {
        this.cr = cr;

        this.status = cr.getStatus();
        if (status >= 400) {
            String msg = cr.getEntity(String.class);
            throw new FedoraClientException(String.format("HTTP %d Error: %s", status, msg));
        }
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public InputStream getEntityInputStream() {
        return cr.getEntityInputStream();
    }

    public <T> T getEntity(Class<T> c) {
        return cr.getEntity(c);
    }
}
