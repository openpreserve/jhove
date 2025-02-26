/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;


import org.xml.sax.*;

/**
 *  This class encapsulates XMP metadata within a file.  It makes use
 *  of an InputStream as a data source.
 *
 *  We don't actually extract any information from the XMP, but
 *  simply check it for well-formedness.  By convention, XMPHandler
 *  should be invoked on an XMPSource (TBW), which provides the
 *  ability to recapture the InputStream from which the XMP was
 *  obtained and put it into a property once it's verified here.
 *  
 */
public class XMPHandler extends org.xml.sax.helpers.DefaultHandler {

    /* URI strings */
    private final static String xmpBasicSchema = 
        "http://ns.adobe.com/xap/1.0/";
//    private final static String xmpRightsSchema =
//        "http://ns.adobe.com/xap/1.0/rights/";
//    private final static String dublinCoreSchema =
//        "http://purl.org/dc/elements/1.1/";
//    private final static String adobePDFSchema =
//        "http://ns.adobe.com/pdf/1.3/";
//    private final static String photoshopSchema =
//        "http://ns.adobe.com/photoshop/1.0/";

    private boolean pdfaCompliant;

    public XMPHandler ()
    {
        super ();
        pdfaCompliant = true;            // compliance is presumed till disproven
    }


    /** Returns true if no violations of PDF/A compliance have been found,
     *  false if a problem was detected. */
    public boolean isPdfaCompliant () {
        return pdfaCompliant;
    }


    @Override
    public void processingInstruction (String target, String data)
    {
        // Parse a so-called packet wrapper, a pair of XML processing
        // instructions enclosing the actual XMP data. This is intended to
        // facilitate searching for XMP when its location in a file is unknown
        // (byte scanning) but is not recommended (albeit neither strictly
        // illegal) when it is clear from the file format specs where to look
        // for XMP. All file types in which JHOVE currently handles XMP (TIFF,
        // GIF, JPEG, PDF including PDF/A) fall in the latter category, so we
        // could safely ignore the packet wrapper, if it wasn't for a little
        // PDF/A tidbit.
        //
        // Note that it is possible to declare the encoding of the XMP packet in
        // its packet wrapper. Either implicitly via a BOM (U+FEFF) in the begin
        // attribute; this can be used to distinguish between UTF-16BE/LE,
        // UTF-32BE/LE, and UTF-8. Or explicitly using the deprecated encoding
        // attribute, see below. However, UTF-8 has been prescribed in all file
        // formats in which JHOVE currently looks for XMP (TIFF, GIF, JPEG, PDF
        // including PDF/A) anyway since at least 2010, see
        // <https://web.archive.org/web/20101009095526/http://www.adobe.com/content/dam/Adobe/en/devnet/xmp/pdfs/XMPSpecificationPart3.pdf>.
        // So let's just ignore what the packet wrapper says. If we run into an
        // error because the XMP is encoded in an unexpected (i.e., not UTF-8)
        // encoding we'd rather know about that anyway, right?
        //
        // The bytes and encoding attributes are not allowed in PDF/A (ISO
        // 19005-1:2005, section 6.7.5). They also have both been deprecated in
        // the XMP specification since at least January 2004, see
        // <https://web.archive.org/web/20040612130530/http://partners.adobe.com/asn/tech/xmp/pdf/xmpspecification.pdf>.
        if ("xpacket".equals (target) &&
            (data.indexOf("bytes=") >= 0 || data.indexOf("encoding=") >= 0)) {
                pdfaCompliant = false;
            }
        }
    }


    /**
     *  Catches the end of an element.
     */
    @Override
    public void endElement (String namespaceURI, String localName,
                String rawName)
    {
        if (xmpBasicSchema.equals (namespaceURI)) {
            // Check for the end of an XMP structure
            if ("Bag".equals (rawName)||
                    "Seq".equals (rawName) ||
                    "Alt".equals (rawName)) {
                // TODO This doesn't do anything. Do we really need this method?
            }
        }
    }

    /** Catch a fatal error.  This is put here because the default
     *  behavior is to report a "fatal error" to standard output,
     *  which is harmless but scary.  
     */
    @Override
    public void fatalError(SAXParseException exception)
    {
    }
}
