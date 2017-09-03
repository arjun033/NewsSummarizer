/**
 * This class represents the vector representation of a document.
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;

import java.util.ArrayList;
import java.util.List;

public class TfIdfVector {
	private Document document;
	private List<Double> tfIdfVector;
	
	public TfIdfVector(Document document) {
		this.document = document;
		this.tfIdfVector = new ArrayList<> ();
	}
	
	/**
	 * This method returns the list containing the
	 * values of the vector
	 * @return
	 */
	public List<Double> getVector() {
		return tfIdfVector;
	}
	
	/**
	 * This method sets the list containing the
	 * values of the vector
	 * @param vector
	 */
	public void setVector(List<Double> vector) {
		this.tfIdfVector = vector;
	}
	
	/**
	 * This method calculates the L2 norm of the
	 * Tf-Idf vector
	 * @return
	 */
	public double getL2Norm() {
		double l2Norm = 0.0;
		double sumOfSquares = 0.0;
		for(double n : this.tfIdfVector) 
			sumOfSquares += Math.pow(n, 2);
		l2Norm = Math.sqrt(sumOfSquares);
		return l2Norm;
	}
	
	/**
	 * This method returns the size of the vector
	 * @return
	 */
	public int getSize() {
		return tfIdfVector.size();
	}
	
	/**
	 * This method calculates the dot product of this
	 * vector with another vector
	 * @param otherVector
	 * @return
	 */
	public double getDotProduct(TfIdfVector otherVector) {
		double dotProduct = 0.0;
		for(int i=0; i<this.getSize() && i<otherVector.getSize(); i++)
			dotProduct += this.getVector().get(i) * otherVector.getVector().get(i);
		return dotProduct;
	}
	
	/**
	 * This method calculates the tf and idf scores and 
	 * builds the vector.
	 */
	public void buildTfIdfVector() {
		int totalDocs = DocTermMap.getDocSetSize();
		int docSize = document.getTermCount();
		for(Term term : DocTermMap.termMap.keySet()) {
			int termFreq = document.getTermFreqMap().getOrDefault(term, 0);
			int numDocsWithTerm = DocTermMap.invertedIndex.get(term).size();
			double tf = (double)termFreq/(double)docSize;
			double idf = 1+(Math.log((double)totalDocs)/(double)numDocsWithTerm);
			tfIdfVector.add(tf*idf);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Size:"+tfIdfVector.size()+" Vector:<");
		for(double n : tfIdfVector)
			sb.append(Double.toString(n)+", ");
		sb.append(">");
		return sb.toString();
	}
	
}
