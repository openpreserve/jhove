package edu.harvard.hul.ois.jhove.module.png;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.PngModule;

public abstract class PNGChunk {
	protected long length;		// length of the data portion
	protected int chunkType;	// chunk type as 32-bit value
	protected char chunkData[];	// data portion, can be any length including 0
	protected CRC32 crc;			// 4-byte CRC
	
	protected NisoImageMetadata _nisoMetadata;
	
	/** The invoking module */
	protected PngModule _module;
	
	/** The invoking module's input stream */
	protected DataInputStream _dstream;
	
//	public enum ChunkType {
//		IHDR (IHDR_HEAD_SIG, "IHDR"),
//		PLTE (PLTE_HEAD_SIG, "PLTE"),
//		IDAT (IDAT_HEAD_SIG, "IDAT"),
//		IEND (IEND_HEAD_SIG, "IEND"),
//		CHRM (cHRM_HEAD_SIG, "cHRN"),
//		GAMA (gAMA_HEAD_SIG, "gAMA"),
//		ICCP (iCCP_HEAD_SIG, "iCCP"),
//		SBIT (sBIT_HEAD_SIG, "sBIT"),
//		SRGB (sRGB_HEAD_SIG, "sRGB"),
//		TEXT (tEXt_HEAD_SIG, "tEXt"),
//		ZTXT (zTXt_HEAD_SIG, "zTXt"),
//		BKGD (bKGD_HEAD_SIG, "bKGD"),
//		HIST (hIST_HEAD_SIG, "hIST"),
//		PHYS (pHYs_HEAD_SIG, "pHYs"),
//		SPLT (sPLT_HEAD_SIG, "sPLT"),
//		TIME (tIME_HEAD_SIG, "tIME"),
//		TRNS (tRNS_HEAD_SIG, "tRNS"),
//		UNKNOWN (0, "");
//		
//		/* The chunk signature, i.e., its four-byte name
//		 * treated as a number, as recommended in the spec */
//		private final int sig;
//		
//		/* The chunk signature as a string for human readability */
//		private final String strValue;
//			
//		private ChunkType(int intSig, String strVal) {
//			this.sig = intSig;
//			this.strValue = strVal;
//		}
//		
//		public String getStrValue() {
//			return strValue;
//		}
//		
//		/** Return the chunkType as an array of 4 bytes */
//		public int[] getByteValues () {
//			int b[] = new int[4];
//			b[0] = (sig >> 24) & 0XFF;
//			b[1] = (sig >> 16) & 0XFF;
//			b[2] = (sig >> 8) & 0XFF;
//			b[3] = sig & 0XFF;
//			return b;
//		}
//	}
	
    /*
     * Chunk signatures.
     *
     * Java *IS* Big Endian, PNG chunk signatures are 4 byte strings we
     * *CAN* read into an int variable since all of them have bit 7
     * set to 0.
     *
     * Therefore we can check each chunk signature against int
     * constants (one opcode executed, no loops).
     *
     * About names: these name violate the Java naming rules for
     * constants, but I prefer to keep the PNG chunk name cases, since
     * they are meaningful for the properties of each chunk.
     */
    private final static int IHDR_HEAD_SIG = 0x49484452;
    private final static int PLTE_HEAD_SIG = 0x504c5445;
    private final static int IDAT_HEAD_SIG = 0x49444154;
    private final static int IEND_HEAD_SIG = 0x49454e44;
    private final static int cHRM_HEAD_SIG = 0x6348524d;
    private final static int gAMA_HEAD_SIG = 0x67414d41;
    private final static int iCCP_HEAD_SIG = 0x69434350;
    private final static int sBIT_HEAD_SIG = 0x73424954;
    private final static int sRGB_HEAD_SIG = 0x73524742;
    private final static int tEXt_HEAD_SIG = 0x74455874;
    private final static int zTXt_HEAD_SIG = 0x7a545874;
    private final static int iTXt_HEAD_SIG = 0x69545874;
    private final static int bKGD_HEAD_SIG = 0x624b4744;
    private final static int hIST_HEAD_SIG = 0x68495354;
    private final static int pHYs_HEAD_SIG = 0x70485973;
    private final static int sPLT_HEAD_SIG = 0x73504c54;
    private final static int tIME_HEAD_SIG = 0x74494d45;
    private final static int tRNS_HEAD_SIG = 0x74524e53;
	
    public PNGChunk() {
		length = 0;
		chunkType = 0;
		chunkData = null;		// Not populated till we know the length
		this.crc = new CRC32();
	}
	
	
	/** Construct a PNGChunk object of the appropriate subtype
	 *  based on the chunk type. */
	public static PNGChunk makePNGChunk (long length, int sig) {
		switch (sig) {
		case IHDR_HEAD_SIG:
			return new IhdrChunk (sig, length);
		case IDAT_HEAD_SIG:
			return new IdatChunk (sig, length);
		case IEND_HEAD_SIG:
			return new IendChunk (sig, length);
		default:
			return new UnknownChunk (sig, length);
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
	
	/** Give the chunk a reference to the data stream. */
	public void setInputStream (DataInputStream dstrm) {
		_dstream = dstrm;
	}
	
	public long getLength () {
		return length;
	}
	
	public int getChunkType () {
		return chunkType;
	}
	
	public char[] getChunkData() {
		return chunkData;
	}
	
	public long getCRC () {
		return crc.getValue();
	}
	
	/** Process a chunk. When this is called, the input stream needs
	 *  to have read the type and length and be positioned at
	 *  the start of the data.
	 *  
	 *  The default behavior is to eat the chunk. This should
	 *  be the behavior only for UnknownChunk when we're done.
	 */
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon();
		_module.eatChunk(this);	// TODO temporary
	}	

	/** Common code to call at the start of every processChunk method. 
	 */
	public void processChunkCommon () {
		int[] chunkTypeVal = chunkTypeBytes();
		for (int i = 0; i < 4; i++) {
			crc.update(chunkTypeVal[i]);
		}
	}
	
	/* Use these methods exclusively to read the data portion
	 * (and nothing else), so that the CRC is calculated.
	 */
	
	/** Read a 4-byte unsigned integer and update the CRC */
	public long readUnsignedInt() throws IOException {
		long val = 0;
		for (int i = 0; i < 4; i++) {
			int b = _dstream.readUnsignedByte();
			val = (val << 8) | b;
			crc.update((int) b);
		}
		return val;
	}
	
	/** Read a single byte and update the CRC */
	public int readUnsignedByte() throws IOException {
		int b = _dstream.readUnsignedByte();
		crc.update(b);
		return b;
	}

	/** Skip over all the bytes, updating the CRC */
	public void skipBytes(int count) throws IOException {
		for (int i = 0; i < count; i++) {
			int b = _dstream.readUnsignedByte();
			crc.update(b);
		}
	}
	
	/** Read the CRC itself. Naturally, this doesn't update
	 *  the CRC.
	 */
	public long readCRC() throws IOException {
		long c = ModuleBase.readUnsignedInt (_dstream, true);
		return c;
	}

	/* Create a ChunkType from a signature. I'd have liked to use a
	 * Switch, but even though JHOVE 1.12 is expected to build
	 * to Java 7, the Maven settings haven't been changed yet
	 * and I don't feel like messing with them. -- GDM */
//	private static ChunkType toChunkType(int sig) {
//		for (ChunkType ctyp : ChunkType.values()) {
//			if (sig == ctyp.sig) {
//				return ctyp;
//			}
//		}
//		// No match to any known chunk type
//		return ChunkType.UNKNOWN;
//	}
	
	/* Convert chunk type to string value. */
	public String chunkTypeString() {
		int[] bytes = chunkTypeBytes();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int b = bytes[i];
			if (b < 32) {
				sb.append("?");
			} else {
				sb.append ((char) b);
			}
		}
		return sb.toString();
	}
	
	/* Convert chunk type to byte array. */
	private int[] chunkTypeBytes() {
		int[] bytes = new int[4];
		bytes[0] = (chunkType >> 24) & 0X7F;
		bytes[1] = (chunkType >> 16) & 0X7F;
		bytes[2] = (chunkType >> 8) & 0X7F;
		bytes[3] = chunkType & 0X7F;
		return bytes;
	}
}
