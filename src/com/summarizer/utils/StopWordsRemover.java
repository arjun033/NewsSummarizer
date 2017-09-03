/**
 * This utility class is used to remove stop words.
 * @author Arjun Bhattacharya
 */
package com.summarizer.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class StopWordsRemover {
	public static Set<String> stopWordsSet;
	
	/**
	 * This method calls the population logic only if the set has
	 * never been populated before.
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getStopWordsSet() throws IOException {
		if(stopWordsSet==null || stopWordsSet.size()==0) {
			StopWordsRemover.populateSet();
		}
		return stopWordsSet;
	}
	
	/**
	 * This method reads a list of stop words from a text file and
	 * populates a set from it.
	 * @throws IOException
	 */
	public static void populateSet() throws IOException {
		stopWordsSet = new HashSet<> ();
		FileReader fr = new FileReader("STOPWORDS.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, " ");
			while(st.hasMoreTokens()){
				String s = st.nextToken();
				stopWordsSet.add(s);
			}
		}
		if(br != null) br.close();
		if(fr != null) fr.close();
	}
	
	
}
