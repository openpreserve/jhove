package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.RepInfo;

/** Class for chunks of unknown type. These might be errors
 *  or extensions.
 */
public class UnknownChunk extends PNGChunk {
	public UnknownChunk(long leng) {
		length = leng;
		chunkType = ChunkType.UNKNOWN;
	}
	

}
