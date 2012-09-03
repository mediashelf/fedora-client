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
package com.yourmediashelf.fedora.cargo.fcfg;

import java.util.List;
import java.util.Map;

/**
 * A holder of configuration name-value pairs for a datastore.
 *
 * <p>A datastore is a system for retrieving and storing information. This
 * class is a convenient placeholder for the configuration values of such a
 * system.
 *
 * <p>Configuration values for datastores are set in the server configuration
 * file. (see fedora-config.xsd)
 *
 * @author Chris Wilper
 */
public class DatastoreConfig
        extends Configuration {

    /**
     * Creates and initializes the <code>DatastoreConfig</code>.
     *
     * <p>When the server is starting up, this is invoked as part of the
     * initialization process.  The inheritance structure is convoluted
     * because of an effort to remove the class, which was basically
     * identical to {@link org.fcrepo.server.config.DatastoreConfiguration}
     *
     * @param parameters
     *        A pre-loaded Map of name-value pairs comprising the intended
     *        configuration for the datastore.
     */
    public DatastoreConfig(Map<String,String> parameters) {
        super(parameters);
    }
    public DatastoreConfig(List<Parameter> parameters) {
        super(parameters);
    }
}
