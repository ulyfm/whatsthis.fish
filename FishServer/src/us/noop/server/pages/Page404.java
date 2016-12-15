package us.noop.server.pages;

import us.noop.server.response.RequestData;
import us.noop.server.response.ResponseManager;

public class Page404 implements Page {

	@Override
	public byte[] getResponse(RequestData req) {
		return ResponseManager.generateHeader(404, "Not Found", "Invalid page.", "text/html").getBytes();
	}

	@Override
	public String getAddress() {
		return null;
	}

	@Override
	public String mimeType() {
		return "text/html";
	}

}
