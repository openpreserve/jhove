package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The sBIT (significant bits) chunk */
public class SbitChunk extends PNGChunk {

	/** Constructor */
	public SbitChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	/** The SBIT chunk contains 1 to 4 bytes of information giving the
	 *  number of significant bits per color.  Ideally, we should check
	 *  this against the color model. */
	public void processChunk(RepInfo info) throws Exception {
		final String badChunk = "Bad sBIT chunk";
		processChunkCommon();
		if (_module.isPlteSeen() || _module.isIdatSeen()) {
			ErrorMessage msg = new ErrorMessage ("sBIT chunk occurs after PLTE or IDAT");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		if (length == 0 || length > 4) {
			ErrorMessage msg = new ErrorMessage ("Bad length in sBIT chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		int[] sbitVal = new int[(int) length];
		for (int i = 0; i < length; i++) {
			sbitVal[i] = readUnsignedByte();
		}
		Property prop = new Property ("Significant bits",
				PropertyType.INTEGER,
				PropertyArity.ARRAY,
				sbitVal);
		_propList.add (prop);
	}
}
