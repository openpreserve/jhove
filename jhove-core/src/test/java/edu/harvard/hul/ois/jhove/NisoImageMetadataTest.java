package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class NisoImageMetadataTest {
	private final static String ERROR_TEST = "Not a icc profile ";

  @Test
  public void testMake8601Valid() {
     assertEquals("2020-10-20T23:58:45", NisoImageMetadata.make8601Valid("2020-10-20T23:58:45"));
     assertEquals("2020-10-20T23:58:45", NisoImageMetadata.make8601Valid("2020:10:20 23:58:45"));
     assertEquals("2020-10-20T23:58:45", NisoImageMetadata.make8601Valid("2020.10.20 23.58.45"));
     assertNull(NisoImageMetadata.make8601Valid("2020:10:20 23:58:73"));
     assertNull(NisoImageMetadata.make8601Valid("2020:10:20"));
  }

	@Test
	public void testExtractIccProfileDescriptionBad() {
		final byte ANY_BYTE_1 = (byte) 0xFF;
		final byte ANY_BYTE_2 = (byte) 0xFE;
		final byte[] BAD_ICC = new byte[] { ANY_BYTE_1, ANY_BYTE_2, ANY_BYTE_1,
				ANY_BYTE_2 };
		try {
			NisoImageMetadata.extractIccProfileDescription(BAD_ICC);
			fail(ERROR_TEST);
		} catch (IllegalArgumentException iae) {
			assertNotNull(iae); // Should always be true
		}
	}

	@Test
	public void testExtractIccProfileDescriptionGoodv2() throws IOException {

		try (InputStream is = this.getClass().getResourceAsStream("sRGB2014.icc")) {
			byte[] iccData = toByteArray(is);
			String profileName = NisoImageMetadata
					.extractIccProfileDescription(iccData);
			assertEquals("sRGB2014", profileName);
		} catch (IllegalArgumentException iae) {
			fail(ERROR_TEST + iae.getMessage());
		}
	}

	@Test
	public void testExtractIccProfileDescriptionGoodv4() throws IOException {


		try (InputStream is = this.getClass().getResourceAsStream(
				"sRGB_v4_ICC_preference.icc")) {
			byte[] iccData = toByteArray(is);
			String profileName = NisoImageMetadata
					.extractIccProfileDescription(iccData);
			assertEquals("sRGB v4 ICC preference perceptual intent beta",
					profileName);
		} catch (IllegalArgumentException iae) {
			fail(ERROR_TEST + iae.getMessage());
		}
	}

	private static byte[] toByteArray(InputStream is) throws IOException {
		final int NB_READ = 16384;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[NB_READ];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();

	}
}
