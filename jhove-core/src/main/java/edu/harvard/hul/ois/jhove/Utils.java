/**
 * 
 */
package edu.harvard.hul.ois.jhove;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Utils {
  INSTANCE;
  private static final char amp = '&';
  private static final String ampsym = "&amp;";
  private static final char lt = '<';
  private static final String ltsym = "&lt;";
  private static final char gt = '>';
  private static final String gtsym = "&gt;";
  private static final char qut = '"';
  private static final String qutsym = "&quot;";

  /**
   * Encodes a content String in XML-clean form, converting characters to entities
   * as necessary and removing control characters disallowed by XML. The null
   * string will be converted to an empty string.
   */
  public static String encodeContent(String content) {
    if (content == null) {
      return "";
    }
    int n = content.length();
    // Expect a growth of 30%
    StringBuilder buffer = new StringBuilder((int)(n * 1.3));
    
    // Do it in ONE pass
    for (int i = 0; i < n; i++) {
      int ch = content.charAt(i);
      // Skip disallowed control characters from the content string.
      if ((0x00 <= ch && ch <= 0x08) || 
          (0x0b <= ch && ch <= 0x0c) || 
          (0x0e <= ch && ch <= 0x1f) || 
          (0xfffe <= ch && ch <= 0xffff) || 
          0x7f == ch
      ) {
        continue;
      }

      switch(ch) {
        case amp:  buffer.append(ampsym); break;
        case gt:  buffer.append(gtsym); break;
        case lt:  buffer.append(ltsym); break;
        default:   buffer.append((char)ch);
      }
    }
    return buffer.toString();
  }

  /**
   * Encodes an attribute value String in XML-clean form, converting quote
   * characters to entities and removing control characters disallowed by XML.
   */
  public static String encodeValue(String value) {
    if (value == null) {
      return "";
    }

    int n = value.length();
    // Expect a growth of 30%
    StringBuilder buffer = new StringBuilder((int)(n * 1.3));

    // Just make ONE pass
    for (int i = 0; i < n; i++) {
      char ch = value.charAt(i);
      // Skip disallowed control characters from the content string.
      if ((0x00 <= ch && ch <= 0x08) || 
          (0x0b <= ch && ch <= 0x0c) || 
          (0x0e <= ch && ch <= 0x1f) || 
          (0xfffe <= ch && ch <= 0xffff) || 
          0x7f == ch
      ) {
        continue;
      }

      switch(ch) {
        case amp:  buffer.append(ampsym); break;
        case gt:  buffer.append(gtsym); break;
        case lt:  buffer.append(ltsym); break;
        case qut:  buffer.append(qutsym); break;
        default:   buffer.append((char)ch);
      }
    }
    return buffer.toString();
  }

  /**
   * Checks if a property would produce an empty XML element, and returns true if
   * it would.
   */
  public static boolean isPropertyEmpty(Property property, PropertyArity arity) {
    try {
      if (arity.equals(PropertyArity.SET)) {
        Set propSet = (Set) property.getValue();
        return (propSet.isEmpty());
      } else if (arity.equals(PropertyArity.LIST)) {
        List propList = (List) property.getValue();
        return (propList.isEmpty());
      } else if (arity.equals(PropertyArity.MAP)) {
        Map propMap = (Map) property.getValue();
        return (propMap.isEmpty());
      } else if (arity.equals(PropertyArity.ARRAY)) {
        // Ack! Is there any easy way to do this?
        boolean[] boolArray = null;
        byte[] byteArray = null;
        char[] charArray = null;
        java.util.Date[] dateArray = null;
        double[] doubleArray = null;
        float[] floatArray = null;
        int[] intArray = null;
        long[] longArray = null;
        Object[] objArray = null;
        Property[] propArray = null;
        short[] shortArray = null;
        String[] stringArray = null;
        Rational[] rationalArray = null;
        NisoImageMetadata[] nisoArray = null;
        AESAudioMetadata[] aesArray = null;
        TextMDMetadata[] textMDArray = null;
        int n = 0;

        PropertyType propType = property.getType();
        if (PropertyType.BOOLEAN.equals(propType)) {
          boolArray = (boolean[]) property.getValue();
          n = boolArray.length;
        } else if (PropertyType.BYTE.equals(propType)) {
          byteArray = (byte[]) property.getValue();
          n = byteArray.length;
        } else if (PropertyType.CHARACTER.equals(propType)) {
          charArray = (char[]) property.getValue();
          n = charArray.length;
        } else if (PropertyType.DATE.equals(propType)) {
          dateArray = (java.util.Date[]) property.getValue();
          n = dateArray.length;
        } else if (PropertyType.DOUBLE.equals(propType)) {
          doubleArray = (double[]) property.getValue();
          n = doubleArray.length;
        } else if (PropertyType.FLOAT.equals(propType)) {
          floatArray = (float[]) property.getValue();
          n = floatArray.length;
        } else if (PropertyType.INTEGER.equals(propType)) {
          intArray = (int[]) property.getValue();
          n = intArray.length;
        } else if (PropertyType.LONG.equals(propType)) {
          longArray = (long[]) property.getValue();
          n = longArray.length;
        } else if (PropertyType.OBJECT.equals(propType)) {
          objArray = (Object[]) property.getValue();
          n = objArray.length;
        } else if (PropertyType.SHORT.equals(propType)) {
          shortArray = (short[]) property.getValue();
          n = shortArray.length;
        } else if (PropertyType.STRING.equals(propType)) {
          stringArray = (String[]) property.getValue();
          n = stringArray.length;
        } else if (PropertyType.RATIONAL.equals(propType)) {
          rationalArray = (Rational[]) property.getValue();
          n = rationalArray.length;
        } else if (PropertyType.PROPERTY.equals(propType)) {
          propArray = (Property[]) property.getValue();
          n = propArray.length;
        } else if (PropertyType.NISOIMAGEMETADATA.equals(propType)) {
          nisoArray = (NisoImageMetadata[]) property.getValue();
          n = nisoArray.length;
        } else if (PropertyType.AESAUDIOMETADATA.equals(propType)) {
          aesArray = (AESAudioMetadata[]) property.getValue();
          n = aesArray.length;
        } else if (PropertyType.TEXTMDMETADATA.equals(propType)) {
          textMDArray = (TextMDMetadata[]) property.getValue();
          n = textMDArray.length;
        }
        return (n == 0);
      } else {
        return property.getValue().toString().length() == 0;
      }
    } catch (Exception e) {
      // If something goes seriously wrong, return true to punt the property
      return true;
    }
  }
}
