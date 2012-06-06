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

package com.yourmediashelf.fedora.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Edwin Shin
 */
public class ChecksumUtility {

    public static String checksum(String algorithm, String input)
            throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(input.getBytes("UTF-8"));
        return byteArray2HexString(md.digest());
    }

    public static String checksum(String algorithm, InputStream is)
            throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            byte[] buffer = new byte[1024];
            int read;
            do {
                read = bis.read(buffer);
                if (read > 0) md.update(buffer, 0, read);
            } while (read != -1);
        } finally {
            is.close();
        }
        return byteArray2HexString(md.digest());
    }

    public static String byteArray2HexString(byte[] buf) {
        final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
}
