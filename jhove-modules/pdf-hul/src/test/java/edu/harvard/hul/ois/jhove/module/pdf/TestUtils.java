package edu.harvard.hul.ois.jhove.module.pdf;

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
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 14 Mar 2018:20:14:19
 */

public final class TestUtils {

	private TestUtils() {
		// Keep out
		throw new AssertionError("Should never be in constructor.");
	}

	public static void testValidateFile(final PdfModule pdfModule,
			final String pathToTest, final int expctWllFrmd, final int expctVld,
			final String expctMessage) throws URISyntaxException {
		File toTest = new File(
				TestUtils.class.getResource(pathToTest).toURI());

		RepInfo info = parseTestFile(pdfModule, toTest);
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
				for (Message mess: info.getMessage()) {
					System.out.println(" - message:" + mess.getMessage());
				}
			}
			assertTrue("Expected message: " + expctMessage, messagePresent);
		}
	}

	private static RepInfo parseTestFile(final PdfModule pdfModule, final File toTest) {
		RepInfo info = new RepInfo(toTest.getName());
		try (RandomAccessFile raf = new RandomAccessFile(toTest, "r")){
			pdfModule.parse(raf, info);
		} catch (FileNotFoundException excep) {
			excep.printStackTrace();
			fail("Couldn't find file to test: " + toTest.getName());
		} catch (IOException excep) {
			excep.printStackTrace();
			fail("IOException Reading: " + toTest.getName());
		}
		return info;
	}

}
