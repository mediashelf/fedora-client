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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p>A builder for a Fedora 3.x RELS-EXT datastream.
 * This class is only necessary for working with Fedora repositories that do not
 * support the Relationships API (via the REST API), i.e., Fedora 3.3 and
 * earlier.</p>
 *
 * <p>With Fedora 3.4.0 and later, it's recommended to use AddRelationship.</p>
 *
 * @author Edwin Shin
 *
 */
public class RelsExt {

    public static class Builder {

        private final String pid;

        private final Resource subject;

        private final Model model = ModelFactory.createDefaultModel();

        public Builder(String pid) {
            if (pid.startsWith("info:fedora/")) {
                pid = pid.substring(12);
            }
            this.pid = pid;
            model.setNsPrefix("fedora-model",
                    "info:fedora/fedora-system:def/model#");
            subject =
                    model.createResource(String.format("info:fedora/%s", pid));
            addCModel("info:fedora/fedora-system:ContentModel-3.0");
        }

        public RelsExt build() {
            RelsExt result = new RelsExt(pid, model);
            return result;
        }

        public Builder addRelationship(String predicate, String object,
                boolean isLiteral, String datatype) {
            Property p = model.createProperty(predicate);
            RDFNode o;
            if (isLiteral) {
                o = model.createTypedLiteral(object, datatype);
            } else {
                o = model.createResource(object);
            }
            model.add(subject, p, o);
            return this;
        }

        public Builder addCModel(String cmodel) {
            Property p =
                    model.createProperty("info:fedora/fedora-system:def/model#hasModel");
            if (!cmodel.startsWith("info:fedora/")) {
                cmodel = "info:fedora/" + cmodel;
            }
            RDFNode o = model.createResource(cmodel);
            model.add(subject, p, o);
            return this;
        }
    }

    private final String pid;

    private final Model model;

    private RelsExt(String pid, Model model) {
        this.pid = pid;
        this.model = model;
    }

    @Override
    public String toString() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        model.write(bout);
        try {
            return bout.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getPid() {
        return pid;
    }
}
