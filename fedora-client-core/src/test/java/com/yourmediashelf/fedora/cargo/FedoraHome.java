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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.yourmediashelf.fedora.cargo.fcfg.ModuleConfiguration;
import com.yourmediashelf.fedora.cargo.fcfg.ServerConfiguration;
import com.yourmediashelf.fedora.cargo.fcfg.ServerConfigurationParser;

public class FedoraHome {

	private final InstallOptions _opts;

	private final File _installDir;

	private boolean _usingAkubra;

    public FedoraHome(InstallOptions opts) {
		_opts = opts;
		_installDir = new File(_opts.getValue(InstallOptions.FEDORA_HOME));
	}

	public void install() throws InstallationFailedException {
        configure();
	}

	/**
	 * Sets various configuration files based on InstallOptions
	 * 
	 * @throws InstallationFailedException
	 */
	private void configure() throws InstallationFailedException {
		configureFCFG();
		if (_usingAkubra) {
			configureAkubra();
		}
		configureSpringProperties();
		configureSpringAuth();
		configureSpringTestConfigs();
	}

	private void configureFCFG() throws InstallationFailedException {
		System.out.println("\tConfiguring fedora.fcfg");
		File fcfgBase = new File(_installDir,
				"server/fedora-internal-use/config/fedora-base.fcfg");
		File fcfg = new File(_installDir, "server/config/fedora.fcfg");

		Properties props = new Properties();
		if (_opts.getValue(InstallOptions.TOMCAT_HTTP_PORT) != null) {
			props.put("server:fedoraServerPort",
					_opts.getValue(InstallOptions.TOMCAT_HTTP_PORT));
		}
		if (_opts.getValue(InstallOptions.TOMCAT_SHUTDOWN_PORT) != null) {
			props.put("server:fedoraShutdownPort",
					_opts.getValue(InstallOptions.TOMCAT_SHUTDOWN_PORT));
		}
		if (_opts.getValue(InstallOptions.TOMCAT_SSL_PORT) != null) {
			props.put("server:fedoraRedirectPort",
					_opts.getValue(InstallOptions.TOMCAT_SSL_PORT));
		}
		if (_opts.getValue(InstallOptions.FEDORA_SERVERHOST) != null) {
			props.put("server:fedoraServerHost",
					_opts.getValue(InstallOptions.FEDORA_SERVERHOST));
		}

		if (_opts.getValue(InstallOptions.FEDORA_APP_SERVER_CONTEXT) != null) {
			props.put("server:fedoraAppServerContext",
					_opts.getValue(InstallOptions.FEDORA_APP_SERVER_CONTEXT));
		}

		String database = _opts.getValue(InstallOptions.DATABASE);
		String dbPoolName = "";
		String backslashIsEscape = "true";
		if (database.equals(InstallOptions.DERBY)
				|| database.equals(InstallOptions.INCLUDED)) {
			dbPoolName = "localDerbyPool";
			backslashIsEscape = "false";
		} else if (database.equals(InstallOptions.MYSQL)) {
			dbPoolName = "localMySQLPool";
		} else if (database.equals(InstallOptions.ORACLE)) {
			dbPoolName = "localOraclePool";
			backslashIsEscape = "false";
		} else if (database.equals(InstallOptions.POSTGRESQL)) {
			dbPoolName = "localPostgreSQLPool";
		} else {
			throw new InstallationFailedException(
					"unable to configure for unknown database: " + database);
		}
		props.put("module.org.fcrepo.server.storage.DOManager:storagePool",
				dbPoolName);
		props.put("module.org.fcrepo.server.search.FieldSearch:connectionPool",
				dbPoolName);
		props.put(
				"module.org.fcrepo.server.storage.ConnectionPoolManager:poolNames",
				dbPoolName);
		props.put(
				"module.org.fcrepo.server.storage.ConnectionPoolManager:defaultPoolName",
				dbPoolName);
		props.put(
				"module.org.fcrepo.server.storage.lowlevel.ILowlevelStorage:backslash_is_escape",
				backslashIsEscape);
		props.put("datastore." + dbPoolName + ":jdbcURL",
				_opts.getValue(InstallOptions.DATABASE_JDBCURL));
		props.put("datastore." + dbPoolName + ":dbUsername",
				_opts.getValue(InstallOptions.DATABASE_USERNAME));
		props.put("datastore." + dbPoolName + ":dbPassword",
				_opts.getValue(InstallOptions.DATABASE_PASSWORD));
		props.put("datastore." + dbPoolName + ":jdbcDriverClass",
				_opts.getValue(InstallOptions.DATABASE_DRIVERCLASS));

		if (_opts.getBooleanValue(InstallOptions.XACML_ENABLED, true)) {
			props.put(
					"module.org.fcrepo.server.security.Authorization:ENFORCE-MODE",
					"enforce-policies");
		} else {
			props.put(
					"module.org.fcrepo.server.security.Authorization:ENFORCE-MODE",
					"permit-all-requests");
		}

		if (_opts.getBooleanValue(InstallOptions.RI_ENABLED, true)) {
			props.put(
					"module.org.fcrepo.server.resourceIndex.ResourceIndex:level",
                    "1");
		} else {
			props.put(
					"module.org.fcrepo.server.resourceIndex.ResourceIndex:level",
                    "0");
		}

		if (_opts.getBooleanValue(InstallOptions.MESSAGING_ENABLED, false)) {
			props.put("module.org.fcrepo.server.messaging.Messaging:enabled",
					String.valueOf(true));
			props.put(
					"module.org.fcrepo.server.messaging.Messaging:java.naming.provider.url",
					_opts.getValue(InstallOptions.MESSAGING_URI));
		} else {
			props.put("module.org.fcrepo.server.messaging.Messaging:enabled",
					String.valueOf(false));
		}

		props.put(
				"module.org.fcrepo.server.access.Access:doMediateDatastreams",
				_opts.getValue(InstallOptions.APIA_AUTH_REQUIRED));

		// FeSL AuthZ needs a management decorator for syncing the policy cache
		// with policies in objects
		if (_opts.getBooleanValue(InstallOptions.FESL_AUTHZ_ENABLED, false)) {
			// NOTE: assumes messaging decorator only is present in
			// fedora-base.fcfg as decorator1
			props.put(
					"module.org.fcrepo.server.management.Management:decorator2",
					"org.fcrepo.server.security.xacml.pdp.decorator.PolicyIndexInvocationHandler");
		}

		try {
			FileInputStream fis = new FileInputStream(fcfgBase);
			ServerConfiguration config = new ServerConfigurationParser(fis)
					.parse();
			config.applyProperties(props);

			// If using akubra-fs, set the class of the module and clear params.
			String llStoreType = _opts.getValue(InstallOptions.LLSTORE_TYPE);
			if (llStoreType == null || llStoreType.equals("akubra-fs")) {
				ModuleConfiguration mConfig = config
						.getModuleConfiguration("org.fcrepo.server.storage.lowlevel.ILowlevelStorage");
				config.getModuleConfigurations().remove(mConfig);
				_usingAkubra = true;
			}

			config.serialize(new FileOutputStream(fcfg));
		} catch (IOException e) {
			throw new InstallationFailedException(e.getMessage(), e);
		}
	}

	private void configureAkubra() throws InstallationFailedException {
		// Rewrite server/config/akubra-llstore.xml replacing the
		// /tmp/[object|datastream]Store constructor-arg values
		// with $FEDORA_HOME/data/[object|datastream]Store
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			File file = new File(_installDir,
					"server/config/spring/akubra-llstore.xml");
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));

			File dataDir = new File(_installDir, "data");
			String oPath = dataDir.getPath() + File.separator + "objectStore";
			String dPath = dataDir.getPath() + File.separator
					+ "datastreamStore";
			StringBuilder xml = new StringBuilder();

			String line = reader.readLine();
			while (line != null) {
				if (line.indexOf("/tmp/objectStore") != -1) {
					line = "    <constructor-arg value=\"" + oPath + "\"/>";
				} else if (line.indexOf("/tmp/datastreamStore") != -1) {
					line = "    <constructor-arg value=\"" + dPath + "\"/>";
				}
				xml.append(line + "\n");
				line = reader.readLine();
			}
			reader.close();

			writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			writer.print(xml.toString());
			writer.close();
		} catch (IOException e) {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(writer);
			throw new InstallationFailedException(e.getClass().getName() + ":"
					+ e.getMessage());
		}
	}

	private void configureSpringProperties() throws InstallationFailedException {
		Properties springProps = new Properties();

		/* Set up ssl configuration */
		springProps.put("fedora.port",
				_opts.getValue(InstallOptions.TOMCAT_HTTP_PORT, "8080"));
		if (_opts.getBooleanValue(InstallOptions.SSL_AVAILABLE, false)) {
			springProps.put("fedora.port.secure",
					_opts.getValue(InstallOptions.TOMCAT_SSL_PORT, "8443"));
		} else {
			springProps.put("fedora.port.secure",
					_opts.getValue(InstallOptions.TOMCAT_HTTP_PORT, "8080"));
		}

		springProps
				.put("security.ssl.api.access",
						_opts.getBooleanValue(InstallOptions.APIA_SSL_REQUIRED,
								false) ? "REQUIRES_SECURE_CHANNEL"
								: "ANY_CHANNEL");
		springProps
				.put("security.ssl.api.management",
						_opts.getBooleanValue(InstallOptions.APIM_SSL_REQUIRED,
								false) ? "REQUIRES_SECURE_CHANNEL"
								: "ANY_CHANNEL");
		springProps.put("security.ssl.api.default", "ANY_CHANNEL");

		springProps.put("security.fesl.authN.jaas.apia.enabled",
				_opts.getValue(InstallOptions.APIA_AUTH_REQUIRED, "false"));

		springProps.put("security.fesl.authZ.enabled",
				_opts.getValue(InstallOptions.FESL_AUTHZ_ENABLED, "false"));

		/* Set up authN, authZ filter configuration */
		StringBuilder filters = new StringBuilder();
        if (_opts.getBooleanValue(InstallOptions.FESL_AUTHN_ENABLED, true)) {
			filters.append("AuthFilterJAAS");
		} else {
			filters.append("SetupFilter,XmlUserfileFilter,EnforceAuthnFilter,FinalizeFilter");
		}

		if (_opts.getBooleanValue(InstallOptions.FESL_AUTHZ_ENABLED, false)) {
			filters.append(",PEPFilter");
		}

		springProps.put("security.auth.filters", filters.toString());

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(_installDir,
					"server/config/spring/web/web.properties"));
			springProps.store(out, "Spring override properties");
		} catch (IOException e) {
			throw new InstallationFailedException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/*
	 * This is an ugly workaround for the fact that spring security namespace
	 * config does not support property substitution into lists of beans. It is
	 * also ugly because
	 */
	private void configureSpringAuth() throws InstallationFailedException {
		String PATTERN = "${security.auth.filters}";
		String PATTERN_APIA = "${security.auth.filters.apia}";
		String PATTERN_REST = "${security.auth.filters.rest}";

		boolean fesl_authn_enabled = _opts.getBooleanValue(
InstallOptions.FESL_AUTHN_ENABLED, true);
		boolean apia_auth_required = _opts.getBooleanValue(
				InstallOptions.APIA_AUTH_REQUIRED, false);
		boolean upstream_auth_enabled = _opts.getBooleanValue(
				InstallOptions.UPSTREAM_AUTH_ENABLED, false);

		StringBuilder filters = new StringBuilder();
		StringBuilder filters_apia = new StringBuilder();
		StringBuilder filters_rest = new StringBuilder();

		boolean needsbugFix = false;

		if (fesl_authn_enabled) {
			filters.append("AuthFilterJAAS");
			filters_apia.append("AuthFilterJAAS");
			filters_rest.append("AuthFilterJAAS");
		} else if (upstream_auth_enabled) {
			// use upstream auth filter and nothing else
			String upstreamAuthFilter = "UpstreamAuthFilter";
			filters.append(upstreamAuthFilter);
			filters_apia.append(upstreamAuthFilter);
			filters_rest.append(upstreamAuthFilter);
		}
		else {
			filters.append("SetupFilter,XmlUserfileFilter,EnforceAuthnFilter,FinalizeFilter");
			if (apia_auth_required) {
				filters_apia
						.append("SetupFilter,XmlUserfileFilter,EnforceAuthnFilter,FinalizeFilter");
				filters_rest
						.append("SetupFilter,XmlUserfileFilter,EnforceAuthnFilter,FinalizeFilter");
			} else {
				filters_apia.append("");
				filters_rest
						.append("SetupFilter,XmlUserfileFilter,RestApiAuthnFilter,FinalizeFilter");
			}

			needsbugFix = true;
		}

		if (_opts.getBooleanValue(InstallOptions.FESL_AUTHZ_ENABLED, false)) {
			filters.append(",PEPFilter");
			filters_apia.append(",PEPFilter");
			filters_rest.append(",PEPFilter");
			copyFESLConfigs();
		}

		FileInputStream springConfig = null;
		PrintWriter writer = null;
		try {
			File xmlFile = new File(_installDir,
					"server/config/spring/web/security.xml");
			springConfig = new FileInputStream(xmlFile);
			String content = IOUtils.toString(springConfig)
					.replace(PATTERN, filters)
					.replace(PATTERN_APIA, filters_apia)
					.replace(PATTERN_REST, filters_rest);

			if (!needsbugFix) {
				/* Delete classic authN bugfix when not applicable */
				content = content.replaceFirst("(?s)<!-- BUG.+?/BUG -->", "");
			}

			springConfig.close();

			writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(xmlFile), "UTF-8"));
			writer.print(content);
			writer.close();
		} catch (Exception e) {
			IOUtils.closeQuietly(springConfig);
			IOUtils.closeQuietly(writer);
			throw new InstallationFailedException(e.getMessage(), e);
		}
	}

	private void copyFESLConfigs() throws InstallationFailedException {
		File feslWebDir = new File(_installDir, "server/config/spring/fesl/web");
		File feslModuleDir = new File(_installDir,
				"server/config/spring/fesl/module");
		File webDir = new File(_installDir, "server/config/spring/web");
		File moduleDir = new File(_installDir, "server/config/spring");

		for (File beanDef : feslWebDir.listFiles()) {
			if (beanDef.isFile()) {
				FileReader reader = null;
				FileWriter writer = null;
				try {
					File copy = new File(webDir, beanDef.getName());
					reader = new FileReader(beanDef);
					writer = new FileWriter(copy);
					IOUtils.copy(reader, writer);
					writer.flush();
				} catch (Exception e) {
					throw new InstallationFailedException(e.getMessage(), e);
				} finally {
					IOUtils.closeQuietly(writer);
					IOUtils.closeQuietly(reader);
				}
			}
		}
		for (File beanDef : feslModuleDir.listFiles()) {
			if (beanDef.isFile()) {
				FileReader reader = null;
				FileWriter writer = null;
				try {
					File copy = new File(moduleDir, beanDef.getName());
					reader = new FileReader(beanDef);
					writer = new FileWriter(copy);
					IOUtils.copy(reader, writer);
					writer.flush();
				} catch (Exception e) {
					throw new InstallationFailedException(e.getMessage(), e);
				} finally {
					IOUtils.closeQuietly(writer);
					IOUtils.closeQuietly(reader);
				}
			}
		}
	}

	private void configureSpringTestConfigs()
			throws InstallationFailedException {
		if (_opts.getBooleanValue(InstallOptions.TEST_SPRING_CONFIGS, false)) {
			FileInputStream springConfig = null;
			PrintWriter writer = null;
			try {
				File springDir = new File(_installDir, "server/config/spring");
				for (File file : springDir.listFiles()) {
					if (file.isFile()) {
						springConfig = new FileInputStream(file);
						String content = IOUtils.toString(springConfig);
						content = content.replaceAll(
								"(?s)<!-- TESTONLY(.+?)/TESTONLY -->", "$1");
						springConfig.close();

						writer = new PrintWriter(new OutputStreamWriter(
								new FileOutputStream(file), "UTF-8"));
						writer.print(content);
						writer.close();
					}
				}
			} catch (Exception e) {
				IOUtils.closeQuietly(springConfig);
				IOUtils.closeQuietly(writer);
				throw new InstallationFailedException(e.getMessage(), e);
			}
		}
	}

    /**
     * Loads a Map from the given Properties file.
     *
     * @param f
     *        the Properties file to parse.
     * @return a Map<String, String> representing the given Properties file.
     * @throws IOException
     * @see java.util.Properties
     * @see java.util.Map
     */
    public static Map<String, String> loadMap(File f) throws IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(f);
        try {
            props.load(in);
            Map<String, String> map = new HashMap<String, String>();
            Set<Entry<Object, Object>> entrySet = props.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                // The casts to String should always succeed
                map.put((String) entry.getKey(), (String) entry.getValue());
            }
            return map;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Command-line entry point.
     */
    public static void main(String[] args) throws Exception {
        InstallOptions opts;
        Map<String, String> props = new HashMap<String, String>();
        for (String file : args) {
            props.putAll(loadMap(new File(file)));
        }
        opts = new InstallOptions(props);
        FedoraHome fh = new FedoraHome(opts);
        fh.install();
    }

}
