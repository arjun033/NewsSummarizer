/**
 * This class represents a term in the document. It is
 * the smallest building block of data for the application.
 * @author Arjun Bhattacharya
 */
package com.summarizer.core;

import java.util.Objects;

public class Term {
	private int termId;
	private String strValue;
	
	public Term(String strValue) {
		this.strValue = strValue;
	}
	
	/**
	 * This method returns the term id
	 * @return
	 */
	public int getTermId() {
		return termId;
	}
	
	/**
	 * This method sets the term id
	 * @param termId
	 */
	public void setTermId(int termId) {
		this.termId = termId;
	}
	
	/**
	 * This method returns the string representation
	 * of the term
	 * @return
	 */
	public String getStrValue() {
		return strValue;
	}
	
	/**
	 * This method sets the string value of the term
	 * @param strValue
	 */
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	@Override
	public boolean equals(Object t) {
		Term otherTerm = (Term) t;
		if(Objects.equals(otherTerm.strValue, this.strValue))
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
	    return this.strValue.hashCode();
	}
	
}
