/**
 * This class represents the implementation of SummaryEngine for
 * Cosine Similarity
 * @author Arjun Bhattacharya
 */

package com.summarizer.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.summarizer.app.NewsSummarizerApp;
import com.summarizer.similarity.Similarity;
import com.summarizer.similarity.SimilarityFactory;
import com.summarizer.similarity.SimilarityType;

public class CosineSummaryEngine extends SummaryEngine {
	
	public CosineSummaryEngine(String corpusPath, String summaryPath) {
		this.corpusPath = corpusPath;
		this.summaryPath = summaryPath;
	}
	
	@Override
	public Map<Integer, Set<Integer>> getAdjacencyList(double[][] similarityMatrix) {
		Map<Integer, Set<Integer>> adjacencyList = new LinkedHashMap<> ();
		
		for(int i=0; i<similarityMatrix.length; i++){
			for(int j=0; j<similarityMatrix[i].length; j++){
				if(i!=j && similarityMatrix[i][j] >= NewsSummarizerApp.cosineThreshold) {
					if(adjacencyList.containsKey(i)){
						adjacencyList.get(i).add(j);
					} else {
						Set<Integer> nodesListCosine = new LinkedHashSet<> ();
						nodesListCosine.add(j);
						adjacencyList.put(i, nodesListCosine);
					}
				}
			}
		}
		return adjacencyList;
	}
	
	@Override
	public double[][] buildSimilarityMatrix() {
		int dimension = DocTermMap.getDocSetSize();
		double[][] simMatrix = new double[dimension][dimension];
		Similarity sim = SimilarityFactory.getSimilarityInstance(SimilarityType.COSINE);
		int i=0;
		for(Document doc1 : DocTermMap.docMap.keySet()){
			int j=0;
			for(Document doc2 : DocTermMap.docMap.keySet()){
				simMatrix[i][j] = Math.round(sim.getSimilarity(doc1, doc2)*100.0)/100.0;
				j++;
			}
			i++;
		}
		return simMatrix;
	}
	
	@Override
	public void printOutput(Set<Integer> keepNodes) throws IOException{
		StringBuilder output = new StringBuilder();
		for(int docId : keepNodes){
			for(Document doc : DocTermMap.docMap.keySet()){
				if(docId == doc.getDocId())
					output.append("\n\n"+doc.getTextContent());
			}
		}
		int sim = (int)(NewsSummarizerApp.cosineThreshold*10);
		String fileName = this.summaryPath+"/Summary_"+Integer.toString(sim)+"_"+SimilarityType.COSINE.toString()+".txt";
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		writer.print(output.toString());
		writer.close();
		System.out.println("Summary generated in: \n"+fileName);
	}
}
