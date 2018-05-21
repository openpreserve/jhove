/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2007 by JSTOR and the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.*;
import java.io.*;

/**
 *  Module for analysis of content as a byte stream.
 *  This is the module of last resort, accepting any content as
 *  valid and well-formed.
 */
public final class BytestreamModule
    extends ModuleBase
{

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    private static final String NAME = "BYTESTREAM";
    private static final String RELEASE = "1.3";
    private static final int [] DATE = {2007, 4, 10};
    private static final String [] FORMAT = {"bytestream"};
    private static final String COVERAGE = null;
    private static final String [] MIMETYPE = {"application/octet-stream"};
    private static final String WELLFORMED = "All bytestreams are well-formed";
    private static final String VALIDITY = null;
    private static final String REPINFO = null;
    private static final String NOTE = "This is the default format";
    private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and " +
	"the President and Fellows of Harvard College. " +
	"Released under the GNU Lesser General Public License.";

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     *  Creates a BytestreamModule.
     */
    public BytestreamModule ()
    {
	super (NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
	       VALIDITY, REPINFO, NOTE, RIGHTS, false);

	_vendor = Agent.harvardInstance();
    }

    /******************************************************************
     * PUBLIC INSTANCE METHODS.
     *
     * Parsing methods.
     ******************************************************************/

    /**
     *   Parse the content of a stream digital object and store the
     *   results in RepInfo.
     *   Any arbitrary bytestream is considered well-formed.
     */
    @Override
    public final int parse (InputStream stream, RepInfo info, int parseIndex)
	throws IOException
    {
        initParse ();
        info.setModule (this);
        info.setFormat (_format[0]);
        info.setMimeType (_mimeType[0]);

	/* We may have already done the checksums while converting a
	   temporary file. */
        setupDataStream(stream, info);

        boolean eof = false;
        _nByte = 0;
        byte[] byteBuf = new byte[4096];
        while (!eof) {
            try {
//                int ch = readUnsignedByte (_dstream, this);
                // All the calculations are done down in ChecksumInputStream
                int n = readByteBuf (_dstream, byteBuf, this);
                if (n <= 0) {
                    break;
                }
	    }
            catch (EOFException e) {
                eof = true;
            }
        }
        info.setSize (_nByte);
	if (_nByte == 0) {
	    info.setMessage (new InfoMessage (CoreMessageConstants.INF_FILE_EMPTY));
	}
        if (_ckSummer != null) {
	    info.setChecksum (new Checksum (_ckSummer.getCRC32 (), 
					ChecksumType.CRC32));
	    String value = _ckSummer.getMD5 ();
	    if (value != null) {
		info.setChecksum (new Checksum (value, ChecksumType.MD5));
	    }
	    if ((value = _ckSummer.getSHA1 ()) != null) {
		info.setChecksum (new Checksum (value, ChecksumType.SHA1));
	    }
        }

        return 0;
    }

    /**
     * Check signature.  Bytestreams have no signatures, but since any
     * byte stream is considered a valid Bytestream, return immediately
     * doing nothing.  The RepInfo._consistent flag will remain true.
     */
    @Override
    public void checkSignatures (File file, InputStream stream,
				 RepInfo info)
    {
            info.setFormat (_format[0]);
            info.setMimeType (_mimeType[0]);
            info.setModule (this);
            info.setSigMatch(_name);
    }
}
