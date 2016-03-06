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
	}
	
	/** Process the data in the chunk.  */
	public void processChunk(RepInfo info) throws Exception {
		final String badChunk = "Bad sRGB chunk";
		processChunkCommon();
		ErrorMessage msg = null;
		int colorIntent = 0;
		if (_module.isPlteSeen()) {
			msg = new ErrorMessage ("sRGB chunk found after PLTE chunk");
		}
		else if (_module.isIdatSeen()) {
			msg = new ErrorMessage ("sRGB chunk found after IDAT chunk");
		}
		else if (length == 0) {
			msg = new ErrorMessage ("sRGB chunk too short");
		}
		else {
			colorIntent = readUnsignedByte();
			if (colorIntent > 3) {
				msg = new ErrorMessage ("Invalid sRGB rendering intent: " + colorIntent);
			}
		}
		if (msg != null) {
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
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
