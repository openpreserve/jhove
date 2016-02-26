package edu.harvard.hul.ois.jhove.module.png;

public class PNGChunk {
	private long length;		// length of the data portion
	private ChunkType chunkType;	// 4-character string giving chunk type
	private char chunkData[];	// data portion, can be any length including 0
	private char crc[];			// 4-byte CRC
	
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
	
	public PNGChunk (long length, String chunkTypeStr, char[] chunkData, char crc[]) {
		this.length = length;
		this.chunkType = toChunkType(chunkTypeStr);
		this.chunkData = chunkData;
		this.crc = crc;
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
	
	/* Convert a String to a ChunkType. I'd have liked to use a
	 * Switch, but even though JHOVE 1.12 is expected to build
	 * to Java 7, the Maven settings haven't been changed yet
	 * and I don't feel like messing with them. -- GDM */
	private ChunkType toChunkType(String s) {
		for (ChunkType ctyp : ChunkType.values()) {
			if (s.equals(ctyp.value)) {
				return ctyp;
			}
		}
		// No match to any known chunk type
		return ChunkType.UNKNOWN;
	}

}
