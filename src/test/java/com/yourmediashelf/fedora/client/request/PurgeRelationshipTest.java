
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

public class PurgeRelationshipTest extends FedoraMethodBaseTest {

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
        assertEquals(204, response.getStatus());

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
}
