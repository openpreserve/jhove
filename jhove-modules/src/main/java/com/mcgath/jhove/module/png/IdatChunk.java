package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** Representation of the IDAT chunk */
public class IdatChunk extends PNGChunk {

	/** Constructor */
	public IdatChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon();
		_module.setIdatSeen(true);
		if (_module.isIdatFinished()) {
			ErrorMessage msg = new ErrorMessage("IDAT chunks are not consecutive in file");
			info.setMessage(msg);
			info.setWellFormed(false);
			throw new PNGException ("Misplaced IDAT chunk");
		}
		System.out.println("Chunk Type " + chunkTypeString() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}

}
