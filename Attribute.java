package nBayes;

import java.util.HashMap;
import java.util.HashSet;

public class Attribute {
	
	private HashMap<String, Count> list;
	
	public Attribute() {
		list = new HashMap<String, Count>();
	}
	
	public void addAttribute(String attr, String label) {
		if(list.containsKey(attr))
			list.get(attr).increment(label);
		else
			list.put(attr, new Count(label));
	}
	
	public int getFrequency(String attr, String label) {
		if(list.containsKey(attr))
			return list.get(attr).getFrequency(label);
		else
			return 1;
	}
	
	
}