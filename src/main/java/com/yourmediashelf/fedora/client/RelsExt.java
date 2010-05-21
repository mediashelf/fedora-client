package com.yourmediashelf.fedora.client;

import java.util.HashSet;
import java.util.Set;

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
		private final Set<String> cmodels = new HashSet<String>();

		public Builder(String pid) {
			if (pid.startsWith("info:fedora/")) {
				pid = pid.substring(12);
			}
			this.pid = pid;
			cmodels.add("info:fedora/fedora-system:ContentModel-3.0");
		}

		public RelsExt build() {
			RelsExt result = new RelsExt(pid);
			result.cmodels = cmodels;
			return result;
		}

		public Builder cmodel(String cmodel) {
			if (cmodel == null) {
				return this;
			}
			if (!cmodel.startsWith("info:fedora/")) {
				cmodel = "info:fedora/" + cmodel;
			}
			cmodels.add(cmodel);
			return this;
		}
	}

	private final String pid;
	private Set<String> cmodels = new HashSet<String>();

	private RelsExt(String pid) {
		this.pid = pid;
	}

	@Override
    public String toString() {
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
	}

	public String getPid() {
		return pid;
	}

	public Set<String> getCmodels() {
		return cmodels;
	}
}
