package us.noop.fd.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.waataja.fooddrop.FoodDonator;
import com.waataja.fooddrop.FoodItem;
import com.waataja.fooddrop.Giveaway;
import com.waataja.fooddrop.Giveaway.GiveawayType;
import static java.lang.Math.toIntExact;

/**
 * 
 * @author Ulysses
 *
 */
public class BigData {
	
	private File dataDir;
	private JSONParser parser;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy mm dd");
	
	private ArrayList<Giveaway> giveaways;//should be a LinkedHashMap
	
	public BigData(File dataDir){
		if(dataDir == null || (dataDir.exists() && !dataDir.isDirectory())) throw new IllegalArgumentException();
		if(!dataDir.exists()) dataDir.mkdir();
		this.dataDir = dataDir;
		parser = new JSONParser();
		loadSavedData();
	}
	
	public ArrayList<Giveaway> getGiveaways(){
		return giveaways;
	}
	
	public void loadSavedData(){
		try{
			ArrayList<Giveaway> gs = new ArrayList<Giveaway>();
			File s = new File(dataDir, "save");
			donators = new HashMap<Integer, ArrayList<FoodDonator>>();
			if(!s.exists() || s.length() == 0){
				giveaways = gs;
				return;
			}
			JSONArray ar = (JSONArray) ((JSONObject) parser.parse(new FileReader(s))).get("saves");
			Iterator<?> it = ar.iterator();
			while(it.hasNext()){
				try{
					gs.add(parseGiveaway((JSONObject) it.next()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			giveaways = gs;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Giveaway parseGiveaway(JSONObject dat) throws FileNotFoundException {
		Giveaway g = new Giveaway(parseDonator((JSONObject) dat.get("donator")), parseDate(dat.get("start")), parseDate(dat.get("end")), parseGiveawayType(dat.get("type")), (String) dat.get("availability"));
		g.setItems(parseItemList((JSONArray) dat.get("items")));
		return g;
	}
	
	private List<FoodItem> parseItemList(JSONArray jsonArray) {
		List<FoodItem> r = new ArrayList<FoodItem>();
		Iterator<?> it = jsonArray.iterator();
		while(it.hasNext()){
			r.add(parseFoodItem((JSONObject) it.next()));
		}
		return r;
	}

	private FoodItem parseFoodItem(JSONObject next) {
		return new FoodItem((String) next.get("name"), (String) next.get("desc"), toIntExact((long) next.get("amount")));
	}

	private GiveawayType parseGiveawayType(Object object) {
		String t = (String) object;
		switch(t){
		case "ANY": return GiveawayType.ANY;
		case "FOODBANK": return GiveawayType.FOODBANK;
		case "PEOPLE": return GiveawayType.PEOPLE;
		default: return null;
		}
	}

	@SuppressWarnings("deprecation")
	private GregorianCalendar parseDate(Object object) {
		try {
			Date d = dateFormat.parse((String) object);
			return new GregorianCalendar(d.getYear() + 1900, d.getMonth(), d.getDay());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<Integer, ArrayList<FoodDonator>> donators;
	
	public FoodDonator parseDonator(JSONObject donator){
		String name = (String) donator.get("name");
		String address = (String) donator.get("address");
		String desc = (String) donator.get("desc");
		double lat = (Double) donator.get("lat");
		double lng = (Double) donator.get("lon");
		int hash = (name + address + desc).hashCode();
		FoodDonator ff = new FoodDonator(name, address, desc, lat, lng);
		if(donators.keySet().contains(hash)){
			for(FoodDonator fd : donators.get(hash)){
				if(fd.getAddress().equals(address) && fd.getDescription().equals(desc) && fd.getName().equals(name)){
					return fd;
				}
			}
			donators.get(hash).add(ff);
			return ff;
		}else{
			ArrayList<FoodDonator> fffff = new ArrayList<FoodDonator>();
			fffff.add(ff);
			donators.put(hash, fffff);
			return ff;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void saveGiveaways(){
		try {
			File saves = new File(dataDir, "save");
			saves.createNewFile();
			JSONArray gives = new JSONArray();
			for(Giveaway g : giveaways){
				gives.add(jsonGiveaway(g));
			}
			JSONObject obj = new JSONObject();
			obj.put("saves", gives);
			FileWriter fw = new FileWriter(saves);
			fw.write(obj.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject jsonGiveaway(Giveaway g) {
		JSONObject ts = new JSONObject();
		ts.put("items", jsonItemList(g.getItems()));
		ts.put("start", dateString(g.getStart()));
		ts.put("end", dateString(g.getEnd()));
		ts.put("availability", g.getAvailability());
		ts.put("donator", jsonDonator(g.getDonator()));
		ts.put("type", jsonGiveawayType(g.getType()));
		return ts;
	}
	
	private String jsonGiveawayType(GiveawayType g) {
		switch(g){
		case PEOPLE:
			return "PEOPLE";
		case FOODBANK:
			return "FOODBANK";
		case ANY:
			return "ANY";
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject jsonDonator(FoodDonator donator) {
		JSONObject jo = new JSONObject();
		jo.put("name", donator.getName());
		jo.put("address", donator.getAddress());
		jo.put("desc", donator.getDescription());
		jo.put("lat", donator.getLatitude());
		jo.put("lon", donator.getLongitude());
		return jo;
	}

	@SuppressWarnings("unchecked")
	private JSONArray jsonItemList(List<FoodItem> items) {
		JSONArray r = new JSONArray();
		for(FoodItem f : items){
			r.add(jsonFoodItem(f));
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	private JSONObject jsonFoodItem(FoodItem f) {
		JSONObject jo = new JSONObject();
		jo.put("name", f.getName());
		jo.put("desc", f.getDescription());
		jo.put("amount", f.getAmount());
		return jo;
	}
	
	public String dateString(GregorianCalendar g){
		return g.get(GregorianCalendar.YEAR) + " " + g.get(GregorianCalendar.MONTH) + " " + g.get(GregorianCalendar.DAY_OF_MONTH);
	}
}
