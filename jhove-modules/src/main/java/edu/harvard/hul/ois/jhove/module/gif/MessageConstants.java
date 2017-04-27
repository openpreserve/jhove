package edu.harvard.hul.ois.jhove.module.gif;

public enum MessageConstants {
	INSTANCE;

	/**
	 * Error messages
	 */
	public static final String ERR_APP_EXTENSION_BLOCK_SIZE_INVALID = "Wrong application extension block size";
	public static final String ERR_BLOCK_TYPE_UNKNOWN = "Unknown data block type";
	public static final String ERR_EXTENSION_BLOCK_TYPE_UNKNOWN = "Unknown extension block type";
	public static final String ERR_GIF_HEADER_INVALID = "Invalid GIF header";
	public static final String ERR_GRAPH_CTL_BLOCK_MULTIPLE = "Multiple graphics control blocks for one image";
	public static final String ERR_GRAPH_CTL_BLOCK_SIZE_INVALID = "Wrong graphics control block size";
	public static final String ERR_PLAIN_TEXT_EXTENSION_BLOCK_SIZE_INVALID = "Wrong plain text extension block size";
	public static final String ERR_PLAIN_TEXT_EXTENSION_COLOR_TABLE_MISSING = "Plain text extension requires global color table";
	public static final String ERR_TRAILER_BLOCK_MISSING = "End of file reached without encountering Trailer block";
	public static final String ERR_UNEXPECTED_END_OF_FILE = "Unexpected end of file";

}
