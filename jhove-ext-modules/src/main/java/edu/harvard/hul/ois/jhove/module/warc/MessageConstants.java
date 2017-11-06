package edu.harvard.hul.ois.jhove.module.warc;

public enum MessageConstants {
	
	INSTANCE;

	/**
	 * Error messages
	 */
	
	public static final String ERR_RECORD_DATA_NULL = "'record' should never be null";
	public static final String ERR_BLOCK_DIGEST_INVALID = "Invalid block digest algorithm: ";
	public static final String ERR_PAYLOAD_DIGEST_INVALID = "Invalid payload digest algorithm: ";
	public static final String ERR_RECORD_NULL = "WarcReader has not been properly instantiated.";
}
