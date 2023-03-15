package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Representation of the PLTE chunk
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
		processChunkCommon(info);
        boolean chunkOk = true;
		if (_module.isPlteSeen()) {
            info.setMessage(new ErrorMessage (MessageConstants.PNG_GDM_34));
            chunkOk = false;
		}
		_module.setPlteSeen(true);

		if (_module.isIdatSeen()) {
            info.setMessage(new ErrorMessage (MessageConstants.PNG_GDM_35));
            chunkOk = false;
		}
		if ((length % 3) != 0) {
			// must be a multiple of 3 bytes
            JhoveMessage msg = JhoveMessages.getMessageInstance(MessageConstants.PNG_GDM_36.getId(),
                    String.format(MessageConstants.PNG_GDM_36.getMessage(),
					length));
            info.setMessage(new ErrorMessage (msg));
            chunkOk = false;
		}
		if (!chunkOk) {
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_37);
		}
		for (int i = 0; i <length; i++) {
			// We don't care about the contents
			readUnsignedByte();
		}
	}

}
