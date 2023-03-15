/**
 * 
 */
package org.openpreservation.jhove;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public class ReleaseDetailsTest {

    /**
     * Test method for {@link ReleaseDetails#getInstance()}.
     */
    @Test
    public final void testGetInstance() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        ReleaseDetails secondInstance = ReleaseDetails.getInstance();
        assertSame(instance, secondInstance);
    }

    /**
     * Test method for {@link ReleaseDetails#getVersion()}.
     */
    @Test
    public final void testGetVersion() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        assertEquals("0.1.2-TESTER", instance.getVersion());
    }

    /**
     * Test method for {@link ReleaseDetails#getBuildDate()}.
     */
    @Test
    public final void testGetBuildDate() throws ParseException {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2011-07-31");
        assertEquals(instance.getBuildDate(), date);
    }

    /**
     * Test the hash and equals contract for the class using EqualsVerifier.
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ReleaseDetails.class).verify();
    }
}
