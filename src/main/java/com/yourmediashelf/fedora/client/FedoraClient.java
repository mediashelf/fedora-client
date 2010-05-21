
package com.yourmediashelf.fedora.client;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.yourmediashelf.fedora.client.request.FedoraRequest;

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

    public FedoraClient(FedoraCredentials fc) {
        this.fc = fc;
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

    public ClientResponse execute(FedoraRequest method)
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
}
