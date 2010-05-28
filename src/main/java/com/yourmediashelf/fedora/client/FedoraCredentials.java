package com.yourmediashelf.fedora.client;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Authentication credentials for a Fedora repository instance.
 *
 * @author Edwin Shin
 *
 */
public class FedoraCredentials {
	private URL baseUrl;
	private String username;
	private String password;

	/**
	 *
	 * @param baseUrl e.g., http://localhost:8080/fedora
	 * @param username e.g., fedoraAdmin
	 * @param password e.g., fedoraAdmin
	 */
	public FedoraCredentials(URL baseUrl, String username, String password) {
		// remove any trailing slashes
		String url = baseUrl.toString();
		if (url.endsWith("/")) {
			while (url.endsWith("/") && url.length() > 1) {
				url = url.substring(0, url.length() - 1);
			}
			try {
				baseUrl = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace(); // should never happen
			}
		}
		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
	}

	/**
	 * Convenience constructor that takes the baseUrl as a String.
	 *
	 * @param baseUrl e.g., http://localhost:8080/fedora
     * @param username e.g., fedoraAdmin
     * @param password e.g., fedoraAdmin
	 * @throws MalformedURLException
	 */
	public FedoraCredentials(String baseUrl, String username, String password) throws MalformedURLException {
		this(new URL(baseUrl), username, password);
	}

	/**
	 * Gets the Fedora base url, e.g. "http://example.org:8080/fedora".
	 * The url never includes a trailing slash.
	 *
	 * @return The Fedora base url, e.g. "http://example.org:8080/fedora".
	 */
	public URL getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the Fedora base url, e.g. "http://example.org:8080/fedora".
	 * Any trailing slash will be removed (e.g. "http://example.org/fedora/"
	 * will be replaced with "http://example.org/fedora").
	 *
	 * @param baseUrl
	 */
	public void setBaseUrl(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
