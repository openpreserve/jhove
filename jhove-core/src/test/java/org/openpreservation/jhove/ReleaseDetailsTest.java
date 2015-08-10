/**
 * 
 */
package org.openpreservation.jhove;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class ReleaseDetailsTest {


    /**
     * Test method for {@link org.openpreservation.jhove.JhoveReleaseDetails#getInstance()}.
     */
    @Test
    public final void testGetInstance() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        ReleaseDetails secondInstance = ReleaseDetails.getInstance();
        assertTrue(instance == secondInstance);
    }

    /**
     * Test method for {@link org.openpreservation.jhove.JhoveReleaseDetails#getVersion()}.
     */
    @Test
    public final void testGetVersion() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        assertTrue("0.1.2-TESTER".equals(instance.getVersion()));
    }

    /**
     * Test method for {@link org.openpreservation.jhove.JhoveReleaseDetails#getBuildDate()}.
     */
    @Test
    public final void testGetBuildDate() throws ParseException {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2011-07-31");
        assertTrue(instance.getBuildDate().equals(date));
    }

    /**
     * Test the hash and equals contract for the class using EqualsVerifier
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(ReleaseDetails.class).verify();
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @Test
    public final void testToString() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        System.out.println(instance.toString());
        assertEquals("ReleaseDetails [version=0.1.2-TESTER, buildDate=Sun Jul 31 00:00:00 BST 2011]", instance.toString());
    }

}
