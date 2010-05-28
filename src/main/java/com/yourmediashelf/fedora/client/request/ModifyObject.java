package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.util.DateUtility.getXSDDateTime;

import java.util.Date;

import org.joda.time.DateTime;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the ModifyObject method.
 *
 * @author Edwin Shin
 */
public class ModifyObject extends FedoraRequest<ModifyObject> {
    private final String pid;

    public ModifyObject(String pid) {
        this.pid = pid;
    }

    public ModifyObject label(String label) {
        addQueryParam("label", label);
        return this;
    }

    public ModifyObject lastModifiedDate(DateTime lastModifiedDate) {
        addQueryParam("lastModifiedDate", getXSDDateTime(lastModifiedDate));
        return this;
    }

    public ModifyObject lastModifiedDate(Date lastModifiedDate) {
        addQueryParam("lastModifiedDate", getXSDDateTime(lastModifiedDate));
        return this;
    }

    public ModifyObject lastModifiedDate(String lastModifiedDate) {
        addQueryParam("lastModifiedDate", lastModifiedDate);
        return this;
    }

    public ModifyObject logMessage(String logMessage) {
        addQueryParam("logMessage", logMessage);
        return this;
    }

    public ModifyObject ownerId(String ownerId) {
        addQueryParam("ownerId", ownerId);
        return this;
    }

    public ModifyObject state(String state) {
        addQueryParam("state", state);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s", pid);
        ClientResponse cr = wr.path(path).queryParams(getQueryParams()).put(ClientResponse.class);
        return new FedoraResponseImpl(cr);
    }
}