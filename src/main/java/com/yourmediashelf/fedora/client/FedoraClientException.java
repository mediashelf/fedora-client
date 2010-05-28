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
	private int status;

	public FedoraClientException(String message) {
        super(message, null);
    }

    public FedoraClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public FedoraClientException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
