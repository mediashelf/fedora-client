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

/**
 *
 */

package com.yourmediashelf.fedora.client.messaging;

/**
 * @author Edwin Shin
 *
 */
public class MessagingException extends Exception {

    private static final long serialVersionUID = 1L;

    private int status;

    public MessagingException(String message) {
        super(message, null);
    }

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param status the HTTP status code of the error.
     * @param message
     */
    public MessagingException(int status, String message) {
        super(message);
        this.status = status;
    }

    /**
     *
     * @return the HTTP status code of the error
     */
    public int getStatus() {
        return status;
    }
}
