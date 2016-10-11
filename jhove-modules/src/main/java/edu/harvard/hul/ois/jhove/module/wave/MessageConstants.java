/**
 *
 */
package edu.harvard.hul.ois.jhove.module.wave;

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
 * Information messages
 */
public static final String ERR_NOT_START_RIFF = "Document does not start with RIFF chunk"
public static final String ERR_NOT_WAVE_HEAD_BYTES = _nByte
public static final String ERR_NOT_WAVE_HEAD_STRING = "File type in RIFF header is not WAVE " + ERR_NOT_WAVE_HEAD_BYTES 
public static final String ERR_UNEX_EOF_BYTES = _nByte
public static final String ERR_UNEX_EOF_BYTES_STRING = "Unexpected end of file " + ERR_UNEX_EOF_BYTES
public static final String ERR_EX_READ_NAME =  // e.getClass().getName() 
public static final String ERR_EX_READ_MESSAGE = // e.getMessage()
public static final String ERR_EX_READ_BYTES = _nByte
public static final String ERR_EX_READ_STRING = "Exception reading file: " + ERR_EX_READ_NAME  + ", " + ERR_EX_READ_MESSAGE, ERR_EX_READ_BYTES
public static final String ERR_INV_CHU_SIZE_BYTES = _nByte
public static final String ERR_INV_CHU_SIZE_STRING = "Invalid chunk size", ERR_INV_CHU_SIZE_BYTES 
public static final String ERR_MULTI_CHU_NO_PERM_NAME = chunkName
public static final String ERR_MULTI_CHU_NO_PERM_BYTES = _nByte
public static final String ERR_MULTI_CHU_NO_PERM_STRING = "Multiple " + ERR_MULTI_CHU_NO_PERM_NAME + " Chunks not permitted", ERR_MULTI_CHU_NO_PERM_BYTES
public static final String ERR_UNKNO_LIST_TYPE_IN_CHU_TYPE = typeID
public static final String ERR_UNKNO_LIST_TYPE_IN_CHU_BYTES = // _module.getNByte()
public static final String ERR_UNKNO_LIST_TYPE_IN_CHU_STRING = "Unknown list type in Associated Data List Chunk", "Type = " + ERR_UNKNO_LIST_TYPE_IN_CHU_TYPE , ERR_UNKNO_LIST_TYPE_IN_CHU_BYTES 
public static final String INF_ASSOC_CHU_IGNOR_ID = id
public static final String INF_ASSOC_CHU_IGNOR_STRING = "Chunk type '" + INF_ASSOC_CHU_IGNOR_ID + "' in Associated Data Chunk ignored"
public static final String ERR_EXIF_USER_COMM__TOO_SHORT = "Exif User Comment Chunk is too short"
public static final String ERR_INCOR_LEN_EXIF_V_CHU = "Incorrect length for Exif Version Chunk"
public static final String JHO_ERR_IN_FORMAT_CHU_NAME = //e.getClass().getName()
public static final String JHO_ERR_IN_FORMAT_CHU_STRING = "Error in FormatChunk: " + JHO_ERR_IN_FORMAT_CHU_NAME
public static final String ERR_SAX_EXC_READ_LINK_CHU = "SAXException in reading Link Chunk"
public static final String ERR_PARS_EXC_READ_LINK_CHU = "ParserConfigurationException in reading Link Chunk"
public static final String ERR_UNKN_LIST_TYPE_ID = typeID
public static final String ERR_UNKN_LIST_TYPE_NAME = // _module.getNByte()
public static final String ERR_UNKN_LIST_TYPE_STRING = "Unknown list type " + ERR_UNKN_LIST_TYPE_ID + " in List Chunk", ERR_UNKN_LIST_TYPE_NAME
public static final String INF_CHU_IGNOR_LIST_ID = id
public static final String INF_CHU_IGNOR_LIST_STRING = "Chunk type '" + INF_CHU_IGNOR_LIST_ID + "' in List Info Chunk ignored"
public static final String INF_CHU_IGNOR_ASS_ID = id
public static final String INF_CHU_IGNOR_ASS_STRING = "Chunk type '" + INF_CHU_IGNOR_ASS_ID + "' in Associated Data Chunk ignored"
public static final String INF_CHU_IGNOR_LIST_CHU_ID = _chunkID
public static final String INF_CHU_IGNOR_LIST_CHU_STRING = "Chunk type '" + INF_CHU_IGNOR_LIST_CHU_ID + "' in List Info Chunk ignored"
public static final String ERR_INVA_PEAK_VALUE = "Invalid format value in Peak Envelope Chunk"
public static final String ERR_INVA_PPV_PEAK_ENV_CHU = "Invalid pointsPerValue in Peak Envelope Chunk"
