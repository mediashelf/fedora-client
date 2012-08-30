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

package com.yourmediashelf.fedora.client.messaging;

import java.util.Date;

public interface ApimMessage extends FedoraMessage {

    /**
     * @return the Base URL of the Fedora Repository that generated the message,
     *         e.g. http://localhost:8080/fedora
     */
    public String getBaseUrl();

    /**
     * @return the PID or null if not applicable for the API-M method
     */
    public String getPID();

    /**
     * @return the name of the API-M method invoked
     */
    public String getMethodName();

    /**
     * @return the Date object representing the timestamp of the method call
     */
    public Date getDate();

    /**
     * @return the return value of the API-M method invoked
     */
    public String getReturnVal();

    /**
     * @return the value (as a string) of the parameter of the API-M method invoked.
     *
     * Return null if parameter not present.
     */
    public String getMethodParamVal(String paramName);

}
