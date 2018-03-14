/**
 * 
 */
package edu.harvard.hul.ois.jhove.module.pdf;

import static org.junit.Assert.*;

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
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 13 Mar 2018:18:17:45
 */

public class PdfModuleTest {
	private static final String pdfResourcePath = "/edu/harvard/hul/ois/jhove/module/pdf/";
	private static final String minimalPdfPath = pdfResourcePath
			+ "T01_000_minimal.pdf";
	private static final String invalidCatTypePath = pdfResourcePath
			+ "T02-01_006_document-catalog-wrong-type-key.pdf";
	
	private PdfModule module;

	@Before
	public void setUp() throws Exception {
		this.module = new PdfModule();
		JhoveBase je = new JhoveBase();
		this.module.setBase(je);
	}

	/**
	 * Test method for
	 * {@link edu.harvard.hul.ois.jhove.module.pdf.PdfModule#getCatalogDict()}.
	 */
	@Test
	public final void testValidCatType()
			throws URISyntaxException {
		File validCatType = new File(
				PdfModuleTest.class.getResource(minimalPdfPath).toURI());

		RepInfo info = parseTestFile(validCatType);

		// Major version number < 8 should be well formed
		assertEquals("Should be well formed.", RepInfo.TRUE, info.getWellFormed());
		// Major version number < 8 should be invalid
		assertEquals("Should be valid.", RepInfo.TRUE, info.getValid());
	}


	/**
	 * Test method for
	 * {@link edu.harvard.hul.ois.jhove.module.pdf.PdfModule#getCatalogDict()}.
	 */
	@Test
	public final void testInvalidCatType()
			throws URISyntaxException {
		File invalidCatType = new File(
				PdfModuleTest.class.getResource(invalidCatTypePath).toURI());

		RepInfo info = parseTestFile(invalidCatType);

		// Doc catalog type not Catalog, should not be well formed 
		assertEquals("Should NOT be well formed.", RepInfo.FALSE, info.getWellFormed());
		// Doc catalog type not Catalog, should be invalid
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
