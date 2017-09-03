/**
 * This is a wrapper class for building the document set
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;
import java.io.File;
import java.io.IOException;

import com.summarizer.core.DocTermMap;
import com.summarizer.core.Document;
import com.summarizer.core.TfIdfVector;

public class DocumentBuilder {
	
	/**
	 * This method lists the text files containing the news articles and 
	 * uses other methods to build the document set and the Tf-Idf vectors
	 * @param corpusPath
	 * @throws IOException
	 */
	public void buildDocumentSet(String corpusPath) throws IOException {
		File directory = new File(corpusPath);
		File[] filesList = directory.listFiles();
		
		//Put content of each file in the Document data structure
		for(File file : filesList) {
			Document document = new Document();
			document.populateDocumentInstance(file.getAbsolutePath());
		}
		
		//Build tf-idf vector for each document
		for(Document document : DocTermMap.docMap.keySet()){
			TfIdfVector tfIdfVector = new TfIdfVector(document);
			tfIdfVector.buildTfIdfVector();
			document.setTfIdfVector(tfIdfVector);
		}
	}
}
