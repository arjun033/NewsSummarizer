/**
 * This is the factory class encapsulating summary engine instance generation.
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;

import com.summarizer.similarity.SimilarityType;

public class SummaryEngineFactory {
	
	/**
	 * This method returns a summary engine instance depending on the type of
	 * similarity measure.
	 * @param simType
	 * @param corpusPath
	 * @param summaryPath
	 * @return
	 */
	public static SummaryEngine getSummaryEngineInstance(SimilarityType simType, String corpusPath, String summaryPath) {
		if(simType==SimilarityType.COSINE) {
			return new CosineSummaryEngine(corpusPath, summaryPath);
		} else if (simType==SimilarityType.JACCARD) {
			return new JaccardSummaryEngine(corpusPath, summaryPath);
		} else {
			throw new IllegalArgumentException("Similarity type not defined.");
		}
	}
}

