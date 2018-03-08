/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.IOException;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Mar 2018:00:46:39
 */

public final class PdfHeader {
	public static final String PDF_VER1_HEADER_PREFIX = "PDF-1.";
	public static final String PDF_SIG_HEADER = "%" + PDF_VER1_HEADER_PREFIX;
	public static final String POSTSCRIPT_HEADER_PREFIX = "!PS-Adobe-";
	
	private final int majorVersion;
	private final int minorVersion;

	
	/**
	 * 
	 */
	private PdfHeader(final int majorVersion, final int minorVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public static boolean parseHeader(final RepInfo info, final Parser _parser, final String _name) throws IOException {
		Token token = null;
		String value = null;

		/* Parse file header. */

		boolean foundSig = false;
		for (;;) {
			if (_parser.getOffset() > 1024) {
				break;
			}
			try {
				token = null;
				token = _parser.getNext(1024L);
			} catch (IOException ee) {
				break;
			} catch (Exception e) {
			} // fall through
			if (token == null) {
				break;
			}
			if (token instanceof Comment) {
				value = ((Comment) token).getValue();
				if (value.indexOf(PDF_VER1_HEADER_PREFIX) == 0) {
					foundSig = true;
					_version = value.substring(4, 7);
					/*
					 * If we got this far, take note that the signature is OK.
					 */
					info.setSigMatch(_name);
					break;
				}
				// The implementation notes (though not the spec)
				// allow an alternative signature of %!PS-Adobe-N.n PDF-M.m
				if (value.indexOf(POSTSCRIPT_HEADER_PREFIX) == 0) {
					// But be careful: that much by itself is the standard
					// PostScript signature.
					int n = value.indexOf(PDF_VER1_HEADER_PREFIX);
					if (n >= 11) {
						foundSig = true;
						_version = value.substring(n + 4);
						// However, this is not PDF-A compliant.
						_pdfACompliant = false;
						info.setSigMatch(_name);
						break;
					}
				}
			}

			// If we don't find it right at the beginning, we aren't
			// PDF/A compliant.
			_pdfACompliant = false;
		}
		if (!foundSig) {
			info.setWellFormed(false);
			info.setMessage(new ErrorMessage(
					MessageConstants.ERR_PDF_HEADER_MISSING, 0L));
			return false;
		}
		// Check for PDF/A conformance. The next item must be
		// a comment with four characters, each greater than 127
		try {
			token = _parser.getNext();
			String cmt = ((Comment) token).getValue();
			char[] cmtArray = cmt.toCharArray();
			int ctlcnt = 0;
			for (int i = 0; i < 4; i++) {
				if (cmtArray[i] > 127) {
					ctlcnt++;
				}
			}
			if (ctlcnt < 4) {
				_pdfACompliant = false;
			}
		} catch (Exception e) {
			// Most likely a ClassCastException on a non-comment
			_pdfACompliant = false;
		}
		return true;
	}

}
