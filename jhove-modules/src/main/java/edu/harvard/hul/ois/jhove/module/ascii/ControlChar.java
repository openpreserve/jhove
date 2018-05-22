package edu.harvard.hul.ois.jhove.module.ascii;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

@SuppressWarnings("nls")
public enum ControlChar {
	NUL("NUL", 0x00, "NULL"),
	SOH("SOH", 0x01, "START OF HEADING"),
	STX("STX", 0x02, "START OF TEXT"),
	ETX("ETX", 0x03, "END OF TEXT"),
	EOT("EOT", 0x04, "END OF TRANSMISSION"),
	ENQ("ENQ", 0x05, "ENQUIRY"),
	ACK("ACK", 0x06, "ACKNOWLEDGE"),
	BEL("BEL", 0x07, "BELL"),
	BS("BS", 0x08, "BACKSPACE"),
	HT("HT", 0x09, "HORIZONTAL TABULATION"),
	LF("LF", 0x0A, "LINE FEED"),
	VT("VT", 0x0B, "VERTICAL TABULATION"),
	FF("FF", 0x0C, "FORM FEED"),
	CR("CR", 0x0D, "CARRIAGE RETURN"),
	SO("SO", 0x0E, "SHIFT-OUT"),
	SI("SI", 0x0F, "SHIFT-IN"),
	DLE("DLE", 0x10, "DATA LINK ESCAPE"),
	DC1("DC1", 0x11, "DEVICE CONTROL ONE"),
	DC2("DC2", 0x12, "DEVICE CONTROL TWO"),
	DC3("DC3", 0x13, "DEVICE CONTROL THREE"),
	DC4("DC4", 0x14, "DEVICE CONTROL FOUR"),
	NAK("NAK", 0x15, "NEGATIVE ACKNOWLEDGE"),
	SYN("SYN", 0x16, "SYNCHRONOUS IDLE"),
	ETB("ETB", 0x17, "END OF TRANSMISSION BLOCK"),
	CAN("CAN", 0x18, "CANCEL"),
	EM("EM", 0x19, "END OF MEDIUM"),
	SUB("SUB", 0x1A, "SUBSTITUTE CHARACTER"),
	ESC("ESC", 0x1B, "ESCAPE"),
	FS("FS", 0x1C, "FILE SEPARATOR (INFORMATION SEPARATOR FOUR"),
	GS("GS", 0x1D, "GROUP SEPARATOR (INFORMATION SEPARATOR THREE"),
	RS("RS", 0x1E, "RECORD SEPARATOR (INFORMATION SEPARATOR TWO"),
	US("US", 0x1F, "UNIT SEPARATOR (INFORMATION SEPARATOR ONE"),
	DEL("DEL", 0x7F, "DELETE");

	/** JHOVE reporting property name */
	public final static String PROP_NAME = "ControlCharacters";

	/** The three character code */
	public final String code;
	/** The byte value of the character. **/
	public final int value;
	/** The control character's ANSI name **/
	public final String ansiName;
	/**
	 * JHOVE's reporting mnemonic, the code followed by the in value in hex.
	 **/
	public final String mnemonic;

	private ControlChar(final String code, final int value, final String ansiName) {
		this.code = code;
		this.value = value;
		this.ansiName = ansiName;
		this.mnemonic = String.format("%s (0x%02X)", code, Integer.valueOf(value));
	}

	/**
	 * Tests whether the passed ControlChar toTest is a line ending character.
	 *
	 * @param toTest
	 *            the ControlChar to test
	 * @return true if toTest is a CR or LF character.
	 */
	public final static boolean isLineEndChar(final ControlChar toTest) {
		return (toTest == ControlChar.CR || toTest == ControlChar.LF);
	}

	/**
	 * Returns the appropriate control character for an int character value.
	 *
	 * @param value
	 *            the int value of a possible ASCII character
	 * @return the control character with matching ASCII value or null
	 *         if the value is out of the ASCII control character range.
	 */
	public final static ControlChar fromIntValue(final int charVal) {
		if (charVal > US.value) {
			return (charVal == DEL.value) ? DEL : null;
		}
		for (ControlChar ctrlChar : ControlChar.values()) {
			if (ctrlChar.value == charVal) return ctrlChar;
		}
		// Must be a negative charVal
		return null;
	}
}
