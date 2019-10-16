package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.Rational;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The gAMA chunk, holding the gamma value.
 * (And I can't think why!)
 * 
 * @author Gary McGath
 */
public class GamaChunk extends PNGChunk {

	/** Constructor */
	public GamaChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);
		if (_module.isPlteSeen() || _module.isIdatSeen()) {
			ErrorMessage msg = new ErrorMessage (MessageConstants.PNG_GDM_7);
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (MessageConstants.PNG_GDM_8);
		}
		if (length != 4) {
			ErrorMessage msg = new ErrorMessage (MessageConstants.PNG_GDM_9, 
					String.format(MessageConstants.PNG_GDM_9_SUB.getMessage(),  
							length));
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_8);
		}
		// The value is stored multiplied by 100000
		int gamma = (int) readUnsignedInt();
		Rational ratGamma = new Rational (100000, gamma);
		Property gammaProp = new Property ("Gamma",
				PropertyType.RATIONAL,
				ratGamma);
		_propList.add(gammaProp);
		
		if (_module.isPlteSeen()) {
			ErrorMessage msg = new ErrorMessage (MessageConstants.PNG_GDM_10);
			info.setMessage (msg);
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_8);
		}
		if (_module.isIdatSeen()) {
			ErrorMessage msg = new ErrorMessage (MessageConstants.PNG_GDM_11);
			info.setMessage (msg);
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_8);
		}
	}
}
