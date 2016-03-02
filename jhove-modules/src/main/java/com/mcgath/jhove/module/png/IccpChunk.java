package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The iCCP (color profile) chunk */
public class IccpChunk extends PNGChunk {

	/** Constructor */
	public IccpChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon();
		// The iCCP chunk consists of a name, a null, 
		// a compression type (1 byte), and the profile.
		
		// state: 0 = reading name, 1 = reading compression type,
		//    2 = discarding profile data
		int state = 0;
		int compression = -1;
		StringBuilder nameSB = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int c = readUnsignedByte();
			switch (state) {
			case 0:
				if (c == 0) {
					//null ends the name
					state = 1;
				} else {
					nameSB.append((char) c);
				}
				break;
			case 1:
				// Compression is 1 byte. Save it and go to discarding-profile state
				compression = c;
				state = 2;
				break;
			case 2:
			default:
				// Just throw the profile data away
				break;
			}
		}
		if (state != 2) {
			// If we didn't reach state 2, something's wrong with the chunk structure
			ErrorMessage msg = new ErrorMessage ("Malformed iCCP chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException ("Bad iCCP chunk");
		}
		Property profile = new Property ("ICC Profile name",
				PropertyType.STRING,
				nameSB.toString());
		String cmprsType;
		if (compression == 0) {
			cmprsType = "Deflate";
		}
		else {
			cmprsType = Integer.toString (compression);
		}
		Property profileCompression = new Property ("ICC Profile compression",
				PropertyType.STRING,
				cmprsType);
		_propList.add (profile);
		_propList.add (profileCompression);
	}
}
