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
	 * Info messages
	 */
	public static final String INF_CHUNK_TYPE_IGNORED = "Ignored chunk type with ID: ";
	/**
	 * Error messages
	 */
// 	public static final String ERR_NOT_AIFF_CHU = "Document does not start with AIFF FORM Chunk";
//	public static final String ERR_COMMON_CHUNK_MISS = "Document does not contain a Common Chunk";
//	public static final String ERR_FORMAT_VER_CHUNK_MISS = "AIFF-C document must contain a Format Version Chunk";
	public static final String ERR_EOF_UNEXPECTED = "Unexpected EOF";
//	public static final String ERR_FORM_CHUNK_NOT_AAIF = "File type in Form Chunk is not AIFF or AIFC";
//	public static final String ERR_MULTI_CHUNK_NOT_PERM = "Multiple ";
//	public static final String ERR_MULTI_CHUNK_NOT_PERM_2 = " Chunks not permitted";
//	public static final String ERR_REC_CHUNK_WRONG_SIZE = "Audio Recording Chunk is incorrect size";
//	public static final String ERR_COMMON_CHUNK_NO_COMP_TYPE = "Common Chunk in AIFF-C does not have compression type";
}