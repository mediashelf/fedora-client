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

package com.yourmediashelf.fedora.cargo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class InstallOptions {

    public static final String INSTALL_TYPE = "install.type";

    public static final String FEDORA_HOME = "fedora.home";

    public static final String FEDORA_SERVERHOST = "fedora.serverHost";

    public static final String FEDORA_APP_SERVER_CONTEXT =
            "fedora.serverContext";

    public static final String APIA_AUTH_REQUIRED = "apia.auth.required";

    public static final String UPSTREAM_AUTH_ENABLED = "upstream.auth.enabled";

    public static final String SSL_AVAILABLE = "ssl.available";

    public static final String APIA_SSL_REQUIRED = "apia.ssl.required";

    public static final String APIM_SSL_REQUIRED = "apim.ssl.required";

    public static final String SERVLET_ENGINE = "servlet.engine";

    public static final String TOMCAT_HOME = "tomcat.home";

    public static final String FEDORA_ADMIN_PASS = "fedora.admin.pass";

    public static final String TOMCAT_SHUTDOWN_PORT = "tomcat.shutdown.port";

    public static final String TOMCAT_HTTP_PORT = "tomcat.http.port";

    public static final String TOMCAT_SSL_PORT = "tomcat.ssl.port";

    public static final String KEYSTORE_FILE = "keystore.file";

    public static final String KEYSTORE_PASSWORD = "keystore.password";

    public static final String KEYSTORE_TYPE = "keystore.type";

    public static final String DATABASE = "database";

    public static final String DATABASE_DRIVER = "database.driver";

    public static final String DATABASE_JDBCURL = "database.jdbcURL";

    public static final String DATABASE_DRIVERCLASS =
            "database.jdbcDriverClass";

    public static final String EMBEDDED_DATABASE_DRIVERCLASSNAME =
            "org.apache.derby.jdbc.EmbeddedDriver";

    public static final String DATABASE_USERNAME = "database.username";

    public static final String DATABASE_PASSWORD = "database.password";

    public static final String XACML_ENABLED = "xacml.enabled";

    public static final String FESL_AUTHN_ENABLED = "fesl.authn.enabled";

    public static final String FESL_AUTHZ_ENABLED = "fesl.authz.enabled";

    public static final String LLSTORE_TYPE = "llstore.type";

    public static final String RI_ENABLED = "ri.enabled";

    public static final String MESSAGING_ENABLED = "messaging.enabled";

    public static final String MESSAGING_URI = "messaging.uri";

    public static final String DEPLOY_LOCAL_SERVICES = "deploy.local.services";

    public static final String TEST_SPRING_CONFIGS = "test.spring.configs";

    public static final String UNATTENDED = "unattended";

    public static final String DATABASE_UPDATE = "database.update";

    public static final String DEFAULT = "default";

    public static final String INSTALL_QUICK = "quick";

    public static final String INSTALL_CLIENT = "client";

    public static final String INCLUDED = "included";

    public static final String DERBY = "derby";

    public static final String MYSQL = "mysql";

    public static final String ORACLE = "oracle";

    public static final String POSTGRESQL = "postgresql";

    public static final String OTHER = "other";

    public static final String EXISTING_TOMCAT = "existingTomcat";

    private final Map<String, String> _map;

    /**
     * Initialize options from the given map of String values, keyed by option
     * id.
     */
    public InstallOptions(Map<String, String> map)
            throws OptionValidationException {
        _map = map;

        applyDefaults();
        validateAll();
    }

    /**
     * Dump all options (including any defaults that were applied) to the given
     * stream, in java properties file format. The output stream remains open
     * after this method returns.
     */
    public void dump(OutputStream out) throws IOException {
        Properties props = new Properties();
        Iterator<String> iter = _map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            props.setProperty(key, getValue(key));
        }
        props.store(out, "Install Options");
    }

    /**
     * Get the value of the given option, or <code>null</code> if it doesn't
     * exist.
     */
    public String getValue(String name) {
        return System.getProperty(name, _map.get(name));
    }

    public String getValue(String name, String defaultVal) {
        String value = getValue(name);
        if (value == null) {
            return defaultVal;
        } else {
            return value;
        }
    }

    /**
     * Get the value of the given option as a boolean, or the given default
     * value if unspecified. If specified, the value is assumed to be
     * <code>true</code> if given as "true", regardless of case. All other
     * values are assumed to be <code>false</code>.
     */
    public boolean getBooleanValue(String name, boolean defaultValue) {
        String value = getValue(name);
        if (value == null) {
            return defaultValue;
        } else {
            return value.equals("true");
        }
    }

    /**
     * Get an iterator of the names of all specified options.
     */
    public Collection<String> getOptionNames() {
        return _map.keySet();
    }

    /**
     * Apply defaults to the options, where possible.
     */
    private void applyDefaults() {
        for (String name : getOptionNames()) {
            String val = _map.get(name);
            if (val == null || val.length() == 0) {
                OptionDefinition opt = OptionDefinition.get(name, this);
                _map.put(name, opt.getDefaultValue());
            }
        }
    }

    /**
     * Validate the options, assuming defaults have already been applied.
     * Validation for a given option might entail more than a syntax check. It
     * might check whether a given directory exists, for example.
     *
     */
    private void validateAll() throws OptionValidationException {
        boolean unattended = getBooleanValue(UNATTENDED, false);
        for (String optionId : getOptionNames()) {
            OptionDefinition opt = OptionDefinition.get(optionId, this);
            if (opt == null) {
                throw new OptionValidationException("Option is not defined",
                        optionId);
            }
            opt.validateValue(getValue(optionId), unattended);
        }
    }
}
