package edu.harvard.hul.ois.jhove.module.gif;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

public enum MessageConstants {
	INSTANCE;
	public static final JhoveMessageFactory messageFactory = JhoveMessages
			.getInstance("edu.harvard.hul.ois.jhove.module.gif.ErrorMessages");

	/**
	 * Error messages
	 */
	public static final JhoveMessage GIF_HUL_1 = messageFactory
			.getMessage("GIF-HUL-1");
	public static final JhoveMessage GIF_HUL_2 = messageFactory
			.getMessage("GIF-HUL-2");
	public static final JhoveMessage GIF_HUL_3 = messageFactory
			.getMessage("GIF-HUL-3");
	public static final JhoveMessage GIF_HUL_4 = messageFactory
			.getMessage("GIF-HUL-4");
	public static final JhoveMessage GIF_HUL_5 = messageFactory
			.getMessage("GIF-HUL-5");
	public static final JhoveMessage GIF_HUL_6 = messageFactory
			.getMessage("GIF-HUL-6");
	public static final JhoveMessage GIF_HUL_7 = messageFactory
			.getMessage("GIF-HUL-7");
	public static final JhoveMessage GIF_HUL_8 = messageFactory
			.getMessage("GIF-HUL-8");
	public static final JhoveMessage GIF_HUL_9 = messageFactory
			.getMessage("GIF-HUL-9");
	public static final JhoveMessage GIF_HUL_10 = messageFactory
			.getMessage("GIF-HUL-10");
}
