package edu.harvard.hul.ois.jhove.module.warc;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

public enum MessageConstants {
	INSTANCE;

    private static JhoveMessageFactory messageFactory = JhoveMessages
    .getInstance(
            "edu.harvard.hul.ois.jhove.module.warc.ErrorMessages");

/**
* Error messages
*/
public static final JhoveMessage WARC_KB_1 = messageFactory
    .getMessage("WARC-KB-1");

	/**
     * Exception messages
	 */
	public static final String ERR_RECORD_DATA_NULL = "'record' should never be null";
	public static final String ERR_BLOCK_DIGEST_INVALID = "Invalid block digest algorithm: ";
	public static final String ERR_PAYLOAD_DIGEST_INVALID = "Invalid payload digest algorithm: ";
	public static final String ERR_RECORD_NULL = "WarcReader has not been properly instantiated.";
}
