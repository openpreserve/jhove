package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class MessageTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.simple().forClass(Message.class).verify();
        assertEquals(new ErrorMessage(JhoveMessages.DEFAULT_MESSAGE, 0),
                new ErrorMessage(JhoveMessages.DEFAULT_MESSAGE, 0));
        assertEquals(new InfoMessage(JhoveMessages.DEFAULT_MESSAGE, 0),
                new InfoMessage(JhoveMessages.DEFAULT_MESSAGE, 0));
        assertNotEquals(new ErrorMessage(JhoveMessages.DEFAULT_MESSAGE, 0),
                new InfoMessage(JhoveMessages.DEFAULT_MESSAGE, 0));
    }
}
