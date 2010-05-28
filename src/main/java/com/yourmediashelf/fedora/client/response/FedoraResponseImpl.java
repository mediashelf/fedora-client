package com.yourmediashelf.fedora.client.response;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.util.NamespaceContextImpl;


public class FedoraResponseImpl implements FedoraResponse {
    private final ClientResponse cr;
    private final int status;
    private final NamespaceContextImpl nsCtx;

    public FedoraResponseImpl(ClientResponse cr) throws FedoraClientException {
        this.cr = cr;
        nsCtx = new NamespaceContextImpl();
        nsCtx.addNamespace("f", "info:fedora/fedora-system:def/foxml#");

        this.status = cr.getStatus();
        if (status >= 400) {
            String msg = cr.getEntity(String.class);
            throw new FedoraClientException(status,
                                            String.format("HTTP %d Error: %s", status, msg));
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

    @Override
    public <T> T getEntity(Class<T> c) {
        return cr.getEntity(c);
    }

    protected XPath getXPath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(nsCtx);
        return xpath;
    }
}
