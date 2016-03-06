package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** Representation of the PLTE chunk
 */
public class PlteChunk extends PNGChunk {

	/** Constructor */
	public PlteChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		ErrorMessage msg = null;
		processChunkCommon();
		if (_module.isPlteSeen()) {
			msg = new ErrorMessage ("Multiple PLTE chunks");
		}
		_module.setPlteSeen(true);

		if (_module.isIdatSeen()) {
			msg = new ErrorMessage("PLTE chunk comes after first IDAT chunk");
		}
		if ((length % 3) != 0) {
			// must be a multiple of 3 bytes
			msg = new ErrorMessage("Invalid PLTE chunk length " + length);
		}
		if (msg != null) {
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException ("PLTE chunk error");
		}
		for (int i = 0; i <length; i++) {
			// We don't care about the contents
			readUnsignedByte();
		}
	}

}
