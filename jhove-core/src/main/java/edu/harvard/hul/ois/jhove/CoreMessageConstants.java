package edu.harvard.hul.ois.jhove;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 *
 * Created 2 Nov 2017:09:24:33
 */

public enum CoreMessageConstants {
	INSTANCE;

    public static final JhoveMessageFactory messageFactory = JhoveMessages
            .getInstance("edu.harvard.hul.ois.jhove.ErrorMessages");

    public static final JhoveMessage JHOVE_CORE_1 = messageFactory
            .getMessage("JHOVE-CORE-1");
    public static final JhoveMessage JHOVE_CORE_2 = messageFactory
            .getMessage("JHOVE-CORE-2");
    public static final JhoveMessage JHOVE_CORE_3 = messageFactory
            .getMessage("JHOVE-CORE-3");
    public static final JhoveMessage JHOVE_CORE_4 = messageFactory
            .getMessage("JHOVE-CORE-4");
    public static final JhoveMessage JHOVE_CORE_5 = messageFactory
            .getMessage("JHOVE-CORE-5");

	public static final String EXC_CHAR_ENC_UNSPPTD = "Unsupported character encoding: ";
	public static final String EXC_CONF_FILE_LOC_MISS = "Initialization exception; location not specified for configuration file.";
	public static final String EXC_CONF_FILE_INVAL = "Use -c to specify a configuration file. Path not found or not readable: ";
	public static final String EXC_CONF_FILE_UNRDBL = "Cannot read configuration file: ";
	public static final String EXC_CONF_FILE_UNPRS = "Error parsing configuration file: ";
	public static final String EXC_FILE_OPEN = "Cannot open output file: ";
	public static final String EXC_HNDL_INST_FAIL = "Cannot instantiate handler: ";
	public static final String EXC_JAVA_VER_INCMPT = "Java 1.8 or higher is required";
	public static final String EXC_MODL_INST_FAIL = "Cannot instantiate module: ";
	public static final String EXC_PRV_CNSTRCT = "Entered private constructor for: ";
	public static final String EXC_PROP_VAL_NULL = "Null value not permitted for property: ";
	public static final String EXC_SAX_PRSR_MISS = "SAX parser not found: ";
	public static final String EXC_PROP_CLSS_INCMPT = "Incompatible class for property: ";
	public static final String EXC_SCL_PROP_CLSS_INCMPT = "Scalar.";
	public static final String EXC_MAP_PROP_CLSS_INCMPT = "Map.";
	public static final String EXC_SET_PROP_CLSS_INCMPT = "Set.";
	public static final String EXC_LIST_PROP_CLSS_INCMPT = "List.";
	public static final String EXC_TEMP_FILE_CRT = "Cannot create temporary file";
	public static final String EXC_URI_CONV_FAIL = "Cannot convert URI to URL: ";
	public static final String EXC_URL_NOT_FND = "URL not found: ";

	public static final String ERR_APP_PROP_MISS = "No application properties found for: ";
	public static final String ERR_ICC_PRFL_DESC_MISS = "No description in ICC profile v4";
}
