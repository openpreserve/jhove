/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * An InputStream layered on top of a RandomAccessFile.
 * This is useful for a Module which has requirements that
 * force it to use a RandomAccessFile, but is usually
 * accessed sequentially.
 * 
 * An RAFInputStream maintains its own position information
 * in the file, so multiple RAFInputStreams in the same file
 * will work without interference.  However, this class is
 * not thread-safe.
 * 
 * @author Gary McGath
 */
public class RAFInputStream extends InputStream {

    /** Default file buffer size */
    private static int DEFAULT_BUFFER_SIZE = 65536;

    /** The file on which the stream is based */
    private RandomAccessFile _raf;
    
    /** Size of fileBuf */
    private int fileBufSize;
    
    /** Buffer for reading from the file */
    private byte[] fileBuf;
    
    /** Offset for reading next byte from fileBuf */
    private int fileBufPos;
    
    /** Number of valid bytes in fileBuf */
    private int fileBufBytes;
    
    /** Position in file for next read from RandomAccessFile */
    private long fileOffset;
    
    /** End-of-file flag */
    private boolean eof;
    
    /**
     * Constructor with default buffer size.
     *
     * The stream starts at the current position of the RandomAccessFile.
     *
     * @param   raf   the file on which the stream is to be based.
     */
    public RAFInputStream(RandomAccessFile raf)
    {
        super();
        _raf = raf;
        fileBufSize = DEFAULT_BUFFER_SIZE;
        init();
    }

    /**
     * Constructor with buffer size.
     *
     * The stream starts at the current position of the RandomAccessFile.
     *
     * @param   raf         the file on which the stream is to be based.
     *
     * @param   bufferSize  the buffer size to be used. If less than or
     *                      equal to 0, the default buffer size is used.
     */
    public RAFInputStream(RandomAccessFile raf, int bufferSize)
    {
        super();
        _raf = raf;
        fileBufSize = (bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
        init();
    }

    private void init()
    {
        fileBufBytes = 0;
        fileBufPos = 0;
        fileBuf = new byte[fileBufSize];
        try {
            fileOffset = _raf.getFilePointer();
        }
        catch (IOException e) {}
        eof = false;
    }

    /**
     * Seeks to the next offset and fills the file buffer. Sets <code>eof</code>
     * to <code>true</code> if the offset to read is beyond the end of file.
     */
    private void fillFileBuffer() throws IOException
    {
        _raf.seek(fileOffset);
        fileBufBytes = _raf.read(fileBuf);
        fileBufPos = 0;
        if (fileBufBytes <= 0) {
            eof = true;
        }
        fileOffset += fileBufBytes;
    }

    /**
     * Reads a single byte from the file.
     */
    @Override
    public int read() throws IOException
    {
        if (eof) {
            return -1;
        }
        if (fileBufPos >= fileBufBytes) {
            // Need to read another bufferful
            fillFileBuffer();
            if (eof) {
                return -1;
            }
        }
        return (fileBuf[fileBufPos++] & 0xFF);
    }

    /**
     * Reads some number of bytes from the input stream and
     * stores them into the buffer array b. The number of
     * bytes actually read is returned as an integer.
     */
    @Override
    public int read(byte[] b) throws IOException
    {
        int bytesToRead = b.length;
        int bytesRead = 0;
        for (;;) {
            // See how many bytes are available in fileBuf.
            int fbAvail = fileBufBytes - fileBufPos;
            if (fbAvail <= 0) {
                // Need to read another bufferful
                fillFileBuffer();
                if (eof) {
                    // No more in file -- return what we have
                    return bytesRead;
                }
                fbAvail = fileBufBytes;
            }
            if (fbAvail > bytesToRead) {
                // We have more than enough bytes.
                fbAvail = bytesToRead;
            }
            for (int i = 0; i < fbAvail; i++) {
                b[bytesRead++] = fileBuf[fileBufPos++];
                bytesToRead--;
            }
            if (bytesToRead == 0) {
                return bytesRead;
            }
        }
    }

    /**
     * Reads up to len bytes of data from the input stream 
     * into an array of bytes. An attempt is made to read as 
     * many as len bytes, but a smaller number may be read, 
     * possibly zero. The number of bytes actually read is
     * returned as an integer.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException 
    {
        int bytesToRead = len;
        int bytesRead = 0;
        for (;;) {
            // See how many bytes are available in fileBuf.
            int fbAvail = fileBufBytes - fileBufPos;
            if (fbAvail <= 0) {
                // Need to read another bufferful
                fillFileBuffer();
                if (eof) {
                    // No more in file -- return what we have
                    return bytesRead;
                }
                fbAvail = fileBufBytes;
            }
            if (fbAvail > bytesToRead) {
                // We have more than enough bytes.
                fbAvail = bytesToRead;
            }
            for (int i = 0; i < fbAvail; i++) {
                b[off + bytesRead++] = fileBuf[fileBufPos++];
                bytesToRead--;
            }
            if (bytesToRead == 0) {
                return bytesRead;
            }
        }
    }

    /**
     * Skips some number of bytes.
     *
     * @return  the number of bytes actually skipped.
     */
    @Override
    public long skip(long n) throws IOException
    {
        // If the range of the skip lies within the current buffer,
        // we simply adjust our position in the buffer.
        int bytesLeft = fileBufBytes - fileBufPos;
        if (bytesLeft > n) {
            fileBufPos += (int) n;
        }
        else {
            // The bytes to skip don't lie within the current buffer,
            // so we set the next file seek location instead.
            if (fileOffset + n - bytesLeft  > _raf.length()) {
                fileOffset = _raf.length();
            }
            else {
                fileOffset += n - bytesLeft;
            }
            fileBufBytes = 0;   // Invalidate current buffer
        }
        return n;
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or
     * skipped over) from this input stream without blocking. A single read or
     * skip of this many bytes will not block, but may read or skip fewer bytes.
     *
     * @return  an estimate of the number of bytes that can be read (or skipped
     *          over) from this input stream without blocking, or <code>0</code>
     *          when it reaches the end of the input stream.
     */
    @Override
    public int available() throws IOException
    {
        if (eof) return 0;
        if (fileBufPos >= fileBufBytes) {
            // The buffer has been read through and needs refilling
            // before we can check for any remaining available bytes.
            fillFileBuffer();
            if (eof) return 0;
        }
        return fileBufBytes - fileBufPos;
    }

    /**
     * Returns the RandomAccessFile object.
     */
    public RandomAccessFile getRAF()
    {
        return _raf;
    }

    /**
     * Positions the stream to a different point in the file,
     * invalidating the buffer.
     */
    public void seek(long offset) throws IOException
    {
        _raf.seek(offset);
        fileBufBytes = 0;
        fileBufPos = 0;
        eof = false;
    }

    /**
     * Returns the current position in the file.
     * What is reported is the position of the byte
     * in the file which was last extracted from
     * the buffer.
     */
    public long getFilePos() throws IOException
    {
        return _raf.getFilePointer() -
                (fileBufBytes - fileBufPos);
    }
}
