package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The bKGD (background color) chunk */
public class BkgdChunk extends PNGChunk {

	/** Constructor
         * @param sig: the chunktype
         * @param leng: the length
         */
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
         * @param info: RepInfo object
         * @throws Exception
	 */
	@Override
	public void processChunk(RepInfo info) throws Exception {
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
		default :
		    break;
		}
		if (_module.isIdatSeen()) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_1);
		}
		else if (length < minLength) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_2);
		}
		else {
			switch (colorType) {
			case 0:
			case 4:
				int grayBkgd = readUnsignedShort();
				Property grayProp = new Property ("Gray background value",
						PropertyType.INTEGER,
						grayBkgd);
				info.setProperty (grayProp);
				break;
			case 2:
			case 6:
				int redBkgd = readUnsignedShort();
				int greenBkgd = readUnsignedShort();
				int blueBkgd = readUnsignedShort();
				Property redProp = new Property ("Red background value",
						PropertyType.INTEGER,
						redBkgd);
				info.setProperty (redProp);
				Property greenProp = new Property ("Green background value",
						PropertyType.INTEGER,
						greenBkgd);
				info.setProperty (greenProp);
				Property blueProp = new Property ("Blue background value",
						PropertyType.INTEGER,
						blueBkgd);
				info.setProperty (blueProp);
				break;
			case 3:
				int bkgdIndex = readUnsignedByte();
				Property bkgdProp = new Property ("Background palette index",
						PropertyType.INTEGER,
						bkgdIndex);
				info.setProperty (bkgdProp);
				break;
			default :
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
			throw new PNGException (MessageConstants.PNG_GDM_3);
		}
	}
}
