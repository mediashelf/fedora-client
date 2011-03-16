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

import static com.yourmediashelf.fedora.client.FedoraClient.riSearch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.RiSearchResponse;

public class RiSearchTest extends BaseFedoraRequestTest {

    @Test
    public void testItqlQuery() throws Exception {
    	String pidUri = "info:fedora/" + getTestPid();
    	String query = String.format("select $subject from <#ri> " +
    			"where $subject <mulgara:is> <%s>", pidUri);

        RiSearchResponse response = null;

        response = riSearch(query).lang("itql").format("csv").execute(fedora());
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);
        assertTrue(result.contains("\"subject\""));
        assertTrue(result.contains(getTestPid()));
    }
    
    @Test
    public void testSparqlQuery() throws Exception {
    	String pidUri = "info:fedora/" + getTestPid();
    	String query = String.format("select ?s from <#ri> " +
    			"where {?s <fedora-model:hasModel> ?o . " +
    			"FILTER(STR(?s) = \"%s\")}", pidUri);

        RiSearchResponse response = null;

        response = riSearch(query).flush(true).execute(fedora());
        assertEquals(200, response.getStatus());
        String result = response.getEntity(String.class);
        assertTrue(result.contains(getTestPid()));
    }
}
