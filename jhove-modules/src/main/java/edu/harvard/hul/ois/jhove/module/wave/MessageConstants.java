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

	public static final String SUB_MESS_TYPE = "Type = ";
	public static final String SUB_MESS_BYTES_MISSING = "Bytes missing = ";
	public static final String SUB_MESS_TRUNCATED_CHUNK = "Truncated chunk = ";

	/**
	 * Information messages
	 */
	public static final String INF_CHU_TYPE_IGND = "Ignored Chunk type: ";
	public static final String INF_CHU_DATA_IGND = "Ignored unexpected data in chunk: ";
	public static final String INF_DATA_CHUNK_TYPE_IGN = "Ignored Associated Data Chunk of type: ";
	public static final String INF_INFO_CHUNK_TYPE_IGN = "Ignored List Info Chunk of type: ";
	public static final String INF_FILE_TOO_LARGE = "File too large to validate";

	/**
	 * Error messages
	 */
	public static final String ERR_CHUNK_DUP = "Duplicate Chunks found for type: ";
	public static final String ERR_EXIF_COMM_TOO_SHORT = "Exif User Comment Chunk is too short";
	public static final String ERR_EXIF_VER_CHUNK_LEN_WRNG = "Incorrect length for Exif Version Chunk";
	public static final String ERR_CHUNK_SIZE_INVAL = "Invalid chunk size";
	public static final String ERR_BWF_VER_UNREC = "Unrecognized BWF version: ";
	public static final String ERR_EOF_UNEXPECTED = "Unexpected end of file";
	public static final String ERR_FILE_IO_EXCEP = "Exception reading file: ";
	public static final String ERR_FMT_CHUNK_MISS = "No Format Chunk";
	public static final String ERR_DATA_CHUNK_MISS = "No Data chunk found";
	public static final String ERR_DATA_BEFORE_FMT = "Data chunk appears before Format chunk";
	public static final String ERR_LINK_CHUNK_SAX_EXCEP = "SAXException in reading Link Chunk";
	public static final String ERR_LINK_CHUNK_PARS_EXCEP = "ParserConfigurationException in reading Link Chunk";
	public static final String ERR_LIST_TYPE_UNK = "Unknown list type in Associated Data List Chunk";
	public static final String ERR_LIST_CHUNK_TYPE_UNK = "List Chunk contains unknown type: ";
	public static final String ERR_PEC_FORMAT_INVAL = "Invalid format value in Peak Envelope Chunk";
	public static final String ERR_PEC_PPV_INVAL = "Invalid pointsPerValue in Peak Envelope Chunk";
	public static final String ERR_RIFF_CHUNK_MISSING = "Document does not start with RIFF chunk";
	public static final String ERR_RIFF_HDR_TYPE_NOT_WAV = "File type in RIFF header is not WAVE ";
	public static final String ERR_DS64_NOT_FIRST_CHUNK = "Data Size 64 chunk not in required location";
}
