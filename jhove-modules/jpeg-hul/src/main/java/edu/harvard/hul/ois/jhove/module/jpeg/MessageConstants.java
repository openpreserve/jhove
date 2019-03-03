/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.jpeg;

/**
 * Enum used to externalise the JPEG modules message Strings. Using an enum
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
 * <li>ENTITY_NAME: the name of the entity causing the problem.</li>
 * <li>Problem: a short indicator of the problem type, e.g. MISSING, ILLEGAL,
 * etc.</li>
 * </ol>
 * The elements should be separated by underscores. The messages currently don't
 * follow a consistent vocabulary, that is terms such as invalid, illegal, or
 * malformed are used without definition.
 * 
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Oct 2016:20:54:20
 */

public enum MessageConstants {
	INSTANCE;
	
	public static final String INF_EXIF_REPORT_REQUIRES_TIFF = "TIFF-HUL module required to report Exif data";
	public static final String INF_EXIF_APP2_MULTI_REPORT = "ICCProfile in multiple APP2 segments; not handled by JPEG-hul";
	
//	public static final String ERR_DTT_SEG_MISSING_PREV_DTI = "DTT segment without previous DTI";
//	public static final String ERR_EOF_UNEXPECTED = "Unexpected end of file";
//	public static final String ERR_EXIF_PROCESSING_IO_EXCEP = "I/O exception processing Exif metadata: ";
//	public static final String ERR_HEADER_INVALID = "Invalid JPEG header";
//	public static final String ERR_ICCPROFILE_INVALID = "Invalid ICCProfile in APP2 segment; message ";
//	public static final String ERR_JFIF_APP_MARKER_MISSING = "JFIF APP0 marker not at beginning of file";
//	public static final String ERR_MARKER_INVALID = "Marker not valid in context";
//	public static final String ERR_MARKER_MISSING = "Expected marker byte 255, got ";
//	public static final String ERR_SPIF_MARKER_MISSING = "SPIFF marker not at beginning of file";
//	public static final String ERR_START_SEGMENT_MISSING = "File does not begin with SPIFF, Exif or JFIF segment";
//	public static final String ERR_TEMP_FILE_CREATION = "Error creating temporary file. Check your configuration: ";
//	public static final String ERR_TILING_DATA_UNRECOGNISED = "Unrecognized tiling data";
}
