/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated use types for a Signature in a module.
 * These give information on whether a signature is required in
 * valid content.
 * Applications will not create or modify SignatureUseTypes, but will
 * use one of the predefined SignatureUseType instances
 * MANDATORY, MANDATORY_IF_APPLICABLE, or OPTIONAL.
 *
 * @see Signature
 */
public enum SignatureUseType {
	/******************************************************************
	 * PRIVATE CLASS FIELDS.
	 ******************************************************************/
	MANDATORY("Mandatory"),
	MANDATORY_IF_APPLICABLE("Mandatory if applicable"),
	OPTIONAL("Optional");
	public final String name;
	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/
	/**
	 * Applications will never create SignatureUseTypes directly.
	 **/
	private SignatureUseType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
