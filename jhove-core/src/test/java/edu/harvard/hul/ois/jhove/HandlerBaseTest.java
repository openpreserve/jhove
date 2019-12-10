package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HandlerBaseTest {
  private static final Logger LOGGER = Logger.getLogger(HandlerBaseTest.class.getName());
  private static final String GIVES = " => ";
  private static final String DUMMY = "dummy";
  
  @Test
  public void testEncodeContent() {
    /* Test values */
    final String[] VALUES = {
      DUMMY, "<<>>\"\"''&&"
    };

    final String[] EXPECTED = {
      DUMMY, "&lt;&lt;&gt;&gt;\"\"''&amp;&amp;"
    };

    String encodeValue;
    for (int i = 0; i < VALUES.length; i++) {
      encodeValue = Utils.encodeContent(VALUES[i]);
      LOGGER.info("testEncodeContent: " + VALUES[i] + GIVES + encodeValue);
      assertEquals(EXPECTED[i], encodeValue);
    }
  }

  @Test
  public void testEncodeValue() {
    /* Test values */
    final String[] VALUES = {
      DUMMY, "<<>>\"'&", "" + (char)0xf + "\"\""
    };
    final String[] EXPECTED = {
      DUMMY, "&lt;&lt;&gt;&gt;&quot;'&amp;", "&quot;&quot;"
    };

    String encodeValue;
    for (int i = 0; i < VALUES.length; i++) {
      encodeValue = Utils.encodeValue(VALUES[i]);
      LOGGER.info("testEncodeValue: " + VALUES[i] + GIVES + encodeValue);
      assertEquals(EXPECTED[i], encodeValue);
    }
  }

}
