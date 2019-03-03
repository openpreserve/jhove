package edu.harvard.hul.ois.jhove.module.jpeg2000;

/**
 * Enum used to externalise the JPEG 2000 modules message Strings. Using an
 * enum INSTANCE as a "trick" to ensure a single instance of the class. String
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
 * <li>ENTITY_NAME: the name of the JPEG 2000 entity causing the prohlem, e.g.
 * BOX, FILE_TYPE_BOX, COC_MARKER, etc.</li>
 * <li>PROBLEM_LOCATION: the location or component inside the entity, e.g.
 * POSITION, SIZE, TYPE, NUMBER_OF_ENTRIES, etc.</li>
 * <li>PROBLEM_TYPE: a short indicator of the problem type, e.g. MISSING,
 * INVALID, EMPTY, OVERRUN, UNDERRUN, etc.</li>
 * </ol>
 * The elements should be separated by underscores. The messages currently
 * don't follow a consistent vocabulary, that is terms such as invalid,
 * illegal, or malformed are used without definition.
 * 
 * @author <a href="mailto:martin@hoppenheit.info">Martin Hoppenheit</a>
 *         <a href="https://github.com/marhop">marhop AT github</a>
 * @version 0.1 Created 27 Apr 2017:09:39:41
 */

public enum MessageConstants {
    INSTANCE;

    /**
     * Warning messages
     */
    // None yet.

    /**
     * Information messages
     */
    public static final String INF_BINARY_FILTER_BOX_NOT_GZIP =
        "Binary Filter Box of type other than Gzip, contents not processed";
    public static final String INF_FRAGMENT_LIST_BOX_EXT_FILE_REFERENCE =
        "Document references an external file";

    /**
     * Error messages
     */
//    public static final String ERR_BOX_CONTEXT_INVALID =
//        "Invalid context for ";
//    public static final String ERR_BOX_CONTENT_EMPTY =
//        "Box is empty";
//    public static final String ERR_BOX_SIZE_INVALID =
//        "Incorrect Box size for ";
//    public static final String ERR_COC_MARKER_POSITION_INVALID =
//        "COC marker segment at wrong position in codestream";
//    public static final String ERR_CODESTREAM_HEADER_BOX_START_INVALID =
//        "First box of Codestream Header must be image header";
//    public static final String ERR_CODESTREAM_CONTENT_INVALID =
//        "Ill-formed codestream";
//    public static final String ERR_CODESTREAM_MARKER_SEGMENT_INVALID =
//        "Invalid marker segment";
//    public static final String ERR_COLOR_SPEC_BOX_ICC_PROFILE_INVALID =
//        "Color spec box with method 2 has unrecognized ICC profile";
//    public static final String ERR_COMPOSITION_BOX_START_INVALID =
//        "First box in Composition Box must be Composition Options Box";
//    public static final String ERR_COMP_LAYER_HEADER_BOX_OPACITY_WITH_CHANNEL =
//        "Compositing Layer Header may not have both Opacity and Channel " +
//        "Definition Boxes";
//    public static final String ERR_COM_MARKER_TYPE_INVALID =
//        "Unrecognized comment type";
//    public static final String ERR_CRG_MARKER_POSITION_INVALID =
//        "CRG header allowed only in main header of codestream";
//    public static final String ERR_CRG_MARKER_SIZE_INVALID =
//        "CRG marker segment has incorrect length";
//    public static final String ERR_CROSS_REF_BOX_FRAGMENT_LIST_BOX_MISSING =
//        "Cross Reference Box does not contain Fragment List Box";
//    public static final String ERR_DATA_ENTRY_URL_BOX_VERSION_FLAG_INVALID =
//        "Unrecognized version or flag value in Data Entry URL Box";
//    public static final String ERR_DIGITAL_SIGNATURE_BOX_PTR_TYPE_INVALID =
//        "Unknown digital signature pointer type";
//    public static final String ERR_DIGITAL_SIGNATURE_BOX_TYPE_INVALID =
//        "Unknown digital signature type";
//    public static final String ERR_FILE_TYPE_BOX_COMPAT_ITEM_NON_ASCII =
//        "Non-ASCII characters in compatibility item of File Type Box";
//    public static final String ERR_FILE_TYPE_BOX_COMPAT_LIST_EMPTY =
//        "Empty compatibility list in File Type Box";
//    public static final String ERR_FILE_TYPE_BOX_POSITION_INVALID =
//        "Expected File Type Box, got ";
//    public static final String ERR_FRAGMENT_LIST_BOX_TABLE_SIZE_INVALID =
//        "Fragment Table has invalid length";
//    public static final String ERR_FRAGMENT_TABLE_BOX_CONTENT_INVALID =
//        "Invalid fragment table";
//    public static final String ERR_IMAGE_HEADER_BOX_COMPONENTS_EMPTY =
//        "ImageHeader Box has zero components";
//    public static final String ERR_IMAGE_HEADER_BOX_CONTEXT_INVALID =
//        "ImageHeader Box in illegal context";
//    public static final String ERR_IMAGE_HEADER_BOX_SIZE_INVALID =
//        "Image Header Box is incorrect size";
//    public static final String ERR_INSTRUCTION_SET_BOX_DATA_OVERRUN =
//        "Data overrun in Instruction Set Box";
//    public static final String ERR_INSTRUCTION_SET_BOX_DATA_UNDERRUN =
//        "Data underrun in Instruction Set Box";
//    public static final String ERR_JP2_HEADER_BOX_COMPONENT_MISSING =
//        "JP2 Header has Palette box without Component Mapping Box";
//    public static final String ERR_JP2_HEADER_BOX_PALETTE_MISSING =
//        "JP2 Header has Component Mapping box without Palette Box";
//    public static final String ERR_JP2_HEADER_BOX_REDUNDANT =
//        "Multiple JP2 Header Boxes not allowed";
//    public static final String ERR_JP2_HEADER_BOX_START_INVALID =
//        "First box of JP2 header must be image header";
//    public static final String ERR_JP2_HEADER_CONTENT_MISSING =
//        "Other boxes may not occur before JP2 Header";
//    // TODO This should be rewritten to something like "Invalid JPEG 2000 file
//    // format signature".
//    public static final String ERR_JP2_SIGNATURE_INVALID =
//        "No JPEG 2000 header";
//    public static final String ERR_OPACITY_BOX_OTYP_INVALID =
//        "Invalid OTyp field in Opacity Box";
//    public static final String ERR_PALETTE_BOX_NUMBER_OF_ENTRIES_INVALID =
//        "Palette must have 1 to 1024 entries";
//    public static final String ERR_PLM_MARKER_PACKET_SIZE_INVALID =
//        "Packet length in PLM marker segment crosses segment boundaries";
//    public static final String ERR_PLT_MARKER_PACKET_SIZE_INVALID =
//        "Packet length in PLT marker segment crosses segment boundaries";
//    public static final String ERR_PLT_MARKER_POSITION_INVALID =
//        "PLT marker segment not allowed in codestream header";
//    public static final String ERR_POC_MARKER_POSITION_INVALID =
//        "POC marker segment at wrong position in codestream";
//    public static final String ERR_POC_MARKER_SIZE_INVALID =
//        "Invalid size for POC marker segment";
//    public static final String ERR_PPM_MARKER_TILE_PART_HEADER_SIZE_INVALID =
//        "Invalid length for tile-part header in PPM packet";
//    public static final String ERR_PPT_MARKER_POSITION_INVALID =
//        "PPT not allowed in codestream header";
//    public static final String ERR_PPT_MARKER_WITH_PPM_MARKER =
//        "PPT and PPM not allowed in same codestream";
//    public static final String ERR_QCC_MARKER_POSITION_INVALID =
//        "QCC marker segment at wrong position in codestream";
//    public static final String ERR_QCC_MARKER_QUANTIZATION_TYPE_INVALID =
//        "Unrecognized quantization type in QCC marker segment";
//    public static final String ERR_QCD_MARKER_QUANTIZATION_TYPE_INVALID =
//        "Unrecognized quantization type in QCD marker segment";
//    public static final String ERR_READER_REQUIREMENTS_BOX_CONTENT_INVALID =
//        "Invalid data in Reader Requirements box";
//    public static final String ERR_RGN_MARKER_POSITION_INVALID =
//        "RGN marker segment at wrong position in codestream";
//    public static final String ERR_ROI_BOX_CONTENT_INVALID =
//        "Invalid data in ROI";
//    public static final String ERR_ROI_BOX_REGION_TYPE_INVALID =
//        "Invalid region type in ROI Box";
//    public static final String ERR_SUPERBOX_OVERRUN =
//        "Size of contained Box overruns ";
//    public static final String ERR_SUPERBOX_UNDERRUN =
//        "Size of contained Boxes underruns ";
//    public static final String ERR_TLM_MARKER_ST_VALUE_INVALID =
//        "Invalid ST value in TLM marker segment";
//    // TODO Use generic ERR_SUPERBOX_UNDERRUN instead of this?
//    public static final String ERR_UUID_INFO_BOX_UNDERRUN =
//        "Size of contained boxes underruns UUID Info Box";
}
