
package com.yourmediashelf.fedora.client.request;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponseImpl;

/**
 * Builder for the Export method.
 *
 * @author Edwin Shin
 *
 */
public class Export
        extends FedoraRequest<Export> {

    private final String pid;

    /**
     * @param pid
     *        persistent identifier of the digital object
     */
    public Export(String pid) {
        this.pid = pid;
    }

    /**
     *
     * @param format The XML format to export. Defaults to
     *               "info:fedora/fedora-system:FOXML-1.1".
     * @return this builder
     */
    public Export format(String format) {
        addQueryParam("format", format);
        return this;
    }

    /**
     *
     * @param context The export context, which determines how datastream URLs
     *        and content are represented. One of "public", "migrate", "archive".
     *        Defaults to "public".
     * @return this builder
     */
    public Export context(String context) {
        addQueryParam("context", context);
        return this;
    }

    /**
     *
     * @param encoding The preferred encoding of the exported XML. Defaults to
     *                 "UTF-8".
     * @return this builder
     */
    public Export encoding(String encoding) {
        addQueryParam("encoding", encoding);
        return this;
    }

    @Override
    public FedoraResponse execute(FedoraClient fedora) throws FedoraClientException {
        WebResource wr = fedora.resource();
        String path = String.format("objects/%s/export", pid);

        return new FedoraResponseImpl(wr.path(path).queryParams(getQueryParams()).get(ClientResponse.class));
    }

}