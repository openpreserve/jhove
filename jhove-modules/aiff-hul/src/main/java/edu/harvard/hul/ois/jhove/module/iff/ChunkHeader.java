/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 *
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.iff;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This class encapsulates an IFF/AIFF chunk header.
 * 
 * @author Gary McGath
 */
public class ChunkHeader {

    private ModuleBase _module;
    private RepInfo _repInfo;
    private String _chunkId;         // Four-character ID of the chunk
    private long _offset;            // Offset from the beginning of file
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
        final int LOWEST_PRINTABLE_ASCII = 32;
        final int HIGHEST_PRINTABLE_ASCII = 126;

        _offset = _module.getNByte();

        boolean idBeginsWithSpace = false;
        boolean spacePrecedesPrintableCharacters = false;
        StringBuilder id = new StringBuilder(Chunk.ID_LENGTH);

        for (int i = 0; i < Chunk.ID_LENGTH; i++) {

            boolean printableCharacter = false;
            int ch = ModuleBase.readUnsignedByte(dstrm, _module);

            // Characters should be in the printable ASCII range
            if (ch < LOWEST_PRINTABLE_ASCII || ch > HIGHEST_PRINTABLE_ASCII) {
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
            JhoveMessage message = JhoveMessages.getMessageInstance(
                    MessageConstants.IFF_HUL_2.getId(), String.format(
                            MessageConstants.IFF_HUL_2.getMessage(), _chunkId));
            _repInfo.setMessage(new ErrorMessage(message,
                    _module.getNByte() - Chunk.ID_LENGTH));
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

    /** Returns the chunk size, which excludes the length of the header. */
    public long getSize()
    {
        return _size;
    }

    /** Returns the chunk offset in bytes from the beginning of file. */
    public long getOffset()
    {
        return _offset;
    }
}
