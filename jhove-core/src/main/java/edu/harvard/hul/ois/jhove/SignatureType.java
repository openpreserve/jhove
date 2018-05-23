/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated types for a Signature in a module.
 * Applications will not create or modify SignatureTypes, but will
 * use one of the predefined SignatureType instances
 * EXTENSION, FILETYPE, or MAGIC.
 *
 * @see Signature
 */
public enum SignatureType {
	/**
	 * Signature type for a file extension, i.e., a sequence of
	 * characters following a period character in a file name.
	 */
	EXTENSION("File extension"),
	/**
	 * Signature type for a Macintosh OS file type. This applies
	 * only to Mac OS files, and is always a four-character code.
	 */
	FILETYPE("Mac OS file type"),
	/**
	 * Signature type for a "magic number" stored in the file.
	 */
	MAGIC("Magic number");
	/** A String name for the type, used for reporting. */
	public final String name;

	private SignatureType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
