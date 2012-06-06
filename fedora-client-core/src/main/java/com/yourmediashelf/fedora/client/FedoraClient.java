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

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.net.ssl.SSLContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.yourmediashelf.fedora.client.request.AddDatastream;
import com.yourmediashelf.fedora.client.request.AddRelationship;
import com.yourmediashelf.fedora.client.request.DescribeRepository;
import com.yourmediashelf.fedora.client.request.Export;
import com.yourmediashelf.fedora.client.request.FedoraRequest;
import com.yourmediashelf.fedora.client.request.FindObjects;
import com.yourmediashelf.fedora.client.request.GetDatastream;
import com.yourmediashelf.fedora.client.request.GetDatastreamDissemination;
import com.yourmediashelf.fedora.client.request.GetDatastreamHistory;
import com.yourmediashelf.fedora.client.request.GetDissemination;
import com.yourmediashelf.fedora.client.request.GetNextPID;
import com.yourmediashelf.fedora.client.request.GetObjectHistory;
import com.yourmediashelf.fedora.client.request.GetObjectProfile;
import com.yourmediashelf.fedora.client.request.GetObjectXML;
import com.yourmediashelf.fedora.client.request.GetRelationships;
import com.yourmediashelf.fedora.client.request.Ingest;
import com.yourmediashelf.fedora.client.request.ListDatastreams;
import com.yourmediashelf.fedora.client.request.ListMethods;
import com.yourmediashelf.fedora.client.request.ModifyDatastream;
import com.yourmediashelf.fedora.client.request.ModifyObject;
import com.yourmediashelf.fedora.client.request.PurgeDatastream;
import com.yourmediashelf.fedora.client.request.PurgeObject;
import com.yourmediashelf.fedora.client.request.PurgeRelationship;
import com.yourmediashelf.fedora.client.request.RiSearch;
import com.yourmediashelf.fedora.client.request.Upload;
import com.yourmediashelf.fedora.client.request.Validate;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamResponse;
import com.yourmediashelf.fedora.client.response.GetObjectProfileResponse;

import eu.medsea.mimeutil.MimeUtil2;

/**
 * A client for the Fedora REST API.
 *
 * @author Edwin Shin
 */
public class FedoraClient {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(this.getClass());

    private final FedoraCredentials fc;

    private final Client client;

    private String serverVersion;

    private boolean debug = false;

    private static MimeUtil2 mimeUtil;

    // Workaround for MimeUtil2 memory leak
    // see: https://sourceforge.net/tracker/index.php?func=detail&aid=3152843&group_id=205064&atid=992132
    // and also: https://jira.duraspace.org/browse/FCREPO-964
    static {
        if (mimeUtil == null) {
            mimeUtil = new MimeUtil2();
            mimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        }
    }

    /**
     * Constructor for FedoraClient.
     *
     * @param fc credentials for a Fedora repository
     */
    public FedoraClient(FedoraCredentials fc) {
        this.fc = fc;

        // TODO validate fc
        // null check for username & password
        // null check for baseUrl

        // FIXME this isn't very security-minded
        if (fc.getBaseUrl().toString().startsWith("https")) {
            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("SSL");
                ctx.init(null, null, null);
            } catch (NoSuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
            } catch (KeyManagementException e) {
                logger.error(e.getMessage(), e);
            }
            ClientConfig config = new DefaultClientConfig();
            // FIXME Consider using not-yet-commons-ssl hostnameverifier
            // config by property: strict, none, etc.
            config.getProperties().put(
                    HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    new HTTPSProperties(null, ctx));
            client = Client.create(config);
        } else {
            client = Client.create();
        }
        client.setFollowRedirects(true);
        client.addFilter(new HTTPBasicAuthFilter(fc.getUsername(), fc
                .getPassword()));
    }

    /**
     * Convenience method for executing FedoraRequests. Note that this method
     * returns a FedoraResponse and therefore requires an explicit cast if a
     * specific implementation of FedoraResponse is desired.
     *
     * <p>The preferred usage is actually the reverse: pass an instance of
     * FedoraClient to the FedoraRequest, e.g.</p>
     *
     * <pre>    IngestResponse r = new Ingest("my:pid").execute(client);</pre>
     * 
     * <p>As a further shorthand, if the default instance of FedoraClient that 
     * FedoraRequests should use is set ({@link FedoraRequest#setDefaultClient(FedoraClient)}),
     * then you can use the following:</p>
     * 
     * <pre>    IngestResponse r = new Ingest("my:pid").execute();</pre>
     *
     * <p>Finally, FedoraClient provides a number of static convenience methods
     * for FedoraRequests. Therefore, with a static import, the following,
     * abbreviated syntax can be used:</p>
     *
     * <pre>import static com.yourmediashelf.fedora.client.FedoraClient.*;
     * ...
     *     IngestResponse r = ingest("my:pid").execute();</pre>
     *
     * @param method a FedoraRequest
     * @return the wrapped HTTP response
     * @throws FedoraClientException
     */
    public FedoraResponse execute(FedoraRequest<?> method)
            throws FedoraClientException {
        return method.execute(this);
    }

    public WebResource resource() {
        return resource(fc.getBaseUrl().toString());
    }

    public WebResource resource(String uri) {
        WebResource wr = client.resource(uri);
        if (debug) {
            wr.addFilter(new LoggingFilter(System.out));
        }
        return wr;
    }

    public String getMimeType(File file) {
        return MimeUtil2.getMostSpecificMimeType(mimeUtil.getMimeTypes(file))
                .toString();
    }

    /**
     * <p>Convenience method for returning the lastModifiedDate of an object.
     * 
     * <p>Equivalent to <code>getObjectProfile(pid).execute().getLastModifiedDate()</code>.
     * 
     * @param pid the pid of the object
     * @return the lastModifiedDate of the requested object
     * @throws FedoraClientException
     */
    public Date getLastModifiedDate(String pid) throws FedoraClientException {
        GetObjectProfileResponse response = getObjectProfile(pid).execute(this);
        return response.getLastModifiedDate();
    }

    /**
     * <p>Convenience method for returning the lastModifiedDate of a datastream.
     * 
     * <p>Equivalent to <code>getDatastream(pid, dsId).execute().getLastModifiedDate()</code>.
     * 
     * @param pid the pid of the object
     * @param dsId the datastream ID
     * @return the lastModifiedDate of the requested datastream
     * @throws FedoraClientException
     */
    public Date getLastModifiedDate(String pid, String dsId)
            throws FedoraClientException {
        GetDatastreamResponse response =
                getDatastream(pid, dsId).format("xml").execute(this);
        return response.getLastModifiedDate();
    }

    /**
     * If enabled, adds a logging filter to all requests that outputs to
     * System.out.
     *
     * @param debug true to enable debugging
     */
    public void debug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Get the version of the Fedora Repository this client is associated with
     * as reported by DescribeRepository
     *
     * @return the version of the Fedora Repository this client is associated with.
     * @throws FedoraClientException
     */
    public String getServerVersion() throws FedoraClientException {
        if (serverVersion == null) {
            serverVersion =
                    describeRepository().execute(this).getRepositoryVersion();
        }
        return serverVersion;
    }

    //    private XPath getXPath() {
    //        XPathFactory factory = XPathFactory.newInstance();
    //        XPath xpath = factory.newXPath();
    //        xpath.setNamespaceContext(nsCtx);
    //        return xpath;
    //    }

    ////////////////////////////////////////////
    //        static convenience methods      //
    ////////////////////////////////////////////
    /**
     * <p>Static convenience method for the AddDatastream request.
     * 
     * <p>Equivalent to invoking: <code>new AddDatastream(pid, dsId)</code>.
     *
     * @param pid
     *        the pid of the object to add the datastream to.
     * @param dsId
     *        the identifier of the datastream to be added.
     * @return builder for the AddDatastream method
     * @see AddDatastream
     */
    public static AddDatastream addDatastream(String pid, String dsId) {
        return new AddDatastream(pid, dsId);
    }

    /**
     * <p>Static convenience method for the AddRelationship request.
     * 
     * <p>Equivalent to invoking: <code>new AddRelationship(subject)</code>.
     * 
     * @param subject the subject of the object (e.g. <code>demo:123</code> or 
     * <code>info:fedora/demo:123/DC</code>) 
     * @return builder for the AddRelationship method
     * @see AddRelationship
     */
    public static AddRelationship addRelationship(String subject) {
        return new AddRelationship(subject);
    }

    /**
     * <p>Static convenience method for the DescribeRepository request.
     * 
     * <p>Equivalent to invoking: <code>new DescribeRepository()</code>.
     * 
     * @return builder for the DescribeRepository method
     * @see DescribeRepository
     */
    public static DescribeRepository describeRepository() {
        return new DescribeRepository();
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

    public static GetDatastreamDissemination getDatastreamDissemination(
            String pid, String dsId) {
        return new GetDatastreamDissemination(pid, dsId);
    }

    public static GetDatastreamHistory getDatastreamHistory(String pid,
            String dsId) {
        return new GetDatastreamHistory(pid, dsId);
    }

    public static GetDissemination getDissemination(String pid, String sdefPid,
            String method) {
        return new GetDissemination(pid, sdefPid, method);
    }

    public static GetNextPID getNextPID() {
        return new GetNextPID();
    }

    public static GetObjectHistory getObjectHistory(String pid) {
        return new GetObjectHistory(pid);
    }

    public static GetObjectProfile getObjectProfile(String pid) {
        return new GetObjectProfile(pid);
    }

    public static GetObjectXML getObjectXML(String pid) {
        return new GetObjectXML(pid);
    }

    public static GetRelationships getRelationships(String subject) {
        return new GetRelationships(subject);
    }

    /**
     * Ingest request that uses a server-assigned pid
     *
     * @return builder for the Ingest method
     */
    public static Ingest ingest() {
        return new Ingest();
    }

    /**
     * @param pid persistent identifier of the object to be created
     * @return builder for the Ingest method
     * @see Ingest
     */
    public static Ingest ingest(String pid) {
        return new Ingest(pid);
    }

    public static ListDatastreams listDatastreams(String pid) {
        return new ListDatastreams(pid);
    }

    public static ListMethods listMethods(String pid) {
        return new ListMethods(pid);
    }

    public static ListMethods listMethods(String pid, String sdefPid) {
        return new ListMethods(pid, sdefPid);
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

    public static PurgeRelationship purgeRelationship(String subject) {
        return new PurgeRelationship(subject);
    }

    public static RiSearch riSearch(String query) {
        return new RiSearch(query);
    }

    public static Upload upload(File file) {
        return new Upload(file);
    }

    public static Validate validate(String pid) {
        return new Validate(pid);
    }
}
