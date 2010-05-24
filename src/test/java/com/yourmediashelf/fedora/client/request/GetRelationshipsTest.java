
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.request.FedoraRequest.addRelationship;
import static com.yourmediashelf.fedora.client.request.FedoraRequest.getRelationships;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileUtils;
import com.sun.jersey.api.client.ClientResponse;

public class GetRelationshipsTest extends FedoraMethodBaseTest {

    @Test
    public void testGetAllRelationships() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);

        response = fedora().execute(getRelationships(testPid).subject(subject).build());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetRelationships() throws Exception {
        ClientResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/p";
        String object = "Able was I ere I saw Elba";

        // first add a relationship
        response = fedora().execute(addRelationship(testPid).subject(subject).predicate(predicate).object(object).isLiteral(true).build());
        assertEquals(200, response.getStatus());

        // now get it
        response = fedora().execute(getRelationships(testPid).subject(subject).predicate(predicate).build());
        assertEquals(200, response.getStatus());

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
}
