package edu.harvard.hul.ois.jhove.module.xml;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Enum used to externalise the XML module message Strings. Using an enum
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
 * <li>ENTITY_NAME: the name of the entity causing the problem.</li>
 * <li>Problem: a short indicator of the problem type, e.g. MISSING, ILLEGAL,
 * etc.</li>
 * </ol>
 * The elements should be separated by underscores. The messages currently don't
 * follow a consistent vocabulary, that is terms such as invalid, illegal, or
 * malformed are used without definition.
 *
 * @author Thomas Ledoux
 */

public enum MessageConstants {
    INSTANCE;

	public static final JhoveMessageFactory messageFactory = JhoveMessages
			.getInstance("edu.harvard.hul.ois.jhove.module.xml.ErrorMessages");

	public static final JhoveMessage XML_HUL_1 = messageFactory
			.getMessage("XML-HUL-1");
	public static final JhoveMessage XML_HUL_1_SUB = messageFactory
			.getMessage("XML-HUL-1-SUB");
	public static final JhoveMessage XML_HUL_2 = messageFactory
			.getMessage("XML-HUL-2");
	public static final JhoveMessage XML_HUL_3 = messageFactory
			.getMessage("XML-HUL-3");
	public static final JhoveMessage XML_HUL_4 = messageFactory
			.getMessage("XML-HUL-4");
	public static final JhoveMessage XML_HUL_5 = messageFactory
			.getMessage("XML-HUL-5");
	public static final JhoveMessage XML_HUL_6 = messageFactory
			.getMessage("XML-HUL-6");
	public static final JhoveMessage XML_HUL_7 = messageFactory
			.getMessage("XML-HUL-7");
	public static final JhoveMessage XML_HUL_8 = messageFactory
			.getMessage("XML-HUL-8");
	public static final JhoveMessage XML_HUL_9 = messageFactory
			.getMessage("XML-HUL-9");
	public static final JhoveMessage XML_HUL_10 = messageFactory
			.getMessage("XML-HUL-10");
	public static final JhoveMessage XML_HUL_11 = messageFactory
			.getMessage("XML-HUL-11");
	public static final JhoveMessage XML_HUL_12 = messageFactory
			.getMessage("XML-HUL-12");
	public static final JhoveMessage XML_HUL_13 = messageFactory
			.getMessage("XML-HUL-13");
}
