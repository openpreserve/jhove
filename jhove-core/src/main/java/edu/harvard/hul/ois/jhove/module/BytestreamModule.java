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
import java.util.logging.Level;

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
    private static final String RELEASE = "1.4";
    private static final int [] DATE = {2018, 10, 1};
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

	this._vendor = Agent.harvardInstance();
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
        initInfo(info);

        // Setup the data stream, will determine if we use checksum stream
        setupDataStream(stream, info);

        boolean eof = false;
        this._nByte = 0;
        byte[] byteBuf = new byte[4096];
        while (!eof) {
            try {
                // All the calculations are done down in ChecksumInputStream
                int n = readByteBuf (this._dstream, byteBuf, this);
                if (n <= 0) {
                    break;
                }
            }
            catch (EOFException e) {
                _logger.log(Level.FINEST, "End of file exception when parsing.", e);
                eof = true;
            }
        }
        info.setSize (this._nByte);
        if (this._nByte == 0) {
            info.setMessage (new InfoMessage (CoreMessageConstants.JHOVE_CORE_3));
        }
        // Set the checksums in the report if they're calculated
        setChecksums(this._ckSummer, info);
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
            info.setFormat (this._format[0]);
            info.setMimeType (this._mimeType[0]);
            info.setModule (this);
            info.setSigMatch(this._name);
    }
}
