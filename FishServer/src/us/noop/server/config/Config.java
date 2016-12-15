package us.noop.server.config;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import us.noop.server.Main;

/**
 * 
 * @author Ulysses
 *
 */
public class Config {
	private Main instance;
	private File config;
	
	private String[] defaultvals = {
			"PORT 80",
			"MIN_LOG_LEVEL 0"
	};
	
	private HashMap<String, String> confmap = new HashMap<String, String>();
	
	public Config(Main instance, File dataDir){
		this.instance = instance;
		if(dataDir == null || (dataDir.exists() && !dataDir.isDirectory())) throw new IllegalArgumentException();
		if(!dataDir.exists()) dataDir.mkdir();
		File c = new File(dataDir, "config.cfg");
		if(!c.exists()){
			try {
				initializeNewFile(c);
			} catch (IOException e) {
				instance.getLogger().logEx(e);
			}
		}
		config = c;
		loadConfig();
	}
	
	private void initializeNewFile(File c) throws IOException {
		c.createNewFile();
		PrintStream ps = new PrintStream(c);
		for(String s : defaultvals){
			ps.println(s);
		}
		ps.close();
	}
	
	public String getString(String key){
		return confmap.get(key);
	}
	
	public double getDouble(String key){
		try{
			double d = Double.parseDouble(getString(key));
			return d;
		}catch(Exception e){
			throw new IllegalArgumentException();
		}
	}
	
	public int getInteger(String key){
		try{
			int i = Integer.parseInt(getString(key));
			return i;
		}catch(Exception e){
			throw new IllegalArgumentException();
		}
	}
	
	public void loadConfig(){
		try{
			Scanner sc = new Scanner(config);
			while(sc.hasNextLine()){
				confmap.put(sc.next().trim(), sc.nextLine().trim());
			}
			sc.close();
		}catch(IOException e){
			instance.getLogger().logEx(e);
		}
	}
}
