package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.RepInfo;
//import edu.harvard.hul.ois.jhove.module.png.PNGChunk.ChunkType;

public class IendChunk extends PNGChunk {

	/** Constructor */
	public IendChunk(long leng) {
		length = leng;
		chunkType = ChunkType.IEND;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		_module.setIendSeen(true);
		System.out.println("Chunk Type " + chunkType.getValue() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}

}
