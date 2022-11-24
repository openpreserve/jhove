package edu.harvard.hul.ois.jhove.messages;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class JhoveMessagesTest {
	@BeforeClass
	public static void setUp() {
		// Ensure the default locale is English for testing on any platform
		Locale.setDefault(Locale.ENGLISH);
	}
	
    @Test
    public void testDefault() {
        System.getProperties().remove("module.language");
        JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("English", msg.getMessage());
    }

    @Test
    public void testEnglish() {
        System.setProperty("module.language", "en");
        JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("English", msg.getMessage());
    }

    @Test
    public void testUnknown() {
        System.setProperty("module.language", "jp");
        JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("English", msg.getMessage());
    }

    @Test
    public void testCustomized() {
        {
            System.setProperty("module.language", "da");
            JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
            JhoveMessage msg = messageFactory.getMessage("MSG");
            assertEquals("Danish", msg.getMessage());
        }

        {
            System.setProperty("module.language", "fr");
            JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
            JhoveMessage msg = messageFactory.getMessage("MSG");
            assertEquals("French", msg.getMessage());
        }
    }
}
