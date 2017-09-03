/**
 * This is the concrete class representing Cosine Similarity.
 * @author Arjun Bhattacharya
 */
package com.summarizer.similarity;

import com.summarizer.core.Document;
import com.summarizer.core.TfIdfVector;

public class CosineSimilarity implements Similarity {
	
	@Override
	public double getSimilarity(Document doc1, Document doc2) {
		TfIdfVector v1 = doc1.getTfIdfVector();
		TfIdfVector v2 = doc2.getTfIdfVector();
		double similarity = (v1.getDotProduct(v2))/(v1.getL2Norm()*v2.getL2Norm());
		return similarity;
	}
}
