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

import java.util.regex.Pattern;

/**
 * Abstract base class for the *Relationship methods.
 *
 * @author Edwin Shin
 */
public abstract class RelationshipsRequest extends
        FedoraRequest<RelationshipsRequest> {

    protected final String pid;

    public RelationshipsRequest(String subject) {
        pid = SubjectProcessor.getSubjectPID(subject);
        addQueryParam("subject", SubjectProcessor.getSubjectAsUri(subject));
    }

    /**
     * Helper class to get pid from subject and to get URI form of subject.
     * Subject can either be a pid or an info:fedora/ uri
     *
     * Adapted from {@link org.fcrepo.server.management.DefaultManagement.SubjectProcessor}
     */
    private static class SubjectProcessor {

        private static String FEDORA_URI_PREFIX = "info:fedora/";

        private static Pattern pidRegex =
                Pattern.compile("^([A-Za-z0-9]|-|\\.)+:(([A-Za-z0-9])|-|\\.|~|_|(%[0-9A-F]{2}))+$");

        static String getSubjectAsUri(String subject) {
            // if we weren't given a pid, assume it's a URI
            if (!isPid(subject)) {
                return subject;
            }
            // otherwise return URI from the pid
            return toUri(subject);
        }

        static String getSubjectPID(String subject) {
            if (isPid(subject)) {
                return subject;
            }
            // check for info:uri scheme
            if (subject.startsWith(FEDORA_URI_PREFIX)) {
                // pid is everything after the first / to the 2nd / or to the end of the string
                return subject.split("/", 3)[1];

            } else {
                throw new IllegalArgumentException(
                        "Subject URI must be in the " + FEDORA_URI_PREFIX +
                                " scheme.");
            }
        }

        private static boolean isPid(String subject) {
            return pidRegex.matcher(subject).matches();
        }

        private static String toUri(String pidString) {
            return FEDORA_URI_PREFIX + pidString;
        }
    }

}