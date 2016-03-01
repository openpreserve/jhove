package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.RepInfo;

public class IdatChunk extends PNGChunk {

	/** Constructor */
	public IdatChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon();
		_module.setIdatSeen(true);
		System.out.println("Chunk Type " + chunkTypeString() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}

}
