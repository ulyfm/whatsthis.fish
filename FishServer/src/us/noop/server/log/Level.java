package us.noop.server.log;

/**
 * An enum of the logging levels for the Logger class.
 * @author Ulysses
 *
 */
public enum Level {
	INFO(0, "INFO"), LOW(1, "LOW_"), MEDIUM(2, "MEDI"), HIGH(3, "HIGH"), CRITICAL(4, "FUCK");
	
	private int val;
	
	private String tag;
	
	private Level(int val, String tag){
		this.tag = tag;
		this.val = val;
	}
	
	/**
	 * Returns the int level (0 = INFO, 4 = CRITICAL)
	 * @return an integer value representing the warning severity level.
	 */
	public int getLevel(){
		return val;
	}
	
	/**
	 * Returns the string name of the level, always 4 characters
	 * @return a string representation 4 letters long
	 */
	public String getTag(){
		return tag;
	}
	
	public static Level getLevelByInt(int i){
		for(Level l : Level.values()){
			if(l.val == i) return l;
		}
		return null;
	}
}
