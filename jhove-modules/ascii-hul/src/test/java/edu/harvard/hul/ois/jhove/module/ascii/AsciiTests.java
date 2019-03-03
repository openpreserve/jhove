package edu.harvard.hul.ois.jhove.module.ascii;

import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.AsciiModule;
import edu.harvard.hul.ois.jhove.module.TestUtils;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public class AsciiTests {
	private static final String asciiResourcePath = TestUtils.MODULE_RESOURCE_BASE
			+ "ascii/";

	private static final String nullsOnly = asciiResourcePath
			+ "nulls-only.txt";
	private static final String allCtrls = asciiResourcePath + "all-ctrls.txt";
	private static final String allGraphics = asciiResourcePath
			+ "all-graphics.txt";
	private static final String allAscii = asciiResourcePath + "all-ascii.txt";
	private static final String allAsciiInv = asciiResourcePath
			+ "all-ascii-and-invalid.txt";
	private AsciiModule module;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.module = new AsciiModule();
		JhoveBase je = new JhoveBase();
		this.module.setBase(je);
	}

	@Test
	public final void testEmpty() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, TestUtils.EMPTY_FILE_PATH,
				RepInfo.FALSE, RepInfo.FALSE,
				MessageConstants.ASCII_HUL_2.getMessage());
	}

	@Test
	public final void testNullOnly() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, nullsOnly, RepInfo.TRUE,
				RepInfo.TRUE, MessageConstants.ASCII_HUL_2.getMessage(),
				false);
	}

	@Test
	public final void testNotEmpty() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, allAscii, RepInfo.TRUE,
				RepInfo.TRUE, MessageConstants.ASCII_HUL_2.getMessage(),
				false);
	}

	@Test
	public final void testCharOutOfRange() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, allAsciiInv, RepInfo.FALSE,
				RepInfo.FALSE, MessageConstants.ASCII_HUL_1.getMessage());
	}

	@Test
	public final void testPrintable() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, allAscii, RepInfo.TRUE,
				RepInfo.TRUE, MessageConstants.ASCII_HUL_3.getMessage(), false);
	}

	@Test
	public final void testNoPrintable() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, allCtrls, RepInfo.TRUE,
				RepInfo.TRUE, MessageConstants.ASCII_HUL_3.getMessage());
	}

	@Test
	public final void testControlChars() throws URISyntaxException {
		RepInfo info = TestUtils.testValidateResource(this.module, allCtrls,
				RepInfo.TRUE, RepInfo.TRUE, MessageConstants.ASCII_HUL_3.getMessage());
		testAllAsciiCtrlCharsDetected(info);
	}

	@Test
	public final void testControlCharsWithGraphics() throws URISyntaxException {
		RepInfo info = TestUtils.testValidateResource(this.module, allAscii,
				RepInfo.TRUE, RepInfo.TRUE, MessageConstants.ASCII_HUL_3.getMessage(),
				false);
		testAllAsciiCtrlCharsDetected(info);
	}

	@Test
	public final void testControlCharsFalsePos() throws URISyntaxException {
		RepInfo info = TestUtils.testValidateResource(this.module, allGraphics,
				RepInfo.TRUE, RepInfo.TRUE, MessageConstants.ASCII_HUL_3.getMessage(),
				false);
		Property asciiProp = info.getProperty("ASCIIMetadata");
		assertTrue(asciiProp == null);
	}

	private final static void testAllAsciiCtrlCharsDetected(
			final RepInfo info) {
		Property ctrlCharProp = info.getProperty("ASCIIMetadata")
				.getByName(ControlChar.PROP_NAME);
		assertTrue(ctrlCharProp.getArity() == PropertyArity.LIST);
		@SuppressWarnings("unchecked")
		List<String> mnemonics = (List<String>) ctrlCharProp.getValue();
		Set<ControlChar> ctrlCharSet = new HashSet<>();
		for (String mnemonic : mnemonics) {
			ControlChar ctrlChar = ControlChar.fromMnemonic(mnemonic);
			ctrlCharSet.add(ctrlChar);
		}
		Integer mnemonicsCount = Integer.valueOf(mnemonics.size());
		Integer asciiCtrlMinusCrLf = Integer
				.valueOf(ControlChar.ASCII.size() - 2);
		assertTrue(
				String.format("Only %d characters found, expecting %d",
						mnemonicsCount, asciiCtrlMinusCrLf),
				asciiCtrlMinusCrLf.equals(mnemonicsCount));
	}
}
