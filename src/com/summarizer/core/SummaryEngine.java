/**
 * This is the heart of the application which is responsible for the 
 * summary generation.
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.util.Set;

public abstract class SummaryEngine {
	String corpusPath;
	String summaryPath;
	
	/**
	 * This method is used to construct the similarity matrix where
	 * each cell M(i,j) is the similarity measure between
	 * documents Di and Dj
	 * @return
	 */
	public abstract double[][] buildSimilarityMatrix();
	
	/**
	 * This method uses the similarity matrix and the threshold values
	 * for different similarity measures to construct a map of each
	 * document and a set of similar documents
	 * @param similarityMatrix
	 * @return
	 */
	public abstract Map<Integer, Set<Integer>> getAdjacencyList(double[][] similarityMatrix);
	
	/**
	 * This method takes the set of nodes to be included in the summary
	 * and prints them in a text file.
	 * @param keepNodes
	 * @throws IOException
	 */
	public abstract void printOutput(Set<Integer> keepNodes) throws IOException;
	
	/**
	 * This is the master method which is responsible for calling different methods
	 * to generate the summary.
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void generateSummary() throws IOException, InterruptedException {
		DocumentBuilder docBuilder = new DocumentBuilder();
		docBuilder.buildDocumentSet(corpusPath);
		
		double[][] similarityMatrix = buildSimilarityMatrix();
		Map<Integer, Set<Integer>> adjacencyList = getAdjacencyList(similarityMatrix);
		
		Set<Integer> removeNodes = new LinkedHashSet<> ();
		for(int node : adjacencyList.keySet()){
			if(!removeNodes.contains(node) && getSetSize(adjacencyList.get(node), removeNodes)>0){
				int maxAdjSize = getSetSize(adjacencyList.get(node), removeNodes);
				int maxAdjNode = node;
				for(int adjNode : adjacencyList.get(node)){
					if(!removeNodes.contains(adjNode) && getSetSize(adjacencyList.get(adjNode), removeNodes)>maxAdjSize){
						maxAdjSize = getSetSize(adjacencyList.get(adjNode), removeNodes);
						maxAdjNode = adjNode;
					}
				}
				removeNodes.addAll(adjacencyList.get(maxAdjNode));
			}
		}
		
		Set<Integer> keepNodes = new LinkedHashSet<> (DocTermMap.docMap.values());
		keepNodes.removeAll(removeNodes);
		
		double[][] docTopicMatrix = buildDocTopicMatrix();
		
		Map<Integer, Double> generalityScore = new LinkedHashMap<> ();
		for(int doc : keepNodes){
			generalityScore.put(doc, getGenerality(docTopicMatrix[doc-1]));
		}
		
		generalityScore.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(
                Map.Entry::getKey, 
                Map.Entry::getValue, 
                (x,y)-> {throw new AssertionError();},
                LinkedHashMap::new
        ));
		
		printOutput(keepNodes);
	}
	
	/**
	 * This method calls external python sources for topic modeling
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public double[][] buildDocTopicMatrix() throws IOException, InterruptedException {
		int rowNum = 0;
		double[][] docTopicMatrix = new double[DocTermMap.getDocSetSize()][5];
		
		ProcessBuilder pb = new ProcessBuilder("/usr/local/Cellar/python3/3.6.0/Frameworks/Python.framework/Versions/3.6/bin/python3","topic_LDA_3.py", corpusPath);
		Process p = pb.start();
		
		BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        p.waitFor();
        
        while ((line = bfr.readLine()) != null){
        	Matcher m = Pattern.compile("\\((.*?)\\)").matcher(line);
        	while(m.find()) {
        	    String[] pyValue = m.group(1).split(",");
        	    docTopicMatrix[rowNum][Integer.parseInt(pyValue[0].trim())]
        	    		= Math.round(Double.parseDouble(pyValue[1].trim())*10000.0)/10000.0;
        	}
        	rowNum++;
        }
        
		return docTopicMatrix;
	}
	
	/**
	 * This method returns the intersection size between sets
	 * @param set
	 * @param removeNodes
	 * @return
	 */
	public int getSetSize(Set<Integer> set, Set<Integer> removeNodes){
		int size = 0;
		if (set.isEmpty()) return size;
		for(int node : set){
			if(!removeNodes.contains(node)){
				size++;
			}
		}
		return size;
	}
	
	/**
	 * This method is used to implement the Shannon Entropy formula
	 * @param topicScore
	 * @return
	 */
	public double getGenerality(double[] topicScore) {
		double score = 0.0;
		
		for(int i=0; i<topicScore.length; i++) {
			score += -topicScore[i]*Math.log(topicScore[i]);
		}
		return score;
	}
	
}
