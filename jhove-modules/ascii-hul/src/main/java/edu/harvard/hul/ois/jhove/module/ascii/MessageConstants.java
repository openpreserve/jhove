package edu.harvard.hul.ois.jhove.module.ascii;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public enum MessageConstants {
	INSTANCE;
	private static JhoveMessageFactory messageFactory = JhoveMessages
			.getInstance(
					"edu.harvard.hul.ois.jhove.module.ascii.ErrorMessages");

	public static final JhoveMessage ASCII_HUL_1 = messageFactory
			.getMessage("ASCII-HUL-1");
	public static final JhoveMessage ASCII_HUL_1_SUB = messageFactory
			.getMessage("ASCII-HUL-1-SUB");
	public static final JhoveMessage ASCII_HUL_2 = messageFactory
			.getMessage("ASCII-HUL-2");
	public static final JhoveMessage ASCII_HUL_3 = messageFactory
			.getMessage("ASCII-HUL-3");
}
