package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The IDAT (pixel data) chunk */
public class IdatChunk extends PNGChunk {

	/** Constructor */
	public IdatChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = false;
	}
	
	/** Process the chunk. We don't analyze or report the data, but
	 *  we enforce the requirement that IDAT chunks must be consecutive. */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);
		_module.setIdatSeen(true);
		if (_module.isIdatFinished()) {
			ErrorMessage msg = new ErrorMessage(MessageConstants.PNG_GDM_20);
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException ("Misplaced IDAT chunk");
		}
		for (int i = 0; i <length; i++) {
			readUnsignedByte();
		}
	}

}
