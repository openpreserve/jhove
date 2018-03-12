/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.IOException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Mar 2018:00:46:39
 */

public final class PdfHeader {
	public static final String PDF_VER1_HEADER_PREFIX = "PDF-1."; //$NON-NLS-1$
	public static final String PDF_SIG_HEADER = "%" + PDF_VER1_HEADER_PREFIX; //$NON-NLS-1$
	public static final String POSTSCRIPT_HEADER_PREFIX = "!PS-Adobe-"; //$NON-NLS-1$

	private final String versionString;
	private final boolean isPdfACompilant;

	/**
	 * 
	 */
	private PdfHeader(final String versionString,
			final boolean isPdfaCompliant) {
		this.versionString = versionString;
		this.isPdfACompilant = isPdfaCompliant;
	}

	/**
	 * @return the version string parsed from the PDF Header
	 */
	public String getVersionString() {
		return this.versionString;
	}

	/**
	 * @return true if the header is considered PDF/A compliant, otherwise false
	 */
	public boolean isPdfACompliant() {
		return this.isPdfACompilant;
	}

	/**
	 * Factory method for {@link PdfHeader} that parses a new instance using the
	 * supplied {@link Parser} instance.
	 * 
	 * @param _parser
	 *            the {@link Parser} instance that will be used to parse header
	 *            details
	 * @return a new {@link PdfHeader} instance derived using the supplied
	 *         {@link Parser} or <code>null</code> when no header could be found
	 *         and parsed.
	 */
	public static PdfHeader parseHeader(final Parser _parser) {
		Token token = null;
		String value = null;
		boolean isPdfACompliant = false;
		String version = null;

		/* Parse file header. */
		for (;;) {
			if (_parser.getOffset() > 1024) {
				return null;
			}
			try {
				token = null;
				token = _parser.getNext(1024L);
			} catch (IOException ee) {
				return null;
			} catch (Exception e) {
			} // fall through

			if (token == null) {
				return null;
			}
			if (token instanceof Comment) {
				value = ((Comment) token).getValue();
				if (value.indexOf(PDF_VER1_HEADER_PREFIX) == 0) {
					version = value.substring(4, 7);
					isPdfACompliant = true;
					break;
				}
				// The implementation notes (though not the spec)
				// allow an alternative signature of %!PS-Adobe-N.n PDF-M.m
				if (value.indexOf(POSTSCRIPT_HEADER_PREFIX) == 0) {
					// But be careful: that much by itself is the standard
					// PostScript signature.
					int n = value.indexOf(PDF_VER1_HEADER_PREFIX);
					if (n >= 11) {
						version = value.substring(n + 4);
						break;
					}
				}
			}
		}

		if (version == null) {
			return null;
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
				isPdfACompliant = false;
			}
		} catch (Exception e) {
			// Most likely a ClassCastException on a non-comment
			isPdfACompliant = false;
		}
		return new PdfHeader(version, isPdfACompliant);
	}

}
