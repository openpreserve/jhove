package edu.harvard.hul.ois.jhove.module.tiff;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.Property;

public class TiffIFDTest {

	private TiffIFD ifd;

	@Before
	public void setUp() {
		ifd = new TiffIFD(0L, null, null, true);
	}

	@Test
	public void testAddFocalPlaneValue5() {
		final String testedPropertyName = "FocalPlaneResolutionUnit";
		final int testedFocalPlaneResolutionUnit = 5;

		// Test for raw value
		Property p1 = ifd.addIntegerProperty(testedPropertyName,
				testedFocalPlaneResolutionUnit,
				TiffIFD.FOCALPLANERESOLUTIONUNIT_L, true);
		assertNotNull(p1);
		assertEquals(new Integer(testedFocalPlaneResolutionUnit), p1.getValue());

		// Test for decoded value
		Property p2 = ifd.addIntegerProperty(testedPropertyName,
				testedFocalPlaneResolutionUnit,
				TiffIFD.FOCALPLANERESOLUTIONUNIT_L, false);
		assertNotNull(p2);
		assertEquals(
				"Bad value " + p2.getValue(),
				true,
				TiffIFD.FOCALPLANERESOLUTIONUNIT_L[testedFocalPlaneResolutionUnit]
						.equals(p2.getValue()));
	}

}
