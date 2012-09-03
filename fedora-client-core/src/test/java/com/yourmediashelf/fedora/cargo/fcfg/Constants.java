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

/**
 * Constants of general utility.
 *
 */
public interface Constants {

    /**
     * The "Fedora Home" directory.
     * <p>
     * This is normally derived from the <code>FEDORA_HOME</code> environment
     * variable, but if defined, the <code>fedora.home</code> system property
     * will be used instead.
     * </p>
     */
    public static final String FEDORA_HOME = FedoraHome.getValue();

    //---
    // Static helpers
    //---

    /**
     * Utility to determine and provide the value of the "Fedora Home" constant.
     */
    static class FedoraHome {

        private static String value;

        /**
         * Determines the value of "Fedora Home" based on the
         * <code>servlet.fedora.home</code> system property (checked first)
         * <code>fedora.home</code> system property (checked next) or the
         * <code>FEDORA_HOME</code> environment variable (checked last).
         * <p>
         * Once successfully determined, the value is guaranteed not to change
         * during the life of the application.
         *
         * @returns the value, or <code>null</code> if undefined in any way.
         */
        public static final String getValue() {
            if (value == null) {
                if (System.getProperty("servlet.fedora.home") != null) {
                    value = System.getProperty("servlet.fedora.home");
                } else if (System.getProperty("fedora.home") != null) {
                    value = System.getProperty("fedora.home");
                } else {
                    value = System.getenv("FEDORA_HOME");
                }
            }
            return value;
        }
    }

}
