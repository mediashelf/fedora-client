
package com.yourmediashelf.fedora.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.c14n.Canonicalizer;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Provides various methods for serializing XML.
 *
 * <p>The various canonicalize methods use Apache XML Security to provide
 * canonicalization according to Canonical XML Version 1.1.</p>
 *
 * <p>Note that canonicalization doesn't involve pretty-printing and
 * vice-versa.</p>
 *
 * @author Edwin Shin
 * @see "http://www.w3.org/TR/xml-c14n11"
 */
public class XmlSerializer {

    private final static org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(XmlSerializer.class);

    static {
        // initialize xmlsec
        org.apache.xml.security.Init.init();
    }

    public static void canonicalize(InputStream in, OutputStream out)
            throws Exception {
        Document document = getDocument(in);
        canonicalize(document, out);
    }

    /**
     * Convenience method that takes a String as input.
     *
     * @param input
     * @param out
     * @throws Exception
     */
    public static void canonicalize(String input, OutputStream out)
            throws Exception {
        canonicalize(string2document(input), out);
    }

    /**
     * Canonicalizes the input Document according to Canonical XML version 1.1.
     *
     * @param document
     * @param out
     * @throws Exception
     */
    public static void canonicalize(Document document, OutputStream out)
            throws Exception {
        Canonicalizer c14n =
                Canonicalizer
                        .getInstance(Canonicalizer.ALGO_ID_C14N11_WITH_COMMENTS);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        bout.write(c14n.canonicalizeSubtree(document));
        bout.flush();
    }

    /**
     * This isn't actually supported by any parser I've come across, so it's
     * recommended to use the other canonicalize methods (which use Apache
     * xmlsec)
     *
     * @param document
     * @param out
     */
    public static void canonicalizeWithDOM3LS(Document document, OutputStream out) {
        DOMImplementationLS domImpl = getDOMImplementationLS(document);
        LSSerializer lsSerializer = domImpl.createLSSerializer();
        DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
        if (domConfiguration.canSetParameter("canonical-form",
                                             Boolean.TRUE)) {
            lsSerializer.getDomConfig().setParameter("canonical-form",
                                                     Boolean.TRUE);
            LSOutput lsOutput = domImpl.createLSOutput();
            lsOutput.setEncoding("UTF-8");
            lsOutput.setByteStream(out);
            lsSerializer.write(document, lsOutput);
        } else {
            throw new RuntimeException("DOMConfiguration 'canonical-form' parameter isn't settable.");
        }
    }

    public static void prettyPrintWithDOM3LS(Document document, OutputStream out) {
        DOMImplementationLS domImpl = getDOMImplementationLS(document);
        LSSerializer lsSerializer = domImpl.createLSSerializer();
        DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
        if (domConfiguration.canSetParameter("format-pretty-print",
                                             Boolean.TRUE)) {
            lsSerializer.getDomConfig().setParameter("format-pretty-print",
                                                     Boolean.TRUE);
            LSOutput lsOutput = domImpl.createLSOutput();
            lsOutput.setEncoding("UTF-8");
            lsOutput.setByteStream(out);
            lsSerializer.write(document, lsOutput);
        } else {
            throw new RuntimeException("DOMConfiguration 'format-pretty-print' parameter isn't settable.");
        }
    }

    private static DOMImplementationLS getDOMImplementationLS(Document document) {
        DOMImplementation domImplementation = document.getImplementation();
        if (domImplementation.hasFeature("LS", "3.0")
                && domImplementation.hasFeature("Core", "2.0")) {
            DOMImplementationLS domImplementationLS;
            try {
                domImplementationLS =
                        (DOMImplementationLS) domImplementation
                                .getFeature("LS", "3.0");
            } catch (NoSuchMethodError nse) {
                logger.warn("Caught NoSuchMethodError for "
                        + domImplementation.getClass().getName()
                        + "#getFeature. "
                        + "Trying fallback for DOMImplementationLS.");
                try {
                    domImplementationLS = getDOMImplementationLS();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

            }
            return domImplementationLS;
        } else {
            throw new RuntimeException("DOM 3.0 LS and/or DOM 2.0 Core not supported.");
        }
    }

    private static DOMImplementationLS getDOMImplementationLS()
            throws ClassCastException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        DOMImplementationRegistry registry;
        registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl =
                (DOMImplementationLS) registry.getDOMImplementation("LS");
        return impl;
    }

    /**
     * Get a new DOM Document object from parsing the specified InputStream.
     *
     * @param in
     *        the InputStream to parse.
     * @return a new DOM Document object.
     * @throws ParserConfigurationException
     *         if a DocumentBuilder cannot be created.
     * @throws SAXException
     *         If any parse errors occur.
     * @throws IOException
     *         If any IO errors occur.
     */
    private static Document getDocument(InputStream in)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        // do not load external dtds
        builder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });
        return builder.parse(in);
    }

    protected static byte[] doc2bytes(Node node) {
        try {
            Source source = new DOMSource(node);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return out.toByteArray();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String getStringFromDoc(Document document) {
        DOMImplementationLS domImplementation =
                (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(document);
    }

    protected static Document string2document(String s) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new InputSource(new StringReader(s)));
    }

}
