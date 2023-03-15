/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.jpeg;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Enum used to externalise the JPEG modules message Strings. Using an enum
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
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Oct 2016:20:54:20
 */

public enum MessageConstants {
	INSTANCE;

	public static final JhoveMessageFactory messageFactory = JhoveMessages.getInstance(
			"edu.harvard.hul.ois.jhove.module.jpeg.ErrorMessages");
	public static final String INF_EXIF_APP2_MULTI_REPORT = "ICCProfile in multiple APP2 segments; not handled by JPEG-hul";

    public static final JhoveMessage JPEG_HUL_14 = messageFactory.getMessage("JPEG-HUL-14");


	public static final JhoveMessage JPEG_HUL_1 = messageFactory.getMessage("JPEG-HUL-1");
	public static final JhoveMessage JPEG_HUL_2 = messageFactory.getMessage("JPEG-HUL-2");
	public static final JhoveMessage JPEG_HUL_3 = messageFactory.getMessage("JPEG-HUL-3");
	public static final JhoveMessage JPEG_HUL_4 = messageFactory.getMessage("JPEG-HUL-4");
	public static final JhoveMessage JPEG_HUL_5 = messageFactory.getMessage("JPEG-HUL-5");
	public static final JhoveMessage JPEG_HUL_6 = messageFactory.getMessage("JPEG-HUL-6");
	public static final JhoveMessage JPEG_HUL_7 = messageFactory.getMessage("JPEG-HUL-7");
	public static final JhoveMessage JPEG_HUL_7_SUB = messageFactory.getMessage("JPEG-HUL-7-SUB");
	public static final JhoveMessage JPEG_HUL_8 = messageFactory.getMessage("JPEG-HUL-8");
	public static final JhoveMessage JPEG_HUL_9 = messageFactory.getMessage("JPEG-HUL-9");
	public static final JhoveMessage JPEG_HUL_10 = messageFactory.getMessage("JPEG-HUL-10");
	public static final JhoveMessage JPEG_HUL_11 = messageFactory.getMessage("JPEG-HUL-11");
	public static final JhoveMessage JPEG_HUL_12 = messageFactory.getMessage("JPEG-HUL-12");
	public static final JhoveMessage JPEG_HUL_13 = messageFactory.getMessage("JPEG-HUL-13");

    public static final JhoveMessage JHOVE_SYS_1 = messageFactory.getMessage("JHOVE-SYS-1");
}
