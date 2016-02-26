package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.PngModule;

public abstract class PNGChunk {
	protected long length;		// length of the data portion
	protected ChunkType chunkType;	// 4-character string giving chunk type
	protected char chunkData[];	// data portion, can be any length including 0
	protected char crc[];			// 4-byte CRC
	
	protected NisoImageMetadata _nisoMetadata;
	
	protected PngModule _module;
	
	public enum ChunkType {
		IHDR ("IHDR"),
		PLTE ("PLTE"),
		IDAT ("IDAT"),
		IEND ("IEND"),
		UNKNOWN ("");
		
		private final String value;
			
		private ChunkType(String strValue) {
			this.value = strValue;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public PNGChunk() {
		length = 0;
		chunkType = null;
		chunkData = null;		// Not populated till we know the length
		crc = new char[4];
	}
	
	public PNGChunk (long length, String chunkTypeStr) {
		this.length = length;
		this.chunkType = toChunkType(chunkTypeStr);
//		this.chunkData = chunkData;
//		this.crc = crc;
	}
	
	/** Construct a PNGChunk object of the appropriate subtype
	 *  based on the chunk type. */
	public static PNGChunk makePNGChunk (long length, String chunkTypeStr) {
		ChunkType chunkType = toChunkType(chunkTypeStr);
		switch (chunkType) {
		case IHDR:
			return new IhdrChunk (length);
		case IDAT:
			return new IdatChunk (length);
		case IEND:
			return new IendChunk (length);
		default:
			return new UnknownChunk (length);
		}
	}
	
	/** Hand the chunk the NISO metadata object if it needs to
	 *  put information into it. */
	public void setNisoMetadata (NisoImageMetadata nmd) {
		_nisoMetadata = nmd;
	}
	
	/** Give the chunk a reference to the PNG module. */
	public void setModule (PngModule mdl) {
		_module = mdl;
	}
	
	public long getLength () {
		return length;
	}
	
	public ChunkType getChunkType () {
		return chunkType;
	}
	
	public char[] getChunkData() {
		return chunkData;
	}
	
	public char[] getCRC () {
		return crc;
	}
	
	/** Process a chunk. When this is called, the input stream needs
	 *  to have read the type and length and be positioned at
	 *  the start of the data.
	 *  
	 *  The default behavior is to eat the chunk. This should
	 *  be the behavior only for UnknownChunk when we're done.
	 *  TODO check the CRC. Especially for unknown chunks,
	 *  since we may have gotten out of phase.
	 */
	public void processChunk(RepInfo info) throws Exception {
		System.out.println("Chunk Type " + chunkType.getValue() + " length " + length);
		_module.eatChunk(this);	// TODO temporary
	}	

	/* Convert a String to a ChunkType. I'd have liked to use a
	 * Switch, but even though JHOVE 1.12 is expected to build
	 * to Java 7, the Maven settings haven't been changed yet
	 * and I don't feel like messing with them. -- GDM */
	private static ChunkType toChunkType(String s) {
		for (ChunkType ctyp : ChunkType.values()) {
			if (s.equals(ctyp.value)) {
				return ctyp;
			}
		}
		// No match to any known chunk type
		return ChunkType.UNKNOWN;
	}

}
