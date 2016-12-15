package us.noop.server.pages;

import us.noop.server.response.RequestData;

/**
 * A class that responds to a request
 * @author Ulysses
 *
 */
public interface Page {
	
	/**
	 * The response based on the input
	 * @param input the input, just an address with GET, HTTP/1.1, etc. removed.
	 * @return an appropriate response
	 */
	public byte[] getResponse(RequestData req);
	
	/**
	 * The address that will be responded to.
	 * i.e. /a/b/x
	 * or /a/b
	 * The address must not end with a /, because those are removed automatically to standardize inputs.
	 * @return the address
	 */
	public String getAddress();
	
	public String mimeType();
}
