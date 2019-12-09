package edu.harvard.hul.ois.jhove.module.ascii;

import java.util.EnumSet;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */
@SuppressWarnings("nls")
public enum ControlChar {
	/**
	 * 7 bit ASCII 0x00 - 0x1F + 0x7F
	 */
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
	DEL("DEL", 0x7F, "DELETE"),
	/**
	 * Unicode Extensions 0x80 - 0x9F
	 * Currently not used
	 */
	PAD("PAD", 0X80, "PADDING CHARACTER"),
	HOP("HOP", 0X81, "HIGH OCTET PRESET"),
	BPH("BPH", 0X82, "BREAK PERMITTED HERE"),
	NBH("NBH", 0X83, "NO BREAK HERE"),
	IND("IND", 0X84, "INDEX"),
	NEL("NEL", 0X85, "NEXT LINE"),
	SSA("SSA", 0X86, "START OF SELECTED AREA"),
	ESA("ESA", 0X87, "END OF SELECTED AREA"),
	HTS("HTS", 0X88, "HORIZONTAL TAB SET"),
	HTJ("HTJ", 0X89, "HORIZONTAL TAB JUSTIFIED"),
	VTS("VTS", 0X8A, "VERTICAL TAB SET"),
	PLD("PLD", 0X8B, "PARTIAL LINE FORWARD"),
	PLU("PLU", 0X8C, "PARTIAL LINE BACKWARD"),
	RI("RI", 0X8D, "REVERSE LINE FEED"),
	SS2("SS2", 0X8E, "SINGLE-SHIFT 2"),
	SS3("SS3", 0X8F, "SINGLE-SHIFT 2"),
	DCS("DCS", 0X90, "DEVICE CONTROL STRING"),
	PU1("PU1", 0X91, "PRIVATE USE 1"),
	PU2("PU2", 0X92, "PRIVATE USE 2"),
	STS("STS", 0X93, "SET TRANSMIT STATE"),
	CCH("CCH", 0X94, "CANCEL CHARACTER"),
	MW("MW", 0X95, "MESSAGE WAITING"),
	SPA("SPA", 0X96, "START OF PROTECTED AREA"),
	EPA("EPA", 0X97, "ENDO OF PROTECTED AREA"),
	SOS("SOS", 0X98, "START OF STRING"),
	SGCI("SCGI", 0X99, "SINGLE GRAPHIC CHAR INTRO"),
	SCI("SCI", 0X9A, "SINGLE CHAR INTRO"),
	CSI("CSI", 0X9B, "CONTROL SEQUENCE INTRO"),
	ST("ST", 0X9C, "STRING TERMINATOR"),
	OSC("OSC", 0X9D, "OS COMMAND"),
	PM("PM", 0X9E, "PRIVATE MESSAGE"),
	APC("APC", 0X9F, "APP PROGRAM COMMAND");

	/** JHOVE reporting property name */
	public final static String PROP_NAME = "ControlCharacters";
	/** Set of ASCII 7 bit Control Chars */
	public final static EnumSet<ControlChar> ASCII = EnumSet.range(NUL, DEL);
	/** Set of Control Chars that are only Unicode */
	public final static EnumSet<ControlChar> UNICODE_EXTENSIONS = EnumSet.complementOf(ASCII);
	/** Set of Unicode Control Chars, {@code ASCII} + {@code UNICODE_EXTENSIONS} */
	public final static EnumSet<ControlChar> UNICODE = EnumSet.allOf(ControlChar.class);


	/** The three character code */
	public final String code;
	/** The byte value of the character. **/
	public final int value;
	/** The control character's ANSI name **/
	public final String ansiName;
	
	/**
	 * JHOVE's reporting mnemonic, the code followed by the int value in hex.
	 **/
	public final String mnemonic;

	private ControlChar(final String code, final int value, final String ansiName) {
		this.code = code;
		this.value = value;
		this.ansiName = ansiName;
		this.mnemonic = String.format("%s (0x%02X)", code, value);
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
	 * Returns the appropriate ASCII control character for an int character value.
	 *
	 * @param charVal
	 *            the int value of a possible ASCII character
	 * @return the control character with matching ASCII value or null
	 *         if the value is out of the ASCII control character range.
	 */
	public final static ControlChar asciiFromInt(final int charVal) {
		if (charVal > US.value) {
			return (charVal == DEL.value) ? DEL : null;
		}
		for (ControlChar ctrlChar : ControlChar.values()) {
			if (ctrlChar.value == charVal) return ctrlChar;
		}
		// Must be a negative charVal
		return null;
	}

	public final static ControlChar fromMnemonic(final String mnemonic) {
		for (ControlChar ctrlChar : ControlChar.values()) {
			if (ctrlChar.mnemonic.equalsIgnoreCase(mnemonic)) return ctrlChar;
		}
		// Must be a negative charVal
		return null;
	}
}
