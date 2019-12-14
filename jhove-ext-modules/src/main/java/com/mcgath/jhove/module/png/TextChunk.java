package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
//import edu.harvard.hul.ois.jhove.Property;
//import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** Representation of the tEXt (plain text) chunk */
public class TextChunk extends GeneralTextChunk {


	
	/** Constructor */
	public TextChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = true;
	}
	
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);
		
		// The tEXt chunk consists of a keyword, a null, and a value.
		// There needs to be exactly one null in the data.
		StringBuilder sb = new StringBuilder();
		String keyword = null;
		String value = null;
		ErrorMessage msg;
		for (int i = 0; i <length; i++) {
			int c = readUnsignedByte();
			if (c == 0) {
				if (keyword != null) {
					// We already had a null. This shouldn't be here.
					msg = new ErrorMessage (MessageConstants.PNG_GDM_54);
					info.setMessage (msg);
					info.setWellFormed(false);
					throw new PNGException (MessageConstants.PNG_GDM_55);
				}
				keyword = sb.toString();
				sb = new StringBuilder();	// Now for the value
			} else {
				sb.append((char) c);
			}
		}
		if (keyword != null) {
			value = sb.toString();
		}
		_module.addKeyword (keyword, value);
	}
	

}
