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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FindObjectsResponse;

/**
 * Builder for the FindObjects method.
 * 
 * @author Edwin Shin
 */
public class FindObjects extends FedoraRequest<FindObjects> {

	public FindObjects() {
	}

	/**
	 * A phrase represented as a sequence of characters (including the ? and *
	 * wildcards) for the search.
	 * 
	 * <p>
	 * If this sequence is found in any of the fields for an object, the object
	 * is considered a match. Do NOT use this parameter in combination with the
	 * "query" parameter.
	 * 
	 * @param terms
	 *            A phrase represented as a sequence of characters
	 * @return this builder
	 */
	public FindObjects terms(String terms) {
		addQueryParam("terms", terms);
		return this;
	}

	/**
	 * A sequence of space-separated conditions.
	 * 
	 * <p>
	 * A condition consists of a metadata element name followed directly by an
	 * operator, followed directly by a value.
	 * 
	 * <p>
	 * Valid element names are (pid, label, state, ownerId, cDate, mDate,
	 * dcmDate, title, creator, subject, description, publisher, contributor,
	 * date, type, format, identifier, source, language, relation, coverage,
	 * rights).
	 * 
	 * <p>
	 * Valid operators are: contains (), equals (=), greater than (>), less than
	 * (<), greater than or equals (>=), less than or equals (<=). The contains
	 * () operator may be used in combination with the ? and * wildcards to
	 * query for simple string patterns. Space-separators should be encoded in
	 * the URL as %20.
	 * 
	 * <p>
	 * Operators must be encoded when used in the URL syntax as follows: the (=)
	 * operator must be encoded as %3D, the (>) operator as %3E, the (<)
	 * operator as %3C, the (>=) operator as %3E%3D, the (<=) operator as
	 * %3C%3D, and the (~) operator as %7E. Values may be any string. If the
	 * string contains a space, the value should begin and end with a single
	 * quote character ('). If all conditions are met for an object, the object
	 * is considered a match.
	 * 
	 * <p>
	 * Do NOT use this parameter in combination with the "terms" parameter.
	 * 
	 * @param query
	 *            A sequence of space-separated conditions.
	 * @return this builder
	 */
	public FindObjects query(String query) {
		addQueryParam("query", query);
		return this;
	}

	/**
	 * The maximum number of results that the server should provide at once. If
	 * this is unspecified, the server will default to a small value.
	 * 
	 * @param maxResults
	 *            The maximum number of results that the server should provide
	 *            at once
	 * @return this builder
	 */
	public FindObjects maxResults(int maxResults) {
		addQueryParam("maxResults", Integer.toString(maxResults));
		return this;
	}

	/**
	 * The preferred output format.
	 * 
	 * @param resultFormat
	 *            one of "html" or "xml". Defaults to "xml".
	 * @return this builder
	 */
	public FindObjects resultFormat(String resultFormat) {
		addQueryParam("resultFormat", resultFormat);
		return this;
	}

	/**
	 * The identifier of the session to which the search results are being
	 * returned.
	 * 
	 * @param sessionToken
	 *            The session identifier
	 * @return this builder
	 */
	public FindObjects sessionToken(String sessionToken) {
		addQueryParam("sessionToken", sessionToken);
		return this;
	}

	/**
	 * Return the Fedora persistent identifier (PID) element of matching objects
	 * in the response.
	 * 
	 * @return this builder
	 */
	public FindObjects pid() {
		addQueryParam("pid", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects label() {
		addQueryParam("label", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects state() {
		addQueryParam("state", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects ownerId() {
		addQueryParam("ownerId", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects cDate() {
		addQueryParam("cDate", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects mDate() {
		addQueryParam("mDate", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects dcmDate() {
		addQueryParam("dcmDate", Boolean.TRUE.toString());
		return this;
	}

	/**
	 * Return the Dublin Core title element(s) of matching objects in the
	 * response.
	 * 
	 * @return this builder
	 */
	public FindObjects title() {
		addQueryParam("title", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects creator() {
		addQueryParam("creator", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects subject() {
		addQueryParam("subject", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects description() {
		addQueryParam("description", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects publisher() {
		addQueryParam("publisher", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects contributor() {
		addQueryParam("contributor", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects date() {
		addQueryParam("date", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects type() {
		addQueryParam("type", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects format() {
		addQueryParam("format", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects identifier() {
		addQueryParam("identifier", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects source() {
		addQueryParam("source", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects language() {
		addQueryParam("language", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects relation() {
		addQueryParam("relation", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects coverage() {
		addQueryParam("coverage", Boolean.TRUE.toString());
		return this;
	}

	public FindObjects rights() {
		addQueryParam("rights", Boolean.TRUE.toString());
		return this;
	}

	@Override
	public FindObjectsResponse execute(FedoraClient fedora)
			throws FedoraClientException {
		// default to xml for the format, so we can parse the results
		if (getFirstQueryParam("resultFormat") == null) {
			addQueryParam("resultFormat", "xml");
		}

		WebResource wr = fedora.resource();
		String path = String.format("objects");

		return new FindObjectsResponse(wr.path(path)
				.queryParams(getQueryParams()).get(ClientResponse.class));
	}

}