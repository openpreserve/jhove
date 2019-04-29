/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 *
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class defines enumerated types for the analog/digital
 * flag of AESAudioMetadata.
 * Applications will not create or modify instances of this class, but will
 * use one of the predefined AnalogDigitalFlagType instances.
 * 
 * @author Gary McGath
 *
 */
public enum AnalogDigitalFlagType {

	/** Enumeration instance for analog data */
	ANALOG("ANALOG"),
	/** Enumeration instance for physical digital data */
	PHYS_DIGITAL("PHYS_DIGITAL"),
	/** Enumeration instance for FILE digital data */
	FILE_DIGITAL("FILE_DIGITAL");
	/** A String value for the type */
	public final String value;

	private AnalogDigitalFlagType(final String value) {
		this.value = value;
	}

}
