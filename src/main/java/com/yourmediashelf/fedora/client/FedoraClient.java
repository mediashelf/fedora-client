
package com.yourmediashelf.fedora.client;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.xml.sax.InputSource;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.yourmediashelf.fedora.client.request.AddDatastream;
import com.yourmediashelf.fedora.client.request.AddRelationship;
import com.yourmediashelf.fedora.client.request.Export;
import com.yourmediashelf.fedora.client.request.FedoraMethod;
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
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.util.DateUtility;
import com.yourmediashelf.fedora.util.NamespaceContextImpl;

import eu.medsea.mimeutil.MimeUtil2;

/**
 * A client for Fedora's REST API.
 *
 * @author Edwin Shin
 */
public class FedoraClient {

    private final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final FedoraCredentials fc;

    private Client client;

    private final MimeUtil2 mimeUtil;

    private final NamespaceContextImpl nsCtx;

    public FedoraClient(FedoraCredentials fc) {
        this.fc = fc;
        nsCtx = new NamespaceContextImpl();
        nsCtx.addNamespace("f", "info:fedora/fedora-system:def/foxml#");
        mimeUtil = new MimeUtil2();
        mimeUtil
                .registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");

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
            config.getProperties()
                    .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                         new HTTPSProperties(null, ctx));
            client = Client.create(config);
        } else {
            client = Client.create();
        }
        client.setFollowRedirects(true);
    }

    public FedoraResponse execute(FedoraMethod<?> method)
            throws FedoraClientException {
        return method.execute(this);
    }

    public WebResource resource() {
        WebResource wr = client.resource(fc.getBaseUrl().toString());
        wr
                .addFilter(new HTTPBasicAuthFilter(fc.getUsername(), fc
                        .getPassword()));
        //wr.addFilter(new LoggingFilter(System.out));
        return wr;
    }

    public String getMimeType(File file) {
        return MimeUtil2.getMostSpecificMimeType(mimeUtil.getMimeTypes(file))
                .toString();
    }

    public DateTime getLastModifiedDate(String pid) throws FedoraClientException {
        FedoraResponse response = execute(getObjectXML(pid));

        String expr = "//f:objectProperties/f:property[@NAME='info:fedora/fedora-system:def/view#lastModifiedDate']/@VALUE";
        String lastModifiedDate;
        try {
            lastModifiedDate = getXPath().evaluate(expr, new InputSource(response.getEntityInputStream()));
        } catch (XPathExpressionException e) {
            throw new FedoraClientException(e.getMessage(), e);
        }
        return DateUtility.parseXSDDateTime(lastModifiedDate);
    }

    public DateTime getLastModifiedDate(String pid, String dsId) throws FedoraClientException {
        FedoraResponse response = execute(getDatastream(pid, dsId).format("xml"));
        String expr = "/datastreamProfile/dsCreateDate";
        String lastModifiedDate;
        try {
            lastModifiedDate = getXPath().evaluate(expr, new InputSource(response.getEntityInputStream()));
        } catch (XPathExpressionException e) {
            throw new FedoraClientException(e.getMessage(), e);
        }
        return DateUtility.parseXSDDateTime(lastModifiedDate);
    }

    public String getNextPid(String namespace) throws FedoraClientException {
        FedoraResponse response = execute(getNextPID().namespace(namespace).format("xml"));
        String expr = "/pidList/pid";
        try {
            return getXPath().evaluate(expr, new InputSource(response.getEntityInputStream()));
        } catch (XPathExpressionException e) {
            throw new FedoraClientException(e.getMessage(), e);
        }
    }

    private XPath getXPath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(nsCtx);
        return xpath;
    }

    ////////////////////////////////////////////
    //        static convenience methods      //
    ////////////////////////////////////////////
    /**
     * @param pid
     *        the persistent identifier
     * @param dsId
     *        the datastream identifier
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

    public static GetDatastreamDissemination getDatastreamDissemination(String pid,
                                                                        String dsId) {
        return new GetDatastreamDissemination(pid, dsId);
    }

    public static GetDatastreamHistory getDatastreamHistory(String pid,
                                                            String dsId) {
        return new GetDatastreamHistory(pid, dsId);
    }

    public static GetDissemination getDissemination(String pid,
                                                    String sdefPid,
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

    public static GetRelationships getRelationships(String pid) {
        return new GetRelationships(pid);
    }

    /**
     * @param pid
     *        persistent identifier of the object to be created or null for a
     *        server-assigned pid
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

    public static PurgeRelationship purgeRelationship(String pid) {
        return new PurgeRelationship(pid);
    }
}
