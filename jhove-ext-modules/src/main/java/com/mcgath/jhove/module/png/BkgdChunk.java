package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The bKGD (background color) chunk */
public class BkgdChunk extends PNGChunk {

	/** Constructor */
	public BkgdChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	/** Process the data in the chunk. All we do is note the
	 *  presence of the chunk in a property.
	 *  
	 *  The greyscale, RGB, and palette backgrounds are all
	 *  different kinds of data, so make them three different
	 *  properties so that processing software doesn't get confused.
	 */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		final String badChunk = "Bad bKGD chunk";
		processChunkCommon(info);
		ErrorMessage msg = null;
		int colorType = _module.getColorType();
		// Make sure there are enough bytes 
		int minLength = 0;
		switch (colorType) {
		case 0:
		case 4:
			minLength = 2;
			break;
		case 2:
		case 6:
			minLength = 6;
			break;
		case 3:
			minLength = 1;
			break;
		}
		if (_module.isIdatSeen()) {
			msg = new ErrorMessage ("bKGD chunk is not allowed after IDAT chunk");
		}
		else if (length < minLength) {
			msg = new ErrorMessage ("bKGD chunk is too short");
		}
		else {
			switch (colorType) {
			case 0:
			case 4:
				int grayBkgd = readUnsignedShort();
				Property grayProp = new Property ("Gray background value",
						PropertyType.INTEGER,
						Integer.valueOf(grayBkgd));
				info.setProperty (grayProp);
				break;
			case 2:
			case 6:
				int redBkgd = readUnsignedShort();
				int greenBkgd = readUnsignedShort();
				int blueBkgd = readUnsignedShort();
				Property redProp = new Property ("Red background value",
						PropertyType.INTEGER,
						Integer.valueOf(redBkgd));
				info.setProperty (redProp);
				Property greenProp = new Property ("Green background value",
						PropertyType.INTEGER,
						Integer.valueOf(greenBkgd));
				info.setProperty (greenProp);
				Property blueProp = new Property ("Blue background value",
						PropertyType.INTEGER,
						Integer.valueOf(blueBkgd));
				info.setProperty (blueProp);
				break;
			case 3:
				int bkgdIndex = readUnsignedByte();
				Property bkgdProp = new Property ("Background palette index",
						PropertyType.INTEGER,
						Integer.valueOf(bkgdIndex));
				info.setProperty (bkgdProp);
				break;
			}
			// Throw away extra bytes
			for (int i = 0; i < length - minLength; i++) {
				readUnsignedByte();
			}
		}
		if (msg != null) {
			info.setMessage (msg);
			info.setWellFormed(false);
			throw new PNGException (badChunk);
		}
	}
}
