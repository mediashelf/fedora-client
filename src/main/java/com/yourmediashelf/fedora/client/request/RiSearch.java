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
public class RiSearch
        extends FedoraRequest<RiSearch> {
	
	private final org.slf4j.Logger logger =
        org.slf4j.LoggerFactory.getLogger(this.getClass());

	private String type;

    /**
     * @param type one of tuples or triples
     * @param lang if type is tuples, one of itql or sparql. If type is triples,
     * one of itql, sparql or spo.
     * 
     */
    public RiSearch(String query) {
		validateQuery(query);
        addQueryParam("query", query);
    }
    
    /**
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
     * Defaults to sparql.
     * 
     * @param lang
     * @return this builder
     */
    public RiSearch lang(String lang) {
    	validateLang(lang);
        addQueryParam("lang", lang);
        return this;
    }
    
    /**
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
     * Defaults to false.
     * 
     * @param flush
     * @return this builder
     */
    public RiSearch flush(boolean flush) {
        addQueryParam("flush", Boolean.toString(flush));
        return this;
    }
    
    /**
     * Default is no limit.
     * 
     * @param limit
     * @return this builder
     */
    public RiSearch limit(int limit) {
        addQueryParam("limit", Integer.toString(limit));
        return this;
    }
    
    /**
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
     * Default is off (false).
     * 
     * @param stream
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
    public RiSearchResponse execute(FedoraClient fedora) throws FedoraClientException {
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
        WebResource wr = fedora.resource().path(path);
        
        // Check for a 302 (expected if baseUrl is http but Fedora is configured
        // to require SSL
        response = wr.head();
        if (response.getStatus() == 302) {
            URI newLocation = response.getLocation();
            logger.warn("302 status for upload request: " + newLocation);
            wr = fedora.resource(newLocation.toString());
        }
        
        return new RiSearchResponse(wr.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).
        		post(ClientResponse.class, getQueryParams()));
    }
    
    private void validateType(String type) {
    	if (type == null || type.isEmpty() || 
    			!(type.equalsIgnoreCase("tuples") || type.equalsIgnoreCase("triples"))) {
    		throw new IllegalArgumentException("type must be one of tuples or triples");
        }
    }
    
    private void validateLang(String lang) {
    	if (lang == null || lang.isEmpty()) {
    		throw new IllegalArgumentException("lang cannot be null or empty");
    	} else if (type != null && type.equalsIgnoreCase("tuples")) {
    		if (!(lang.equalsIgnoreCase("sparql") || lang.equalsIgnoreCase("itql"))) {
    			throw new IllegalArgumentException("lang must be one of sparql " +
    					"or itql if type is tuples");
    		}
		} else if (type != null && type.equalsIgnoreCase("triples")) {
			if (!(lang.equalsIgnoreCase("sparql") || 
					lang.equalsIgnoreCase("itql") || 
					lang.equalsIgnoreCase("spo"))) {
    			throw new IllegalArgumentException("lang must be one of sparql, " +
    					"itql, or spo if type is triples");
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
    				format.equalsIgnoreCase("tsv"))) {
    			throw new IllegalArgumentException("format must be one of " +
    					"csv, simple, sparql, or tsv if type is tuples");
    		}
		} else if (type != null && type.equalsIgnoreCase("triples")) {
			if (!(format.equalsIgnoreCase("n-triples") || 
					format.equalsIgnoreCase("Notation 3") || 
					format.equalsIgnoreCase("RDF/XML") || 
					format.equalsIgnoreCase("Turtle"))) {
    			throw new IllegalArgumentException("format must be one of " +
    					"n-triples, notation 3, rdf/xml, or turtle if type is triples");
    		}
		}
    }
    
    private void validateQuery(String query) {
    	if (query == null || query.isEmpty()) {
    		throw new IllegalArgumentException("query cannot be null or empty");
    	}
    }
}