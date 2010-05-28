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

    /**
     *
     * @param status the HTTP status code of the error.
     * @param message
     */
    public FedoraClientException(int status, String message) {
        super(message);
        this.status = status;
    }

    /**
     *
     * @return the HTTP status code of the error
     */
    public int getStatus() {
        return status;
    }
}
