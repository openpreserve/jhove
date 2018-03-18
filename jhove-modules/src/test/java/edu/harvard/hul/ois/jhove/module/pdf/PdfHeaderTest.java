package edu.harvard.hul.ois.jhove.module.pdf;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.PdfModule;
import edu.harvard.hul.ois.jhove.module.TestUtils;

/**
 * Tests for the {@link PdfHeader} class and for JHOVE's PDF Header validation
 * capabilities.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Mar 2018:11:28:10
 */
@SuppressWarnings("static-method")
public class PdfHeaderTest {
	private static final String pdfResourcePath = "/edu/harvard/hul/ois/jhove/module/pdf/";
	private static final String minimalPdfPath = pdfResourcePath
			+ "T01_000_minimal.pdf";
	private static final String invalidMinorPath = pdfResourcePath
			+ "T01_002_header-invalid-minor-version.pdf";

	private PdfModule module;

	@Before
	public void setUp() throws Exception {
		this.module = new PdfModule();
		JhoveBase je = new JhoveBase();
		this.module.setBase(je);
	}

	/**
	 * Test method for {@link edu.harvard.hul.ois.jhove.module.pdf.PdfHeader}.
	 * Ensures that a valid PDF passes.
	 */
	@Test
	public final void testMinorVersion() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, minimalPdfPath,
				RepInfo.TRUE, RepInfo.TRUE, null);
	}

	/**
	 * Test method for
	 * {@link edu.harvard.hul.ois.jhove.module.pdf.PdfHeader#getVersionString()}.
	 * Ensures that a file with %PDF-1.9 is invalid.
	 */
	@Test
	public final void testInvalidMinorVersion() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, invalidMinorPath,
				RepInfo.TRUE, RepInfo.FALSE,
				MessageConstants.ERR_PDF_MINOR_INVALID);

	}
}
