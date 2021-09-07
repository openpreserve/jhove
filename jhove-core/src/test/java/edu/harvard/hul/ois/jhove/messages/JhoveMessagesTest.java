package edu.harvard.hul.ois.jhove.messages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JhoveMessagesTest {
    @Test
    public void testDefault() {
        System.getProperties().remove("module.language");
        JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assert "English".equals(msg.getMessage());
    }

    @Test
    public void testEnglish() {
        System.setProperty("module.language", "en");
        JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assert "English".equals(msg.getMessage());
    }

    @Test
    public void testUnknown() {
        System.setProperty("module.language", "jp");
        JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assert "English".equals(msg.getMessage());
    }

    @Test
    public void testCustomized() {
        {
            System.setProperty("module.language", "da");
            JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
            JhoveMessage msg = messageFactory.getMessage("MSG");
            assert "Danish".equals(msg.getMessage());
        }

        {
            System.setProperty("module.language", "fr");
            JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
            JhoveMessage msg = messageFactory.getMessage("MSG");
            assert "French".equals(msg.getMessage());
        }
    }
}
