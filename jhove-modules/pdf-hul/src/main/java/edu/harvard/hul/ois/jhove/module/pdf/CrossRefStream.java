/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2005 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.*;
import java.util.*;

/**
 * This class implements the Cross-Reference Stream, an alternative
 * to the Cross-Reference Table starting in PDF 1.4.
 * 
 * A cross-reference stream is identified by a startxref keyword,
 * as opposed to the xref keyword which identifies the old-style
 * cross-reference table.
 * 
 * JHOVE supports only FlateDecode as a filter for cross-reference 
 * streams. This is consistent with the implementation limitation
 * described in Appendix H of the PDF manual for Acrobat 6 and earlier.
 * 
 *
 * @author Gary McGath
 *
 */
public class CrossRefStream {

    private PdfStream _xstrm;   // The underlying Stream object.
    private PdfDictionary _dict;
    private int _size;
    private IndexRange[] _index;
    private int[] _fieldSizes;
    private int _freeCount;
    private Filter[] _filters;
    private int _readRange;
    private int _readIndex;

    private int _bytesPerEntry;
    private long _prevXref;    // byte offset to previous xref stream, if any

    /* Per-object variables. */
    private int _objType;
    private int _objNum;
    private int _objField1;
    private int _objField2;

    /**
     * Range elements of the _index array:
     * Starting object and number of objects.
     */
    private class IndexRange {
        public int start;
        public int len;
    }

    /**
     * Constructor.
     * 
     * @param   xstrm	PdfStream object which contains a presumed
     *          cross-reference stream.
     */
    public CrossRefStream(PdfStream xstrm) {
        _xstrm = xstrm;
        _dict = xstrm.getDict ();
        _freeCount = 0;
    }

    /**
     * Returns <code>true</code> if the PdfStream object meets
     * the requirements of a cross-reference stream.  Also extracts
     * information from the dictionary for subsequent processing.
     */
    public boolean isValid () {
        PdfObject typeObj = _dict.get ("Type");
        String typeStr = null;
        if (typeObj instanceof PdfSimpleObject) {
            typeStr = ((PdfSimpleObject) typeObj).getStringValue ();
            if (!("XRef".equals (typeStr))) {
                return false;
            }
        }
        if (typeStr == null) {
            return false;
        }

        PdfObject sizeObj = _dict.get ("Size");
        if (sizeObj instanceof PdfSimpleObject) {
            _size = ((PdfSimpleObject) sizeObj).getIntValue();
        }
        else {
            return false;
        }

        // The Index entry is optional, but must have the right
        // format if present: [start1 length1 start2 length2 ...]
        PdfObject indexObj = _dict.get ("Index");
        if (indexObj instanceof PdfArray) {
            Vector indexObjs = ((PdfArray) indexObj).getContent();
            int indexObjCount = indexObjs.size();

            // Must contain an even number of objects
            if (indexObjCount % 2 != 0) return false;

            _index = new IndexRange[indexObjCount / 2];
            for (int i = 0; i < _index.length; i++) {
                _index[i] = new IndexRange();
                _index[i].start = ((PdfSimpleObject)indexObjs.get(i * 2)).getIntValue();
                _index[i].len = ((PdfSimpleObject)indexObjs.get(i * 2 + 1)).getIntValue();
            }
        }
        else {
            // Set up default index.
            _index = new IndexRange[1];
            _index[0] = new IndexRange();
            _index[0].start = 0;
            _index[0].len = _size;
        }

        // Get the field sizes.
        PdfObject wObj = _dict.get ("W");
        if (wObj instanceof PdfArray) {
            Vector vec = ((PdfArray) wObj).getContent ();
            int len = vec.size ();
            _fieldSizes = new int[len];
            for (int i = 0; i < len; i++) {
                PdfSimpleObject ob = (PdfSimpleObject) vec.get (i);
                _fieldSizes[i] = ob.getIntValue ();
            }
        }

        // Get the offset to the previous xref stream, if any.
        PdfObject prevObj = _dict.get ("Prev");
        if (prevObj instanceof PdfSimpleObject) {
            _prevXref = ((PdfSimpleObject) prevObj).getIntValue();
        }
        else {
            _prevXref = -1;
        }

        // Get the filter, for subsequent decompression.
        // We're guaranteed by the spec that this won't be a decryption
        // filter.
        _filters = _xstrm.getFilters();
        // Why isn't this being used?

        // passed all tests
        return true;
    }

    /**
     * Prepares for reading the Stream.
     * If the filter list includes one which we don't support, throws a
     * PdfException.
     */
    public void initRead (RandomAccessFile raf) 
            throws IOException 
    {
        Stream strm = _xstrm.getStream ();
        strm.setFilters (_xstrm.getFilters ());
        strm.initRead (raf);
        _readRange = 0;
        _readIndex = 0;
        
        /* Calculate the total bytes per entry.  This may have
         * some utility. */
        _bytesPerEntry = 0;
        for (int i = 0; i < _fieldSizes.length; i++) {
            _bytesPerEntry += _fieldSizes[i];
        }
    }

    /**
     * Reads the next object in the stream.
     *  
     * After calling <code>readObject</code>, it is possible to
     * call accessors to get information about the object.
     * For the moment, we punt on the question of how to deal with
     * Object Streams.
     *
     * Free objects are skipped over while being counted.  After
     * <code>readNextObject</code> returns <code>false</code>, the caller
     * may call <code>getFreeCount</code> to determine the
     * number of free objects.
     * 
     * @return  <code>true</code> if there is an object, <code>false</code>
     *          if no more objects are available.
     */
    public boolean readNextObject () throws IOException
    {
        /* Get the field type. */
        int wid;
        Stream is = _xstrm.getStream ();
        int i;
        int b;
        
        for (;;) {
            /* Loop till we find an actual object; we just count
             * type 0's, which are free entries. */
            wid  = _fieldSizes[0];
            if (_readIndex++ >= _index[_readRange].len) {
                _readIndex = 1;
                if (_readRange++ >= _index.length) {
                    return false;       // Read full complement
                }
            }
            if (wid != 0) {
                /* "Fields requiring more than one byte are stored 
                 * with the high-order byte first." */
                _objType = 0;
                for (i = 0; i < wid; i++) {
                    b = is.read ();
                    if (b < 0) {
                        return false;
                    } 
                    _objType = _objType * 256 + b;
                }
            }
            else {
                _objType = 1;   // Default if field width is 0
            }
            
            wid = _fieldSizes[1];
            _objField1 = 0;
            for (i = 0; i < wid; i++) {
                b = is.read ();
                if (b < 0) {
                    return false;
                } 
                _objField1 = _objField1 * 256 + b;
            }
            
            wid = _fieldSizes[2];
            _objField2 = 0;
            for (i = 0; i < wid; i++) {
                b = is.read ();
                if (b < 0) {
                    return false;
                } 
                _objField2 = _objField2 * 256 + b;
            }
            
            if (_objType != 0) {
                _objNum = _index[_readRange].start + _readIndex - 1;
                return true;
            }
            ++_freeCount;
        }
    }

    /**
     * Returns number of the last object read by
     * <code>readNextObject ()</code>.
     * Do not call if <code>readNextObject ()</code>
     * returns <code>false</code>.
     */
    public int getObjNum ()
    {
        return _objNum;
    }

    /**
     * Returns <code>true</code> if the last object read by
     * <code>readNextObject ()</code> is a compressed object.
     * Do not call if <code>readNextObject ()</code>
     * returns <code>false</code>.
     */
    public boolean isObjCompressed ()
    {
        return (_objType == 2);
    }

    /**
     * Returns the number of free objects detected.  This may
     * be called after <code>readNextObject</code> returns
     * <code>false</code>, signifying that all the objects
     * have been read and all the free objects counted.
     */
    public int getFreeCount ()
    {
        return _freeCount;
    }

    /**
     * Returns the total number of objects in the document's
     * cross-reference table at the time this stream was written.
     */
    public int getCrossRefTableSize()
    {
        return _size;
    }

    /**
     * Returns the offset of the last object read.
     * This is meaningful only if the last object read
     * was type 1 (uncompressed).
     */
    public int getOffset ()
    {
        return _objField1;
    }

    /**
     * Returns the object number of the content stream in
     * which this object is stored.
     * This is meaningful only if the last object read
     * was type 2 (compressed in content stream).
     */
    public int getContentStreamObjNum ()
    {
        return _objField1;
    }

    /**
     * Returns the offset of the previous cross-reference stream,
     * or -1 if none is specified.
     */
    public long getPrevXref ()
    {
        return _prevXref;
    }

    /**
     * Returns the content stream index of the last object read.
     * This is meaningful only if the last object read
     * was type 2 (compressed in content stream).
     */
    public int getContentStreamIndex ()
    {
        return _objField2;
    }
}
