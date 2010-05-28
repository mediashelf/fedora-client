
package com.yourmediashelf.fedora.client.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.jersey.api.client.ClientResponse;
import com.yourmediashelf.fedora.client.FedoraClientException;

public class GetNextPIDResponse
        extends FedoraResponseImpl {

    private List<String> pids;

    public GetNextPIDResponse(ClientResponse cr)
            throws FedoraClientException {
        super(cr);
    }

    /**
     * The Set of requested pids.
     * <p>If the GetNextPIDs request explicitly set format=html, this method
     * call will fail.</p>
     *
     * @return the Set of next pids
     * @throws FedoraClientException
     */
    public List<String> getPids() throws FedoraClientException {
        if (pids == null) {
            pids = new ArrayList<String>();
            XPath xpath = getXPath();
            NodeList nodes;
            try {
                nodes =
                        (NodeList) xpath
                                .evaluate("/pidList/pid/text()",
                                          new InputSource(getEntityInputStream()),
                                          XPathConstants.NODESET);
            } catch (XPathExpressionException e) {
                throw new FedoraClientException(e.getMessage(), e);
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                pids.add(nodes.item(i).getNodeValue());
            }
        }
        return pids;
    }

    /**
     * Convenience method that returns the first pid returned by the request.
     *
     * @return the first pid returned by the request
     * @throws FedoraClientException
     */
    public String getPid() throws FedoraClientException {
        return getPids().get(0);
    }
}
