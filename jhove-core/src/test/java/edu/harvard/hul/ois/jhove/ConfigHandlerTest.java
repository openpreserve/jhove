package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

@RunWith(JUnit4.class)
public class ConfigHandlerTest {
    @BeforeClass
    public static void setUp() {
        // Ensure the default locale is English for testing on any platform
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    public void testFrench() throws ParserConfigurationException, SAXException, IOException {
        ConfigHandler configHandler = this.loadHandler("jhove_lang_fr_test.conf");
        System.setProperty("module.language", configHandler.getLanguage());
        JhoveMessageFactory messageFactory = JhoveMessages
                .getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("French", msg.getMessage());
    }

    @Test
    public void testDanish() throws ParserConfigurationException, SAXException, IOException {
        ConfigHandler configHandler = this.loadHandler("jhove_lang_da_test.conf");
        System.setProperty("module.language", configHandler.getLanguage());
        JhoveMessageFactory messageFactory = JhoveMessages
                .getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("Danish", msg.getMessage());
    }

    @Test
    public void testUnknown() throws ParserConfigurationException, SAXException, IOException {
        ConfigHandler configHandler = this.loadHandler("jhove_lang_jp_test.conf");
        System.setProperty("module.language", configHandler.getLanguage());
        JhoveMessageFactory messageFactory = JhoveMessages
                .getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("English", msg.getMessage());
    }

    @Test
    public void testBad() throws ParserConfigurationException, SAXException, IOException {
        ConfigHandler configHandler = this.loadHandler("jhove_lang_bad_test.conf");
        System.setProperty("module.language", configHandler.getLanguage());
        JhoveMessageFactory messageFactory = JhoveMessages
                .getInstance("edu.harvard.hul.ois.jhove.messages.ErrorMessages");
        JhoveMessage msg = messageFactory.getMessage("MSG");
        assertEquals("English", msg.getMessage());
    }

    private ConfigHandler loadHandler(final String confFileName)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        ConfigHandler configHandler = new ConfigHandler();
        try (InputStream is = this.getClass().getResourceAsStream(confFileName)) {
            saxParser.parse(is, configHandler);
        }
        return configHandler;
    }
}
