/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.utf8;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Enum used to externalise the UTF8 module message Strings. Using an enum
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
 * 
 */

public enum MessageConstants {
	INSTANCE;

	public static final JhoveMessageFactory messageFactory = JhoveMessages
			.getInstance("edu.harvard.hul.ois.jhove.module.utf8.ErrorMessages");

	public static final JhoveMessage UTF8_HUL_1 = messageFactory.getMessage("UTF8-HUL-1");
	public static final JhoveMessage UTF8_HUL_2 = messageFactory.getMessage("UTF8-HUL-2");
	public static final JhoveMessage UTF8_HUL_3 = messageFactory.getMessage("UTF8-HUL-3");
	public static final JhoveMessage UTF8_HUL_4 = messageFactory.getMessage("UTF8-HUL-4");
	public static final JhoveMessage UTF8_HUL_5 = messageFactory.getMessage("UTF8-HUL-5");
	public static final JhoveMessage UTF8_HUL_6 = messageFactory.getMessage("UTF8-HUL-6");
	public static final JhoveMessage UTF8_HUL_7 = messageFactory.getMessage("UTF8-HUL-7");
	public static final JhoveMessage UTF8_HUL_8 = messageFactory.getMessage("UTF8-HUL-8");
	public static final JhoveMessage UTF8_HUL_9 = messageFactory.getMessage("UTF8-HUL-9");
    public static final JhoveMessage UTF8_HUL_10 = messageFactory.getMessage("UTF8-HUL-10");
}
