package com.yourmediashelf.fedora.client;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileUtils;


public class RelsExtTest {

    @Test
    public void testAddCModel() throws Exception {
        String testPid = "info:fedora/test:pid";
        String hasModel = "info:fedora/fedora-system:def/model#hasModel";
        String cmodel = "info:fedora/my:cmodel";
        String predicate = "some:thing";
        String literal = "hey";
        String relsExt = new RelsExt.Builder(testPid)
                            .addCModel(cmodel)
                            .addRelationship(predicate, literal, true, null)
                            .build().toString();

        //System.out.println(relsExt);

        Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(relsExt.getBytes("UTF-8")), null, FileUtils.langXML);

        ResIterator ri = model.listSubjectsWithProperty(model.createProperty(hasModel));
        Resource r = ri.next();
        assertEquals(testPid, r.toString());
        assertFalse(ri.hasNext());

        ri = model.listSubjectsWithProperty(model.createProperty(predicate));
        r = ri.next();
        assertEquals(testPid, r.toString());
        assertFalse(ri.hasNext());
    }

}
