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
	public static final JhoveMessage TIFF_HUL_72 = messageFactory.getMessage("TIFF-HUL-72");
}