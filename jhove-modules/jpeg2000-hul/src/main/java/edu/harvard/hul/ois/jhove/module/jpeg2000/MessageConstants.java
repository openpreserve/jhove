package edu.harvard.hul.ois.jhove.module.jpeg2000;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Enum used to externalise the JPEG 2000 modules message Strings. Using an
 * enum INSTANCE as a "trick" to ensure a single instance of the class. String
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
 * <li>ENTITY_NAME: the name of the JPEG 2000 entity causing the prohlem, e.g.
 * BOX, FILE_TYPE_BOX, COC_MARKER, etc.</li>
 * <li>PROBLEM_LOCATION: the location or component inside the entity, e.g.
 * POSITION, SIZE, TYPE, NUMBER_OF_ENTRIES, etc.</li>
 * <li>PROBLEM_TYPE: a short indicator of the problem type, e.g. MISSING,
 * INVALID, EMPTY, OVERRUN, UNDERRUN, etc.</li>
 * </ol>
 * The elements should be separated by underscores. The messages currently
 * don't follow a consistent vocabulary, that is terms such as invalid,
 * illegal, or malformed are used without definition.
 * 
 * @author <a href="mailto:martin@hoppenheit.info">Martin Hoppenheit</a>
 *         <a href="https://github.com/marhop">marhop AT github</a>
 * @version 0.1 Created 27 Apr 2017:09:39:41
 */

public enum MessageConstants {
	INSTANCE;

	public static final JhoveMessageFactory messageFactory = JhoveMessages
			.getInstance(
					"edu.harvard.hul.ois.jhove.module.jpeg2000.ErrorMessages");
	/**
	 * Warning messages
	 */
	// None yet.

	/**
	 * Information messages
	 */
	public static final JhoveMessage JPEG2000_HUL_1 = messageFactory.getMessage("JPEG2000-HUL-1");
	public static final JhoveMessage JPEG2000_HUL_2 = messageFactory.getMessage("JPEG2000-HUL-2");

	/**
	 * Error messages
	 */
	public static final JhoveMessage JPEG2000_HUL_3 = messageFactory.getMessage("JPEG2000-HUL-3");
	public static final JhoveMessage JPEG2000_HUL_4 = messageFactory.getMessage("JPEG2000-HUL-4");
	public static final JhoveMessage JPEG2000_HUL_5 = messageFactory.getMessage("JPEG2000-HUL-5");
	public static final JhoveMessage JPEG2000_HUL_6 = messageFactory.getMessage("JPEG2000-HUL-6");
	public static final JhoveMessage JPEG2000_HUL_7 = messageFactory.getMessage("JPEG2000-HUL-7");
	public static final JhoveMessage JPEG2000_HUL_8 = messageFactory.getMessage("JPEG2000-HUL-8");
	public static final JhoveMessage JPEG2000_HUL_9 = messageFactory.getMessage("JPEG2000-HUL-9");
	public static final JhoveMessage JPEG2000_HUL_10 = messageFactory.getMessage("JPEG2000-HUL-10");
	public static final JhoveMessage JPEG2000_HUL_11 = messageFactory.getMessage("JPEG2000-HUL-11");
	public static final JhoveMessage JPEG2000_HUL_12 = messageFactory.getMessage("JPEG2000-HUL-12");
	public static final JhoveMessage JPEG2000_HUL_13 = messageFactory.getMessage("JPEG2000-HUL-13");
	public static final JhoveMessage JPEG2000_HUL_14 = messageFactory.getMessage("JPEG2000-HUL-14");
	public static final JhoveMessage JPEG2000_HUL_15 = messageFactory.getMessage("JPEG2000-HUL-15");
	public static final JhoveMessage JPEG2000_HUL_16 = messageFactory.getMessage("JPEG2000-HUL-16");
	public static final JhoveMessage JPEG2000_HUL_17 = messageFactory.getMessage("JPEG2000-HUL-17");
	public static final JhoveMessage JPEG2000_HUL_18 = messageFactory.getMessage("JPEG2000-HUL-18");
	public static final JhoveMessage JPEG2000_HUL_19 = messageFactory.getMessage("JPEG2000-HUL-19");
	public static final JhoveMessage JPEG2000_HUL_20 = messageFactory.getMessage("JPEG2000-HUL-20");
	public static final JhoveMessage JPEG2000_HUL_21 = messageFactory.getMessage("JPEG2000-HUL-21");
	public static final JhoveMessage JPEG2000_HUL_22 = messageFactory.getMessage("JPEG2000-HUL-22");
	public static final JhoveMessage JPEG2000_HUL_23 = messageFactory.getMessage("JPEG2000-HUL-23");
	public static final JhoveMessage JPEG2000_HUL_24 = messageFactory.getMessage("JPEG2000-HUL-24");
	public static final JhoveMessage JPEG2000_HUL_25 = messageFactory.getMessage("JPEG2000-HUL-25");
	public static final JhoveMessage JPEG2000_HUL_26 = messageFactory.getMessage("JPEG2000-HUL-26");
	public static final JhoveMessage JPEG2000_HUL_27 = messageFactory.getMessage("JPEG2000-HUL-27");
	public static final JhoveMessage JPEG2000_HUL_28 = messageFactory.getMessage("JPEG2000-HUL-28");
	public static final JhoveMessage JPEG2000_HUL_29 = messageFactory.getMessage("JPEG2000-HUL-29");
	public static final JhoveMessage JPEG2000_HUL_30 = messageFactory.getMessage("JPEG2000-HUL-30");
	public static final JhoveMessage JPEG2000_HUL_31 = messageFactory.getMessage("JPEG2000-HUL-31");
	public static final JhoveMessage JPEG2000_HUL_32 = messageFactory.getMessage("JPEG2000-HUL-32");
	public static final JhoveMessage JPEG2000_HUL_33 = messageFactory.getMessage("JPEG2000-HUL-33");
	public static final JhoveMessage JPEG2000_HUL_34 = messageFactory.getMessage("JPEG2000-HUL-34");
	public static final JhoveMessage JPEG2000_HUL_35 = messageFactory.getMessage("JPEG2000-HUL-35");
	public static final JhoveMessage JPEG2000_HUL_36 = messageFactory.getMessage("JPEG2000-HUL-36");
	public static final JhoveMessage JPEG2000_HUL_37 = messageFactory.getMessage("JPEG2000-HUL-37");
	public static final JhoveMessage JPEG2000_HUL_38 = messageFactory.getMessage("JPEG2000-HUL-38");
	public static final JhoveMessage JPEG2000_HUL_39 = messageFactory.getMessage("JPEG2000-HUL-39");
	public static final JhoveMessage JPEG2000_HUL_40 = messageFactory.getMessage("JPEG2000-HUL-40");
	public static final JhoveMessage JPEG2000_HUL_41 = messageFactory.getMessage("JPEG2000-HUL-41");
	public static final JhoveMessage JPEG2000_HUL_42 = messageFactory.getMessage("JPEG2000-HUL-42");
	public static final JhoveMessage JPEG2000_HUL_43 = messageFactory.getMessage("JPEG2000-HUL-43");
	public static final JhoveMessage JPEG2000_HUL_44 = messageFactory.getMessage("JPEG2000-HUL-44");
	public static final JhoveMessage JPEG2000_HUL_45 = messageFactory.getMessage("JPEG2000-HUL-45");
	public static final JhoveMessage JPEG2000_HUL_46 = messageFactory.getMessage("JPEG2000-HUL-46");
	public static final JhoveMessage JPEG2000_HUL_47 = messageFactory.getMessage("JPEG2000-HUL-47");
	public static final JhoveMessage JPEG2000_HUL_48 = messageFactory.getMessage("JPEG2000-HUL-48");
	public static final JhoveMessage JPEG2000_HUL_49 = messageFactory.getMessage("JPEG2000-HUL-49");
	public static final JhoveMessage JPEG2000_HUL_50 = messageFactory.getMessage("JPEG2000-HUL-50");
	public static final JhoveMessage JPEG2000_HUL_51 = messageFactory.getMessage("JPEG2000-HUL-51");
	public static final JhoveMessage JPEG2000_HUL_52 = messageFactory.getMessage("JPEG2000-HUL-52");
	public static final JhoveMessage JPEG2000_HUL_53 = messageFactory.getMessage("JPEG2000-HUL-53");
	public static final JhoveMessage JPEG2000_HUL_54 = messageFactory.getMessage("JPEG2000-HUL-54");
	public static final JhoveMessage JPEG2000_HUL_55 = messageFactory.getMessage("JPEG2000-HUL-55");
	public static final JhoveMessage JPEG2000_HUL_56 = messageFactory.getMessage("JPEG2000-HUL-56");
}
