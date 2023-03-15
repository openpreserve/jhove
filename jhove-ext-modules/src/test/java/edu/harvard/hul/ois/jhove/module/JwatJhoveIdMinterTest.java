package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jwat.common.Diagnosis;
import org.jwat.common.DiagnosisType;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

public class JwatJhoveIdMinterTest {
    @Test
    public void testPrefix() {
        String[] prefixes = new String[] { "TEST", "OPF", "MINT" };
        for (String prefix : prefixes) {
            JwatJhoveIdMinter minter = JwatJhoveIdMinter.getInstance(prefix);
            String id = minter.mint(new Diagnosis(DiagnosisType.UNKNOWN, "test", "some info")).getId();
            assertTrue(id.startsWith(prefix));
        }
    }

    @Test
    public void testEmptyPrefix() {
        assertThrows("Illegal Argument exception expected",
                IllegalArgumentException.class,
                () -> {
                    JwatJhoveIdMinter.getInstance("");
                });
    }

    @Test
    public void testNullPrefix() {
        assertThrows("Illegal Argument exception expected",
                IllegalArgumentException.class,
                () -> {
                    JwatJhoveIdMinter.getInstance(null);
                });
    }

    @Test
    public void testDelimeter() {
        String[] delimeters = new String[] { "-", "%", "@" };
        for (String delimeter : delimeters) {
            JwatJhoveIdMinter minter = JwatJhoveIdMinter.getInstance("PREF", delimeter);
            String id = minter.mint(new Diagnosis(DiagnosisType.UNKNOWN, "test", "some info")).getId();
            assertTrue(id.startsWith("PREF" + delimeter));
        }
    }

    @Test
    public void testEmptyDelimeter() {
        assertThrows("Illegal Argument exception expected",
                IllegalArgumentException.class,
                () -> {
                    JwatJhoveIdMinter.getInstance("PREF", "");
                });
    }

    @Test
    public void testNullDelimeter() {
        assertThrows("Illegal Argument exception expected",
                IllegalArgumentException.class,
                () -> {
                    JwatJhoveIdMinter.getInstance("PREF", null);
                });
    }

    @Test
    public void testMint() {
        JwatJhoveIdMinter minter = JwatJhoveIdMinter.getInstance("XML-OPF");
        JhoveMessage msg = minter.mint(new Diagnosis(DiagnosisType.DUPLICATE, "test", "some info"));
        assertTrue(msg.getId().startsWith("XML-OPF-"));
        assertEquals("XML-OPF-100", msg.getId());
        assertEquals("DUPLICATE", msg.getMessage());
        assertEquals("Entity: test, some info", msg.getSubMessage());
    }
}
