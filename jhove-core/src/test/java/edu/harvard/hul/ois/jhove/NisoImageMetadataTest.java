package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class NisoImageMetadataTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExtractIccProfileDescriptionBad() {
		byte[] badIcc = new byte[] { (byte)0xFF, (byte)0xFE, (byte)0xFD, (byte)0xFC };
		try {
			NisoImageMetadata.extractIccProfileDescription(badIcc);
			fail("Not a profile !!!");
		} catch (IllegalArgumentException ia) {
			assertTrue(true);
		}
	}

	@Test
	public void testExtractIccProfileDescriptionGoodv2() throws IOException {
		InputStream is = this.getClass().getResourceAsStream("sRGB2014.icc");
		byte[] iccData = toByteArray(is);
		is.close();
		
		try {
			String profileName = NisoImageMetadata.extractIccProfileDescription(iccData);
			assertEquals("sRGB2014", profileName);
		} catch (IllegalArgumentException iae) {
			fail("Not a icc profile " + iae.getMessage());
		}
	}

	@Test
	public void testExtractIccProfileDescriptionGoodv4() throws IOException {
		InputStream is = this.getClass().getResourceAsStream("sRGB_v4_ICC_preference.icc");
		byte[] iccData = toByteArray(is);
		is.close();
		
		try {
			String profileName = NisoImageMetadata.extractIccProfileDescription(iccData);
			assertEquals("sRGB v4 ICC preference perceptual intent beta", profileName);
		} catch (IllegalArgumentException iae) {
			fail("Not a icc profile " + iae.getMessage());
		}
	}
	
	private byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();		
		
	}
}
