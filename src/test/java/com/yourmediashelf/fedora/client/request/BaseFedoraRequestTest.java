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

/**
 * Base class for FedoraRequest integration tests.
 *
 * @author Edwin Shin
 */
public abstract class BaseFedoraRequestTest {

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
        ingest(testPid).logMessage("ingestTestObject for " + getClass()).execute(fedora());
    }

    public void purgeTestObject() throws FedoraClientException {
        purgeObject(testPid).logMessage("purgeTestObject for " + getClass()).execute(fedora());
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
