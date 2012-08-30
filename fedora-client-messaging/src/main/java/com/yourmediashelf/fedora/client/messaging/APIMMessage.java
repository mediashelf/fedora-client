package com.yourmediashelf.fedora.client.messaging;

import java.util.Date;

public interface APIMMessage extends FedoraMessage {
	
	/**
	 * @return the Base URL of the Fedora Repository that generated the message,
	 *         e.g. http://localhost:8080/fedora
	 */
	public String getBaseUrl();

	/**
	 * @return the PID or null if not applicable for the API-M method
	 */
	public String getPID();

	/**
	 * @return the name of the API-M method invoked
	 */
	public String getMethodName();

	/**
	 * @return the Date object representing the timestamp of the method call
	 */
	public Date getDate();

	/**
	 * @return the return value of the API-M method invoked
	 */
	public String getReturnVal();

	/**
	 * @return the value (as a string) of the parameter of the API-M method invoked.
	 *
	 * Return null if parameter not present.
	 */
	public String getMethodParamVal(String paramName);
	
}
