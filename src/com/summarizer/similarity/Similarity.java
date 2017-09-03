/**
 * This interface defines the general behavior of a similarity type. 
 * @author Arjun Bhattacharya
 */
package com.summarizer.similarity;

import com.summarizer.core.Document;

public interface Similarity {
	/**
	 * This method computes the similarity score between two documents.
	 * @param doc1
	 * @param doc2
	 * @return
	 */
	public double getSimilarity(Document doc1, Document doc2);
}
