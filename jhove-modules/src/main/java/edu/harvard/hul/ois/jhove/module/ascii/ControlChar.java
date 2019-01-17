package edu.harvard.hul.ois.jhove.module.ascii;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 19 May 2018:19:08:50
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

	public final static String PROP_NAME = "ControlCharacters";

	public final String name;
	public final int value;
	public final String mnemonic;

	private ControlChar(final String name, final int value) {
		this.name = name;
		this.value = value;
		this.mnemonic = String.format("%s (0x%02X)", name, Integer.valueOf(value));
	}

	public final static boolean isLineEndChar(final ControlChar toTest) {
		return (toTest == ControlChar.CR || toTest == ControlChar.LF);
	}

	public final static ControlChar fromValue(final int value) {
		ControlChar retVal = null;
		for (ControlChar ctrlChar : ControlChar.values()) {
			if (ctrlChar.value == value) retVal = ctrlChar;
		}
		return retVal;
	}
}
