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

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 */
public abstract class Configuration implements Constants {

    /** a reference to the provided params for this component */
    private final Map<String, Parameter> m_parameters =
            new HashMap<String, Parameter>();

    public void
            setParameterValue(String name, String value, boolean autoCreate) {
        Parameter param = getParameter(name, Parameter.class);
        if (param == null && !autoCreate) {
            return;
        }
        setParameter(name, value);
    }

    /**
     * Creates a Parameterized with name-value pairs from the supplied Map.
     *
     * @param parameters
     *        The map from which to derive the name-value pairs.
     */
    public Configuration(Map<String, String> parameters) {
        setParameters(parameters);
    }

    public Configuration(List<Parameter> parameters) {
        setParameters(parameters);
    }

    /**
     * Sets the parameters with name-value pairs from the supplied Map. This is
     * protected because it is intended to only be called by subclasses where
     * super(Map m) is not possible to call at the start of the constructor.
     * Server.java:Server(URL) is an example of this.
     *
     * @param parameters
     *        The map from which to derive the name-value pairs.
     */
    protected final void setParameters(Map<String, String> parameters) {
        setParameters(Configuration.getParameterList(parameters));
    }

    protected final void setParameters(List<Parameter> parameters) {
        if (parameters == null) {
            m_parameters.clear();
        } else {
            m_parameters.clear();
            for (Parameter p : parameters) {
                m_parameters.put(p.getName(), p);
            }
        }
    }

    /**
     * Gets the value of a named configuration parameter. Same as
     * getParameter(String name) but prepends the location of FEDORA_HOME if
     * asAbsolutePath is true and the parameter location does not already
     * specify an absolute pathname.
     *
     * @param name
     *        The parameter name.
     * @param asAbsolutePath
     *        Whether to return the parameter value as an absolute path relative
     *        to FEDORA_HOME.
     * @return The value, null if undefined.
     */
    public final String getParameter(String name, boolean asAbsolutePath) {
        if (!m_parameters.containsKey(name)) return null;

        String paramValue = m_parameters.get(name).getValue();
        if (asAbsolutePath && paramValue != null) {
            File f = new File(paramValue);
            if (!f.isAbsolute()) {
                paramValue = FEDORA_HOME + File.separator + paramValue;
            }
        }
        return paramValue;
    }

    /**
     * Gets the value of a named configuration parameter.
     *
     * @param name
     *        The parameter name.
     * @return String The value, null if undefined.
     */
    public final String getParameter(String name) {
        return getParameter(name, false);
    }

    public final Parameter getParameter(String name, Class<Parameter> type) {
        return m_parameters.get(name);
    }

    protected final void setParameter(String name, String value) {
        Parameter parm = m_parameters.get(name);
        if (parm == null) {
            parm = new Parameter(name);
            m_parameters.put(name, parm);
        }
        parm.setValue(value);
    }

    public Map<String, String> getParameters() {
        Map<String, String> result =
                new HashMap<String, String>(m_parameters.size(), 1);
        for (Entry<String, Parameter> entry : m_parameters.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getValue());
        }
        return result;
    }

    public Collection<Parameter> getParameters(Class<Parameter> type) {
        return m_parameters.values();
    }


    /**
     * Gets an Iterator over the names of parameters for this component.
     *
     * @return Iterator The names.
     */
    public final Iterator<String> parameterNames() {
        return m_parameters.keySet().iterator();
    }

    protected static List<Parameter> getParameterList(Map<String, String> map) {
        Set<String> keys = map.keySet();
        Parameter[] parms = new Parameter[keys.size()];
        int i = 0;
        for (String key : keys) {
            parms[i] = new Parameter(key);
            parms[i].setValue(map.get(key));
            i++;
        }
        return Arrays.asList(parms);
    }
}
