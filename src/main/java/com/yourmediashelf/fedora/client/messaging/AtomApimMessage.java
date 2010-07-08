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

package com.yourmediashelf.fedora.client.messaging;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Category;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Representation of an API-M method call as an Atom entry.
 * <ul>
 * <li>atom:title corresponds to the method name, e.g. ingest</li>
 * <li>Each atom:category corresponds to a method's argument:
 * <ul>
 * <li>The scheme indicates the argument name</li>
 * <li>The term indicates the argument value. However, null values are
 * indicated as "null", and non-null <code>xsd:base64Binary</code> values are
 * indicated as "[OMITTED]".</li>
 * <li>The label indicates the argument datatype</li>
 * </ul>
 * </li>
 * <li>atom:content corresponds to the textual representation of the method's
 * return value, noting the following:
 * <ul>
 * <li>Null values are represented as "null".</li>
 * <li><code>fedora-types:ArrayOfString</code> values are represented as a
 * comma-separated list, e.g. "value1, value2, value3".</li>
 * <li>Non-null <code>xsd:base64Binary</code> values are not returned, and
 * only indicated as "[OMITTED]".</li>
 * <li>Non-null <code>fedora-types:Datastream</code> values are not returned,
 * and only indicated as "[OMITTED]".</li>
 * <li><code>fedora-types:RelationshipTuple</code> values are represented in
 * Notation3 (N3).</li>
 * </ul>
 * </li>
 * <li>atom:uri element of atom:author corresponds to the baseURL of the Fedora
 * repository, e.g. http://localhost:8080/fedora.</li>
 * <li>atom:summary corresponds to the PID of the method, if applicable.</li>
 * </ul>
 *
 * @see <a href="http://atomenabled.org/developers/syndication/atom-format-spec.php">The Atom Syndication Format</a>
 *
 * @author Edwin Shin
 */
public class AtomApimMessage {

    private static final Logger logger =
            LoggerFactory.getLogger(AtomApimMessage.class);

    private final Abdera abdera = Abdera.getInstance();

    private final static String versionPredicate = "info:fedora/fedora-system:def/view#version";

    private final static String formatPredicate = "http://www.fedora.info/definitions/1/0/types/formatURI";

    private final String fedoraBaseUrl;

    private final String serverVersion;

    private final String format;

    private final String methodName;

    private final String pid;

    private final Date date;

    private final String author;

    private final String returnVal;

    private final Entry entry;

    public AtomApimMessage(String messageText) {
        Parser parser = abdera.getParser();
        Document<Entry> entryDoc = parser.parse(new StringReader(messageText));
        entry = entryDoc.getRoot();
        methodName = entry.getTitle();
        date = entry.getUpdated();
        author = entry.getAuthor().getName();
        fedoraBaseUrl = entry.getAuthor().getUri().toString();

        pid = entry.getSummary();
        returnVal = entry.getContent();

        serverVersion = getCategoryTerm(versionPredicate);
        format = getCategoryTerm(formatPredicate);
    }

    /**
     * Serialization of the API-M message as an Atom entry. {@inheritDoc}
     */
    @Override
    public String toString() {
        Writer sWriter = new StringWriter();

        try {
            entry.writeTo("prettyxml", sWriter);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return sWriter.toString();
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getBaseUrl() {
        return fedoraBaseUrl;
    }

    /**
     *
     * {@inheritDoc}
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getPID() {
        return pid;
    }

    public String getAuthor() {
        return author;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getFormat() {
        return format;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getServerVersion() {
        return serverVersion;
    }

    public String getReturnVal() {
        return returnVal;
    }

    /**
     * Get the first atom:category term that matches the provided scheme.
     *
     * @param scheme
     * @return the term or null if no match.
     */
    private String getCategoryTerm(String scheme) {
        List<Category> categories = entry.getCategories(scheme);
        if (categories.isEmpty()) {
            return null;
        } else {
            return categories.get(0).getTerm();
        }
    }
}