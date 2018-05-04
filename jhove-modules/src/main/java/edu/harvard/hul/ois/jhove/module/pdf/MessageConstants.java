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
	 * Logger Messages
	 */
	public static final String LOG_HINT_ARRY_CHK = "Checking hint array";
	public static final String LOG_IMAGE_XOBJ = "Image XObject";
	public static final String LOG_IMAGE_GET = "Getting image";
	public static final String LOG_K_ELEM_IS_ARRY = "Type K element is an array";
	public static final String LOG_K_ELEM_IS_DICT = "Type K element is dictionary";
	public static final String LOG_LIN_PROF_CHK = "Checking Linearized Profile";
	public static final String LOG_NAMES_DICT_EXCEP = "Exception on names dictionary: ";
	public static final String LOG_NO_CHILD_STRUCT_ELEM = "No children are structure elements";
	public static final String LOG_NO_CHILD_OBJS = "No child objects, exiting";
	public static final String LOG_REVISION_NUM_RETRIEVAL_EXCEP = "Exception getting revision number: ";
	public static final String LOG_SUBTREE_BUILDING = "Building subtree";
	public static final String LOG_XREF_TABLE_VERIFYING = "Verifying cross-reference table";

	/**
	 * Information messages
	 */
	public static final String INF_FONT_REPORT_LIMIT = "Too many fonts to report; some fonts omitted."; // PDF-HUL-150
	public static final String INF_FONT_REPORT_LIMIT_SUB = "Total fonts = "; // PDF-HUL-150
	public static final String INF_FONTS_SKIPPED = "Fonts exist, but are not displayed; to display " + // PDF-HUL-116
			"remove param value of f from the config file";
	public static final String INF_OUTLINES_SKIPPED = "Outlines exist, but are not displayed; to display " + // PDF-HUL-145
			"remove param value of o from the config file";
	public static final String INF_ANNOTATIONS_SKIPPED = "Annotations exist, but are not displayed; to display " + // PDF-HUL-128
			"remove param value of a from the config file";
	public static final String INF_PAGES_SKIPPED = "Page information is not displayed; to display " + // PDF-HUL-124
		 	"remove param value of p from the config file";
	public static final String INF_HEADER_CAT_VER_MISMATCH_1 = "File header gives version as "; // PDF-HUL-89
	public static final String INF_HEADER_CAT_VER_MISMATCH_2 = ", but catalog dictionary gives version as "; // PDF-HUL-89
	public static final String INF_OUTLINES_RECURSIVE = "Outlines contain recursive references.";// PDF-HUL-136, PDF-HUL-141, PDF-HUL-142
	
	/**
	 * Error messages
	 */
	public static final String ERR_ANNOT_DICT_TYPES_MISSING = "Annotation dictionary missing required type (S) entry"; // PDF-HUL-133
	public static final String ERR_ANNOT_INVALID = "Invalid Annotations"; // PDF-HUL-20, PDF-HUL-21
	public static final String ERR_ANNOT_LIST_INVALID = "Invalid Annotation list"; // PDF-HUL-129
	public static final String ERR_ANNOT_OBJ_NOT_DICT = "Annotation object is not a dictionary"; // PDF-HUL-127
	public static final String ERR_ANNOT_PROP_INVALID = "Invalid Annotation property"; // PDF-HUL-134
	public static final String ERR_ARRAY_CONTAINS_UNEXPECTED_TOKEN = "Unexpected token in array"; // PDF-HUL-39
	public static final String ERR_ARRAY_IMPROPERLY_NESTED = "Improperly nested array delimiters"; // PDF-HUL-33
	public static final String ERR_COMPRESSION_INVALID_OR_UNKNOWN = "Compression method is invalid or unknown to JHOVE"; // PDF-HUL-121
	public static final String ERR_DATE_MALFORMED = "Improperly formed date"; // PDF-HUL-147
	public static final String ERR_DEST_OBJ_INVALID = "Invalid destination object"; // PDF-HUL-1
	public static final String ERR_DESTS_DICT_INVALID = "Invalid destinations dictionary"; // PDF-HUL-93, PDF-HUL-94
	public static final String ERR_DICT_CONTAINS_UNEXPECTED_TOKEN = "Unexpected token in dictionary"; // PDF-HUL-42
	public static final String ERR_DICT_DELIMITERS_IMPROPERLY_NESTED = "Improperly nested dictionary delimiters"; // PDF-HUL-32
	public static final String ERR_DICT_MALFORMED = "Malformed dictionary"; // PDF-HUL-41
	public static final String ERR_DOC_CAT_DICT_MISSING = "No document catalog dictionary";// PDF-HUL-87, PDF-HUL-88
	public static final String ERR_DOC_CAT_OBJ_NUM_INCNSTNT = "Document catalog dictionary object number and trailer root ref number are inconsistent."; // PDF-HUL-154
	public static final String ERR_DOC_CAT_TYPE_INVALID = "Document catalog Type key must have value Catalog"; // PDF-HUL-155
	public static final String ERR_DOC_CAT_NO_TYPE = "Document catalog has no Type key or it has a null value."; // PDF-HUL-156
	public static final String ERR_DOC_CAT_NOT_SIMPLE = "Document catalog Type key does not have a simple String value."; // PDF-HUL-157
	public static final String ERR_DOC_CAT_VERSION_INVALID = "Invalid Version in document catalog"; // PDF-HUL-90
	public static final String ERR_DOC_NODE_DICT_MISSING = "Missing dictionary in document node"; // PDF-HUL-2
	public static final String ERR_DOC_STRUCT_ATT_INVALID = "Invalid attribute in document structure"; // PDF-HUL-54
	public static final String ERR_DOC_STRUCT_ROOT_CONTAINS_INVALID_DATA = "Invalid data in document structure root";  // PDF-HUL-59, PDF-HUL-60
	public static final String ERR_DOC_STRUCT_ROOT_INVALID = "Invalid document structure root"; // PDF-HUL-57, PDF-HUL-58
	public static final String ERR_DOC_STRUCT_TREE_DATA_INVALID = "Invalid data in document structure tree"; // PDF-HUL-56
	public static final String ERR_ENCRYPT_DICT_ALG_INVALID = "Invalid algorithm value in encryption dictionary"; // PDF-HUL-97
	public static final String ERR_EOF_UNEXPECTED = "Unexpected EOF"; // PDF-HUL-62
	public static final String ERR_FILE_SPEC_INVALID = "Invalid file specification"; // PDF-HUL-8
	public static final String ERR_FILE_TRAILER_MISSING = "No file trailer"; // PDF-HUL-69
	public static final String ERR_FILTER_MALFORMED = "Malformed filter"; // PDF-HUL-44
	public static final String ERR_FIND_FONTS_ERR = "Unexpected error in findFonts"; // PDF-HUL-118
	public static final String ERR_FONT_PROP_PARSING = "Unexpected error in parsing font property"; // PDF-HUL-149
	public static final String ERR_HEX_STRING_CHAR_INVALID = "Invalid character in hex string"; // PDF-HUL-10, PDF-HUL-65
	public static final String ERR_INDIRECT_OBJ_REF_MALFORMED = "Malformed indirect object reference"; // PDF-HUL-43
	public static final String ERR_INLINE_STRUCT_ELE_CONTAINS_BLOCK_ATTS = "Block-level attributes in inline structure element"; // PDF-HUL-53
	public static final String ERR_IOEXCEP_READING = "An IOException was thrown reading";
	public static final String ERR_IOEXCEP_READING_DEST = ERR_IOEXCEP_READING + " destination array id: %d"; // PDF-HUL-2
	public static final String ERR_LITERAL_UNTERMINATED = "Unterminated literal in PDF file"; // PDF-HUL-9
	public static final String ERR_NAME_TREE_INVALID = "Invalid name tree"; // PDF-HUL-11, PDF-HUL-12, PDF-HUL-13, PDF-HUL-14, PDF-HUL-15
	public static final String ERR_NAMES_DICT_INVALID = "Invalid Names dictionary"; // PDF-HUL-91, PDF-HUL-92
	public static final String ERR_OBJ_DEF_INVALID = "Invalid object definition"; // PDF-HUL-34, PDF-HUL-35, PDF-HUL-36, PDF-HUL-27
	public static final String ERR_OBJ_NOT_PARSABLE = "Cannot parse object"; // PDF-HUL-38
	public static final String ERR_OBJ_STREAM_IMPROPER_NESTING = "Improper nesting of object streams"; // PDF-HUL-119
	public static final String ERR_OBJ_STREAM_OFFSET_OUT_OF_BOUNDS = "Offset out of bounds in object stream"; // PDF-HUL-16
	public static final String ERR_OBJ_STREAM_OR_NUMBER_INVALID = "Invalid object number or object stream"; // PDF-HUL-120, PDF-HUL-122
	public static final String ERR_OUTLINE_DICT_ITEM_INVALID = "Invalid outline dictionary item"; // PDF-HUL-138, PDF-HUL-139, PDF-HUL-140, PDF-HUL-143, PDF-HUL-144
	public static final String ERR_OUTLINE_DICT_MALFORMED = "Malformed outline dictionary"; // PDF-HUL-137
	public static final String ERR_PAGE_DICT_DATA_INVALID = "Invalid dictionary data for page"; // PDF-HUL-25, PDF-HUL-26, PDF-HUL-27, PDF-HUL-28
	public static final String ERR_PAGE_DICT_INVALID = "Invalid page dictionary"; // PDF-HUL-130
	public static final String ERR_PAGE_DICT_NO_TYPE = "Pages dictionary has no Type key or it has a null value."; // PDF-HUL-158
	public static final String ERR_PAGE_DICT_NOT_SIMPLE = "Pages dictionary Type key does not have a simple String value."; // PDF-HUL-159
	public static final String ERR_PAGE_DICT_OBJ_INVALID = "Invalid page dictionary object"; // PDF-HUL-102
	public static final String ERR_PAGE_DICT_TYPE_INVALID = "Pages dictionary Type key must have value /Pages."; // PDF-HUL-160
	public static final String ERR_PAGE_FONT_DICT_MISSING = "Expected dictionary for font entry in page resource"; // PDF-HUL-115
	public static final String ERR_PAGE_LABEL_INFO_INVALID = "Invalid page label info"; // PDF-HUL-126
	public static final String ERR_PAGE_LABEL_NODE_INVALID = "Invalid page label node"; // PDF-HUL-19
	public static final String ERR_PAGE_LABEL_SEQ_INVALID = "Invalid page label sequence"; // PDF-HUL-131
	public static final String ERR_PAGE_LABEL_STRUCT_PROBLEM = "Problem with page label structure"; // PDF-HUL-132
	public static final String ERR_PAGE_LABELS_BAD = "Bad page labels"; // PDF-HUL-123
	public static final String ERR_PAGE_NUMBER_DICT_ELEMENT_MISSING = "Missing expected element in page number dictionary"; // PDF-HUL-17
	public static final String ERR_PAGE_NUMBER_TREE_DATE_INVALID = "Invalid date in page number tree"; // PDF-HUL-18
	public static final String ERR_PAGE_TREE_ARTBOX_MALFORMED = "Malformed ArtBox in page tree"; // PDF-HUL-22
	public static final String ERR_PAGE_TREE_BLEEDBOX_MALFORMED = "Malformed BleedBox in page tree"; // PDF-HUL-24
	public static final String ERR_PAGE_TREE_DEPTH_EXCEEDED = "Excessive depth or infinite recursion in page tree structure"; // PDF-HUL-31
	public static final String ERR_PAGE_TREE_IMPROPERLY_CONSTRUCTED = "Improperly constructed page tree"; // PDF-HUL-30
	public static final String ERR_PAGE_TREE_MEDIA_BOX_MALFORMED = "Malformed MediaBox in page tree"; // PDF-HUL-6, PDF-HUL-7
	public static final String ERR_PAGE_TREE_MISSING = "Document page tree not found"; // PDF-HUL-101
	public static final String ERR_PAGE_TREE_NODE_INVALID = "Invalid page tree node"; // PDF-HUL-29
	public static final String ERR_PAGE_TREE_NODE_NOT_FOUND = "Page tree node not found."; // PDF-HUL-161
	public static final String ERR_PAGE_TREE_TRIMBOX_MALFORMED = "Malformed TrimBox in page tree"; // PDF-HUL-23
	public static final String ERR_PDF_HEADER_MISSING = "No PDF header"; // PDF-HUL-151
	public static final String ERR_PDF_MINOR_INVALID = "PDF minor version number is greater than 7."; // PDF-HUL-162
	public static final String ERR_PDF_TRAILER_MISSING = "No PDF trailer"; // PDF-HUL-152
	public static final String ERR_PREV_OFFSET_TRAILER_DICT_INVALID = "Invalid Prev offset in trailer dictionary"; // PDF-HUL-70
	public static final String ERR_RESOURCES_ENTRY_INVALID = "Invalid Resources Entry in document"; // PDF-HUL-3, PDF-HUL-4
	public static final String ERR_RESOURCES_FONT_ENTRY_INVALID = "Invalid Font entry in Resources"; // PDF-HUL-5
	public static final String ERR_INVALID_ROLE_MAP = "Invalid RoleMap"; // PDF-HUL-61
	public static final String ERR_SIZE_ENTRY_TRAILER_DICT_INVALID = "Invalid Size entry in trailer dictionary"; // PDF-HUL-71
	public static final String ERR_SIZE_ENTRY_TRAILER_DICT_MISSING = "Size entry missing in trailer dictionary"; // PDF-HUL-72
	public static final String ERR_STARTXREF_MISSING = "Missing startxref keyword or value"; // PDF-HUL-153
	public static final String ERR_STREAM_CONTAINS_MALFORMED_ASCII_NUMBER = "Malformed ASCII number in stream"; // PDF-HUL-45
	public static final String ERR_STREAM_EMBEDDED_IN_OBJ_STREAM = "Streams may not be embedded in object streams"; // PDF-HUL-46, PDF-HUL-47
	public static final String ERR_STRUCT_ATT_INVALID = "Invalid structure attribute"; // PDF-HUL-50, PDF-HUL-51
	public static final String ERR_STRUCT_ATT_REF_INVALID = "Invalid structure attribute reference"; // PDF-HUL-49
	public static final String ERR_STRUCT_ATT_TYPE_ILLEGAL = "Structure attribute has illegal type"; // PDF-HUL-52
	public static final String ERR_STRUCT_TREE_ELEMENT_UNKNOWN = "Unknown element in structure tree"; // PDF-HUL-48
	public static final String ERR_STRUCT_TYPE_NAME_NON_STANDARD = "Non-standard structure type name"; // PDF-HUL-55
	public static final String ERR_TOKEN_LEXICAL = "Lexical error"; // PDF-HUL-63, PDF-HUL-64
	public static final String ERR_TRAILER_DICT_INFO_KEY_NOT_DIRECT = "Trailer dictionary Info key is not an indirect reference"; // PDF-HUL-74
	public static final String ERR_TRAILER_DICT_ROOT_MISSING = "Root entry missing in trailer dictionary"; // PDF-HUL-73
	public static final String ERR_TRAILER_ID_INVALID = "Invalid ID in trailer"; // PDF-HUL-75, PDF-HUL-76, PDF-HUL-77
	public static final String ERR_UNEXPECTED_EXCEPTION = "Unexpected exception "; // PDF-HUL-100, PDF-HUL-106, PDF-HUL-111, PDF-HUL-114
	public static final String ERR_VECTOR_OBJ_COUNT_NOT_EVEN = MessageConstants.ERR_DICT_MALFORMED // PDF-HUL-41
			+ ": Vector must contain an even number of objects, but has ";
	public static final String ERR_XMP_INVALID = "Invalid or ill-formed XMP metadata"; // PDF-HUL-107, PDF-HUL-109
	public static final String ERR_XREF_STRM_DICT_ROOT_MISSING = "Root entry missing in cross-ref stream dictionary"; // PDF-HUL-68
	public static final String ERR_XREF_STRM_OBJ_NUM_INVALID = "Invalid object number in cross-reference stream"; // PDF-HUL-79
	public static final String ERR_XREF_STRM_MALFORMED = "Malformed cross-reference stream"; // PDF-HUL-80
	public static final String ERR_XREF_TABLE_INVALID = "Invalid cross-reference table"; // PDF-HUL-66, PDF-HUL-67
	public static final String ERR_XREF_TABLE_MALFORMED = "Malformed cross-reference table"; // PDF-HUL-82, PDF-HUL-83
	public static final String ERR_XREF_TABLE_OPERATOR_ILLEGAL = "Illegal operator in cross-reference table"; // PDF-HUL-84
	public static final String ERR_XREF_TABLES_BROKEN = "Cross-reference tables are broken"; // PDF-HUL-148
	public static final String ERR_INDIRECT_DEST_INVALID_1 = "Invalid indirect destination - referenced object '"; // PDF-HUL-163
	public static final String ERR_INDIRECT_DEST_INVALID_2 = "' cannot be found"; // PDF-HUL-163
}
