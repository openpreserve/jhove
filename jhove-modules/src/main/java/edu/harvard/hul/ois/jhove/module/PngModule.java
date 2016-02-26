package edu.harvard.hul.ois.jhove.module;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import edu.harvard.hul.ois.jhove.module.png.*;
//import java.util.Vector;

import edu.harvard.hul.ois.jhove.*;

public class PngModule extends ModuleBase {

	/**
	 * What would constitute a well-formed but invalid PNG? 
	 */
    /******************************************************************
     * DEBUGGING FIELDS.
     * All debugging fields should be set to false for release code.
     ******************************************************************/

	
    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    private static final String NAME = "PNG-gdm";
    private static final String RELEASE = "1.0";
    private static final int [] DATE = {2016, 2, 25};
    private static final String [] FORMAT = {
    	"PNG", " ISO/IEC 15948:2003", "Portable Network Graphics"
    };
    private static final String COVERAGE = 
    	"PNG (ISO/IEC 15948:2003)";
    private static final String [] MIMETYPE = {"image/png"};
    //private static final String [] ALT_MIMETYPE = {"image/x-png"};
	private static final String WELLFORMED = "Put well-formedness criteria here";
	private static final String VALIDITY = "Put validity criteria here";
	private static final String REPINFO = "Put repinfo note here";
	private static final String NOTE = null;
	private static final String RIGHTS = "Copyright 2016 by Gary McGath. " +
			"Released under the GNU Lesser General Public License.";
    private static final String NISO_IMAGE_MD = "NisoImageMetadata";

    /* Checksummer object */
    protected Checksummer _ckSummer;
    
    /* Top-level property list */
    protected List<Property> _propList;

    /* Input stream wrapper which handles checksums */
    protected ChecksumInputStream _cstream;
    
    /* Data input stream wrapped around _cstream */
    protected DataInputStream _dstream;
    
    /* Top-level metadata property */
    protected Property _metadata;
    
    /* NISO metadata for the image */
    NisoImageMetadata _nisoData;
    

    /* Critical chunk flags. */
    private boolean ihdrSeen;		// IHDR chunk has been seen
    private boolean plteSeen;		// PLTE chunk has been seen
    private boolean idatSeen;		// at least one IDAT chunk has been seen
    private boolean idatFinished;	// a different chunk has been seen after an IDAT chunk
    private boolean iendSeen;		// IEND chunk has been seen
    
    /* File signature bytes, found at the beginning of every well-formed PNG files. */
    private final static int _sigBytes[] = { 137, 80, 78, 71, 13, 10, 26, 10 };
    
    /******************************************************************
    * CLASS CONSTRUCTOR.
    ******************************************************************/
    /**
     *  Instantiate a <tt>PngModule</tt> object.
     */
    public PngModule() {
        super (NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);
        Signature sig =
                new InternalSignature (_sigBytes, SignatureType.MAGIC, 
                                       SignatureUseType.MANDATORY, 0,
                                       "");
        _signature.add (sig);
        sig = new ExternalSignature (".png", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL);
        _signature.add (sig);
	}

    /******************************************************************
     * Parsing methods.
     ******************************************************************/

    /**
     *  Check if the digital object conforms to this Module's
     *  internal signature information.
     *
     *   @param file      A RandomAccessFile, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param stream    An InputStream, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param info      A fresh RepInfo object which will be modified
     *                    to reflect the results of the test
     */
    public void checkSignatures (File file, InputStream stream, RepInfo info) 
        throws IOException
    {
        int i;
        int ch;
        _dstream = getBufferedDataStream (stream, _je != null ?
                    _je.getBufferSize () : 0);
        for (i = 0; i < 8; i++) {
            try {
                ch = readUnsignedByte(_dstream, this);
            }
            catch (Exception e) {
                ch = -1;
            }
            if (ch != _sigBytes[i]) {
                info.setWellFormed (false);
                return;
            }
        }
        info.setModule (this);
        info.setFormat (_format[0]);
        info.setMimeType (_mimeType[0]);
        info.setSigMatch(_name);
    }

    
    /**
     *   Parse the content of a purported JPEG stream digital object and store the
     *   results in RepInfo.
     * 
     *   This function uses the JPEG-L method of detecting a marker following 
     *   a data stream, checking for a 0 high bit rather than an entire 0
     *   byte.  So long at no JPEG markers are defined with a value from 0
     *   through 7F, this is valid for all JPEG files.
     *
     *   @param stream    An InputStream, positioned at its beginning,
     *                    which is generated from the object to be parsed
     *   @param info       A fresh RepInfo object which will be modified
     *                    to reflect the results of the parsing
     *   @param parseIndex  Must be 0 in first call to <code>parse</code>.  If
     *                    <code>parse</code> returns a nonzero value, it must be
     *                    called again with <code>parseIndex</code> 
     *                    equal to that return value.
     */
    public int parse (InputStream stream, RepInfo info, int parseIndex)
        throws IOException
    {
        initParse ();
        info.setFormat (_format[0]);
        info.setMimeType (_mimeType[0]);
        info.setModule (this);
        /* We may have already done the checksums while converting a
        temporary file. */
        _ckSummer = null;
        if (_je != null && _je.getChecksumFlag () &&
        		info.getChecksum ().size () == 0) {
        	_ckSummer = new Checksummer ();
        	_cstream = new ChecksumInputStream (stream, _ckSummer);
        	_dstream = getBufferedDataStream (_cstream, _je != null ?
                 _je.getBufferSize () : 0);
        }
        else {
        	_dstream = getBufferedDataStream (stream, _je != null ?
                 _je.getBufferSize () : 0);
        }
        _propList = new LinkedList<Property> ();
        _metadata = new Property ("PNGMetadata",
             PropertyType.PROPERTY,
             PropertyArity.LIST,
             _propList);
        _nisoData = new NisoImageMetadata();
        Property nisoProp = new Property(NISO_IMAGE_MD,
                PropertyType.NISOIMAGEMETADATA, _nisoData);
        _propList.add(nisoProp);
        ErrorMessage msg;
        
        // Check that the file header matching the PNG magic numbers
        for (int i = 0; i < _sigBytes.length; i++) {
        	int byt = readUnsignedByte (_dstream);
        	if (byt != _sigBytes[i]) {
        		msg = new ErrorMessage("File header does not match PNG signature");
        		info.setMessage(msg);
        		info.setWellFormed(false);
        		return 0;
        	}
        }
        
        // Loop through the chunks
        try {
        	for (;;) {
        		PNGChunk chunk = readChunk(_dstream);
        		if (chunk == null) {
        			break;
        		}
        		if (iendSeen) {
        			msg = new ErrorMessage ("IEND chunk is not last");
        			info.setMessage (msg);
        			info.setWellFormed (false);
        			return 0;
        		}
        		chunk.setModule(this);
        		chunk.setNisoMetadata(_nisoData);
        		chunk.processChunk(info);
        	}
        }
        catch (EOFException e) {
        	msg = new ErrorMessage ("Unexpected end of file",
        			_nByte);
        	info.setMessage (msg);
        	info.setWellFormed (false);
        	return 0;
        }
        catch (Exception e) {
        	e.printStackTrace();		// *** DEBUG *****
        	msg = new ErrorMessage ("Exception " + e.getClass().getName());
        	info.setMessage (msg);
        	info.setWellFormed (false);
        	return 0;
        }
        
        /* A minimal set of requirements for the first pass. */
        boolean criticalMissing = false;
        if (!ihdrSeen) {
        	msg = new ErrorMessage("No IHDR chunk");
        	info.setMessage (msg);
        	criticalMissing = true;
        }
        if (!idatSeen) {
        	msg = new ErrorMessage("No IDAT chunk");
        	info.setMessage (msg);
        	criticalMissing = true;
        }
        if (!iendSeen) {
        	msg = new ErrorMessage("No IEND chunk");
        	info.setMessage (msg);
        	criticalMissing = true;
        }
        if (criticalMissing) {
        	info.setWellFormed (false);
        	return 0;
        }
            
        info.setProperty (_metadata);
        return 0;
    }
    
    /** This lets the module skip over the remainder of a chunk, not
     *  including the name and length. */
    public void eatChunk(PNGChunk chnk) throws IOException {
    	// TODO check the CRC instead of eating it.
    	skipBytes (_dstream, chnk.getLength() + 4);
    }
    
    /**
     *   Initializes the state of the module for parsing.
     */
    protected void initParse ()
    {
        super.initParse ();
        ihdrSeen = false;
        plteSeen = false;
        idatSeen = false;
        idatFinished = false;
        iendSeen = false;
        // TODO more?
    }
    
    /* readChunkHead reads the type and length of a chunk and
     * leaves the rest to the specific chunk processing. */
    private PNGChunk readChunk(DataInputStream dstrm) throws IOException {
    	long chunkLength;
    	String typeStr;
    	
    	char[] crc;
    	try {
    		chunkLength = readUnsignedInt(dstrm, true);
    		System.out.println("chunk length = " + chunkLength);
    	}
    	catch (EOFException e) {
    		// If we get an EOF here, we're done reading chunks.
    		return null;
    	}
    	int dbyt = 0;
    	StringBuilder chktype = new StringBuilder();
    	for (int i = 0; i < 4; i++) {
    		dbyt = readUnsignedByte(dstrm, this);
    		chktype.append ((char) dbyt);
    	}
    	typeStr = chktype.toString();

    	return PNGChunk.makePNGChunk(chunkLength,  typeStr);
    }

    
    /**********************************************************************
     * Chunk-specific functions.
     **********************************************************************/

    /** Note that an IHDR chunk has been seen */
    public void setIhdrSeen (boolean b) {
    	ihdrSeen = b;
    }
    
    /** Note that an IDAT chunk has been seen */
    public void setIdatSeen (boolean b) {
    	idatSeen = b;
    }
    
    /** Note that an IEND chunk has been seen */
    public void setIendSeen (boolean b) {
    	iendSeen = b;
    }
    
    private void processIHDR (PNGChunk chunk, RepInfo info) {
    	if (ihdrSeen) {
    		ErrorMessage msg = new ErrorMessage("More than one IHDR chunk in file");
    		info.setMessage(msg);
    		info.setWellFormed(false);
    	}
    	ihdrSeen = true;
    }
    
}