package edu.harvard.hul.ois.jhove.module.pdf;

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

	/**
	 * Warning messages
	 */
	public static final String WRN_CAT_NON_DICT_TO_DICT = "Tried to cat non-dictionary to PdfDictionary";
	public static final String WRN_FIND_FONTS = "PdfModule.findFonts: ";
	public static final String WRN_HYBRID_XREF_NOT_IMPLEMENTED = "Hybrid cross-reference not yet implemented";

	/**
	 * Information messages
	 */
	public static final String INF_ATT_IO_EXCEPT = "IOException on attribute";
	public static final String INF_FONT_REPORT_LIMIT = "Too many fonts to report; some fonts omitted.";
	public static final String INF_FONT_REPORT_LIMIT_SUB = "Total fonts = ";
	public static final String INF_HEADER_CAT_VER_MISMATCH_1 = "File header gives version as ";
	public static final String INF_HEADER_CAT_VER_MISMATCH_2 = ", but catalog dictionary gives version as ";
	public static final String INF_HINT_ARRY_CHK = "Checking hint array";
	public static final String INF_IMAGE_XOBJ = "Image XObject";
	public static final String INF_IMAGE_GET = "Getting image";
	public static final String INF_K_ELEM_IS_ARRY = "Type K element is an array";
	public static final String INF_K_ELEM_IS_DICT = "Type K element is dictionary";
	public static final String INF_LIN_PROF_CHK = "Checking Linearized Profile";
	public static final String INF_NAMES_DICT_EXCEP = "Exception on names dictionary: ";
	public static final String INF_NO_CHILD_STRUCT_ELEM = "No children are structure elements";
	public static final String INF_NO_CHILD_OBJS = "No child objects, exiting";
	public static final String INF_REVISION_NUM_RETRIEVAL_EXCEP = "Exception getting revision number: ";
	public static final String INF_SUBTREE_BUILDING = "Building subtree";
	public static final String INF_XREF_TABLE_VERIFYING = "Verifying cross-reference table";
	
	/**
	 * Error messages
	 */
	public static final String ERR_ANNOT_DICT_TYPES_MISSING = "Annotation dictionary missing required type (S) entry";
	public static final String ERR_ANNOT_INVALID = "Invalid Annotations";
	public static final String ERR_ANNOT_LIST_INVALID = "Invalid Annotation list";
	public static final String ERR_ANNOT_OBJ_NOT_DICT = "Annotation object is not a dictionary";
	public static final String ERR_ANNOT_PROP_INVALID = "Invalid Annotation property";
	public static final String ERR_ARRAY_CONTAINS_UMEXPECTED_TOKEN = "Unexpected token in array";
	public static final String ERR_ARRAY_IMPROPERLY_NESTED = "Improperly nested array delimiters";
	public static final String ERR_COMPRESSION_INVALID_OR_UNKNOWN = "Compression method is invalid or unknown to JHOVE";
	public static final String ERR_DATE_MALFORMEED = "Improperly formed date";
	public static final String ERR_DEST_OBJ_INVALID = "Invalid destination object";
	public static final String ERR_DESTS_DICT_INVALID = "Invalid Dests dictionary";
	public static final String ERR_DICT_CONTAINS_UNEXPECTED_TOKEN = "Unexpected token in dictionary";
	public static final String ERR_DICT_DELIMETERS_IMPROPERLY_NESTED = "Improperly nested dictionary delimiters";
	public static final String ERR_DICT_MALFORMED = "Malformed dictionary";
	public static final String ERR_DOC_CAT_DICT_MISSING = "No document catalog dictionary";
	public static final String ERR_DOC_CAT_TYPE_NO_CAT = "Document catalog Type key must have value Catalog";
	public static final String ERR_DOC_CAT_NO_TYPE = "Document catalog has no Type key or it has a null value.";
	public static final String ERR_DOC_CAT_NOT_SIMPLE = "Document catalog Type key does not have a simple String value.";
	public static final String ERR_DOC_CAT_VERSION_INVALID = "Invalid Version in document catalog";
	public static final String ERR_DOC_NODE_DICT_MISSING = "Missing dictionary in document node";
	public static final String ERR_DOC_STRUCT_ATT_INVALID = "Invalid attribute in document structure";
	public static final String ERR_DOC_STRUCT_ROOT_CONTAINS_INVALID_DATA = "Invalid data in document structure root";
	public static final String ERR_DOC_STRUCT_ROOT_INVALID = "Invalid document structure root";
	public static final String ERR_DOC_STRUCT_TREE_DATA_INVALID = "Invalid data in document structure tree";
	public static final String ERR_ENCRYPT_DICT_ALG_INVALID = "Invalid algorithm value in encryption dictionary";
	public static final String ERR_EOF_UNEXPECTED = "Unexpected EOF";
	public static final String ERR_FILE_SPEC_INVALID = "Invalid file specification";
	public static final String ERR_FILE_TRAILER_MISSING = "No file trailer";
	public static final String ERR_FILTER_MALFORMED = "Malformed filter";
	public static final String ERR_FIND_FONTS_ERR = "Unexpected error in findFonts";
	public static final String ERR_FONT_PROP_PARSING = "unexpected error in parsing font property";
	public static final String ERR_HEX_STRING_CHAR_INVALID = "Invalid character in hex string";
	public static final String ERR_INDIRECT_OBJ_REF_MALFORMED = "Malformed indirect object reference";
	public static final String ERR_INLINE_STRUCT_ELE_CONTAINS_BLOCK_ATTS = "Block-level attributes in inline structure element";
	public static final String ERR_LITERAL_UNTERMINATED = "Unterminated literal in PDF file";
	public static final String ERR_NAME_TREE_INVALID = "Invalid name tree";
	public static final String ERR_NAMES_DICT_INVALID = "Invalid Names dictionary";
	public static final String ERR_OBJ_DEF_INVALID = "Invalid object definition";
	public static final String ERR_OBJ_NOT_PARSABLE = "Cannot parse object";
	public static final String ERR_OBJ_STREAM_IMPROPER_NESTING = "Improper nesting of object streams";
	public static final String ERR_OBJ_STREAM_OFFSET_OUT_OF_BOUNDS = "Offset out of bounds in object stream";
	public static final String ERR_OBJ_STREAM_OR_NUMBER_INVALID = "Invalid object number or object stream";
	public static final String ERR_OUTLINE_DICT_ITEM_INVALID = "Invalid outline dictionary item";
	public static final String ERR_OUTLINE_DICT_MALFORMED = "Malformed outline dictionary";
	public static final String ERR_PAGE_DICT_DATA_INVALID = "Invalid dictionary data for page";
	public static final String ERR_PAGE_DICT_INVALID = "Invalid page dictionary";
	public static final String ERR_PAGE_DICT_OBJ_INVALID = "Invalid page dictionary object";
	public static final String ERR_PAGE_FONT_DICT_MISSING = "Expected dictionary for font entry in page resource";
	public static final String ERR_PAGE_LABEL_INFO_INVALID = "Invalid page label info";
	public static final String ERR_PAGE_LABEL_NODE_INVALID = "Invalid page label node";
	public static final String ERR_PAGE_LABEL_SEQ_INVALID = "Invalid page label sequence";
	public static final String ERR_PAGE_LABEL_STRUCT_PROBLEM = "Problem with page label structure";
	public static final String ERR_PAGE_LABELS_BAD = "Bad page labels";
	public static final String ERR_PAGE_NUMBER_DICT_ELEMENT_MISSING = "Missing expected element in page number dictionary";
	public static final String ERR_PAGE_NUMBER_TREE_DATE_INVALID = "Invalid date in page number tree";
	public static final String ERR_PAGE_TREE_ARTBOX_MALFORMED = "Malformed ArtBox in page tree";
	public static final String ERR_PAGE_TREE_BLEEDBOX_MALFORMED = "Malformed BleedBox in page tree";
	public static final String ERR_PAGE_TREE_DEPTH_EXCEEDED = "Excessive depth or infinite recursion in page tree structure";
	public static final String ERR_PAGE_TREE_IMPROPERLY_CONSTRUCTED = "Improperly constructed page tree";
	public static final String ERR_PAGE_TREE_MEDIA_BOX_MALFORMED = "Malformed MediaBox in page tree";
	public static final String ERR_PAGE_TREE_MISSING = "Document page tree not found";
	public static final String ERR_PAGE_TREE_NODE_INVALID = "Invalid page tree node";
	public static final String ERR_PAGE_TREE_TRIMBOX_MALFORMED = "Malformed TrimBox in page tree";
	public static final String ERR_PDF_HEADER_MISSING = "No PDF header";
	public static final String ERR_PDF_TRAILER_MISSING = "No PDF trailer";
	public static final String ERR_PREV_OFFSET_TRAILER_DICT_INVALID = "Invalid Prev offset in trailer dictionary";
	public static final String ERR_RESOURCES_ENTRY_INVALID = "Invalid Resources Entry in document";
	public static final String ERR_RESOURCES_FONT_ENTRY_INVALID = "Invalid Font entry in Resources";
	public static final String ERR_SIZE_ENTRY_TRAILER_DICT_INVALID = "Invalid Size entry in trailer dictionary";
	public static final String ERR_SIZE_ENTRY_TRAILER_DICT_MISSING = "Size entry missing in trailer dictionary";
	public static final String ERR_STARTXREF_MISSING = "Missing startxref keyword or value";
	public static final String ERR_STREAM_CONTAINS_MALFORMED_ASCII_NUMBER = "Malformed ASCII number in stream";
	public static final String ERR_STREAM_EMBEDDED_IN_OBJ_STREAM = "Streams may not be embedded in object streams";
	public static final String ERR_STRUCT_ATT_INVALID = "Invalid structure attribute";
	public static final String ERR_STRUCT_ATT_REF_INVALID = "Invalid structure attribute reference";
	public static final String ERR_STRUCT_ATT_TYPE_ILLEGAL = "Structure attribute has illegal type";
	public static final String ERR_STRUCT_TREE_ELEMENT_UNKNOWN = "Unknown element in structure tree";
	public static final String ERR_STRUCT_TYPE_NAME_NON_STANDARD = "Non-standard structure type name";
	public static final String ERR_TOKEN_LEXICAL = "Lexical error";
	public static final String ERR_TRAILER_DICT_INFO_KEY_NOT_DIRECT = "Trailer dictionary Info key is not an indirect reference";
	public static final String ERR_TRAILER_DICT_ROOT_MISSING = "Root entry missing in trailer dictionary";
	public static final String ERR_TRAILER_ID_INVALID = "Invalid ID in trailer";
	public static final String ERR_UNEXPECTED_EXCEPTION = "Unexpected exception ";
	public static final String ERR_VECTOR_OBJ_COUNT_NOT_EVEN = MessageConstants.ERR_DICT_MALFORMED
			+ ": Vector must contain an even number of objects, but has ";
	public static final String ERR_XMP_INVALID = "Invalid or ill-formed XMP metadata";
	public static final String ERR_XREF_STRM_DICT_ROOT_MISSING = "Root entry missing in cross-ref stream dictionary";
	public static final String ERR_XREF_STRM_OBJ_NUM_INVALID = "Invalid object number in cross-reference stream";
	public static final String ERR_XREF_STRM_MALFORMED = "Malformed cross reference stream";
	public static final String ERR_XREF_TABLE_INVALID = "Invalid cross-reference table";
	public static final String ERR_XREF_TABLE_MALFORMED = "Malformed cross-reference table";
	public static final String ERR_XREF_TABLE_OPERATOR_ILLEGAL = "Illegal operator in xref table";
	public static final String ERR_XREF_TABLES_BROKEN = "Cross reference tables are broken";
	public static final String ERR_INDIRECT_DEST_INVALID_1 = "Invalid indirect destination - referenced object '";
	public static final String ERR_INDIRECT_DEST_INVALID_2 = "' cannot be found";
}
