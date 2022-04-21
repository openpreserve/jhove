package com.mcgath.jhove.module;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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

    private static final String ITXTCHUNK_PNG_FILEPATH = "src/test/resources/png/FileWithiTXtChunks.png";
    private static final String PNG_MIMETYPE = "image/png";

    /**
     * Parses valid PNG file with an iTXt Chunk and checks Author value is picked
     * up. This test relates to an NPE issue (#148) in v.1.24: Test file is
     * "cten0g04.png", copied from PNG test suite at http://www.schaik.com/pngsuite/
     *
     * @see <a href=
     *      "https://github.com/openpreserve/jhove/issues/148">https://github.com/openpreserve/jhove/issues/148</a>
     * @see <a href=
     *      "http://www.schaik.com/pngsuite/">http://www.schaik.com/pngsuite/</a>
     *
     * @throws Exception
     */
    @Test
    public void parsePngWithItxtChunk() throws Exception {
        final String EXPECTED_ITXTTITLE_VALUE = "PngSuite";
        final String EXPECTED_ITXTDISCLAIMER_VALUE = "Freeware.";
        final String PROP_KEYWORDS = "Keywords";
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

        // Check the value of the first iTXt is as expected
        Set<String> keywordValues = new HashSet<String>();
        @SuppressWarnings("unchecked")
        LinkedList<Property> iTxtKeywordProperties = (LinkedList<Property>) info.getByName(PROP_KEYWORDS).getValue();
        for (Property prop : iTxtKeywordProperties) {
            keywordValues.add(prop.getByName(PROP_VALUE).getValue().toString());
        }

        assertEquals(6, keywordValues.size());
        assertTrue(keywordValues.contains(EXPECTED_ITXTTITLE_VALUE));
        assertTrue(keywordValues.contains(EXPECTED_ITXTDISCLAIMER_VALUE));
    }
}
