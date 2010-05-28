package com.yourmediashelf.fedora.client.response;

import java.io.InputStream;



public interface FedoraResponse {

    public int getStatus();

    public InputStream getEntityInputStream();

    public <T> T getEntity(Class<T> c);

}
