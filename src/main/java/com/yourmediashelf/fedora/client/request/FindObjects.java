package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the FindObjects method.
 *
 * @author Edwin Shin
 */
public class FindObjects extends FedoraRequest<FindObjects> {

    public FindObjects() {
    }

    public FindObjects terms(String terms) {
        addQueryParam("terms", terms);
        return this;
    }

    public FindObjects query(String query) {
        addQueryParam("query", query);
        return this;
    }

    public FindObjects maxResults(int maxResults) {
        addQueryParam("maxResults", Integer.toString(maxResults));
        return this;
    }

    public FindObjects resultFormat(String resultFormat) {
        addQueryParam("resultFormat", resultFormat);
        return this;
    }

    public FindObjects sessionToken(String sessionToken) {
        addQueryParam("sessionToken", sessionToken);
        return this;
    }

    public FindObjects pid(boolean pid) {
        addQueryParam("pid", Boolean.toString(pid));
        return this;
    }

    public FindObjects label(boolean label) {
        addQueryParam("label", Boolean.toString(label));
        return this;
    }

    public FindObjects state(boolean state) {
        addQueryParam("state", Boolean.toString(state));
        return this;
    }

    public FindObjects ownerId(boolean ownerId) {
        addQueryParam("ownerId", Boolean.toString(ownerId));
        return this;
    }

    public FindObjects cDate(boolean cDate) {
        addQueryParam("cDate", Boolean.toString(cDate));
        return this;
    }

    public FindObjects mDate(boolean mDate) {
        addQueryParam("mDate", Boolean.toString(mDate));
        return this;
    }

    public FindObjects dcmDate(boolean dcmDate) {
        addQueryParam("dcmDate", Boolean.toString(dcmDate));
        return this;
    }

    public FindObjects title(boolean title) {
        addQueryParam("title", Boolean.toString(title));
        return this;
    }

    public FindObjects creator(boolean creator) {
        addQueryParam("creator", Boolean.toString(creator));
        return this;
    }

    public FindObjects subject(boolean subject) {
        addQueryParam("subject", Boolean.toString(subject));
        return this;
    }

    public FindObjects description(boolean description) {
        addQueryParam("description", Boolean.toString(description));
        return this;
    }

    public FindObjects publisher(boolean publisher) {
        addQueryParam("publisher", Boolean.toString(publisher));
        return this;
    }

    public FindObjects contributor(boolean contributor) {
        addQueryParam("contributor", Boolean.toString(contributor));
        return this;
    }

    public FindObjects date(boolean date) {
        addQueryParam("date", Boolean.toString(date));
        return this;
    }

    public FindObjects type(boolean type) {
        addQueryParam("type", Boolean.toString(type));
        return this;
    }

    public FindObjects format(boolean format) {
        addQueryParam("format", Boolean.toString(format));
        return this;
    }

    public FindObjects identifier(boolean identifier) {
        addQueryParam("identifier", Boolean.toString(identifier));
        return this;
    }

    public FindObjects source(boolean source) {
        addQueryParam("source", Boolean.toString(source));
        return this;
    }

    public FindObjects language(boolean language) {
        addQueryParam("language", Boolean.toString(language));
        return this;
    }
    public FindObjects relation(boolean relation) {
        addQueryParam("relation", Boolean.toString(relation));
        return this;
    }
    public FindObjects coverage(boolean coverage) {
        addQueryParam("coverage", Boolean.toString(coverage));
        return this;
    }
    public FindObjects rights(boolean rights) {
        addQueryParam("rights", Boolean.toString(rights));
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects");

        return new FedoraResponseImpl(wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class));
    }

}