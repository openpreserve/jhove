/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated types for a Document.
 * Applications will not create or modify DocumentTypes, but will
 * use one of the predefined DocumentType instances
 * ARTICLE, BOOK, REPORT, RFC, STANDARD, WEB, or OTHER.
 *
 * @see Document
 * 
 */
public enum DocumentType {
	/** Document type for a printed article. */
	ARTICLE("Article"),
	/** Document type for an book. */
	BOOK("Book"),
	/** Document type for a report. */
	REPORT("Report"),
	/** Document type for an IETF Request for Comment. */
	RFC("RFC"),
	/** Document type for a standards body publication. */
	STANDARD("Standard"),
	/** Document type for a Web page. */
	WEB("Web"),
	/** Document type that doesn't fit the other categories. */
	OTHER("Other");
	/** A String name for the type, used for reporting. */
	public final String name;

	private DocumentType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
