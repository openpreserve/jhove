package edu.harvard.hul.ois.jhove.module.tiff;

import static org.junit.Assert.*;

import java.io.RandomAccessFile;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.RepInfo;

public class TiffIFDTest {

	private TiffIFD ifd;
	
	@Before
	public void setUp() {
		ifd = new TiffIFD(0L, null, null, true);
	}

	@Test
	public void testAddFocalPlaneValue5() {
		int _focalPlaneResolutionUnit = 5;
		
		// Test for raw value
		Property p1 = ifd.addIntegerProperty ("FocalPlaneResolutionUnit",
                _focalPlaneResolutionUnit,
                TiffIFD.FOCALPLANERESOLUTIONUNIT_L,
                true);
		assertNotNull(p1);
		assertEquals(new Integer(5), p1.getValue());
		
		// Test for decoded value
		Property p2 = ifd.addIntegerProperty ("FocalPlaneResolutionUnit",
                _focalPlaneResolutionUnit,
                TiffIFD.FOCALPLANERESOLUTIONUNIT_L,
                false);
		assertNotNull(p2);
		assertEquals("Bad value " + p2.getValue(), 
				true, TiffIFD.FOCALPLANERESOLUTIONUNIT_L[5].equals(p2.getValue()));
    }

	
}
