package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Rational;
import edu.harvard.hul.ois.jhove.RepInfo;

/* The cHRM (primary chromaticities and white point) chunk */
public class ChrmChunk extends PNGChunk {

	/* White point X and Y */
	private long whitePtX;
	private long whitePtY;
	
	/* Primary chromaticities */
	private long redX;
	private long redY;
	private long greenX;
	private long greenY;
	private long blueX;
	private long blueY;
	
	/** Constructor */
	public ChrmChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = false;
	}
	
	/** The IHDR chunk contains image information in a fixed format.
	 *  I don't think the spec says it can't have extra bytes
	 *  which would just be padding. */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);
		if (_module.isPlteSeen() || _module.isIdatSeen()) {
			ErrorMessage msg = new ErrorMessage (MessageConstants.PNG_GDM_4);
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (MessageConstants.PNG_GDM_5);
		}
		if (length < 32) {
			ErrorMessage msg = new ErrorMessage(MessageConstants.PNG_GDM_6);
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException (MessageConstants.PNG_GDM_5);
		}
		whitePtX = readUnsignedInt();
		whitePtY = readUnsignedInt();
		redX = readUnsignedInt();
		redY = readUnsignedInt();
		greenX = readUnsignedInt();
		greenY = readUnsignedInt();
		blueX = readUnsignedInt();
		blueY = readUnsignedInt();
		
		_nisoMetadata.setWhitePointXValue(new Rational(whitePtX, 100000));
		_nisoMetadata.setWhitePointYValue(new Rational(whitePtY, 100000));
		_nisoMetadata.setPrimaryChromaticitiesRedX(new Rational(redX, 100000));
		_nisoMetadata.setPrimaryChromaticitiesRedY(new Rational(redY, 100000));
		_nisoMetadata.setPrimaryChromaticitiesGreenX(new Rational(greenX, 100000));
		_nisoMetadata.setPrimaryChromaticitiesGreenY(new Rational(greenY, 100000));
		_nisoMetadata.setPrimaryChromaticitiesBlueX(new Rational(blueX, 100000));
		_nisoMetadata.setPrimaryChromaticitiesBlueY(new Rational(blueY, 100000));
		
		// Discard any excess
		for (int i = 0; i <length - 32; i++) {
			readUnsignedByte();
		}
	}
}
