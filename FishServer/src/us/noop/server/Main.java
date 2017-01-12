package us.noop.server;

import java.io.File;
import java.io.IOException;

import us.noop.server.config.Config;
import us.noop.server.log.Level;
import us.noop.server.log.Logger;
import us.noop.server.response.ResponseManager;

/**
 * The main class of each instance of the application.
 * The start method must be run to initialize the new instance.
 * @author Ulysses
 *
 */
public class Main {
	
	private Logger log;
	private Config conf;
	private Server s;
	private ServerSetup ss;
	
	public Main(ServerSetup ss){
		this.ss = ss;
	}
	
	/**
	 * Initializes server and all required other stuff, starts receiving connections
	 */
	public void start(){
		conf = new Config(this, new File("files"));
		Level l = Level.getLevelByInt(conf.getInteger("MIN_LOG_LEVEL"));
		log = new Logger(l, System.out, System.err);
		log.info("Logger initialized with minimum level " + l);
		log.info("Server starting on port " + conf.getInteger("PORT"));
		try {
			s = new Server(this, conf.getInteger("PORT"));
		} catch (IOException e) {
			log.logEx(e);
		} catch (InterruptedException e) {
			log.logEx(e);
		}
		
		ss.setUpServer(s);
		//PageClassLoader pcl = new PageClassLoader(this, s);
		
		s.run();
	}
	
	/**
	 * Each application instance ought to have its own logger
	 * @return the logger of this instance of the application
	 */
	public Logger getLogger(){
		return log;
	}
	
	/**
	 * 
	 * @return
	 */
	public Config getConfig(){
		return conf;
	}
	/**
	 * The response manager of the active instance
	 * @return the response manager
	 */
	public ResponseManager getResponseManager(){
		return s.getResponseManager();
	}
}
