
package com.yourmediashelf.fedora.util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Edwin Shin
 * @see "http://www.w3.org/TR/xml-c14n11/#Examples"
 */
public class XmlSerializerTest {

    private final File testDir = new File("src/test/resources/c14n");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = RuntimeException.class)
    public void testCanonicalizeWithLS() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        String control = "<a>b</a>";
        XmlSerializer.canonicalizeWithDOM3LS(XmlSerializer
                .string2document(control), bout);
    }

    @Test
    /**
     * Demonstrates:
     * <ul>
     *  <li>Loss of XML declaration
     *  <li>Loss of DTD
     *  <li>Normalization of whitespace outside of document element (first character of both canonical forms is '<'; single line breaks separate PIs and comments outside of document element)
     *  <li>Loss of whitespace between PITarget and its data
     *  <li>Retention of whitespace inside PI data
     *  <li>Comment removal from uncommented canonical form, including delimiter for comments outside document element (the last character in both canonical forms is '>')
     * </ul>
     * @throws Exception
     */
    public void test31() throws Exception {
        testCanonicalization("3.1");
    }

    @Test
    /**
     * Demonstrates:
     * <ul>
     * <li>Retain all whitespace between consecutive start tags, clean or dirty
     * <li>Retain all whitespace between consecutive end tags, clean or dirty
     * <li>Retain all whitespace between end tag/start tag pair, clean or dirty
     * <li>Retain all whitespace in character content, clean or dirty
     * </ul>
     *
     * @throws Exception
     */
    public void test32() throws Exception {
        testCanonicalization("3.2");
    }

    @Test
    /**
     * @throws Exception
     */
    public void test33() throws Exception {
        testCanonicalization("3.3");
    }

    @Test
    /**
     * @throws Exception
     */
    public void test34() throws Exception {
        testCanonicalization("3.4");
    }

    @Test
    /**
     * @throws Exception
     */
    public void test36() throws Exception {
        testCanonicalization("3.6");
    }

    private void testCanonicalization(String prefix) throws Exception {
        String input = String.format("%s-input.xml", prefix);
        String canonical = String.format("%s-canonical.xml", prefix);
        InputStream in = new FileInputStream(new File(testDir, input));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        XmlSerializer.canonicalize(in, bout);
        String control = readFileAsString(new File(testDir, canonical));
        assertEquals(input + " did not match " + canonical, control, bout
                .toString("UTF-8"));
    }

    private static String readFileAsString(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream f =
                new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        return new String(buffer, "UTF-8");
    }

}
