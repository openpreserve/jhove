/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

/**
 *  A class which encapsulates a file specification in PDF.  A file
 *  specification may be given as either a string or a dictionary.
 *  The specification is converted to a string according to the following
 *  rules:  If a PDF string object is the file specifier, that string
 *  is used, without attempting to convert file separators to the local
 *  file system.  If a PDF dictionary is used, one of the following is
 *  used, in decreasing order of preference:
 *
 *  <UL>
 *    <LI>The system-neutral file specification string
 *    <LI>The Unix file specification string
 *    <LI>The DOS file specification string
 *    <LI>The Macintosh file specification string
 *  </UL>
 */
public enum FileSpecification 
{
    INSTANCE;
    private static final String[] dictKeys = {"F", "Unix", "DOS", "Mac"};
    /**
     *  Constructor.
     * 
     *  @param  obj  A PdfDictionary with the file specification under the
     *               key "F", "Unix", "DOS", or "Mac"; or
     *               a PdfSimpleObject whose string value is the
     *               file specification.  If <code>obj</code> is
     *               a dictionary and more than one key is specified,
     *               then the first of the keys F, Unix, DOS, and Mac
     *               to be found is used.
     */
    public static String getFileSpecString (PdfObject obj) throws PdfInvalidException
    {
        if (obj instanceof PdfSimpleObject) {
            return ((PdfSimpleObject) obj).getStringValue ();
        }
        try {
            if (obj instanceof PdfDictionary) {
                PdfDictionary dictObj = (PdfDictionary) obj;
                for (final String dictKey : dictKeys) {
                    PdfSimpleObject pathObj = (PdfSimpleObject) dictObj.get(dictKey);
                    if (pathObj != null) {
                        return pathObj.getStringValue();
                    }
                }
            }
        }
        catch (ClassCastException e) {
            throw new PdfInvalidException(MessageConstants.PDF_HUL_9); // PDF-HUL-9
        }
        return null;
    }
}
