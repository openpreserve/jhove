package edu.harvard.hul.ois.jhove.module.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.PdfModule;

/**
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
	 * Test method for
	 * {@link edu.harvard.hul.ois.jhove.module.pdf.PdfHeader#getVersionString()}.
	 */
	@Test
	public final void testMinorVersion()
			throws URISyntaxException {
		File validMinor = new File(
				PdfHeaderTest.class.getResource(minimalPdfPath).toURI());

		RepInfo info = parseTestFile(validMinor);

		// Major version number < 8 should be well formed
		assertEquals("Should be well formed.", RepInfo.TRUE, info.getWellFormed());
		// Major version number < 8 should be invalid
		assertEquals("Should be valid.", RepInfo.TRUE, info.getValid());
	}

	/**
	 * Test method for
	 * {@link edu.harvard.hul.ois.jhove.module.pdf.PdfHeader#getVersionString()}.
	 */
	@Test
	public final void testInvalidMinorVersion()
			throws URISyntaxException {
		File invalidMajor = new File(
				PdfHeaderTest.class.getResource(invalidMinorPath).toURI());

		RepInfo info = parseTestFile(invalidMajor);

		// Major version number > 7 should be well formed
		assertEquals("Should be well formed.", RepInfo.TRUE, info.getWellFormed());
		assertEquals("Should NOT be valid.", RepInfo.FALSE, info.getValid());
	}

	private RepInfo parseTestFile(final File toTest) {
		RepInfo info = new RepInfo(toTest.getName());
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(toTest, "r");
			this.module.parse(raf, info);
		} catch (FileNotFoundException excep) {
			excep.printStackTrace();
			fail("Couldn't find file to test: " + toTest.getName());
		} catch (IOException excep) {
			excep.printStackTrace();
			fail("IOException Reading: " + toTest.getName());
		}
		try {
			if (raf != null) {
				raf.close();
			}
		} catch (@SuppressWarnings("unused") IOException excep) {
			// We don't care about this..
		}
		return info;
	}
}
