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

import static com.yourmediashelf.fedora.client.FedoraClient.addRelationship;
import static com.yourmediashelf.fedora.client.FedoraClient.getRelationships;
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastreamDissemination;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class AddRelationshipIT extends BaseFedoraRequestIT {

    @Test
    public void testAddRelationship() throws Exception {
        FedoraResponse response = null;
        String predicate = "urn:foo/testAddRelationship";
        String object = "urn:foo/한";

        response = addRelationship(testPid).predicate(predicate).object(object).execute();
        assertEquals(200, response.getStatus());
        
        System.out.println(
                getRelationships(testPid).predicate(predicate).format(null).execute().getEntity(String.class));
        
    }

    @Test
    public void testAddLiteral() throws Exception {
        FedoraResponse response = null;
        String predicate = "urn:foo/testAddLiteral";
        String object = "你好";

        response = addRelationship(testPid).predicate(predicate).object(object, true).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddTypedLiteral() throws Exception {
        FedoraResponse response = null;
        String predicate = "urn:foo/testAddTypedLiteral";
        String object = "1970-01-01T00:00:00Z";
        String datatype = "http://www.w3.org/2001/XMLSchema#dateTime";

        response = addRelationship(testPid).predicate(predicate).object(object, datatype).execute();
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testAddRelsIntStatement() throws Exception {       
        String subject = String.format("info:fedora/%s/RELS-INT", testPid);
        String predicate = "urn:foo/testAddRelsIntStatement";
        String object = "foo";
        
        addRelationship(testPid).subject(subject).predicate(predicate).object(object, true).execute();
        
        FedoraResponse response = getDatastreamDissemination(testPid, "RELS-INT").execute();
        System.out.println("***");
        System.out.println(response.getEntity(String.class));
        System.out.println("===");
        
    }
}
