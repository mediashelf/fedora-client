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

package com.yourmediashelf.fedora.client.request;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.RiSearchResponse;

/**
 * Builder for the RiSearch method.
 *
 * @author Edwin Shin
 */
public class RiSearch extends FedoraRequest<RiSearch> {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(this.getClass());

    private String type;

    /**
     * 
     * 
     * @param query the query text
     * 
     */
    public RiSearch(String query) {
        validateQuery(query);
        addQueryParam("query", query);
    }

    /**
     * A tuple query is one that returns a list of named values. A triple query 
     * is one that returns a list of RDF statements (aka triples).
     * 
     * Defaults to tuples.
     * 
     * @param type tuples or triples
     * @return this builder
     */
    public RiSearch type(String type) {
        validateType(type);
        this.type = type;
        addQueryParam("type", type);
        return this;
    }

    /**
     * The query language to use.
     * 
     * If not specified, defaults to sparql.
     * 
     * @param lang one of itql, sparql or spo (spo only if type = triples)
     * @return this builder
     */
    public RiSearch lang(String lang) {
        //FIXME set type=triples automatically if spo
        validateLang(lang);
        addQueryParam("lang", lang);
        return this;
    }

    /**
     * The desired response format.
     * 
     * Defaults to sparql if type = tuples, n-triples if type = triples.
     * 
     * @param format if type is tuples, then one of csv, simple, sparql, or tsv.
     * If type is triples, then one of n-triples, notation 3, rdf/xml, or turtle.
     * @return this builder
     */
    public RiSearch format(String format) {
        validateFormat(format);
        addQueryParam("format", format);
        return this;
    }

    /**
     * The flush parameter tells the Resource Index to ensure that any 
     * recently-added/modified/deleted triples are flushed to the triplestore 
     * before executing the query. 
     * 
     * This option can be desirable in certain scenarios, but for performance 
     * reasons, should be used sparingly when a process is making many API-M 
     * calls to Fedora in a short period of time: We have found that Mulgara 
     * generally achieves a much better overall update rate with large batches 
     * of triples.
     * 
     * Defaults to false.
     * 
     * @param flush whether the Fedora server should flush its triple buffer 
     * first. If false, the result could be out of date with what was actually 
     * stored in the repository at the time of the request. If true, it may take 
     * considerably longer to get a response. 
     * @return this builder
     */
    public RiSearch flush(boolean flush) {
        addQueryParam("flush", Boolean.toString(flush));
        return this;
    }

    /**
     * The maximum number of results to return. It is useful to set this low 
     * when testing queries.
     * 
     * Default is no limit.
     * 
     * @param limit maximum number of results to return.
     * @return this builder
     */
    public RiSearch limit(int limit) {
        addQueryParam("limit", Integer.toString(limit));
        return this;
    }

    /**
     * Whether to force duplicate results to be dropped. Note: iTQL never 
     * returns duplicates.
     * 
     * Default is off (false).
     * 
     * @param distinct
     * @return this builder
     */
    public RiSearch distinct(boolean distinct) {
        addQueryParam("distinct", distinct ? "on" : "off");
        return this;
    }

    /**
     * Whether to stream the results right away (faster), or to save them to a 
     * temporary file before sending them to the client. 
     * 
     * The default behavior (to save the results before streaming) will give a 
     * more informative error message if a query fails.
     * 
     * Default is off (false).
     * 
     * @param stream whether to stream the results immediately or save them to a
     * temporary file before sending them to the client.
     * @return this builder
     */
    public RiSearch stream(boolean stream) {
        addQueryParam("stream", stream ? "on" : "off");
        return this;
    }

    /**
     * Templates are used to convert tuple query results to triples. A template 
     * consists of one or more triple binding patterns that reference the 
     * binding variables in an iTQL query.
     * 
     * @param template
     * @return this builder
     */
    public RiSearch template(String template) {
        addQueryParam("template", template);
        return this;
    }

    @Override
    public RiSearchResponse execute() throws FedoraClientException {
        return (RiSearchResponse) super.execute();
    }

    @Override
    public RiSearchResponse execute(FedoraClient fedora)
            throws FedoraClientException {
        // set defaults
        if (getFirstQueryParam("type") == null) {
            type = "tuples";
            addQueryParam("type", "tuples");
        }
        if (getFirstQueryParam("lang") == null) {
            addQueryParam("lang", "sparql");
        }
        if (getFirstQueryParam("format") == null) {
            if (type.equalsIgnoreCase("tuples")) {
                addQueryParam("format", "sparql");
            } else if (type.equalsIgnoreCase("triples")) {
                addQueryParam("format", "n-triples");
            }
        }

        ClientResponse response = null;
        String path = String.format("risearch");
        WebResource wr = resource(fedora).path(path);

        // Check for a 302 (expected if baseUrl is http but Fedora is configured
        // to require SSL
        response = wr.head();
        if (response.getStatus() == 302) {
            URI newLocation = response.getLocation();
            logger.warn("302 status for upload request: " + newLocation);
            wr = resource(newLocation.toString());
        }

        return new RiSearchResponse(wr.type(
                MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(
                ClientResponse.class, getQueryParams()));
    }

    private void validateType(String type) {
        if (type == null ||
                type.isEmpty() ||
                !(type.equalsIgnoreCase("tuples") || type
                        .equalsIgnoreCase("triples"))) {
            throw new IllegalArgumentException(
                    "type must be one of tuples or triples");
        }
    }

    private void validateLang(String lang) {
        if (lang == null || lang.isEmpty()) {
            throw new IllegalArgumentException("lang cannot be null or empty");
        } else if (type != null && type.equalsIgnoreCase("tuples")) {
            if (!(lang.equalsIgnoreCase("sparql") || lang
                    .equalsIgnoreCase("itql"))) {
                throw new IllegalArgumentException(
                        "lang must be one of sparql "
                                + "or itql if type is tuples");
            }
        } else if (type != null && type.equalsIgnoreCase("triples")) {
            if (!(lang.equalsIgnoreCase("sparql") ||
                    lang.equalsIgnoreCase("itql") || lang
                        .equalsIgnoreCase("spo"))) {
                throw new IllegalArgumentException(
                        "lang must be one of sparql, "
                                + "itql, or spo if type is triples");
            }
        }
    }

    private void validateFormat(String format) {
        if (format == null || format.isEmpty()) {
            throw new IllegalArgumentException("format cannot be null or empty");
        } else if (type != null && type.equalsIgnoreCase("tuples")) {
            if (!(format.equalsIgnoreCase("csv") ||
                    format.equalsIgnoreCase("simple") ||
                    format.equalsIgnoreCase("sparql") ||
                    format.equalsIgnoreCase("tsv") || format
                        .equalsIgnoreCase("count"))) {
                throw new IllegalArgumentException(
                        "format must be one of "
                                + "csv, simple, sparql, tsv, or count if type is tuples");
            }
        } else if (type != null && type.equalsIgnoreCase("triples")) {
            if (!(format.equalsIgnoreCase("n-triples") ||
                    format.equalsIgnoreCase("Notation 3") ||
                    format.equalsIgnoreCase("RDF/XML") ||
                    format.equalsIgnoreCase("Turtle") || format
                        .equalsIgnoreCase("count"))) {
                throw new IllegalArgumentException(
                        "format must be one of "
                                + "n-triples, notation 3, rdf/xml, turtle, or count if "
                                + "type is triples");
            }
        }
    }

    private void validateQuery(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("query cannot be null or empty");
        }
    }
}