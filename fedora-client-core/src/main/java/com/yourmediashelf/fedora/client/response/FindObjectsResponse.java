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

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.FindObjects;
import com.yourmediashelf.fedora.util.NamespaceContextImpl;

/**
 * A {@link FedoraResponse} for the {@link FindObjects} request.
 * 
 * @author Edwin Shin
 * @since 0.1.6
 */
public class FindObjectsResponse extends FedoraResponseImpl {

    private List<String> pids;

    private boolean hasNext;

    private String token;

    private String cursor;

    private String expirationDate;

    private XPath xpath;

    private Node root;

    public FindObjectsResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);

        pids = new ArrayList<String>();

        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NamespaceContextImpl("f",
                "http://www.fedora.info/definitions/1/0/types/"));
        try {
            parseClientResponse(cr);
        } catch (XPathExpressionException e) {
            throw new FedoraClientException(e.getMessage(), e);
        }
    }

    public List<String> getPids() {
        return pids;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public String getToken() {
        return token;
    }

    public String getCursor() {
        return cursor;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public List<String> getObjectField(String pid, String fieldName)
            throws FedoraClientException {
        List<String> objectField = new ArrayList<String>();
        String expression =
                String.format(
                        "/f:result/f:resultList/f:objectFields[f:pid=\"%s\"]/f:%s",
                        pid, fieldName);
        try {
            NodeList nodes =
                    (NodeList) xpath.evaluate(expression, root,
                            XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                objectField.add(nodes.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            throw new FedoraClientException(e.getMessage(), e);
        }

        return objectField;
    }

    private void parseClientResponse(ClientResponse cr)
            throws XPathExpressionException {
        root =
                (Node) xpath.evaluate("/", new InputSource(cr
                        .getEntityInputStream()), XPathConstants.NODE);

        String expression = "/f:result/f:resultList/f:objectFields/f:pid";
        NodeList nodes =
                (NodeList) xpath.evaluate(expression, root,
                        XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            pids.add(nodes.item(i).getTextContent());
        }

        expression = "/f:result/f:listSession/f:token";
        token = xpath.evaluate(expression, root);

        if (token != null && !token.isEmpty()) {
            hasNext = true;
            expression = "/f:result/f:listSession/f:cursor";
            cursor = xpath.evaluate(expression, root);

            expression = "/f:result/f:listSession/f:expirationDate";
            expirationDate = xpath.evaluate(expression, root);
        } else {
            token = null;
        }
    }
}
