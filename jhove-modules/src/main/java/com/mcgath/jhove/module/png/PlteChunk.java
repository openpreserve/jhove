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
		processChunkCommon();
		_module.setPlteSeen(true);
		if (_module.isIdatSeen()) {
			ErrorMessage msg = new ErrorMessage("PLTE chunk comes after first IDAT chunk");
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException ("Misplaced PLTE chunk");
		}
		_module.eatChunk(this);	// TODO temporary
	}

}
