package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;
//import edu.harvard.hul.ois.jhove.module.png.PNGChunk.ChunkType;

public class IendChunk extends PNGChunk {

	/** Constructor */
	public IendChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon();
		_module.setIendSeen(true);
		// This chunk is supposed to have a length of 0.
		// If it's bigger, eat the extra bytes and declare the
		// file invalid but not ill-formed.
		if (length > 0) {
			ErrorMessage msg = new ErrorMessage("IEND chunk is not empty");
			info.setMessage (msg);
			info.setValid (false);
			for (int i = 0; i < length; i++) {
				readUnsignedByte();
			}
		}
	}

}
