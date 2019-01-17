package edu.harvard.hul.ois.jhove.module.ascii;

import edu.harvard.hul.ois.jhove.TextMDMetadata;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 *          Created 19 May 2018:21:12:36
 */

public enum LineEnding {
	/**
	 * Carriage Return
	 */
	CR(TextMDMetadata.LINEBREAK_CR),
	/**
	 * Line Feed
	 */
	LF(TextMDMetadata.LINEBREAK_LF),
	/**
	 * Carriage Return / Line Feed
	 */
	CRLF(TextMDMetadata.LINEBREAK_CRLF);

	public final static String PROP_NAME = "LineEndings";

	public final int textMdVal;
	private LineEnding(final int textMdVal) {
		this.textMdVal = textMdVal;
	}

	public static LineEnding fromChars(final int lastChar,
			final int prevChar) {
		if (lastChar == ControlChar.LF.value) {
			if (prevChar == ControlChar.CR.value) {
				return CRLF;
			}
			return LF;
		}
		if (prevChar == ControlChar.CR.value) {
			return CR;
		}
		return null;
	}
}
