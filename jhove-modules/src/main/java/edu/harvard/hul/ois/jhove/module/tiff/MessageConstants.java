/**
 *
 */
package edu.harvard.hul.ois.jhove.module.tiff;

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

	private static final String tagMismatch = " mismatch for tag ";

	/**
	 * Error "fill in" strings
	 */
	public static final String MISMATCH_SUB_1 = "; expecting ";
	public static final String MISMATCH_SUB_2 = ", saw ";
	public static final String MISMATCH_SUB_3 = " or ";
	public static final String TAG_SUB_MESS = "Tag = ";
	public static final String MESSAGE_SUB_MESS = "; message ";

	/**
	 * Information messages
	 */
	public static final String INF_COMP_SCH_6_DEPR = "TIFF compression scheme 6 is deprecated";
	public static final String INF_IFD_TAG_UNK = "Unknown TIFF IFD tag: ";
	public static final String INF_IMAGE_LEN_NO_DEF = "ImageLength not defined";
	public static final String INF_IMAGE_WID_NO_DEF = "ImageWidth not defined";
	public static final String INF_PHO_NO_DEF = "PhotometricInterpretation not defined";
	public static final String INF_SHADOW_SCALE = "ShadowScale (50739)";
	public static final String INF_STR_AND_TILE_TOGETHER = "Strips and tiles defined together";
	public static final String INF_STR_AND_TILE_NO_DEF = "Neither strips nor tiles defined";
	public static final String INF_STR_BYTE_COUNT_NO_DEF = "StripByteCounts not defined";
	public static final String INF_STR_OFF_BYTE_COUNT_INCONS = "StripOffsets inconsistent with StripByteCounts: ";
	public static final String INF_STR_OFF_INVALID = "Invalid strip offset";
	public static final String INF_STR_OFF_NO_DEF = "StripOffsets not defined";
	public static final String INF_TIFF_TAG_UNDOC = "Undocumented TIFF tag";
	public static final String INF_VALOFF_NOT_WORD_ALIGN = "Value offset not word-aligned: ";
	
	/**
	 * Error messages
	 */
	public static final String ERR_CELL_LEN_NOT_ALLWD = "CellLength tag not permitted when Threshholding not 2";
	public static final String ERR_CIELAB_BPS_NOT_8_OR_16 = "BitsPerSample not 8 or 16 for CIE L*a*b*";
	public static final String ERR_COL_MAP_NOT_DEF = "ColorMap not defined for palette-color";
	public static final String ERR_COL_MAP_MISS_VALS = "Insufficient ColorMap values for palette-color: ";
	public static final String ERR_DATE_TIME_LEN_INV = "Invalid DateTime length: ";
	public static final String ERR_DATE_TIME_SEP_INV = "Invalid DateTime separator: ";
	public static final String ERR_DATE_TIME_DIG_INV = "Invalid DateTime digit: ";
	public static final String ERR_DOT_RANGE_BPS = "DotRange out of range specified by BitsPerSample";
	public static final String ERR_EXIF_BLOCK_TOO_SHORT = "Embedded Exif block is too short";
	public static final String ERR_FILE_TOO_SHORT = "File is too short";
	public static final String ERR_GEO_KEY_DIRECT_INVALID = "Invalid GeoKeyDirectory tag";
	public static final String ERR_GEO_KEY_OUT_SEQ = "GeoKey ";
	public static final String ERR_GEO_KEY_OUT_SEQ_2 = " out of sequence";
	public static final String ERR_GPS_IFD_TAG_UNK = "Unknown GPSInfo IFD tag";
	public static final String ERR_IFD_MISSING = "No IFD in file ";
	public static final String ERR_IFD_OFF_MISALIGN = "IFD offset not word-aligned:  ";
	public static final String ERR_IFD_MAX_EXCEEDED = "More than 50 IFDs in chain, probably an infinite loop";
	public static final String ERR_IO_READ = "Read error";
	public static final String ERR_JPEGPROC_NO_DEF = "JPEGProc not defined for JPEG compression";
	public static final String ERR_PAL_COL_SPP_NE_1 = "For palette-color SamplesPerPixel must be 1: ";
	public static final String ERR_PHO_AND_NEW_SUBFILE_INCONSISTENT = "PhotometricInterpretation and NewSubfileType must agree on transparency mask";
	public static final String ERR_PHO_INT_SPP_GT_1 = "For PhotometricInterpretation, SamplesPerPixel must be >= 1, equals: ";
	public static final String ERR_PHO_INT_SPP_GT_3 = "For PhotometricInterpretation, SamplesPerPixel must be >= 3, equals: ";
	public static final String ERR_SPP_EXTRA_NT_1_OR_3 = "SamplesPerPixel-ExtraSamples not 1 or 3:";
	public static final String ERR_TAG_COUNT_MISMATCH = "Count" + tagMismatch;
	public static final String ERR_TAG_IO_READ = ERR_IO_READ + " for tag ";
	public static final String ERR_TAG_ICCPROFILE_BAD = "Bad ICCProfile in tag ";
	public static final String ERR_TAG_OUT_OF_SEQ_1 = "Tag ";
	public static final String ERR_TAG_OUT_OF_SEQ_2 = " out of sequence";
	public static final String ERR_TAG_TYPE_MISMATCH = "Type" + tagMismatch;
	public static final String ERR_TIFF_HEADER_MISSING = "No TIFF header: ";
	public static final String ERR_TIFF_MAGIC_NUM_MISSING = "No TIFF magic number: ";
	public static final String ERR_TIFF_PREM_EOF = "Premature EOF";
	public static final String ERR_TRANS_MASK_BPS = "For transparency mask BitsPerSample must be 1";
	public static final String ERR_UNK_DATA_TYPE = "Unknown data type";
	public static final String ERR_UNK_DATA_TYPE_SUB_1 = "Type = ";
	public static final String ERR_UNK_DATA_TYPE_SUB_2 = ", Tag = ";
	public static final String ERR_EXIF_INTER_IFD_UNK = "Unknown Exif Interoperability IFD tag";
	public static final String ERR_VAL_OUT_OF_RANGE = " value out of range: ";
	public static final String ERR_XMP_INVALID = "Invalid or ill-formed XMP metadata";
	public static final String ERR_TILE_WID_NO_DEF = "TileWidth not defined";
	public static final String ERR_TILE_LEN_NO_DEF = "TileLength not defined";
	public static final String ERR_TILE_OFF_NO_DEF = "TileOffsets not defined";
	public static final String ERR_TILE_COUNT_NO_DEF = "TileByteCounts not defined";
	public static final String ERR_TILE_WID_NOT_DIV_16 = "TileWidth not a multiple of 16: ";
	public static final String ERR_TILE_LEN_NOT_DIV_16 = "TileLength not a multiple of 16: ";
	public static final String ERR_TILE_OFF_MISS_VALS = "Insufficient values for TileOffsets: ";
	public static final String ERR_TILE_COUNT_MISS_VALS = "Insufficient values for TileByteCounts: ";
	public static final String ERR_XCLIP_PATH_NO_DEF = "XClipPathUnits not defined for ClipPath";

}