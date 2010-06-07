
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
