/**
 *
 */
package edu.harvard.hul.ois.jhove.module.aiff;

/**
 * Enum used to externalise the PDF modules message Strings. Using an enum
 * INSTANCE as a "trick" to ensure a single instance of the class. String
 * constants should be prefixed according to their use in the module:
 * <ul>
 * <li>WRN_ for warning strings, often logger messages.</li>
 * <li>INF_ for informational messages.</li>
 * <li>ERR_ for error messages that indicate a file is invalid or not well
 * formed.</li>
 * </ul>
 * When adding new messages try to adopt the following order for the naming
 * elements:
 * <ol>
 * <li>PREFIX: one of the three prefixes from the list above.</li>
 * <li>ENTITY_NAME: the name of the PDF entity causing the prohlem, e.g. FONT,
 * or DOC.</li>
 * <li>Problem: a short indicator of the problem type, e.g. MISSING, ILLEGAL,
 * etc.</li>
 * </ol>
 * The elements should be separated by underscores. The messages currently don't
 * follow a consistent vocabulary, that is terms such as invalid, illegal, or
 * malformed are used without definition.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 1 Oct 2016:11:38:18
 */

public enum MessageConstants {
	INSTANCE;


	/**
	 * Error messages
	 */
	public static final String ERR_NOT_AIFF_CHU = "Document does not start with AIFF FORM Chunk";
	public static final String ERR_NO_COMMON_CHU = "Document does not contain a Common Chunk";
	public static final String ERR_DOC_MUST_HAVE_V_CHU = "AIFF-C document must contain a Format Version Chunk";

	// public static final String ERR_FORM_CHU_NOT_AAIF_BYTES = _nByte
	public static final String ERR_FORM_CHU_NOT_AAIF_STRING = "File type in Form Chunk is not AIFF or AIFC";
	// , ERR_FORM_CHU_NOT_AAIF_BYTES

	// public static final String ERR_NO_MULTI_CHU_NAME = chunkName
	// public static final String ERR_NO_MULTI_CHU_BYTES = _nByte
	public static final String ERR_NO_MULTI_CHU_STRING_1 = "Multiple ";
	// + ERR_NO_MULTI_CHU_NAME + 
	public static final String ERR_NO_MULTI_CHU_STRING_2 = " Chunks not permitted";
	//, ERR_NO_MULTI_CHU_BYTES

	// public static final String ERR_CHU_WRONG_SIZE_BYTES = //  module.getNByte ()
	public static final String ERR_CHU_WRONG_SIZE_STRING = "Audio Recording Chunk is incorrect size";
	// , ERR_CHU_WRONG_SIZE_BYTES
	
	// public static final String ERR_NO_COMP_TYPE_BYTES =  // module.getNByte()
	public static final String ERR_NO_COMP_TYPE_STRING = "Common Chunk in AIFF-C does not have compression type";
	// , ERR_NO_COMP_TYPE_BYTES
}