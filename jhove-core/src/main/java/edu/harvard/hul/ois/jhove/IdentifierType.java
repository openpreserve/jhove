/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated types for an Identifier of a
 * format specification document.
 * Applications will not create or modify IdentifierTypes, but will
 * use one of the predefined IdentifierType instances
 * ANSI, DDC, DOI, ECMA, HANDLE, ISO, ISBN, LC, LCCN,
 * NISO, PII, RFC, SICI, URI, URL, URN, or OTHER.
 *
 * @see Identifier
 */
public enum IdentifierType {
	/** Identifier type for American National Standards Institute. */
	ANSI("ANSI"),
	/** Identifier type for Dewey Decimal Classification. */
	DDC("DDC"),
	/** Identifier type for Digital Object Identifier. */
	DOI("DOI"),
	/** Identifier type for ECMA. */
	ECMA("ECMA"),
	/** Identifier type for CNRI Handle. */
	HANDLE("Handle"),
	/** Identifier type for International Standards Organization. */
	ISO("ISO"),
	/** Identifier type for International Standard Book Number. */
	ISBN("ISBN"),
	/** Identifier type for Library of Congress classification. */
	LC("LC"),
	/** Identifier type for Library of Congress catalogue number. */
	LCCN("LCCN"),
	/** Identifier type for NISO standard number. */
	NISO("NISO"),
	/** Identifier type for Publisher Item Identifier. */
	PII("PII"),
	/** Identifier type for IETF Request for Comment. */
	RFC("RFC"),
	/** Identifier type for Serial Item and Contribution Identifier. */
	SICI("SICI"),
	/** Identifier type for Uniform Resource Identifier. */
	URI("URI"),
	/** Identifier type for Uniform Resource Locator. */
	URL("URL"),
	/** Identifier type for Uniform Resource Name. */
	URN("URN"),
	/** Identifier type for CCITT. */
	CCITT("CCITT"),
	/** Identifier type for International Telecommunication Union. */
	ITU("ITU"),
	/**
	 * Identifier type for Japan Electronics and Information Technology
	 * Industries Association.
	 */
	JEITA("JEITA"),
	/** Identifier type for whatever doesn't fit other categories. */
	OTHER("Other");
	/** A String name for the type, used for reporting. */
	public final String name;

	private IdentifierType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
