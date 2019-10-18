package edu.harvard.hul.ois.jhove.module.wave;

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

	public static final JhoveMessageFactory messageFactory = JhoveMessages
			.getInstance("edu.harvard.hul.ois.jhove.module.wave.ErrorMessages");

	public static final JhoveMessage WAVE_HUL_1 = messageFactory.getMessage("WAVE-HUL-1");
	public static final JhoveMessage WAVE_HUL_2 = messageFactory.getMessage("WAVE-HUL-2");
	public static final JhoveMessage WAVE_HUL_3 = messageFactory.getMessage("WAVE-HUL-3");
	public static final JhoveMessage WAVE_HUL_3_SUB = messageFactory.getMessage("WAVE-HUL-3-SUB");
	public static final JhoveMessage WAVE_HUL_3_SUB_2 = messageFactory.getMessage("WAVE-HUL-3-SUB-2");
	public static final JhoveMessage WAVE_HUL_4 = messageFactory.getMessage("WAVE-HUL-4");
	public static final JhoveMessage WAVE_HUL_5 = messageFactory.getMessage("WAVE-HUL-5");
	public static final JhoveMessage WAVE_HUL_6 = messageFactory.getMessage("WAVE-HUL-6");
	public static final JhoveMessage WAVE_HUL_7 = messageFactory.getMessage("WAVE-HUL-7");
	public static final JhoveMessage WAVE_HUL_8 = messageFactory.getMessage("WAVE-HUL-8");
	public static final JhoveMessage WAVE_HUL_9 = messageFactory.getMessage("WAVE-HUL-9");
	public static final JhoveMessage WAVE_HUL_9_SUB = messageFactory.getMessage("WAVE-HUL-9-SUB");
	public static final JhoveMessage WAVE_HUL_10 = messageFactory.getMessage("WAVE-HUL-10");
	public static final JhoveMessage WAVE_HUL_11 = messageFactory.getMessage("WAVE-HUL-11");
	public static final JhoveMessage WAVE_HUL_12 = messageFactory.getMessage("WAVE-HUL-12");
	public static final JhoveMessage WAVE_HUL_13 = messageFactory.getMessage("WAVE-HUL-13");
	public static final JhoveMessage WAVE_HUL_14 = messageFactory.getMessage("WAVE-HUL-14");
	public static final JhoveMessage WAVE_HUL_15 = messageFactory.getMessage("WAVE-HUL-15");
//	public static final JhoveMessage WAVE_HUL_16 = RETIRED
	public static final JhoveMessage WAVE_HUL_17 = messageFactory.getMessage("WAVE-HUL-17");
	public static final JhoveMessage WAVE_HUL_18 = messageFactory.getMessage("WAVE-HUL-18");
	public static final JhoveMessage WAVE_HUL_19 = messageFactory.getMessage("WAVE-HUL-19");
	public static final JhoveMessage WAVE_HUL_20 = messageFactory.getMessage("WAVE-HUL-20");
	public static final JhoveMessage WAVE_HUL_21 = messageFactory.getMessage("WAVE-HUL-21");
	public static final JhoveMessage WAVE_HUL_22 = messageFactory.getMessage("WAVE-HUL-22");
	public static final JhoveMessage WAVE_HUL_23 = messageFactory.getMessage("WAVE-HUL-23");
	public static final JhoveMessage WAVE_HUL_24 = messageFactory.getMessage("WAVE-HUL-24");
	public static final JhoveMessage WAVE_HUL_25 = messageFactory.getMessage("WAVE-HUL-25");
	public static final JhoveMessage WAVE_HUL_26 = messageFactory.getMessage("WAVE-HUL-26");
	public static final JhoveMessage WAVE_HUL_26_SUB = messageFactory.getMessage("WAVE-HUL-26-SUB");
	public static final JhoveMessage WAVE_HUL_27 = messageFactory.getMessage("WAVE-HUL-27");
}
