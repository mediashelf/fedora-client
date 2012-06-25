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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.SAXException;

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

    private Boolean validateSchema = null;

    /**
     * Constructor for a FedoraResponseImpl.
     * 
     * @param cr
     * @throws FedoraClientException
     *             if the HTTP status code of the response is >= 400.
     */
    public FedoraResponseImpl(ClientResponse cr)
            throws FedoraClientException {
        this.cr = cr;
        nsCtx = new NamespaceContextImpl();
        nsCtx.addNamespace("f", "info:fedora/fedora-system:def/foxml#");

        this.status = cr.getStatus();
        if (status >= 400) {
            String msg = cr.getEntity(String.class);
            throw new FedoraClientException(status, String.format(
                    "HTTP %d Error: %s", status, msg));
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

    /**
     * Unmarshall the Fedora ClientResponse using the JAXB schema-generated
     * classes (see: target/generated-sources/).
     * 
     * @param contextPath
     *            JAXB contextPath
     * @return the unmarshalled XML
     * @throws FedoraClientException
     */
    public Object unmarshallResponse(ContextPath contextPath)
            throws FedoraClientException {
        Object response = null;
        Schema schema = null;

        if (validateSchema()) {
            try {
                SchemaFactory schemaFactory =
                        SchemaFactory
                                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                schema = schemaFactory.newSchema();
            } catch (SAXException e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
        }

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath.path());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            response =
                    unmarshaller.unmarshal(new BufferedReader(
                            new InputStreamReader(getEntityInputStream())));
        } catch (JAXBException e) {
            throw new FedoraClientException(e.getMessage(), e);
        }
        return response;
    }

    private boolean validateSchema() {
        if (validateSchema == null) {
            String validate = System.getProperty("test.validate");
            validateSchema = Boolean.parseBoolean(validate);
        }
        return validateSchema.booleanValue();
    }

    /**
     * Enum for JAXB ContextPaths used by FedoraResponse implementations.
     * 
     * @author Edwin Shin
     * 
     */
    public enum ContextPath {
        Access("com.yourmediashelf.fedora.generated.access"), Management(
                "com.yourmediashelf.fedora.generated.management");

        private final String contextPath;

        /**
         * Returns the associated contextPath.
         * 
         * @return the JAXB ContextPath, e.g.
         *         "com.yourmediashelf.fedora.generated.access".
         */
        public String path() {
            return contextPath;
        }

        ContextPath(String contextPath) {
            this.contextPath = contextPath;
        }
    }

    @Override
    public void close() {
        cr.close();
    }

}
