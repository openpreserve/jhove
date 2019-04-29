package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The Transparency chunk.
 * 
 *  The interpretation of the Transparency chunk depends on
 *  the color type.
 *  
 *  For color type 0 (greyscale) there is one two-byte integer,
 *  representing the gray sample value.
 *  
 *  For color type 2 (RGB) there are three two-byte integers,
 *  representing the red, blue, and green sample values respectively.
 *  
 *  For color type 3 (palette), the values are bytes giving the alpha value
 *  for the palette index. The number of values may be less than or
 *  equal to the length of the palette.
 */
public class TrnsChunk extends PNGChunk {

	/** Constructor */
	public TrnsChunk(int sig, long leng) {
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
		if (_module.isIdatSeen()) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_59);
		}
		
		int colorType = _module.getColorType();
		int lengthLeft = (int) length;

		// Make sure there are enough bytes
		if ((colorType == 0 && length < 2) || (colorType == 2 && length < 6)) {
			msg = new ErrorMessage (MessageConstants.PNG_GDM_60);
		}
		
		// tRNS chunk allowed only with certain color types
		if (colorType != 0 && colorType != 2 && colorType != 3) {
			msg = new ErrorMessage (
					String.format(
							MessageConstants.PNG_GDM_61.getMessage(), 
							colorType));
		}
		
		if (msg != null) {
			info.setMessage (msg);
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_62);
		}

		switch (colorType) {
		case 0:
			// Grayscale, one short value 
			int transGray = readUnsignedShort();
			info.setProperty (new Property ("Transparent grey value",
					PropertyType.INTEGER,
					Integer.valueOf(transGray)));
			lengthLeft = (int) length - 2;
			break;
			
		case 2:
			// RGB color, three short values
			int transRed = readUnsignedShort();
			int transGreen = readUnsignedShort();
			int transBlue = readUnsignedShort();
			info.setProperty (new Property ("Transparent red value",
					PropertyType.INTEGER,
					Integer.valueOf(transRed)));
			info.setProperty (new Property ("Transparent green value",
					PropertyType.INTEGER,
					Integer.valueOf(transGreen)));
			info.setProperty (new Property ("Transparent blue value",
					PropertyType.INTEGER,
					Integer.valueOf(transBlue)));
			lengthLeft = (int) length - 6;
			break;
			
		case 3:
			// Palette color, variable number of byte values
			int nTrans = (int) length;
			if (nTrans > 256) {
				nTrans = 256;
				lengthLeft = (int) length - 256;
			}
			else {
				lengthLeft = 0;
			}
			int[] alpha = new int[nTrans];
			for (int i = 0; i < nTrans; i++) {
				alpha[i] = readUnsignedByte();
			}
			info.setProperty (new Property ("Alpha for palette index",
					PropertyType.INTEGER,
					PropertyArity.ARRAY,
					alpha));
			break;
		default:
			// We've already made sure this is unreachable
			break;
		}
		for (int i = 0; i < lengthLeft; i++) {
			readUnsignedByte();
		}
	}
	
}
