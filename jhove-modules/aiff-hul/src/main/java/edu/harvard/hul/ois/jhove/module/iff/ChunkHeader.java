/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 *
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.iff;

import edu.harvard.hul.ois.jhove.*;
import java.io.*;

/**
 * This class encapsulates an IFF/AIFF chunk header.
 * 
 * @author Gary McGath
 */
public class ChunkHeader {

    private static final int CHUNK_ID_LENGTH = 4;

    private ModuleBase _module;
    private RepInfo _repInfo;
    private String _chunkId;         // Four-character ID of the chunk
    private long _size;              // This does not include the 8 bytes of header

    /**
     * Constructor.
     * 
     * @param  module   The module under which the chunk is being read
     * @param  info     The RepInfo object being used by the module
     */
    public ChunkHeader(ModuleBase module, RepInfo info)
    {
        _module = module;
        _repInfo = info;
    }

    /**
     * Reads and validates the header of a chunk.
     *
     * If {@code _chunkId} is non-null it's assumed to have already been read.
     */
    public boolean readHeader(DataInputStream dstrm) throws IOException
    {
        boolean idBeginsWithSpace = false;
        boolean spacePrecedesPrintableCharacters = false;
        StringBuilder id = new StringBuilder(CHUNK_ID_LENGTH);

        for (int i = 0; i < CHUNK_ID_LENGTH; i++) {

            boolean printableCharacter = false;
            int ch = ModuleBase.readUnsignedByte(dstrm, _module);

            // Characters should be in the printable ASCII range
            if (ch < 32 || ch > 126) {
                _repInfo.setMessage(new ErrorMessage(
                        MessageConstants.IFF_HUL_1,
                        String.format(
                                MessageConstants.IFF_HUL_1_SUB.getMessage(),
                                ch),
                        _module.getNByte() - 1));
                _repInfo.setWellFormed(false);
                return false;
            }

            if (ch == ' ') {
                if (i == 0) {
                    idBeginsWithSpace = true;
                }
            } else {
                printableCharacter = true;
            }

            if (idBeginsWithSpace && printableCharacter) {
                spacePrecedesPrintableCharacters = true;
            }

            id.append((char) ch);
        }

        _chunkId = id.toString();

        // Spaces should not precede printable characters
        if (spacePrecedesPrintableCharacters) {
            _repInfo.setMessage(new ErrorMessage(
                    MessageConstants.IFF_HUL_2, "\"" + _chunkId + "\"",
                    _module.getNByte() - CHUNK_ID_LENGTH));
            _repInfo.setValid(false);
        }

        _size = ModuleBase.readUnsignedInt(dstrm, _module.isBigEndian(), _module);

        return true;
    }


    /** Sets the chunk type, which is a 4-character code, directly. */
    public void setID(String id)
    {
        _chunkId = id;
    }

    /** Returns the chunk type, which is a 4-character code */
    public String getID()
    {
        return _chunkId;
    }

    /** Sets the chunk size */
    public void setSize(long size)
    {
        _size = size;
    }

    /** Returns the chunk size (excluding the first 8 bytes) */
    public long getSize()
    {
        return _size;
    }
}
