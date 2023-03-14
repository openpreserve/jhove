package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/** The sRGB chunk, specifying sRGB color intent */
public class SrgbChunk extends PNGChunk {

	private static final String intents[] = {
		"Perceptual",
		"Relative colorimetric",
		"Saturation",
		"Absolute colorimetric"
	};
	
	/** Constructor */
	public SrgbChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	/** Process the data in the chunk.  */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);
        boolean chunkOk = true;
		int colorIntent = 0;
		if (_module.isPlteSeen()) {
            info.setMessage(new ErrorMessage(MessageConstants.PNG_GDM_48));
            chunkOk = false;
        } else if (_module.isIdatSeen()) {
            info.setMessage(new ErrorMessage(MessageConstants.PNG_GDM_49));
            chunkOk = false;
        } else if (_module.isChunkSeen(PNGChunk.iCCP_HEAD_SIG)) {
            info.setMessage(new ErrorMessage(MessageConstants.PNG_GDM_50));
            chunkOk = false;
        } else if (length == 0) {
            info.setMessage(new ErrorMessage(MessageConstants.PNG_GDM_51));
            chunkOk = false;
        } else {
			colorIntent = readUnsignedByte();
			if (colorIntent > 3) {
                JhoveMessage msg = JhoveMessages.getMessageInstance(MessageConstants.PNG_GDM_52.getId(),
						String.format(MessageConstants.PNG_GDM_52.getMessage(),  
								colorIntent)); 
                                info.setMessage(new ErrorMessage(msg));
                                chunkOk = false;
			}
		}
        if (!chunkOk) {
			info.setWellFormed (false);
			throw new PNGException (MessageConstants.PNG_GDM_53);
		}
		Property prop = new Property ("SRGB rendering intent",
				PropertyType.STRING,
				intents[colorIntent]);
		info.setProperty (prop);
		
		// Eat any extra bytes
		for (int i = 0; i <length - 1; i++) {
			readUnsignedByte();
		}
	}

}
