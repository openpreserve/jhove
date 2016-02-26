package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.RepInfo;

public class IhdrChunk extends PNGChunk {

	/** Constructor */
	public IhdrChunk(long leng) {
		length = leng;
		chunkType = ChunkType.IHDR;
	}
	
	/** The IHDR chunk contains image information in a fixed format.
	 *  I don't think the spec says it can't have extra bytes
	 *  which would just be padding. */
	public void processChunk(RepInfo info) throws Exception {
		_module.setIhdrSeen(true);
		System.out.println("Chunk Type " + chunkType.getValue() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}
}
