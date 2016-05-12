package com.mcgath.jhove.module.png;

//import edu.harvard.hul.ois.jhove.RepInfo;

/** Class for chunks of unknown type. These might be errors
 *  or extensions.
 */
public class UnknownChunk extends PNGChunk {
	
	/** Constructor */
	public UnknownChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	

}
