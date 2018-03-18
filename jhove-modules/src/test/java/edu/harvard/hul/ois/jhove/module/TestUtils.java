package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;

import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.PdfModule;

/**
 * Convenience methods to test that the result of JHOVE validation are as
 * expected. The tests are:
 * <ul>
 * <li>The well formed result is equal to a pre-defined value.</li>
 * <li>The is valid result is equal to a pre-defined value.</li>
 * <li>The message list contains an expected message (optionally).</li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 14 Mar 2018:20:14:19
 */

public final class TestUtils {

	private TestUtils() {
		// Keep out
		throw new AssertionError("Should never be in constructor.");
	}

	/**
	 * Convenience method that takes the path to a test resource and tests that
	 * the results of JHOVE are as expected.
	 * 
	 * @param pdfModule
	 *            a {@link edu.harvard.hul.ois.jhove.module.PdfModule} instance
	 *            to use to validate the resource.
	 * @param resToTest
	 *            the String path of the resource to validate and test.
	 * @param expctWllFrmd
	 *            the expected well formed value
	 * @param expctVld
	 *            the expected is valid value
	 * @param expctMessage
	 *            a JHOVE validation string message expected to be found in
	 *            the list of validation messages. If this parameter is null the
	 *            test isn't performed.
	 * @throws URISyntaxException
	 *            when there's an issue converting the resource name to a path
	 */
	public static void testValidateResource(final PdfModule pdfModule,
			final String resToTest, final int expctWllFrmd, final int expctVld,
			final String expctMessage) throws URISyntaxException {
		File toTest = new File(
				TestUtils.class.getResource(resToTest).toURI());

		testValidateFile(pdfModule, toTest, expctWllFrmd, expctVld,
				expctMessage);
	}

	/**
	 * Method that takes a file and tests that the results of JHOVE are as expected.
	 * 
	 * @param pdfModule
	 *            a {@link edu.harvard.hul.ois.jhove.module.PdfModule} instance
	 *            to use to validate the resource.
	 * @param fileToTest
	 *            a Java File instance to validate
	 * @param expctWllFrmd
	 *            the expected well formed value
	 * @param expctVld
	 *            the expected is valid value
	 * @param expctMessage
	 *            a JHOVE validation string message expected to be found in
	 *            the list of validation messages. If this parameter is null the
	 *            test isn't performed.
	 */
	public static void testValidateFile(final PdfModule pdfModule,
			final File fileToTest, final int expctWllFrmd, final int expctVld,
			final String expctMessage) {
		RepInfo info = parseTestFile(pdfModule, fileToTest);
		testResult(info, expctWllFrmd, expctVld, expctMessage);
	}

	private static void testResult(final RepInfo info, final int expctWllFrmd,
			final int expctVld, final String expctMessage) {
		String message = (expctWllFrmd == RepInfo.TRUE)
				? "Should be well formed."
				: "Should NOT be well formed.";
		assertEquals(message, expctWllFrmd, info.getWellFormed());
		message = (expctVld == RepInfo.TRUE) ? "Should be valid."
				: "Should NOT be valid.";
		assertEquals(message, expctVld, info.getValid());
		boolean messagePresent = false;
		if (expctMessage != null) {
			for (Message mess : info.getMessage()) {
				if (mess.getMessage().equals(expctMessage)) {
					messagePresent = true;
				}
			}
			if (!messagePresent) {
				System.out.println("Expected message not found, messages in RepInfo:");
				for (Message mess : info.getMessage()) {
					System.out.println(" - message: " + mess.getMessage());
				}
			}
			assertTrue("Expected message: " + expctMessage, messagePresent);
		}
	}

	private static RepInfo parseTestFile(final PdfModule pdfModule, final File toTest) {
		RepInfo info = new RepInfo(toTest.getName());
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(toTest, "r");
			pdfModule.parse(raf, info);
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
