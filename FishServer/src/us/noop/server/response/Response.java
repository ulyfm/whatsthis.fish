package us.noop.server.response;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.Scanner;

import us.noop.server.Main;
import us.noop.server.log.Level;

/**
 * A response to a client. 
 * Reads request and responds appropriately, perhaps using another class to manage things.
 * @author Ulysses
 *
 */
public class Response implements Runnable {
	
	private OutputStream out;
	private BufferedReader in;
	private int id;
	private String ip;
	private Main instance;
	
	/**
	 * Constructor. Initializes with input and output.
	 * @param out A writer to the client
	 * @param in The client's shit
	 */
	public Response(Main instance, String ip, OutputStream out, BufferedReader in){
		this.instance = instance;
		this.out = out;
		this.in = in;
		
		this.ip = ip;
		id = instance.getResponseManager().nextId();
	}
	
	/**
	 * Starts a response with the given i/o, might work
	 */
	@Override
	public void run() {
		try{
			System.out.println("R: " + id + " initialized from " + ip);
			Scanner in = new Scanner(this.in);
			in.useDelimiter("\r\n\r\n");
			String header = in.next();
			String[] fields = header.split("\r\n");
			RequestData get = new RequestData(ip, fields);
			String val = get.getValue("Content-Length");
			String body = val == null || val.equals("0") ? "" : in.next();
			get.setData(body);
			instance.getLogger().info("R:" + id + " responding to: " + fields[0]);
			out.write(instance.getResponseManager().getResponse(get));
			out.flush();
			in.close();
			out.close();
			instance.getLogger().info("R:" + id + " completed.");
		}catch(Exception e){
			instance.getLogger().log(Level.HIGH, "Error in response id " + id);
			e.printStackTrace();
		}
	}
}
