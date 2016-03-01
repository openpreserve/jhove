package edu.harvard.hul.ois.jhove.module.png;

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
		System.out.println("Chunk Type " + chunkTypeString() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}

}
