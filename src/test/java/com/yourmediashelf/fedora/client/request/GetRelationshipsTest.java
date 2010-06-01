
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
import com.yourmediashelf.fedora.client.response.FedoraResponse;

public class GetRelationshipsTest
        extends BaseFedoraRequestTest {

    @Test
    public void testGetAllRelationships() throws Exception {
        FedoraResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);

        response = fedora().execute(getRelationships(testPid).subject(subject));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetRelationships() throws Exception {
        FedoraResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/testGetRelationships";
        String object = "Able was I ere I saw Elba";

        // first add a relationship
        response =
                fedora().execute(addRelationship(testPid).subject(subject)
                        .predicate(predicate).object(object).isLiteral(true));
        assertEquals(200, response.getStatus());

        // now get it
        response =
                fedora().execute(getRelationships(testPid).subject(subject)
                        .predicate(predicate));
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

    @Test
    public void testGetTypedLiteral() throws Exception {
        FedoraResponse response = null;
        String subject = String.format("info:fedora/%s", testPid);
        String predicate = "urn:foo/testGetTypedLiteral";
        String object = "1970-01-01T00:00:00Z";
        String datatype = "http://www.w3.org/2001/XMLSchema#dateTime";

        response =
                addRelationship(testPid).subject(subject).predicate(predicate)
                        .object(object).isLiteral(true).datatype(datatype)
                        .execute(fedora());
        assertEquals(200, response.getStatus());

        // now get it
        response =
                getRelationships(testPid).subject(subject)
                        .predicate(predicate).execute(fedora());
        assertEquals(200, response.getStatus());

        Model model = ModelFactory.createDefaultModel();
        model.read(response.getEntityInputStream(), null, FileUtils.langXML);
        StmtIterator it = model.listStatements();
        Statement s;
        while (it.hasNext()) {
            s = it.next();
            assertEquals(subject, s.getSubject().toString());
            assertEquals(predicate, s.getPredicate().toString());
            assertEquals(object, s.getLiteral().getLexicalForm());
            assertEquals(datatype, s.getLiteral().getDatatypeURI());
        }
    }
}
