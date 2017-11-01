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
 */

public enum MessageConstants {
    INSTANCE;

    public static final String WRN_SAX_EXCEPTION = "SaxParseException: {0}";
    public static final String WRN_TOO_MANY_MESSAGES = "Error messages in excess of {0} not reported";

    public static final String INF_XML_API_UNSPPRTD = " interface is not supported by your XML implementation."
        + " This may result in some properties not being reported.";
    public static final String INF_EOL_UNDET = "Not able to determine type of end of line";
    public static final String INF_LEX_HND_UNSPPRTD = "LexicalHandler" + INF_XML_API_UNSPPRTD;
    public static final String INF_DEC_HND_UNSPPRTD = "DeclHandler" + INF_XML_API_UNSPPRTD;
    public static final String INF_SAX_UNSPPRTD = "This SAX parser does not support";
    public static final String INF_SAX_NMSPC_UNSPPRTD = " XML namespaces.";
    public static final String INF_SAX_VALID_UNSPPRTD = " validation.";
    public static final String INF_XML_SCHMID_UNSPPRTD = "The XML implementation in use does not "
        + "support schema language identification.  This may result in documents specified by schemas "
        + "being reported as invalid.";

    public static final String ERR_FILE_NOT_FOUND = "File not found";
    public static final String ERR_CHR_ENC_INV = "Invalid character encoding";
    public static final String ERR_SAX_EXCEPTION = WRN_SAX_EXCEPTION;
    public static final String ERR_SAX_EXCEP_CAUSE = "SAXException, cause = ";
    public static final String ERR_SAX_EXCEP_UNSPC = "Unspecified SAXException";
}
