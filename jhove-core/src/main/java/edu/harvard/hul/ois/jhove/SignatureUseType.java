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
	/** Use type for a required signature */
	MANDATORY("Mandatory"),
	/** Use type for a conditionally required signature. */
	MANDATORY_IF_APPLICABLE("Mandatory if applicable"),
	/** Use type for an optional signature. */
	OPTIONAL("Optional");
	/** A String name for the type, used for reporting. */
	public final String name;

	private SignatureUseType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
