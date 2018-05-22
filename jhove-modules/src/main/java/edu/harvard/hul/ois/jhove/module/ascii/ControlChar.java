package edu.harvard.hul.ois.jhove.module.ascii;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public enum ControlChar {
	NUL("NUL", 0x00),
	SOH("SOH", 0x01),
	STX("STX", 0x02),
	ETX("ETX", 0x03),
	EOT("EOT", 0x04),
	ENQ("ENQ", 0x05),
	ACK("ACK", 0x06),
	BEL("BEL", 0x07),
	BS("BS", 0x08),
	TAB("TAB", 0x09),
	LF("LF", 0x0A),
	VT("VT", 0x0B),
	FF("FF", 0x0C),
	CR("CR", 0x0D),
	SO("SO", 0x0E),
	SI("SI", 0x0F),
	DLE("DLE", 0x10),
	DC1("DC1", 0x11),
	DC2("DC2", 0x12),
	DC3("DC3", 0x13),
	DC4("DC4", 0x14),
	NAK("NAK", 0x15),
	SYN("SYN", 0x16),
	ETB("ETB", 0x17),
	CAN("CAN", 0x18),
	EM("EM", 0x19),
	SUB("SUB", 0x1A),
	ESC("ESC", 0x1B),
	FS("FS", 0x1C),
	GS("GS", 0x1D),
	RS("RS", 0x1E),
	US("US", 0x1F),
	DEL("DEL", 0x7F);

	/** JHOVE reporting property name */
	public final static String PROP_NAME = "ControlCharacters";

	/** The three character code */
	public final String code;
	/** The byte value of the character. **/
	public final int value;
	/**
	 * JHOVE's reporting mnemonic, the code followed by the int value in hex.
	 **/
	public final String mnemonic;

	private ControlChar(final String code, final int value) {
		this.code = code;
		this.value = value;
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
