package com.yourmediashelf.fedora.client.response;

import java.io.InputStream;



public interface FedoraResponse {

    /**
     *
     * @return the HTTP Status Code of the response.
     */
    public int getStatus();

    /**
     * <p>Gets the raw response entity.</p>
     *
     * @return the entity response
     */
    public InputStream getEntityInputStream();

    /**
     * <p>Gets the raw response entity.</p>
     *
     * @return the entity response
     */
    public <T> T getEntity(Class<T> c);

}
