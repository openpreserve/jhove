/**
 *
 */
package edu.harvard.hul.ois.jhove.module.aiff;

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

	private static final JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.module.aiff.ErrorMessages"); //$NON-NLS-1$
	/**
	 * Info messages
	 */
	
	
	/**
	 * Error messages
	 */
	public static final JhoveMessage AIFF_HUL_1 = messageFactory.getMessage("AIFF-HUL-1"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_2 = messageFactory.getMessage("AIFF-HUL-2"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_3 = messageFactory.getMessage("AIFF-HUL-3"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_4 = messageFactory.getMessage("AIFF-HUL-4"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_5 = messageFactory.getMessage("AIFF-HUL-5"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_6 = messageFactory.getMessage("AIFF-HUL-6"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_7 = messageFactory.getMessage("AIFF-HUL-7"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_8 = messageFactory.getMessage("AIFF-HUL-8"); //$NON-NLS-1$
	public static final JhoveMessage AIFF_HUL_9 = messageFactory.getMessage("AIFF-HUL-9"); //$NON-NLS-1$
}