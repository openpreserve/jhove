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


	/**
	 * Information messages
	 */
	// public static final String TIFF_PREM_EOF_OFFSET = offset
	public static final String TIFF_PREM_EOF_STRING = "Premature EOF";
	// + TIFF_PREM_EOF_OFFSET

	// public static final String ERR_TAG_OUT_OF_SEQ_TAG = tag // not sure how to pass the original var to this var... its defined IFD.java#L276
	// public static final String ERR_TAG_OUT_OF_SEQ_OFFSET = offset
	public static final String ERR_TAG_OUT_OF_SEQ_STRING_1 = "Tag ";
	// + ERR_TAG_OUT_OF_SEQ_TAG +
	public static final String ERR_TAG_OUT_OF_SEQ_STRING_2 = " out of sequence ";
	// + ERR_TAG_OUT_OF_SEQ_OFFSET

	// public static final String ERR_UNK_DATA_TYPE_TYPE = type
	// public static final String ERR_UNK_DATA_TYPE_TAG = tag
	// public static final String ERR_UNK_DATA_TYPE_OFFSET = offset
	public static final String ERR_UNK_DATA_TYPE_STRING_1 = "Unknown data type Type = ";
	// + ERR_UNK_DATA_TYPE_TYPE +
	public static final String ERR_UNK_DATA_TYPE_STRING_2 = ", Tag = ";
	// + ERR_UNK_DATA_TYPE_TAG + ", " + ERR_UNK_DATA_TYPE_OFFSET 

	// public static final String INF_VAL_NOT_WORD_ALI_OFFSET = offset
	public static final String INF_VAL_NOT_WORD_ALI_STRING = "Value offset not word aligned: ";
	// + INF_VAL_NOT_WORD_ALI_OFFSET

	// public static final String IO_READ_ERR_OFFSET = offset
	public static final String IO_READ_ERR_STRING = "Read error";
	//+ IO_READ_ERR_OFFSET

	// public static final String TIFF_TYPE_MIS_TAG = tag
	// public static final String TIFF_TYPE_MIS_MINCOUNT = minCount
	// public static final String TIFF_TYPE_MIS_COUNT = count
	public static final String TIFF_TYPE_MIS_STRING = "Count mismatch for tag ";
	// + TIFF_TYPE_MIS_TAG + "; expecting " + TIFF_TYPE_MIS_MINCOUNT +", saw " + TIFF_TYPE_MIS_COUNT

	// public static final String TIFF_TYPE_MIS_SINGLE_TAG = tag
	// public static final String TIFF_TYPE_MIS_SINGLE_EXP = expected
	// public static final String TIFF_TYPE_MIS_SINGLE_TYPE = type
	public static final String TIFF_TYPE_MIS_SINGLE_STRING = "Type mismatch for tag ";
	// + TIFF_TYPE_MIS_SINGLE_TAG + "; expecting " + TIFF_TYPE_MIS_SINGLE_EXP + ", saw " + TIFF_TYPE_MIS_SINGLE_TYPE 

	// public static final String TIFF_TYPE_MIS_DOUBLE_TAG = tag
	// public static final String TIFF_TYPE_MIS_DOUBLE_TYPE1 = type1
	// public static final String TIFF_TYPE_MIS_DOUBLE_TYPE2 = type2
	// public static final String TIFF_TYPE_MIS_DOUBLE_TYPE = type
	public static final String TIFF_TYPE_MIS_DOUBLE_STRING = "Type mismatch for tag ";
	// + TIFF_TYPE_MIS_DOUBLE_TAG + "; expecting " + TIFF_TYPE_MIS_DOUBLE_TYPE1 + "or ," + TIFF_TYPE_MIS_DOUBLE_TYPE2 + ", saw " + TIFF_TYPE_MIS_DOUBLE_TYPE

	public static final String TIFF_INV_GEO_KEY_DICT_TAG = "Invalid GeoKeyDirectory tag";

	// public static final String TIFF_GEO_KEY_OUT_SEQ_KEY = key
	public static final String TIFF_GEO_KEY_OUT_SEQ_STRING_1 = "GeoKey ";
	// + TIFF_GEO_KEY_OUT_SEQ_KEY +
	public static final String TIFF_GEO_KEY_OUT_SEQ_STRING_2 = "out of sequence";

	public static final String INF_UNDOC_TAG_SHADOW = "Undocumented TIFF tag ShadowScale (50739)";

	// public static final String INF_UNK_IFD_TAG_TAG = tag
	// public static final String INF_UNK_IFD_TAG_VALUE = value
	public static final String INF_UNK_IFD_TAG_STRING = "Unknown TIFF IFD tag: ";
	// + INF_UNK_IFD_TAG_TAG + INF_UNK_IFD_TAG_VALUE 

	// public static final String IO_READ_ERR_TAG_TAG = tag
	// public static final String IO_READ_ERR_TAG_VALUE = value
	public static final String IO_READ_ERR_TAG_STRING = "Read error for tag ";
	// + IO_READ_ERR_TAG_TAG  + IO_READ_ERR_TAG_VALUE 

	public static final String TIFF_INV_XMP = "Invalid or ill-formed XMP metadata";

	// public static final String ERR_UNK_EXIF_INTER_IFD_TAG = tag
	// public static final String ERR_UNK_EXIF_INTER_IFD_VALUE = value
	public static final String ERR_UNK_EXIF_INTER_IFD_STRING = "Unknown Exif Interoperability IFD tag Tag = ";
	// + ERR_UNK_EXIF_INTER_IFD_TAG + ERR_UNK_EXIF_INTER_IFD_VALUE //might contain newlines... :S

	// public static final String ERR_UNK_GPS_IFD_TAG = tag
	// public static final String ERR_UNK_GPS_IFD_VALUE = value
	public static final String ERR_UNK_GPS_IFD_STRING = "Unknown GPSInfo IFD tag Tag = ";
	// + ERR_UNK_GPS_IFD_TAG + ERR_UNK_GPS_IFD_VALUE//might contain newlines... :S

	// public static final String TIFF_NO_HEAD_CH1 = char0
	// public static final String TIFF_NO_HEAD_CH2 = char1
	public static final String TIFF_NO_HEAD_STRING = "No TIFF header:";
	// + TIFF_NO_HEAD_CH1 + TIFF_NO_HEAD_CH2

	// public static final String TIFF_NO_MAGIC_NUM_MAGIC = magic
	public static final String TIFF_NO_MAGIC_NUM_STRING = "No TIFF magic number: ";
	// + TIFF_NO_MAGIC_NUM_MAGIC

	public static final String INF_STR_AND_TILE = "Strips and tiles defined together";
	public static final String INF_NO_STR_OR_TILE = "Neither strips nor tiles defined";
	public static final String INF_NO_STR_OFF = "StripOffsets not defined";
	public static final String INF_STR_COUNT_NOT_DEF = "StripByteCounts not defined";
	public static final String INF_INV_STR_OFF = "Invalid strip offset";

	// public static final String INF_INCON_STR_OFF_LEN = len
	// public static final String INF_INCON_STR_OFF_COUNT = stripByteCounts.length
	public static final String INF_INCON_STR_OFF_STRING = "StripOffsets inconsistent with StripByteCounts: ";
	// + INF_INCON_STR_OFF_LEN + "!= " + INF_INCON_STR_OFF_COUNT

	public static final String INF_NO_TILE_WID = "TileWidth not defined";
	public static final String INF_TIL_LEN_NO_DEF = "TileLength not defined";
	public static final String INF_TIL_OFF_NO_DEF = "TileOffsets not defined";
	public static final String INF_TIL_COUNT_NO_DEF = "TileByteCounts not defined";

	// public static final String INF_TIL_WID_NOT_DIV_16_TILW = tileWidth
	public static final String INF_TIL_WID_NOT_DIV_16_STRING = "TileWidth not a multiple of 16: ";
	// + INF_TIL_WID_NOT_DIV_16_TILW

	// public static final String INF_TIL_LEN_NOT_DIV_16_TILL = tileLength
	public static final String INF_TIL_LEN_NOT_DIV_16_STRING = "TileWidth not a multiple of 16: ";
	// + INF_TIL_LEN_NOT_DIV_16_TILL

	// public static final String INF_INS_VAL_TIL_OFF_TIL_OFF = tileOffsets.length
	// public static final String INF_INS_VAL_TIL_OFF_SPP = spp_tpi
	public static final String INF_INS_VAL_TIL_OFF_STRING = "Insufficient values for TileOffsets: ";
	// + INF_INS_VAL_TIL_OFF_TIL_OFF + " < " + INF_INS_VAL_TIL_OFF_SPP

	// public static final String INF_INS_VAL_TIL_COUNT_TIL_OFF = tileByteCounts.length
	// public static final String INF_INS_VAL_TIL_COUNT_SPP = spp_tpi
	public static final String INF_INS_VAL_TIL_COUNT_STRING = "Insufficient values for TileByteCountts: ";
	// + INF_INS_VAL_TIL_COUNT_TIL_OFF + " < " + INF_INS_VAL_TIL_COUNT_SPP

	//public static final String INF_INS_VAL_TIL_OFF_TIL_OFF = tileOffsets.length
	// public static final String INF_INS_VAL_TIL_OFF_TPI = tilesPerImage
	public static final String INF_INS_VAL_TIL_OFF_TPI_STRING = "Insufficient values for TileOffsets: ";
	// + INF_INS_VAL_TIL_OFF_TIL_OFF + " < " + INF_INS_VAL_TIL_OFF_TPI

	// public static final String INF_INS_VAL_TIL_COUNT_TIL_OFF = tileByteCounts.length
	// public static final String INF_INS_VAL_TIL_COUNT_TPI = tilesPerImage
	public static final String INF_INS_VAL_TIL_COUNT_TPI_STRING = "Insufficient values for TileByteCounts: ";
	// + INF_INS_VAL_TIL_COUNT_TIL_OFF + " < " + INF_INS_VAL_TIL_COUNT_TPI
	public static final String INF_PHO_AND_NEW_SUB_TYPE_TRANS_MASK = "PhotometricInterpretation and NewSubfileType must agree on transparency mask";
	public static final String INF_TRANS_MASK_BPS = "For transparency mask BitsPerSample must be 1";

	// public static final String INF_PHO_SPP1_SSP = samplesPerPixel
	public static final String INF_PHO_SPP1_STRING = "For PhotometricInterpretation, SamplesPerPixel must be >= 1, equals ";
	//+ INF_PHO_SPP1_SSP 

	//public static final String INF_PHO_SPP3_SSP = samplesPerPixel
	public static final String INF_PHO_SPP3_STRING = "For PhotometricInterpretation, SamplesPerPixel must be >= 1, equals ";
	// + INF_PHO_SPP3_SSP

	public static final String INF_COL_MAP_NOT_DEF = "ColorMap not defined for palette-color";

	// public static final String INF_PAL_COL_SPP_SPP = samplesPerPixel
	public static final String INF_PAL_COL_SPP_STRING = "For palette-color SamplesPerPixel must be 1: ";
	// + INF_PAL_COL_SPP_SPP

	// public static final String INF_INS_COL_MAP_VALS_BIT_VAL = colormapBitCodeValue.length
	// public static final String INF_INS_COL_MAP_VALS_LEN = len
	public static final String INF_INS_COL_MAP_VALS_STRING = "Insufficient ColorMap values for palette-color: ";
	// + INF_INS_COL_MAP_VALS_BIT_VAL + " < " + INF_INS_COL_MAP_VALS_LEN

	public static final String INF_CELL_LEN_THRESH = "CellLength tag not permitted when Threshholding not 2";
	public static final String INF_DOT_RANGE_BPS = "DotRange out of range specified by BitsPerSample";
	public static final String INF_NO_JPEGPROC = "JPEGProc not defined for JPEG compression";

	// public static final String INF_SPP_EXTRA_SAMPLES = samplesPerPixel
	// public static final String INF_SPP_EXTRA_LEN = len
	public static final String INF_SPP_EXTRA_STRING = "SamplesPerPixel-ExtraSamples not 1 or 3:";
	// + INF_SPP_EXTRA_SAMPLES + " - " + INF_SPP_EXTRA_LEN

	public static final String INF_BPS_NOT_8_OR_16 = "BitsPerSample not 8 or 16 for CIE L*a*b*";
	public static final String INF_XCLIP_PATH_NO_DEF = "XClipPathUnits not defined for ClipPath";

	// public static final String INF_INV_DATE_TIME_LEN_DATE = dateTime
	public static final String INF_INV_DATE_TIME_LEN_STRING = "Invalid DateTime length: ";
	// + INF_INV_DATE_TIME_LEN_DATE

	// public static final String INF_INV_DATE_TIME_SEP_DATE = dateTime
	public static final String INF_INV_DATE_TIME_SEP_STRING = "Invalid DateTime length: ";
	// + INF_INV_DATE_TIME_SEP_DATE

	// public static final String INF_INV_DATE_TIME_DIG_DATE = dateTime
	public static final String INF_INV_DATE_TIME_DIG_STRING = "Invalid DateTime digit: ";
	// + INF_INV_DATE_TIME_SEP_DATE

	// public static final String TIFF_NO_IFD_OFFSET = offset
	public static final String TIFF_NO_IFD_STRING = "No IFD in file ";
	// + TIFF_NO_IFD_OFFSET 

	// public static final String TIFF_IFD_NOT_WORD_ALI_NEXT = next
	public static final String TIFF_IFD_NOT_WORD_ALI_STRING = "IFD offset not word-aligned:  ";
	// + TIFF_IFD_NOT_WORD_ALI_NEXT

	public static final String TIFF_INF_LOOP = "More than 50 IFDs in chain, probably an infinite loop";
	public static final String INF_IM_WID_NO_DEF = "ImageWidth not defined";
	public static final String INF_COMP_SCH_6_DEPR = "TIFF compression scheme 6 is deprecated";
	public static final String INF_PHO_NO_DEF = "PhotometricInterpretation not defined";
	public static final String INF_IM_LEN_NO_DEF = "ImageLength not defined";
	public static final String INF_UNDOC_TAG = "Undocumented TIFF tag";
}