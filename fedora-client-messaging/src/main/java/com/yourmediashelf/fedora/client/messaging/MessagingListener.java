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

import javax.jms.Message;

/**
 * A MessagingListener is used to receive asynchronously
 * delivered notifications.
 *
 * @author Bill Branan
 */
public interface MessagingListener {

    /**
     * Processes a message.
     *
     * @param clientId - the identifier of the messaging client from
     *                   which the message came
     * @param message - the message to be processed
     */
    public void onMessage(String clientId, Message message);

}