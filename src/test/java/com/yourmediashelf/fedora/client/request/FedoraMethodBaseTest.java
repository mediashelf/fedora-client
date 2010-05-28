
package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.junit.After;
import org.junit.Before;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;

public abstract class FedoraMethodBaseTest {

    private FedoraCredentials credentials;

    private FedoraClient fedora;

    public final String testPid = "test-rest:1";

    @Before
    public void setUp() throws Exception {
        ingestTestObject();
    }

    @After
    public void tearDown() throws Exception {
        purgeTestObject();
    }

    public FedoraCredentials getCredentials() throws MalformedURLException {
        if (credentials == null) {
            String baseUrl = System.getProperty("fedora.test.baseUrl");
            String username = System.getProperty("fedora.test.username");
            String password = System.getProperty("fedora.test.password");
            credentials =
                    new FedoraCredentials(new URL(baseUrl), username, password);
        }
        return credentials;
    }

    public FedoraClient fedora() throws FedoraClientException {
        if (fedora == null) {
            try {
                fedora = new FedoraClient(getCredentials());
            } catch (MalformedURLException e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
        }
        return fedora;
    }

    public String getTestPid() {
        return testPid;
    }

    public void ingestTestObject() throws FedoraClientException {
        ingest(testPid).execute(fedora());
    }

    public void purgeTestObject() throws FedoraClientException {
        purgeObject(testPid).execute(fedora());
    }

    public XpathEngine getXpathEngine(Map<String, String> nsMap) {
        XpathEngine engine = XMLUnit.newXpathEngine();
        if (nsMap != null) {
            NamespaceContext ctx = new SimpleNamespaceContext(nsMap);
            engine.setNamespaceContext(ctx);
        }
        return engine;
    }

    public XpathEngine getXpathEngine(String prefix, String uri) {
        Map<String, String> nsMap = new HashMap<String, String>();
        nsMap.put(prefix, uri);
        return getXpathEngine(nsMap);
    }
}
