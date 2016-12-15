package us.noop.server.response;

public class ResponseThread extends Thread {
	private long lastResponse = Long.MAX_VALUE;
	private boolean destroy = false;
	private ResponseManager rm;
	
	private Response r;
	
	public ResponseThread(ResponseManager rm, Response r){
		this.rm = rm;
		setResponse(r);
	}
	
	public void setDestroy(boolean b){
		synchronized(this){
			destroy = b;
		}
	}
	
	public long getLastTime(){
		return lastResponse;
	}
	
	public void setResponse(Response r){
		synchronized(this){
			this.r = r;
		}
	}
	public Response getResponse(){
		return r;
	}
	
	@Override
	public void run(){
		r.run();
	}
}
