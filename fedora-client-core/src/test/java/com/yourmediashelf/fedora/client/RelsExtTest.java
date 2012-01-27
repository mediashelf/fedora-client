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
