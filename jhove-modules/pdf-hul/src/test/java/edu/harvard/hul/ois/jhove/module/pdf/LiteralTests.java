package edu.harvard.hul.ois.jhove.module.pdf;

import java.net.URISyntaxException;

import org.junit.Test;

import edu.harvard.hul.ois.jhove.RepInfo;

/**
 * Tests for the {@link Literal} class.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Mar 2018:11:28:10
 */

public class LiteralTests {
    /**
     * Test method for {@link Literal}.
     * Ensures that a valid Date passes.
     * 
     * @throws PdfInvalidException
     */
    @Test
    public final void testValidDates() throws PdfInvalidException {
        Literal.parseDate("D:20180313112810Z");
        Literal.parseDate("D:20180313112810+01'00'");
        Literal.parseDate("D:20180313112810+01'00");
    }

    @Test(expected = PdfInvalidException.class)
    public final void testInvalidDate() throws PdfInvalidException {
        Literal.parseDate("D:20180313112810+01'00`");
    }

}
