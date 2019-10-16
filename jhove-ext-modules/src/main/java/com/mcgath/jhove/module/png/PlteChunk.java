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
		ancillary = false;
	}
	
	@Override
	public void processChunk(RepInfo info) throws Exception {
		ErrorMessage msg = null;
		processChunkCommon(info);
		if (_module.isPlteSeen()) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_34);
		}
		_module.setPlteSeen(true);

		if (_module.isIdatSeen()) {
			msg = new ErrorMessage(MessageConstants.PNG_GDM_35);
		}
		if ((length % 3) != 0) {
			// must be a multiple of 3 bytes
			msg = new ErrorMessage(String.format(MessageConstants.PNG_GDM_36.getMessage(),  
					length));
		}
		if (msg != null) {
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_37);
		}
		for (int i = 0; i <length; i++) {
			// We don't care about the contents
			readUnsignedByte();
		}
	}

}
