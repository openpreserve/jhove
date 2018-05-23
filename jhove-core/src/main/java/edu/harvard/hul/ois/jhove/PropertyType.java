/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2009 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated types for an Property of
 * some given content.
 * Applications will not create or modify PropertyTypes, but will
 * use one of the predefined PropertyType instances
 * BOOLEAN, BYTE, CHARACTER, DATE, DOUBLE, FLOAT, INTEGER,
 * LONG, OBJECT, PROPERTY, SHORT, STRING, RATIONAL, or
 * NISOIMAGEMETADATA.
 *
 * @see Property
 */
public enum PropertyType {
	/**
	 * Property type for a <code>Boolean</code> object, or a
	 * <code>boolean</code> if the Arity is Array.
	 */
	BOOLEAN("Boolean"),
	/**
	 * Property type for a <code>Byte</code> object, or a <code>byte</code>
	 * if the Arity is Array.
	 */
	BYTE("Byte"),
	/**
	 * Property type for a <code>Character</code> object, or a
	 * <code>char</code> if the Arity is Array.
	 */
	CHARACTER("Character"),
	/**
	 * Property type for a <code>Date</code> object.
	 */
	DATE("Date"),
	/**
	 * Property type for a <code>Double</code> object, or
	 * a <code>double</code> if the Arity is Array.
	 */
	DOUBLE("Double"),
	/**
	 * Property type for a <code>Float</code> object, or a
	 * <code>float</code> if the Arity is Array.
	 */
	FLOAT("Float"),
	/**
	 * Property type for an <code>Integer</code> object, or an
	 * <code>integer</code> if the Arity is Array.
	 */
	INTEGER("Integer"),
	/**
	 * Property type for a <code>Long</code> object, or a
	 * <code>long</code> if the Arity is Array.
	 */
	LONG("Long"),
	/**
	 * Property type for an <code>Object</code>.
	 */
	OBJECT("Object"),
	/**
	 * Property type for an <code>AESAudioMetadata</code>.
	 */
	AESAUDIOMETADATA("AESAudioMetadata"),
	/**
	 * Property type for a <code>NisoImageMetadata</code>.
	 */
	NISOIMAGEMETADATA("NISOImageMetadata"),
	/**
	 * Property type for a <code>TextMDMetadata</code>.
	 */
	TEXTMDMETADATA("TextMDMetadata"),
	/**
	 * Property type for a <code>Property</code> object.
	 */
	PROPERTY("Property"),
	/**
	 * Property type for a <code>Short</code> object, or a
	 * <code>short</code> if the Arity is Array.
	 */
	SHORT("Short"),
	/**
	 * Property type for a <code>String</code> object.
	 */
	STRING("String"),
	/**
	 * Property type for a <code>Rational</code> object.
	 */
	RATIONAL("Rational");

	/** A String name for the type, used for reporting. */
	public final String name;

	private PropertyType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
