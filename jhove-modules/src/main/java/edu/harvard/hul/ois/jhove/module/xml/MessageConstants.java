/**
 *
 */
package edu.harvard.hul.ois.jhove.module.xml;

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
 *
 */

public enum MessageConstants {
    INSTANCE;

    public static final String WRN_SAX_EXCEPTION = "SaxParseException: {0}";
    public static final String WRN_TOO_MANY_MESSAGES = "Error messages in excess of {0} not reported";

    public static final String ERR_SAX_EXCEPTION = WRN_SAX_EXCEPTION;
}
