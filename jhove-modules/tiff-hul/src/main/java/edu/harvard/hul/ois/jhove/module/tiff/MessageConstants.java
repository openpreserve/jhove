package edu.harvard.hul.ois.jhove.module.tiff;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

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

	public static final JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.module.tiff.ErrorMessages");
	
	public static final JhoveMessage TIFF_HUL_1 = messageFactory.getMessage("TIFF-HUL-1");
	public static final JhoveMessage TIFF_HUL_2 = messageFactory.getMessage("TIFF-HUL-2");
	public static final JhoveMessage TIFF_HUL_3 = messageFactory.getMessage("TIFF-HUL-3");
	public static final JhoveMessage TIFF_HUL_3_SUB = messageFactory.getMessage("TIFF-HUL-3-SUB");
	public static final JhoveMessage TIFF_HUL_4 = messageFactory.getMessage("TIFF-HUL-4");
	public static final JhoveMessage TIFF_HUL_5 = messageFactory.getMessage("TIFF-HUL-5");
	public static final JhoveMessage TIFF_HUL_6 = messageFactory.getMessage("TIFF-HUL-6");
	public static final JhoveMessage TIFF_HUL_7 = messageFactory.getMessage("TIFF-HUL-7");
	public static final JhoveMessage TIFF_HUL_8 = messageFactory.getMessage("TIFF-HUL-8");
	public static final JhoveMessage TIFF_HUL_9 = messageFactory.getMessage("TIFF-HUL-9");
	public static final JhoveMessage TIFF_HUL_10 = messageFactory.getMessage("TIFF-HUL-10");
	public static final JhoveMessage TIFF_HUL_11 = messageFactory.getMessage("TIFF-HUL-11");
	public static final JhoveMessage TIFF_HUL_11_SUB = messageFactory.getMessage("TIFF-HUL-11-SUB");
	public static final JhoveMessage TIFF_HUL_12 = messageFactory.getMessage("TIFF-HUL-12");
	public static final JhoveMessage TIFF_HUL_13 = messageFactory.getMessage("TIFF-HUL-13");
	public static final JhoveMessage TIFF_HUL_14 = messageFactory.getMessage("TIFF-HUL-14");
	public static final JhoveMessage TIFF_HUL_15 = messageFactory.getMessage("TIFF-HUL-15");
	public static final JhoveMessage TIFF_HUL_15_SUB = messageFactory.getMessage("TIFF-HUL-15-SUB");
	public static final JhoveMessage TIFF_HUL_16 = messageFactory.getMessage("TIFF-HUL-16");
	public static final JhoveMessage TIFF_HUL_17 = messageFactory.getMessage("TIFF-HUL-17");
	public static final JhoveMessage TIFF_HUL_17_SUB = messageFactory.getMessage("TIFF-HUL-17-SUB");
	public static final JhoveMessage TIFF_HUL_18 = messageFactory.getMessage("TIFF-HUL-18");
	public static final JhoveMessage TIFF_HUL_19 = messageFactory.getMessage("TIFF-HUL-19");
	public static final JhoveMessage TIFF_HUL_20 = messageFactory.getMessage("TIFF-HUL-20");
	public static final JhoveMessage TIFF_HUL_21 = messageFactory.getMessage("TIFF-HUL-21");
	public static final JhoveMessage TIFF_HUL_22 = messageFactory.getMessage("TIFF-HUL-22");
	public static final JhoveMessage TIFF_HUL_23 = messageFactory.getMessage("TIFF-HUL-23");
	public static final JhoveMessage TIFF_HUL_24 = messageFactory.getMessage("TIFF-HUL-24");
	public static final JhoveMessage TIFF_HUL_25 = messageFactory.getMessage("TIFF-HUL-25");
	public static final JhoveMessage TIFF_HUL_26 = messageFactory.getMessage("TIFF-HUL-26");
	public static final JhoveMessage TIFF_HUL_27 = messageFactory.getMessage("TIFF-HUL-27");
	public static final JhoveMessage TIFF_HUL_28 = messageFactory.getMessage("TIFF-HUL-28");
	public static final JhoveMessage TIFF_HUL_29 = messageFactory.getMessage("TIFF-HUL-29");
	public static final JhoveMessage TIFF_HUL_30 = messageFactory.getMessage("TIFF-HUL-30");
	public static final JhoveMessage TIFF_HUL_31 = messageFactory.getMessage("TIFF-HUL-31");
	public static final JhoveMessage TIFF_HUL_32 = messageFactory.getMessage("TIFF-HUL-32");
	public static final JhoveMessage TIFF_HUL_33 = messageFactory.getMessage("TIFF-HUL-33");
	public static final JhoveMessage TIFF_HUL_34 = messageFactory.getMessage("TIFF-HUL-34");
	public static final JhoveMessage TIFF_HUL_35 = messageFactory.getMessage("TIFF-HUL-35");
	public static final JhoveMessage TIFF_HUL_36 = messageFactory.getMessage("TIFF-HUL-36");
	public static final JhoveMessage TIFF_HUL_37 = messageFactory.getMessage("TIFF-HUL-37");
	public static final JhoveMessage TIFF_HUL_38 = messageFactory.getMessage("TIFF-HUL-38");
	public static final JhoveMessage TIFF_HUL_39 = messageFactory.getMessage("TIFF-HUL-39");
	public static final JhoveMessage TIFF_HUL_40 = messageFactory.getMessage("TIFF-HUL-40");
	public static final JhoveMessage TIFF_HUL_41 = messageFactory.getMessage("TIFF-HUL-41");
	public static final JhoveMessage TIFF_HUL_42 = messageFactory.getMessage("TIFF-HUL-42");
	public static final JhoveMessage TIFF_HUL_43 = messageFactory.getMessage("TIFF-HUL-43");
	public static final JhoveMessage TIFF_HUL_44 = messageFactory.getMessage("TIFF-HUL-44");
	public static final JhoveMessage TIFF_HUL_45 = messageFactory.getMessage("TIFF-HUL-45");
	public static final JhoveMessage TIFF_HUL_46 = messageFactory.getMessage("TIFF-HUL-46");
	public static final JhoveMessage TIFF_HUL_47 = messageFactory.getMessage("TIFF-HUL-47");
	public static final JhoveMessage TIFF_HUL_48 = messageFactory.getMessage("TIFF-HUL-48");
	public static final JhoveMessage TIFF_HUL_49 = messageFactory.getMessage("TIFF-HUL-49");
	public static final JhoveMessage TIFF_HUL_50 = messageFactory.getMessage("TIFF-HUL-50");
	public static final JhoveMessage TIFF_HUL_51 = messageFactory.getMessage("TIFF-HUL-51");
	public static final JhoveMessage TIFF_HUL_52 = messageFactory.getMessage("TIFF-HUL-52");
	public static final JhoveMessage TIFF_HUL_53 = messageFactory.getMessage("TIFF-HUL-53");
	public static final JhoveMessage TIFF_HUL_54 = messageFactory.getMessage("TIFF-HUL-54");
	public static final JhoveMessage TIFF_HUL_55 = messageFactory.getMessage("TIFF-HUL-55");
	public static final JhoveMessage TIFF_HUL_56 = messageFactory.getMessage("TIFF-HUL-56");
	public static final JhoveMessage TIFF_HUL_57 = messageFactory.getMessage("TIFF-HUL-57");
	public static final JhoveMessage TIFF_HUL_58 = messageFactory.getMessage("TIFF-HUL-58");
	public static final JhoveMessage TIFF_HUL_59 = messageFactory.getMessage("TIFF-HUL-59");
	public static final JhoveMessage TIFF_HUL_60 = messageFactory.getMessage("TIFF-HUL-60");
	public static final JhoveMessage TIFF_HUL_61 = messageFactory.getMessage("TIFF-HUL-61");
	public static final JhoveMessage TIFF_HUL_62 = messageFactory.getMessage("TIFF-HUL-62");
	public static final JhoveMessage TIFF_HUL_63 = messageFactory.getMessage("TIFF-HUL-63");
	public static final JhoveMessage TIFF_HUL_64 = messageFactory.getMessage("TIFF-HUL-64");
	public static final JhoveMessage TIFF_HUL_65 = messageFactory.getMessage("TIFF-HUL-65");
	public static final JhoveMessage TIFF_HUL_66 = messageFactory.getMessage("TIFF-HUL-66");
	public static final JhoveMessage TIFF_HUL_67 = messageFactory.getMessage("TIFF-HUL-67");
	public static final JhoveMessage TIFF_HUL_68 = messageFactory.getMessage("TIFF-HUL-68");
	public static final JhoveMessage TIFF_HUL_69 = messageFactory.getMessage("TIFF-HUL-69");
	public static final JhoveMessage TIFF_HUL_70 = messageFactory.getMessage("TIFF-HUL-70");
	public static final JhoveMessage TIFF_HUL_71 = messageFactory.getMessage("TIFF-HUL-71");

//	private static final String tagMismatch = " mismatch for tag ";
//
//	/**
//	 * Error "fill in" strings
//	 */
//	public static final String MISMATCH_SUB_1 = "; expecting ";
//	public static final String MISMATCH_SUB_2 = ", saw ";
//	public static final String MISMATCH_SUB_3 = " or ";
//	public static final String TAG_SUB_MESS = "Tag = ";
//	public static final String MESSAGE_SUB_MESS = "; message ";
//
//	/**
//	 * Information messages
//	 */
//	public static final String INF_COMP_SCH_6_DEPR = "TIFF compression scheme 6 is deprecated";
//	public static final String INF_IFD_TAG_UNK = "Unknown TIFF IFD tag: ";
//	public static final String INF_IMAGE_LEN_NO_DEF = "ImageLength not defined";
//	public static final String INF_IMAGE_WID_NO_DEF = "ImageWidth not defined";
//	public static final String INF_PHO_NO_DEF = "PhotometricInterpretation not defined";
//	public static final String INF_SHADOW_SCALE = "ShadowScale (50739)";
//	public static final String INF_STR_AND_TILE_TOGETHER = "Strips and tiles defined together";
//	public static final String INF_STR_AND_TILE_NO_DEF = "Neither strips nor tiles defined";
//	public static final String INF_STR_BYTE_COUNT_NO_DEF = "StripByteCounts not defined";
//	public static final String INF_STR_OFF_BYTE_COUNT_INCONS = "StripOffsets inconsistent with StripByteCounts: ";
//	public static final String INF_STR_OFF_INVALID = "Invalid strip offset";
//	public static final String INF_STR_OFF_NO_DEF = "StripOffsets not defined";
//	public static final String INF_TIFF_TAG_UNDOC = "Undocumented TIFF tag";
//	public static final String INF_VALOFF_NOT_WORD_ALIGN = "Value offset not word-aligned: ";
//	
//	/**
//	 * Error messages
//	 */
//	public static final String ERR_CELL_LEN_NOT_ALLWD = "CellLength tag not permitted when Threshholding not 2";
//	public static final String ERR_CIELAB_BPS_NOT_8_OR_16 = "BitsPerSample not 8 or 16 for CIE L*a*b*";
//	public static final String ERR_COL_MAP_NOT_DEF = "ColorMap not defined for palette-color";
//	public static final String ERR_COL_MAP_MISS_VALS = "Insufficient ColorMap values for palette-color: ";
//	public static final String ERR_DATE_TIME_LEN_INV = "Invalid DateTime length: ";
//	public static final String ERR_DATE_TIME_SEP_INV = "Invalid DateTime separator: ";
//	public static final String ERR_DATE_TIME_DIG_INV = "Invalid DateTime digit: ";
//	public static final String ERR_DOT_RANGE_BPS = "DotRange out of range specified by BitsPerSample";
//	public static final String ERR_EXIF_BLOCK_TOO_SHORT = "Embedded Exif block is too short";
//	public static final String ERR_FILE_TOO_SHORT = "File is too short";
//	public static final String ERR_GEO_KEY_DIRECT_INVALID = "Invalid GeoKeyDirectory tag";
//	public static final String ERR_GEO_KEY_OUT_SEQ = "GeoKey ";
//	public static final String ERR_GEO_KEY_OUT_SEQ_2 = " out of sequence";
//	public static final String ERR_GPS_IFD_TAG_UNK = "Unknown GPSInfo IFD tag";
//	public static final String ERR_IFD_MISSING = "No IFD in file ";
//	public static final String ERR_IFD_OFF_MISALIGN = "IFD offset not word-aligned:  ";
//	public static final String ERR_IFD_MAX_EXCEEDED = "More than 50 IFDs in chain, probably an infinite loop";
//	public static final String ERR_IO_READ = "Read error";
//	public static final String ERR_JPEGPROC_NO_DEF = "JPEGProc not defined for JPEG compression";
//	public static final String ERR_PAL_COL_SPP_NE_1 = "For palette-color SamplesPerPixel must be 1: ";
//	public static final String ERR_PHO_AND_NEW_SUBFILE_INCONSISTENT = "PhotometricInterpretation and NewSubfileType must agree on transparency mask";
//	public static final String ERR_PHO_INT_SPP_GT_1 = "For PhotometricInterpretation, SamplesPerPixel must be >= 1, equals: ";
//	public static final String ERR_PHO_INT_SPP_GT_3 = "For PhotometricInterpretation, SamplesPerPixel must be >= 3, equals: ";
//	public static final String ERR_SPP_EXTRA_NT_1_OR_3 = "SamplesPerPixel-ExtraSamples not 1 or 3:";
//	public static final String ERR_TAG_COUNT_MISMATCH = "Count" + tagMismatch;
//	public static final String ERR_TAG_IO_READ = ERR_IO_READ + " for tag ";
//	public static final String ERR_TAG_ICCPROFILE_BAD = "Bad ICCProfile in tag ";
//	public static final String ERR_TAG_OUT_OF_SEQ_1 = "Tag ";
//	public static final String ERR_TAG_OUT_OF_SEQ_2 = ERR_GEO_KEY_OUT_SEQ_2;
//	public static final String ERR_TAG_TYPE_MISMATCH = "Type" + tagMismatch;
//	public static final String ERR_TIFF_HEADER_MISSING = "No TIFF header: ";
//	public static final String ERR_TIFF_MAGIC_NUM_MISSING = "No TIFF magic number: ";
//	public static final String ERR_TIFF_PREM_EOF = "Premature EOF";
//	public static final String ERR_TRANS_MASK_BPS = "For transparency mask BitsPerSample must be 1";
//	public static final String ERR_UNK_DATA_TYPE = "Unknown data type";
//	public static final String ERR_UNK_DATA_TYPE_SUB_1 = "Type = ";
//	public static final String ERR_UNK_DATA_TYPE_SUB_2 = ", Tag = ";
//	public static final String ERR_EXIF_INTER_IFD_UNK = "Unknown Exif Interoperability IFD tag";
//	public static final String ERR_VAL_OUT_OF_RANGE = " value out of range: ";
//	public static final String ERR_XMP_INVALID = "Invalid or ill-formed XMP metadata";
//	public static final String ERR_TILE_WID_NO_DEF = "TileWidth not defined";
//	public static final String ERR_TILE_LEN_NO_DEF = "TileLength not defined";
//	public static final String ERR_TILE_OFF_NO_DEF = "TileOffsets not defined";
//	public static final String ERR_TILE_COUNT_NO_DEF = "TileByteCounts not defined";
//	public static final String ERR_TILE_WID_NOT_DIV_16 = "TileWidth not a multiple of 16: ";
//	public static final String ERR_TILE_LEN_NOT_DIV_16 = "TileLength not a multiple of 16: ";
//	public static final String ERR_TILE_OFF_MISS_VALS = "Insufficient values for TileOffsets: ";
//	public static final String ERR_TILE_COUNT_MISS_VALS = "Insufficient values for TileByteCounts: ";
//	public static final String ERR_XCLIP_PATH_NO_DEF = "XClipPathUnits not defined for ClipPath";

}