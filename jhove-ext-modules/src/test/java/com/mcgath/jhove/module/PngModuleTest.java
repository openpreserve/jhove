package com.mcgath.jhove.module;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.RepInfo;

/**
 * <p>
 * Tests for PngModule.
 * </p>
 *
 * @author Karen Hanson
 */
@RunWith(JUnit4.class)
public class PngModuleTest {

	private static final String ITXTCHUNK_PNG_FILEPATH = "src/test/resources/png/FileWithiTXtChunkNoTranslation.png";
	private static final String PNG_MIMETYPE = "image/png";

	/**
	 * Parses valid PNG file with an iTXt Chunk and checks Comment value is picked
	 * up. This test relates to an NPE issue (#148) in v.1.24:
	 *
	 * @see <a href=
	 *    "https://github.com/openpreserve/jhove/issues/148">https://github.com/openpreserve/jhove/issues/148</a>
	 *
	 * @throws Exception
	 */
	@Test
	public void parsePngWithItxtChunk() throws Exception {
		final String EXPECTED_ITXT_VALUE = "Created with GIMP";
		final String PROP_KEYWORDS = "Keywords";
		final String PROP_KEYWORD = "Keyword";
		final String PROP_VALUE = "Value";

		File pngFile = new File(ITXTCHUNK_PNG_FILEPATH);

		RepInfo info = new RepInfo(pngFile.getAbsolutePath());
		PngModule pngMod = new PngModule();
		pngMod.parse(new FileInputStream(pngFile), info, 0);

		int wellformed = info.getWellFormed();
		int valid = info.getValid();
		String type = info.getMimeType();

		// General checks
		assertEquals(1, wellformed);
		assertEquals(1, valid);
		assertEquals(PNG_MIMETYPE, type);

		// Check the value of the iTXt is as expected
		Property iTxtValueProperty = info.getByName(PROP_KEYWORDS).getByName(PROP_KEYWORD).getByName(PROP_VALUE);
		String iTXtValue = iTxtValueProperty.getValue().toString();
		assertTrue(iTXtValue.equals(EXPECTED_ITXT_VALUE));
	}
}
