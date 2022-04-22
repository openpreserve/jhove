package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.IOException;

import edu.harvard.hul.ois.jhove.ErrorMessage;

/**
 * Simple class that is the a prototype of a proper header parser class. The aim
 * was to introduce a simple version check for the PDF/A minor version number,
 * see {@link PdfHeader#isVersionValid()}, while not changing anything else
 * through over ambition.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Mar 2018:00:46:39
 */

public final class PdfHeader {
	public static final String PDF_VER1_HEADER_PREFIX = "PDF-1."; //$NON-NLS-1$
	public static final String PDF_SIG_HEADER = "%" + PDF_VER1_HEADER_PREFIX; //$NON-NLS-1$
	public static final String POSTSCRIPT_HEADER_PREFIX = "!PS-Adobe-"; //$NON-NLS-1$
	public static final int MAX_VALID_MAJOR_VERSION = 7;

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
	 * Performs a very simple version number validity check. Given version
	 * number is a String of form 1.x, x is the minor version number. This
	 * method parses the minor version number from the version String and tests
	 * whether it is less than or equal to
	 * {@link PdfHeader#MAX_VALID_MAJOR_VERSION}.
	 *
	 * @return true if an integer minor version number can be parsed from the
	 *         version string AND it is less than or equal to
	 *         {@link PdfHeader#MAX_VALID_MAJOR_VERSION}. Otherwise false.
	 */
	public boolean isVersionValid() {
		// Set minor version to one larger than maximum so invalid if parse
		// fails
		int minorVersion = MAX_VALID_MAJOR_VERSION + 1;
		try {
			minorVersion = getMinorVersion(this.versionString);
		} catch (NumberFormatException nfe) {
			// TODO : This currently catches non-numbers and
			// returns false. This marks the version number
			// as invalid and ensured existing JHOVE behaviour
			// changed as little as possible for v1.20 March 2018.
			// Really this should be thrown as it's own validation
			// exception and be assigned its own message
			// Version numbers need better handling as PDF1. is
			// baked into JHOVE's header signature rather than
			// as part of version parsing and validation.
			// The arrival of PDF 2.0 in summer 2017 leaves
			// this looking very dubious behaviour.
		}
		return minorVersion <= MAX_VALID_MAJOR_VERSION;
	}

	/**
	 * Creates a new {@link PdfHeader} instance using the passed parameters.
	 *
	 * @param versionString
	 *            the version number from the PDF Header, should be of form
	 *            <code>1.x</code> where x should be of the range 0-7.
	 * @param isPdfaCompliant
	 *            boolean flag indicating if the PDF/A is compliant or non
	 *            compliant with JHOVE's PDF/A profile.
	 * @return a {@link PdfHeader} instance initialised using
	 *         <code>versionString</code> and <code>isPdfaCompliant</code>.
	 * @throws NullPointerException
	 *             when parameter <code>versionString</code> is null.
	 */
	static PdfHeader fromValues(final String versionString,
			final boolean isPdfaCompliant) {
		if (versionString == null)
			throw new NullPointerException(
					"Parameter versionString can not be null.");
		return new PdfHeader(versionString, isPdfaCompliant);
	}

	/**
	 * Factory method for {@link PdfHeader} that parses a new instance using the
	 * supplied {@link Parser} instance.
	 *
	 * @param parser
	 *            the {@link Parser} instance that will be used to parse header
	 *            details
	 * @return a new {@link PdfHeader} instance derived using the supplied
	 *         {@link Parser} or <code>null</code> when no header could be found
	 *         and parsed.
	 * @throws PdfMalformedException 
	 */
	public static PdfHeader parseHeader(final Parser parser) throws PdfMalformedException {
		Token token = null;
		String value = null;
		boolean isPdfACompliant = false;
		String version = null;

		/* Parse file header. */
		for (;;) {
			if (parser.getOffset() > 1024) {
				return null;
			}
			try {
				token = null;
				token = parser.getNext(1024L);
			} catch (IOException ee) {
				return null;
			} catch (Exception e) {
				// fall through
			}

			if (token == null) {
				return null;
			}
			if (token instanceof Comment) {
				value = ((Comment) token).getValue();
				if (value.indexOf(PDF_VER1_HEADER_PREFIX) == 0) {
					try {
						version = value.substring(4, 7);
					} catch (IndexOutOfBoundsException e ){
						 throw new PdfMalformedException(MessageConstants.PDF_HUL_155); // PDF-HUL-155
					}
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

		try {
			isPdfACompliant = isTokenPdfACompliant(parser.getNext());
		} catch (Exception excep) {
			// Most likely a ClassCastException on a non-comment
			isPdfACompliant = false;
		}
		// Check for PDF/A conformance. The next item must be
		// a comment with four characters, each greater than 127
		return new PdfHeader(version, isPdfACompliant);
	}

	private static int getMinorVersion(final String version) {
		double doubleVer = Double.parseDouble(version);
		double fractPart = doubleVer % 1;
		int minor = (int) (10L * fractPart);
		return minor;
	}

	private static boolean isTokenPdfACompliant(final Token token) {
		String cmt = ((Comment) token).getValue();
		char[] cmtArray = cmt.toCharArray();
		int ctlcnt = 0;
		for (int i = 0; i < 4; i++) {
			if (cmtArray[i] > 127) {
				ctlcnt++;
			}
		}
		return (ctlcnt > 3);
	}
}
