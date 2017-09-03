/**
 * This is the concrete class representing Jaccard type of similarity.
 * @author Arjun Bhattacharya
 */
package com.summarizer.similarity;

import java.util.HashSet;
import java.util.Set;

import com.summarizer.core.Document;
import com.summarizer.core.Term;

public class JaccardSimilarity implements Similarity {

	@Override
	public double getSimilarity(Document doc1, Document doc2) {
		Set<Term> termSet1 = doc1.getTermFreqMap().keySet();
		Set<Term> termSet2 = doc2.getTermFreqMap().keySet();
		
		return (double)getIntersectionSize(termSet1, termSet2)/(double)getUnionSize(termSet1, termSet2);
	}
	
	/**
	 * This method computes the size of the overlap between two documents
	 * in terms of number of common terms.
	 * @param termSet1
	 * @param termSet2
	 * @return
	 */
	public int getIntersectionSize(Set<Term> termSet1, Set<Term> termSet2) {
		Set<Term> s = new HashSet<> (termSet1);
		s.retainAll(termSet2);
		return s.size();
	}
	
	/**
	 * This method computes the size of union between two documents.
	 * @param termSet1
	 * @param termSet2
	 * @return
	 */
	public int getUnionSize(Set<Term> termSet1, Set<Term> termSet2) {
		Set<Term> s = new HashSet<> (termSet1);
		s.addAll(termSet2);
		return s.size();
	}

}
