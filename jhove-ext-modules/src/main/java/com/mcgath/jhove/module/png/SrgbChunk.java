package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

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
		ErrorMessage msg = null;
		int colorIntent = 0;
		if (_module.isPlteSeen()) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_48);
		}
		else if (_module.isIdatSeen()) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_49);
		}
		else if (_module.isChunkSeen(PNGChunk.iCCP_HEAD_SIG)) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_50);
		}
		else if (length == 0) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_51);
		}
		else {
			colorIntent = readUnsignedByte();
			if (colorIntent > 3) {
				msg = new ErrorMessage ( 
						String.format(MessageConstants.PNG_GDM_52.getMessage(),  
								colorIntent)); 
			}
		}
		if (msg != null) {
			info.setMessage (msg);
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
