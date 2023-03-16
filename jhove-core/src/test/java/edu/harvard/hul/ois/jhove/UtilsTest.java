package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.Test;

public class UtilsTest {

  // Max time for running the encoding
  private static final long MAX_MILLIS = 30000;
  
  public String readFile(String name) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    try (InputStream is = this.getClass().getResourceAsStream("xmp_big_anon.xmp");
         BufferedReader br = new BufferedReader(new InputStreamReader(is))
    ) {
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            contentBuilder.append(sCurrentLine).append("\n");
        }
    }
    return contentBuilder.toString();
  }

  @Test
  public void testEncodeContent() throws IOException {
    String content = readFile("xmp_big_anon.xmp");
    long initialSize = content.length();

    long now = System.currentTimeMillis();
    String encoded = Utils.encodeContent(content);
    long newSize = encoded.length();
    assertNotNull(encoded);
    assertTrue(encoded.startsWith("&lt;x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"XMP Core 5.6.0\"&gt;"));
    long end = System.currentTimeMillis();
    long duration = (end - now);
    System.out.println("XML encoding from " + initialSize + " to " + newSize + " in " + duration + " millis");
    assertTrue("Encoding takes " + duration + " millis", duration < MAX_MILLIS); // Should take less than 30 seconds !!!
  }

  @Test
  public void testEncodeValue() throws IOException {
    String content = readFile("xmp_big_anon.xmp");
    long initialSize = content.length();

    long now = System.currentTimeMillis();
    String encoded = Utils.encodeValue(content);
    long newSize = encoded.length();
    assertNotNull(encoded);
    assertTrue(encoded.startsWith(
        "&lt;x:xmpmeta xmlns:x=&quot;adobe:ns:meta/&quot; x:xmptk=&quot;XMP Core 5.6.0&quot;&gt;"));
    long end = System.currentTimeMillis();
    long duration = (end - now);
    System.out.println("XML attribute encoding from " + initialSize + " to " + newSize + " in " + duration + " millis");
    assertTrue("Encoding takes " + duration + " millis", duration < MAX_MILLIS); // Should take less than 30 seconds !!!
  }
}
