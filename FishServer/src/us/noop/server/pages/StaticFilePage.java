package us.noop.server.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import us.noop.server.Main;
import us.noop.server.log.Level;
import us.noop.server.response.RequestData;

/**
 * A page that is just a static file
 * @author Ulysses
 * 
 * TODO: Update to work with non-text files
 */
public class StaticFilePage implements Page {
	
	protected byte[] response;
	private String address;
	protected String mimetype;
	private Main instance;
	
	/**
	 * Constructor. A page that responds to the selected address with a static cached file.
	 * @param address - the address to make the page available at
	 * @param f - the file to cache
	 */
	public StaticFilePage(Main instance, String address, File f, String mimetype){
		this.instance = instance;
		if(!f.exists()){
			instance.getLogger().log(Level.HIGH, "Failed to generate static response page for: " + f.getName());
		}else{
			try {
				FileInputStream fs = new FileInputStream(f);
				byte[] data = new byte[(int) f.length()];
				fs.read(data);
				response = data;
				fs.close();
				byte[] header = ("HTTP/1.1 200 OK\r\nContent-Length: " + (response.length) + "\r\nConnection: Closed\r\nContent-Disposition: inline; filename=\"" + f.getName() + "\"\r\nContent-Type: " + 
						mimetype + "\r\n\r\n").getBytes();
				byte[] nr = new byte[response.length + header.length];
				System.arraycopy(header, 0, nr, 0, header.length);//TODO This shit is fucking stupid
				System.arraycopy(response, 0, nr, header.length, response.length);
				response = nr;
			} catch (IOException e) {
				instance.getLogger().logEx(e);
			}
		}
		this.address = address;
		this.mimetype = mimetype;
	}
	
	/**
	 * Returns the static content.
	 * @param input literally anything
	 */
	@Override
	public byte[] getResponse(RequestData req) {
		return response;
	}
	
	/**
	 * Returns the address argument it's supposed to respond to
	 */
	@Override
	public String getAddress() {
		return address;
	}

	/**
	 * Returns the MIME-type of the content returned in <code>getResponse()</code>
	 */
	@Override
	public String mimeType() {
		return mimetype;
	}
}
