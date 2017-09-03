/**
 * The main application which is rum to generate the summary.
 * @author Arjun Bhattacharya
 */

package com.summarizer.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.summarizer.core.SummaryEngine;
import com.summarizer.core.SummaryEngineFactory;
import com.summarizer.search.NewsSearch;
import com.summarizer.similarity.SimilarityType;
import com.summarizer.utils.SummarizerProperties;

public class NewsSummarizerApp {
	public static String projPath;
	public static String apiKey;
	public static String apiUrl;
	public static double cosineThreshold;
	public static double jaccardThreshold;
	public static int chunkSize;
	
	public static void main(String[] args) {
		//Loading properties file
		try {
			NewsSummarizerApp.getSummarizerProps();
		} catch (IOException e) {
			System.out.println("Properties file could not be loaded. Error:"+e.getMessage());
			System.exit(0);
		}
		
		//Accept query string from user
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the query: ");
		String query = sc.nextLine();
		sc.close();
		
		//Creating directories to store results
		String summaryPath = projPath+query;
		String corpusPath = projPath+query+"/source_"+Integer.toString(chunkSize);
		new File(projPath).mkdir();
		new File(summaryPath).mkdir();
		new File(corpusPath).mkdir();
		
		//Getting article URLs from API
		NewsSearch search = new NewsSearch();
		Set<URL> urlSet = search.getNewsArticles(query);
		
		//Generating documents from news articles
		search.writeContent(urlSet, chunkSize, projPath+query+"/source_"+Integer.toString(chunkSize));
		
		//Generate the summary
		SummaryEngine cosineEngine = SummaryEngineFactory.getSummaryEngineInstance(SimilarityType.COSINE, corpusPath, summaryPath);
		SummaryEngine jaccardEngine = SummaryEngineFactory.getSummaryEngineInstance(SimilarityType.JACCARD, corpusPath, summaryPath);
		try {
			cosineEngine.generateSummary();
			jaccardEngine.generateSummary();
		} catch (IOException | InterruptedException e) {
			System.out.println("Could not generate summary. Error:"+e.getMessage());;
		}
		
	}
	
	/**
	 * This method extracts the application properties from the config.properties file
	 * @throws IOException
	 */
	public static void getSummarizerProps() throws IOException {
		SummarizerProperties props = new SummarizerProperties();
		List<String> propList = props.getPropValues();
		NewsSummarizerApp.projPath = propList.get(0);
		NewsSummarizerApp.cosineThreshold = Double.parseDouble(propList.get(1));
		NewsSummarizerApp.jaccardThreshold = Double.parseDouble(propList.get(2));
		NewsSummarizerApp.chunkSize = Integer.parseInt(propList.get(3));
		NewsSummarizerApp.apiUrl = propList.get(4);
		NewsSummarizerApp.apiKey = propList.get(5);
	}
}
