package edu.harvard.hul.ois.jhove.module.ascii;

import edu.harvard.hul.ois.jhove.TextMDMetadata;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public enum LineEnding {
	/** Carriage Return */
	CR(TextMDMetadata.LINEBREAK_CR),
	/** Line Feed */
	LF(TextMDMetadata.LINEBREAK_LF),
	/** Carriage Return / Line Feed */
	CRLF(TextMDMetadata.LINEBREAK_CRLF);

	/** JHOVE reporting property name */
	public static final String PROP_NAME = "LineEndings";

	/** The TextMD line-break type, an int index to a message array */
	public final int textMdVal;

	private LineEnding(final int textMdVal) {
		this.textMdVal = textMdVal;
	}

	/**
	 * Performs JHOVE's line ending type test, using the last 2 read characters.
	 *
	 * @param lastChar
	 *            the last character read from stream
	 * @param prevChar
	 *            the previous character to the lastChar
	 * @return the appropriate line ending type, or null if the character's
	 *         aren't a line ending combination
	 */
	public static LineEnding fromControlChars(final ControlChar lastChar, final ControlChar prevChar) {
		if (lastChar == ControlChar.LF) {
			if (prevChar == ControlChar.CR) {
				return CRLF;
			}
			return LF;
		}
		if (prevChar == ControlChar.CR) {
			return CR;
		}
		return null;
	}
}
