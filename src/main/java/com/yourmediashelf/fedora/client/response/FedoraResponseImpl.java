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

package com.yourmediashelf.fedora.client.response;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.util.NamespaceContextImpl;

/**
 * Base class for FedoraResponse implementations.
 *
 * @author Edwin Shin
 */
public class FedoraResponseImpl implements FedoraResponse {
    private final ClientResponse cr;
    private final int status;
    private final NamespaceContextImpl nsCtx;

    /**
     * Constructor for a FedoraResponseImpl.
     *
     * @param cr
     * @throws FedoraClientException if the HTTP status code of the response is
     * >= 400.
     */
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
