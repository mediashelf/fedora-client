package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;

/**
 * <p>A request object for FedoraClient.</p>
 *
 * <p>A FedoraRequest is built using any of the static methods that return a
 * representation of the various Fedora methods.</p>
 *
 * For example:
 * <pre>
 * {@code
 * FedoraRequest.ingest(null).build();
 * }</pre>
 *
 * creates an ingest request that can be passed to FedoraClient:
 * <pre>
 * {@code
 * FedoraClient fedora = new FedoraClient(credentials);
 * fedora.execute(FedoraRequest.ingest(null).build());
 * }</pre>
 *
 * @author Edwin Shin
 * @version $Id$
 */
public class FedoraRequest {
    private final FedoraMethod method;

    protected FedoraRequest(FedoraMethod method) {
        this.method = method;
    }

    public ClientResponse execute(FedoraClient fedora) throws FedoraClientException {
        ClientResponse response = method.execute(fedora);
        int status = response.getStatus();
        if (status >= 400) {
            String msg = response.getEntity(String.class);
            throw new FedoraClientException(String.format("HTTP %d Error: %s", status, msg));
        }
        return response;
    }

    /**
     *
     * @param pid the persistent identifier
     * @param dsId the datastream identifier
     * @return builder for the AddDatastream method
     * @see AddDatastream
     */
    public static AddDatastream addDatastream(String pid, String dsId) {
        return new AddDatastream(pid, dsId);
    }

    public static AddRelationship addRelationship(String pid) {
        return new AddRelationship(pid);
    }

    public static Export export(String pid) {
        return new Export(pid);
    }

    public static FindObjects findObjects() {
        return new FindObjects();
    }

    public static GetDatastream getDatastream(String pid, String dsId) {
        return new GetDatastream(pid, dsId);
    }

    public static GetDatastreamDissemination getDatastreamDissemination(String pid, String dsId) {
        return new GetDatastreamDissemination(pid, dsId);
    }

    public static GetDatastreamHistory getDatastreamHistory(String pid, String dsId) {
        return new GetDatastreamHistory(pid, dsId);
    }

    public static GetDissemination getDissemination(String pid, String sdefPid, String method) {
        return new GetDissemination(pid, sdefPid, method);
    }

    public static GetNextPID getNextPID() {
        return new GetNextPID();
    }

    public static GetRelationships getRelationships(String pid) {
        return new GetRelationships(pid);
    }

    /**
     *
     * @param pid persistent identifier of the object to be created or null for
     *        a server-assigned pid
     * @return builder for the Ingest method
     * @see Ingest
     */
    public static Ingest ingest(String pid) {
        return new Ingest(pid);
    }

    public static ListDatastreams listDatastreams(String pid) {
        return new ListDatastreams(pid);
    }

    public static ModifyDatastream modifyDatastream(String pid, String dsId) {
        return new ModifyDatastream(pid, dsId);
    }

    public static ModifyObject modifyObject(String pid) {
        return new ModifyObject(pid);
    }

    public static PurgeDatastream purgeDatastream(String pid, String dsId) {
        return new PurgeDatastream(pid, dsId);
    }

    public static PurgeObject purgeObject(String pid) {
        return new PurgeObject(pid);
    }

    public static PurgeRelationship purgeRelationship(String pid) {
        return new PurgeRelationship(pid);
    }
}
