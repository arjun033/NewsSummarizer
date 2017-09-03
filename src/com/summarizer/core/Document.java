/**
 * This class represents a document
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.summarizer.utils.stanfordstemmer.Stemmer;
import com.summarizer.utils.StopWordsRemover;

public class Document {
	private int docId;
	private String fileName;
	private String textContent;
	private Map<Term, Integer> termFreqMap;
	private TfIdfVector tfIdfVector;
	
	public void populateDocumentInstance(String fileName) throws IOException {
		this.docId = ++DocTermMap.docIdCounter;
		this.fileName = fileName;
		this.readFileIntoDocument();
		this.extractTermsFromText();
		DocTermMap.putNewDocumentInMap(this, docId);
	}
	
	/**
	 * This method reads a file's contents and stores in a string
	 * @throws IOException
	 */
	public void readFileIntoDocument() throws IOException {
		this.textContent = new String(Files.readAllBytes(Paths.get(this.fileName))); 
	}
	
	/**
	 * This method processes the text of the document and extracts terms from it.
	 * @throws IOException
	 */
	public void extractTermsFromText() throws IOException {
		this.termFreqMap = new HashMap<> ();
		String fileText = this.textContent.replaceAll("[^A-Za-z]", " ").trim().replaceAll(" +", " ").toLowerCase();
		StringTokenizer st = new StringTokenizer(fileText, " ");
		
		while(st.hasMoreTokens()){
			String word = st.nextToken();
			if(StopWordsRemover.getStopWordsSet().contains(word)) {
				continue;
			} else {
				Stemmer stemmer = new Stemmer();
				word = stemmer.stem(word);
				Term term = new Term(word);
				if(!DocTermMap.termMap.containsKey(term)){
					int termId = ++DocTermMap.termIdCounter;
					term.setTermId(termId);
					DocTermMap.putNewTermInMap(term, termId);
				} 
				termFreqMap.put(term, termFreqMap.getOrDefault(term, 0)+1);
				DocTermMap.putTermInIndex(term, docId);
			}
		}
	}
	
	/**
	 * This method returns the document id
	 * @return
	 */
	public int getDocId() {
		return docId;
	}

	/**
	 * This method sets the document id
	 * @param docId
	 */
	public void setDocId(int docId) {
		this.docId = docId;
	}
	
	/**
	 * This method returns the term count of the document
	 * @return
	 */
	public int getTermCount() {
		return termFreqMap.size();
	}
	
	/**
	 * This method returns the Tf-Idf vector corresponding
	 * to the document
	 * @return
	 */
	public TfIdfVector getTfIdfVector() {
		return tfIdfVector;
	}
	
	/**
	 * This method sets the Tf-Idf vector corresponding
	 * to the document
	 * @param tfIdfVector
	 */
	public void setTfIdfVector(TfIdfVector tfIdfVector) {
		this.tfIdfVector = tfIdfVector;
	}
	
	/**
	 * This method returns the text content of the document
	 * @return
	 */
	public String getTextContent() {
		return textContent;
	}
	
	/**
	 * This method sets the text content of the document
	 * @param textContent
	 */
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	
	/**
	 * This method returns the term frequencies in the document
	 * @return
	 */
	public Map<Term, Integer> getTermFreqMap() {
		return termFreqMap;
	}
	
}
