package edu.harvard.hul.ois.jhove.module.gzip;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

public enum MessageConstants {
	
	INSTANCE;
	
    private static JhoveMessageFactory messageFactory = JhoveMessages
            .getInstance(
                    "edu.harvard.hul.ois.jhove.module.gzip.ErrorMessages");

	/**
	 * Error messages
	 */
    public static final JhoveMessage GZIP_KB_1 = messageFactory
            .getMessage("GZIP-KB-1");
    /**
     * Exception messages
     */
	public static final String ERR_RECORD_NULL = "GzipReader has not been properly instantiated.";
	
}
