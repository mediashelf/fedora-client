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

package com.yourmediashelf.fedora.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * An implementation of {@link NamespaceContext NamespaceContext} that provides
 * an addNamespace method.
 *
 * @author Edwin Shin
 */
public class NamespaceContextImpl implements NamespaceContext {

    private final Map<String, String> prefix2ns =
            new ConcurrentHashMap<String, String>();

    public NamespaceContextImpl() {
        prefix2ns.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        prefix2ns.put(XMLConstants.XMLNS_ATTRIBUTE,
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    }

    public NamespaceContextImpl(String prefix, String namespaceURI) {
        this();
        addNamespace(prefix, namespaceURI);
    }

    /**
     * Constructor that takes a Map of prefix to namespaces.
     *
     * @param prefix2ns
     *        a mapping of prefixes to namespaces.
     * @throws IllegalArgumentException
     *         if prefix2ns contains {@value javax.xml.XMLConstants#XML_NS_URI} or
     *         {@value javax.xml.XMLConstants#XMLNS_ATTRIBUTE_NS_URI}
     */
    public NamespaceContextImpl(Map<String, String> prefix2ns) {
        this();
        for (String prefix : prefix2ns.keySet()) {
            addNamespace(prefix, prefix2ns.get(prefix));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("null prefix not allowed.");
        }
        if (prefix2ns.containsKey(prefix)) {
            return prefix2ns.get(prefix);
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("null namespaceURI not allowed.");
        }
        for (String prefix : prefix2ns.keySet()) {
            if (prefix2ns.get(prefix).equals(namespaceURI)) {
                return prefix;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        List<String> prefixes = new ArrayList<String>();
        for (String prefix : prefix2ns.keySet()) {
            if (prefix2ns.containsKey(prefix) &&
                    prefix2ns.get(prefix).equals(namespaceURI)) {
                prefixes.add(prefix);
            }
        }
        return Collections.unmodifiableList(prefixes).iterator();
    }

    /**
     * Add a prefix to namespace mapping.
     *
     * @param prefix
     * @param namespaceURI
     * @throws IllegalArgumentException
     *         if namespaceURI is one of {@value javax.xml.XMLConstants#XML_NS_URI} or
     *         {@value javax.xml.XMLConstants#XMLNS_ATTRIBUTE_NS_URI}
     */
    public void addNamespace(String prefix, String namespaceURI) {
        if (prefix == null || namespaceURI == null) {
            throw new IllegalArgumentException("null arguments not allowed.");
        }
        if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            throw new IllegalArgumentException("Adding a new namespace for " +
                    XMLConstants.XML_NS_URI + "not allowed.");
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            throw new IllegalArgumentException("Adding a new namespace for " +
                    XMLConstants.XMLNS_ATTRIBUTE_NS_URI + "not allowed.");
        }
        prefix2ns.put(prefix, namespaceURI);
    }

}