/**
 * 
 */
package com.yourmediashelf.fedora.client;

/**
 * @author Edwin Shin
 *
 */
public class FedoraClientException extends Exception {
	private static final long serialVersionUID = 1L;

	public FedoraClientException(String message) {
        super(message, null);
    }

    public FedoraClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
