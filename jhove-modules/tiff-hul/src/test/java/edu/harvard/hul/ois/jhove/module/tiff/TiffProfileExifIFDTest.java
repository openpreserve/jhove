package edu.harvard.hul.ois.jhove.module.tiff;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TiffProfileExifIFDTest {

	private static final String GOOD_FLASHPIX_VERSION = "0100";
	private static final String BAD_FLASHPIX_VERSION = "0010";
	private static final String GOOD_EXIF_VERSION = "0200";
	private static final String GOOD_EXIF_VERSION2 = "0221";
	private static final String BAD_EXIF_VERSION = "0101";
	private static final int GOOD_COLORSPACE = 65535;
	private static final int BAD_COLORSPACE = 3;
	
	private TiffProfileExifIFD profile;
	
	@Before
	public void setUp() {
		profile = new TiffProfileExifIFD();
	}

	@Test
	public void testSatisfiesThisProfileOK() {
		ExifIFD ifd = new ExifIFD(0, null, null, true);
		ifd._colorSpace = GOOD_COLORSPACE;
		ifd._exifVersion = GOOD_EXIF_VERSION;
		ifd._flashpixVersion = GOOD_FLASHPIX_VERSION;
		
		assertTrue(profile.satisfiesThisProfile(ifd));
		assertEquals(2, profile.getMajorVersion());
		assertEquals(0, profile.getMinorVersion());

		ifd._exifVersion = GOOD_EXIF_VERSION2;
		assertTrue(profile.satisfiesThisProfile(ifd));
		assertEquals(2, profile.getMajorVersion());
		assertEquals(21, profile.getMinorVersion());
	}

	@Test
	public void testSatisfiesThisProfileKO() {
		ExifIFD ifd = new ExifIFD(0, null, null, true);

		ifd._colorSpace = GOOD_COLORSPACE;
		ifd._exifVersion = BAD_EXIF_VERSION;
		ifd._flashpixVersion = GOOD_FLASHPIX_VERSION;
		assertFalse(profile.satisfiesThisProfile(ifd));

		ifd._colorSpace = GOOD_COLORSPACE;
		ifd._exifVersion = GOOD_EXIF_VERSION;
		ifd._flashpixVersion = BAD_FLASHPIX_VERSION;
		assertFalse(profile.satisfiesThisProfile(ifd));

		ifd._colorSpace = BAD_COLORSPACE;
		ifd._exifVersion = GOOD_EXIF_VERSION;
		ifd._flashpixVersion = GOOD_FLASHPIX_VERSION;
		assertFalse(profile.satisfiesThisProfile(ifd));
	}
	
}
