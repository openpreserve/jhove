/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import java.util.*;

/**
 *  A representation of a PDF dictionary object.
 */
public class PdfDictionary extends PdfObject
{
    public static final int PDFA_IMPLEMENTATION_LIMIT = 4095;
    private final Map<String, PdfObject> _entries = new HashMap<> ();

    /** 
     *  Creates a PdfDictionary object.
     *
     *  @param objNumber  The PDF object number
     *  @param genNumber  The PDF generation number
     */
    public PdfDictionary (int objNumber, int genNumber)
    {
        super (objNumber, genNumber);
    }

    /** 
     *  Creates a PdfDictionary object.
     *
     */
    public PdfDictionary ()
    {
        super();
    }

    /**
     *  Accumulate an entry into the dictionary.
     *
     *  @param key   String value of the dictionary key
     *  @param value PdfObject encapsulation of the dictionary value
     */
    public void add (String key, PdfObject value) 
    {
        _entries.put (key, value);
    }

    /** Get the PDFObject whose key has the specified string
     *  value.  Returns null if there is no such key.
     *
     *  @param  key	The string value of the key to look up.
     */
    public PdfObject get (String key)
    {
        return _entries.get (key);
    }

    /** Return true if it's within the PDF/A implementation limit. */
    public boolean isPdfACompliant () 
    {
        return _entries.size() <= PDFA_IMPLEMENTATION_LIMIT;
    }
    
    /** Returns a KeySet with all keys of a dictionary **/
    public Set<String> getKeys()
    {
        return _entries.keySet();
    }

    /**
     *  Returns an iterator which will successively return
     *  all the values in the dictionary.
     */
    public Iterator<PdfObject> iterator ()
    {
        return _entries.values ().iterator ();
    }
}
