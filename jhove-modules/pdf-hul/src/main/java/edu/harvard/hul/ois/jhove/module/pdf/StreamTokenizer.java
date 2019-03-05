/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2005 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import java.io.*;

/**
 * Tokenizer subclass which gets data from an object stream.
 *
 * @author Gary McGath
 *
 */
public class StreamTokenizer extends Tokenizer {

    /** Source from which to read bytes. */
    private Stream _stream;
    
    /** Backup flag. */
    private boolean _backupFlag;
    
    /** Last character read.  Will be returned again if _backupFlag
     *  is true. */
    private int _lastChar;
    

    public StreamTokenizer (RandomAccessFile file, Stream stream)
    {
        super ();
        _file = file;
        _stream = stream;
        _backupFlag = false;
    }

    /** Streams can occur only in files, not in streams,
     *  so this should never be called.
     */
    @Override
    protected void initStream (Stream token)
        throws PdfException
    {
        throw new PdfMalformedException (MessageConstants.PDF_HUL_47); // PDF-HUL-47
    }

    /** Gets a character from the file, using a buffer. */
    @Override
    public int readChar () throws IOException
    {
        if (_backupFlag) {
            _backupFlag = false;
            return _lastChar;
        }
        _lastChar = _stream.read ();
        return _lastChar;
    }

    /**
     *  Set the Tokenizer to a new position in the stream.
     *
     *  @param  offset  The offset in bytes from the start of the stream.
     */
    @Override
    public void seek (long offset)
        throws IOException
    {
        // Advancing in the stream is easy.  Backing up requires starting
        // the stream over.
        if (!_stream.advanceTo ((int) offset)) {
            _stream.initRead (_file);
            _stream.advanceTo ((int) offset);
        }
        seekReset (_stream.getOffset());
    }

    /** Sets the offset of a Stream to the current file position.
     *  Only the file-based tokenizer can do this, so this should never
     *  be called. 
     */
    @Override
    protected void setStreamOffset (Stream token)
        throws PdfException
    {
        throw new PdfMalformedException (MessageConstants.PDF_HUL_48); // PDF-HUL-48
    }


    /**
     *   Back up a byte so it will be read again.
     */
    @Override
    public void backupChar ()
    {
        _backupFlag = true;
    }

}
