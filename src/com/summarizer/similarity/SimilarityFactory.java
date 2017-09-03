/**
 * This class acts as a factory generating Similarity objects.
 * @author Arjun Bhattacharya
 */
package com.summarizer.similarity;

public class SimilarityFactory {
	/**
	 * This method returns an instance of type Similarity depending on the 
	 * type of similarity.
	 * @param simType
	 * @return
	 */
	public static Similarity getSimilarityInstance(SimilarityType simType) {
		if(simType==SimilarityType.COSINE) {
			return new CosineSimilarity();
		} else if (simType==SimilarityType.JACCARD) {
			return new JaccardSimilarity();
		} else {
			throw new IllegalArgumentException("Similarity type not defined.");
		}
	}
}
