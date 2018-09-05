package nBayes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Bayes {
	
	private String trainingFile;
	
	static String register;
	static String output;
	
	private String testFile;
	
	
	private ArrayList<String[]> list;
	private ArrayList<String[]> testlist;
	private HashMap<String, Integer> key; 
	private HashSet<String> values;
	
	private ArrayList<Attribute> list2;
	
	private int corr;
	private int incorr;
	
	
	
	public static void main(String[] args) throws IOException {
		long time1 = System.currentTimeMillis();	
		
		Bayes naiveBayes = new Bayes();
		
		String textfile1 = args[0];
		String textfile2= args[1];
		output=args[2];
		
		 String[] arguments = new String[2];
	        if (args.length == 0 || args == null) {
	            arguments[0] = textfile1;
	            arguments[1] = textfile2;
	        } else {
	            arguments = args;
	        }
		
		naiveBayes.trainingFile = textfile1;
		
		naiveBayes.testFile = textfile2; 
		
		naiveBayes.data();
		
		naiveBayes.attributes();
		
		naiveBayes.tester();
		
		naiveBayes.org();
			
		
		System.out.println("\nAccuracy is: "+ (double)naiveBayes.corr/(double)(naiveBayes.corr+naiveBayes.incorr));
		
		System.out.println("\nTotal time: " + ((double) (System.currentTimeMillis() - time1)/1000) + " seconds");
		
	}

	private void attributes() throws IOException {

		list2 = new ArrayList<Attribute>();
		for(int i = 0; i<list.get(0).length-1; i++)
			list2.add(new Attribute());
		
		for(int i = 0; i<list.size(); i++) {
			for(int j=0; j<list.get(0).length-1; j++)
				list2.get(j).addAttribute(list.get(i)[j+1], list.get(i)[0]);
		}
		
	}
	
	
	private void data() throws IOException {
		
		list = new ArrayList<String[]>();
		key = new HashMap<String, Integer>();
		values = new HashSet<String>();
		Scanner scan = new Scanner(new File(trainingFile));
		
		while(scan.hasNextLine()) {
			
			String header = scan.nextLine();
			String rec[]  = header.split("	");
			list.add(rec);
			
			if(key.containsKey(rec[0]))
				key.put(rec[0], key.get(rec[0])+1);
			else
				key.put(rec[0], 1);
			
			values.add(rec[0]);
			
		}
		scan.close();
	}
	private void tester() throws IOException {
		
		testlist = new ArrayList<String[]>();
		Scanner scan = new Scanner(new File(testFile));
		
		while(scan.hasNextLine()) {
			String header = scan.nextLine();
			String rec[]  = header.split("	");
			testlist.add(rec);
		}
		scan.close();
	
	}

	private void org() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		
		String[] arr = new String[values.size()]; 
		values.toArray(arr);
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(output), "utf-8"))) {
	 
		for(String[] record : testlist) {
			
			Double[] num = new Double[key.size()];
			
			for(int i = 0; i<key.size(); i++){
				num[i] = 1.0;
			}
			
			for(int i = 0; i<record.length-1; i++) {
				
				for(int j = 0; j<values.size(); j++) {
					String label = arr[j];
					num[j] *= (double) list2.get(i).getFrequency(record[i+1], label)/(double)key.get(label);
				}
				
			}
			
			HashMap<Double, String> labelMap = new HashMap<Double, String>();
			
			for(int j = 0; j<values.size(); j++) {
				String label = arr[j];
				num[j] *= (double) key.get(label) / (double) list.size();
				labelMap.put(num[j], label);
			}
			
			Arrays.sort(num);
						
			if(record[0].equals(labelMap.get(num[num.length-1]))) {
				corr++;
				
				System.out.println("Correct classification: " + Arrays.toString(record));
				  writer.write("Correct classification: " + Arrays.toString(record) +"\n");
			}
			else {
				incorr++;
				System.out.println("Incorrect classification: "+labelMap.get(num[num.length-1])+ " " +Arrays.toString(record));
				  writer.write("Incorrect classification: "+labelMap.get(num[num.length-1])+ " " +Arrays.toString(record)+"\n");
			}
			
		}
		}
		
		
	}
	
	
}