package us.noop.fish.data;

public class Fish {
	
	private String commonName;
	private String scientificName;
	
	public Fish(String commonName, String scientificName){
		this.commonName = commonName;
		this.scientificName = scientificName;
	}
	
	public String getCommonName(){
		return commonName;
	}
	
	public String getScientificName(){
		return scientificName;
	}
}
