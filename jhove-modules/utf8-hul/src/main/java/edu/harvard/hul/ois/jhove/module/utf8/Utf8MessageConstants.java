/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.utf8;

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

public enum Utf8MessageConstants {
    INSTANCE;

    public static final String INF_BOM_MARK_PRESENT = "UTF-8 Byte Order Mark signature is present"; // UTF8-HUL-1

    public static final String ERR_INVALID_FIRST_BYTE_ENCODING = "Not valid first byte of UTF-8 encoding"; // UTF8-HUL-2
    public static final String ERR_INVALID_SECOND_BYTE_ENCODING = "Not valid second byte of UTF-8 encoding"; // UTF8-HUL-3
    public static final String ERR_INVALID_THIRD_BYTE_ENCODING = "Not valid third byte of UTF-8 encoding"; // UTF8-HUL-4
    public static final String ERR_INVALID_FOURTH_BYTE_ENCODING = "Not valid fourth byte of UTF-8 encoding"; // UTF8-HUL-5
    public static final String ERR_ZERO_LENGTH_FILE = "Zero-length file"; // UTF8-HUL-6
    public static final String ERR_UCS4_NOT_UTF8 = "UCS-4 little-endian encoding, not UTF-8"; // UTF8-HUL-7
    public static final String ERR_UTF16LE_NOT_UTF8 = "UTF-16 little-endian encoding, not UTF-8"; // UTF8-HUL-8
    public static final String ERR_UTF16BE_NOT_UTF8 = "UTF-16 big-endian encoding, not UTF-8"; // UTF8-HUL-9

}
