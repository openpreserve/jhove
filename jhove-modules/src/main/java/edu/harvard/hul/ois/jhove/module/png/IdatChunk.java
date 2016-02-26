package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.png.PNGChunk.ChunkType;

public class IdatChunk extends PNGChunk {

	/** Constructor */
	public IdatChunk(long leng) {
		length = leng;
		chunkType = ChunkType.IDAT;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		_module.setIdatSeen(true);
		System.out.println("Chunk Type " + chunkType.getValue() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}

}
