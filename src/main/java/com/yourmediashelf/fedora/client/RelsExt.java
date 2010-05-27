package com.yourmediashelf.fedora.client;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A builder for a Fedora 3.x RELS-EXT datastream.
 * Once support for the relationships is added to Fedora's REST API, this class
 * can go away.
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
			model.setNsPrefix("fedora-model", "info:fedora/fedora-system:def/model#");
			this.subject = model.createResource(String.format("info:fedora/%s", pid));
			addCModel("info:fedora/fedora-system:ContentModel-3.0");
		}

		public RelsExt build() {
			RelsExt result = new RelsExt(pid, model);
			return result;
		}

		public Builder addRelationship(String predicate, String object, boolean isLiteral, String datatype) {
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
			Property p = model.createProperty("info:fedora/fedora-system:def/model#hasModel");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

	    /*
		String cmodelStatements = "";
		for (String cmodel : cmodels) {
			cmodelStatements += String
			.format("    <fedora-model:hasModel rdf:resource=\"%s\"></fedora-model:hasModel>\n", cmodel);
		}
		return String
		.format("<rdf:RDF xmlns:fedora-model=\"info:fedora/fedora-system:def/model#\" " +
			   " xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" +
			   "  <rdf:Description rdf:about=\"info:fedora/%s\">" +
			   "%s" +
			   "  </rdf:Description>" +
			   "</rdf:RDF>", pid, cmodelStatements);
	   */
	}

	public String getPid() {
		return pid;
	}
}
