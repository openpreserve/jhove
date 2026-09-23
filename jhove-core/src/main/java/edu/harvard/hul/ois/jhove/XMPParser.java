package edu.harvard.hul.ois.jhove;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *  This class parses XMP.
 */
public class XMPParser {
  /**
   * Read XMP and create a Property, or throw a SAXException if the XMP XML is
   * not well-formed.
   */
  public static Property parse(XMPSource src)
      throws ParserConfigurationException, SAXException, IOException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(true);
    XMLReader parser = factory.newSAXParser().getXMLReader();
    XMPHandler handler = new XMPHandler();
    parser.setContentHandler(handler);
    parser.setErrorHandler(handler);
    parser.parse(src);
    return src.makeProperty();
  }
}
