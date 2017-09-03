/**
 * This class helps to exract the application properties.
 * @author Arjun Bhattacharya
 */
package com.summarizer.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SummarizerProperties {
	List<String> propertyList;
	InputStream inputStream;
 
	/**
	 * This method reads the config.properties file and returns
	 * the property values in a list.
	 * @return
	 * @throws IOException
	 */
	public List<String> getPropValues() throws IOException {
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			}
 
			String projPath = prop.getProperty("projPath");
			String cosineThreshold = prop.getProperty("cosineThreshold");
			String jaccardThreshold = prop.getProperty("jaccardThreshold");
			String chunkSize = prop.getProperty("chunkSize");
			String apiUrl = prop.getProperty("apiUrl");
			String apiKey1 = prop.getProperty("apiKey1");
			String apiKey2 = prop.getProperty("apiKey2");
 
			propertyList = new ArrayList<> ();
			propertyList.add(projPath);
			propertyList.add(cosineThreshold);
			propertyList.add(jaccardThreshold);
			propertyList.add(chunkSize);
			propertyList.add(apiUrl);
			propertyList.add(apiKey1);
			propertyList.add(apiKey2);
									
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		} finally {
			if(inputStream != null)
				inputStream.close();
		}
		return propertyList;
	}
}
