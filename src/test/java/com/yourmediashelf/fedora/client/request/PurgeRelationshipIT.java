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
import static com.yourmediashelf.fedora.client.FedoraClient.purgeRelationship;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileUtils;
import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class PurgeRelationshipIT extends BaseFedoraRequestIT {

    @Test
    public void testPurgeRelationship() throws Exception {
        FedoraResponse response = null;
        Model model = null;
        Statement s = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "你好";

        // first add a relationship
        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true));
        assertEquals(200, response.getStatus());

        // verify it was added
        response = fedora().execute(getRelationships(testPid).subject(subject).predicate(predicate));
        assertEquals(200, response.getStatus());
        model = ModelFactory.createDefaultModel();
        model.read(response.getEntityInputStream(), null, FileUtils.langXML);
        StmtIterator it = model.listStatements();
        while (it.hasNext()) {
            s = it.next();
            assertEquals(subject, s.getSubject().toString());
            assertEquals(predicate, s.getPredicate().toString());
            assertEquals(object, s.getObject().toString());
        }

        // now delete it
        response = fedora().execute(purgeRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true));
        assertEquals(200, response.getStatus());
        assertEquals("true", response.getEntity(String.class));

        // verify it's gone
        response = fedora().execute(getRelationships(testPid).subject(subject).predicate(predicate));
        assertEquals(200, response.getStatus());
        model = ModelFactory.createDefaultModel();
        model.read(response.getEntityInputStream(), null, FileUtils.langXML);
        it = model.listStatements();
        while (it.hasNext()) {
            s = it.next();
            assertFalse(predicate.equals(s.getPredicate().toString()));
            assertFalse(object.equals(s.getObject().toString()));
        }
    }

    /**
     * purgeRelationship should be idempotent with respect to the returned
     * HTTP status code. The response body however, should indicate "false" for
     * successive deletes of the same resource.
     *
     * @throws Exception
     */
    @Test
    public void testIdempotency() throws Exception {
        FedoraResponse response = null;
        Model model = null;
        Statement s = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "你好";

        // first add a relationship
        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true));
        assertEquals(200, response.getStatus());

        // verify it was added
        response = fedora().execute(getRelationships(testPid).subject(subject).predicate(predicate));
        assertEquals(200, response.getStatus());
        model = ModelFactory.createDefaultModel();
        model.read(response.getEntityInputStream(), null, FileUtils.langXML);
        StmtIterator it = model.listStatements();
        while (it.hasNext()) {
            s = it.next();
            assertEquals(subject, s.getSubject().toString());
            assertEquals(predicate, s.getPredicate().toString());
            assertEquals(object, s.getObject().toString());
        }

        // now delete it
        response = fedora().execute(purgeRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true));
        assertEquals(200, response.getStatus());
        assertEquals("true", response.getEntity(String.class));

        // verify it's gone
        response = fedora().execute(getRelationships(testPid).subject(subject).predicate(predicate));
        assertEquals(200, response.getStatus());
        model = ModelFactory.createDefaultModel();
        model.read(response.getEntityInputStream(), null, FileUtils.langXML);
        it = model.listStatements();
        while (it.hasNext()) {
            s = it.next();
            assertFalse(predicate.equals(s.getPredicate().toString()));
            assertFalse(object.equals(s.getObject().toString()));
        }

        response = purgeRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).execute(fedora());
        assertEquals(200, response.getStatus());
        assertEquals("false", response.getEntity(String.class));
    }
}
