package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** The suggested palette (sPLT) chunk */
public class SpltChunk extends PNGChunk {

	/** Constructor */
	public SpltChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = true;
	}
	
	/** Process the chunk. We add a property for the suggested 
	 *  palette by adding it to the module's list of sPLT's.
	 */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		final String badChunk = "Bad sPLT chunk";
		String paletteName = null;
		processChunkCommon(info);
		if (_module.isIdatSeen()) {
			ErrorMessage msg = new ErrorMessage ("sPLT chunk is not allowed after IDAT chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		int lengthLeft = (int) length;
		
		// Read the name.
		int maxNameLen;
		if (length > 80) {
			maxNameLen = 80;
		} else {
			maxNameLen = (int) length;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < maxNameLen; i++) {
			char c = (char) readUnsignedByte();
			--lengthLeft;
			if (c == 0) {
				paletteName = sb.toString();
				break;
			}
			sb.append(c);
		}
		if (paletteName == null) {
			// No null seen to terminate name
			ErrorMessage msg = new ErrorMessage ("Name not terminated in sPLT chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		
		// Sample depth must be 8 or 16 bits
		int sampleDepth = readUnsignedByte();
		--lengthLeft;
		if (sampleDepth != 8 && sampleDepth != 16) {
			ErrorMessage msg = new ErrorMessage ("Invalid sample depth " + sampleDepth + " for sPLT chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		
		// The rest of the chunk is RGBA sample values plus frequency,
		// with each sample being 10 bytes if the sample depth is 16
		// and 6 bytes if it's 8. We don't care about the content
		// but have to make sure the size is properly divisible, and
		// we report the sample count.
		if ((sampleDepth == 8 && (lengthLeft % 6) != 0) ||
				(sampleDepth == 16 && (lengthLeft % 10) != 0)) {
			ErrorMessage msg = new ErrorMessage ("Invalid length for sPLT chunk");
			info.setMessage (msg);
			info.setWellFormed (false);
			throw new PNGException (badChunk);
		}
		int nSamples;
		if (sampleDepth == 8) {
			nSamples = lengthLeft / 6;
		} else {
			nSamples = lengthLeft / 10;
		}
		_module.addSplt(paletteName, sampleDepth, nSamples);
		for (int i = 0; i < lengthLeft; i++) {
			readUnsignedByte();
		}
	}
}
