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

/**
 *
 */
public class ModuleConfiguration extends Configuration {

    private final String m_roleName;

    private String m_className;

    private final String m_comment;

    public ModuleConfiguration(List<Parameter> parameters, String roleName,
            String className, String comment) {
        super(parameters);
        m_roleName = roleName;
        m_className = className;
        m_comment = comment;
    }

    public String getRole() {
        return m_roleName;
    }

    public void setClassName(String className) {
        m_className = className;
    }

    public String getClassName() {
        return m_className;
    }

    public String getComment() {
        return m_comment;
    }

}
