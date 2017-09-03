/**
 * This class maintains the data structures that are used across
 * the application to store documents and terms.
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DocTermMap {
	protected static int termIdCounter;
	protected static int docIdCounter;
	protected static Map<Document, Integer> docMap = new LinkedHashMap<> ();
	protected static Map<Term, Integer> termMap = new LinkedHashMap<> ();
	protected static Map<Term, Set<Integer>> invertedIndex = new HashMap<> ();
	
	/**
	 * This method puts a new document in the docMap map
	 * @param document
	 * @param docId
	 */
	public static void putNewDocumentInMap(Document document, int docId) {
		docMap.put(document, docId);
	}
	
	/**
	 * This method puts a new term in the termMap map
	 * @param term
	 * @param termId
	 */
	public static void putNewTermInMap(Term term, int termId) {
		termMap.put(term, termId);
	}
	
	/**
	 * This method puts a new term in the inverted index
	 * @param term
	 * @param docId
	 */
	public static void putTermInIndex(Term term, int docId) {
		if(invertedIndex.containsKey(term)) {
			invertedIndex.get(term).add(docId);
		} else {
			Set<Integer> docIdSet = new HashSet<> ();
			docIdSet.add(docId);
			invertedIndex.put(term, docIdSet);
		}
	}
	
	/**
	 * This method returns the total number of documents
	 * @return
	 */
	public static int getDocSetSize() {
		return docMap.size();
	}
	
	/**
	 * This method returns the total number of terms
	 * @return
	 */
	public static int getTermSetSize() {
		return termMap.size();
	}
	
}
