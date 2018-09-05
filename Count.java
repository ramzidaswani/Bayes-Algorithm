package nBayes;

import java.util.HashMap;

public class Count {

	private HashMap<String, Integer> freq;
	
	public Count(String label) {
		freq = new HashMap<String, Integer>();
		freq.put(label, 1);
	}
	
	public void increment(String label) {
		if(freq.containsKey(label))
			freq.put(label, freq.get(label)+1);
		else
			freq.put(label, 1);
	}
	
	public int getFrequency(String label) {
		if(freq.containsKey(label))
			return freq.get(label);
		else
			return 1;
	}
	
}