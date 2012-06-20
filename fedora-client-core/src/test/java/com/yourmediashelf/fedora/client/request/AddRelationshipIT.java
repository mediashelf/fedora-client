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
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileUtils;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class AddRelationshipIT extends BaseFedoraRequestIT {

    @Test
    public void testAddRelationship() throws Exception {
        FedoraResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/testAddRelationship";
        String object = "urn:foo/한";

        response =
                addRelationship(testPid).predicate(predicate).object(object)
                        .execute();
        assertEquals(200, response.getStatus());

        response = getRelationships(testPid).predicate(predicate).execute();
        Model model = ModelFactory.createDefaultModel();
        model.read(response.getEntityInputStream(), null, FileUtils.langXML);
        StmtIterator it = model.listStatements();
        Statement s;
        while (it.hasNext()) {
            s = it.next();
            assertEquals(subject, s.getSubject().toString());
            assertEquals(predicate, s.getPredicate().toString());
            assertEquals(object, s.getObject().toString());
        }
    }

    @Test
    public void testAddLiteral() throws Exception {
        FedoraResponse response = null;
        String predicate = "urn:foo/testAddLiteral";
        String object = "你好";

        response =
                addRelationship(testPid).predicate(predicate).object(object,
                        true).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddTypedLiteral() throws Exception {
        FedoraResponse response = null;
        String predicate = "urn:foo/testAddTypedLiteral";
        String object = "1970-01-01T00:00:00Z";
        String datatype = "http://www.w3.org/2001/XMLSchema#dateTime";

        response =
                addRelationship(testPid).predicate(predicate).object(object,
                        datatype).execute();
        assertEquals(200, response.getStatus());
    }

    @Override
    public void testNoDefaultClientRequest() throws FedoraClientException {
        AddRelationship request =
                addRelationship(testPid).predicate("urn:foo").object("urn:bar");
        testNoDefaultClientRequest(request, 200);
    }
}
