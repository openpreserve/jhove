package com.mcgath.jhove.module.png;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.CRC32;

import com.mcgath.jhove.module.PngModule;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.RepInfo;

public abstract class PNGChunk {
	protected long length;		// length of the data portion
	protected int chunkType;	// chunk type as 32-bit value
	protected char chunkData[];	// data portion, can be any length including 0
	protected CRC32 crc;			// 4-byte CRC
	protected boolean ancillary;	// if true, an ancillary chunk
	protected boolean duplicateAllowed;	// ancillary chunks only -- if false, no duplicates of this type allowed
	
	protected NisoImageMetadata _nisoMetadata;
	
	/** The invoking module */
	protected PngModule _module;
	
	/** The invoking module's input stream */
	protected DataInputStream _dstream;
	
	protected List<Property> _propList;
	
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
    protected final static int IHDR_HEAD_SIG = 0x49484452;
    protected final static int PLTE_HEAD_SIG = 0x504c5445;
    protected final static int IDAT_HEAD_SIG = 0x49444154;
    protected final static int IEND_HEAD_SIG = 0x49454e44;
    protected final static int cHRM_HEAD_SIG = 0x6348524d;
    protected final static int gAMA_HEAD_SIG = 0x67414d41;
    protected final static int iCCP_HEAD_SIG = 0x69434350;
    protected final static int sBIT_HEAD_SIG = 0x73424954;
    protected final static int sRGB_HEAD_SIG = 0x73524742;
    protected final static int tEXt_HEAD_SIG = 0x74455874;
    protected final static int zTXt_HEAD_SIG = 0x7a545874;
    protected final static int iTXt_HEAD_SIG = 0x69545874;
    protected final static int bKGD_HEAD_SIG = 0x624b4744;
    protected final static int hIST_HEAD_SIG = 0x68495354;
    protected final static int pHYs_HEAD_SIG = 0x70485973;
    protected final static int sPLT_HEAD_SIG = 0x73504c54;
    protected final static int tIME_HEAD_SIG = 0x74494d45;
    protected final static int tRNS_HEAD_SIG = 0x74524e53;
	
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
		case PLTE_HEAD_SIG:
			return new PlteChunk (sig, length);

		case bKGD_HEAD_SIG:
			return new BkgdChunk (sig, length);
		case cHRM_HEAD_SIG:
			return new ChrmChunk (sig, length);
		case gAMA_HEAD_SIG:
			return new GamaChunk (sig, length);
		case hIST_HEAD_SIG:
			return new HistChunk (sig, length);
		case iCCP_HEAD_SIG:
			return new IccpChunk (sig, length);
		case iTXt_HEAD_SIG:
			return new ItxtChunk (sig, length);
		case pHYs_HEAD_SIG:
			return new PhysChunk (sig, length);
		case sBIT_HEAD_SIG:
			return new SbitChunk (sig, length);
		case sPLT_HEAD_SIG:
			return new SpltChunk (sig, length);
		case sRGB_HEAD_SIG:
			return new SrgbChunk (sig, length);
		case tEXt_HEAD_SIG:
			return new TextChunk (sig, length);
		case tIME_HEAD_SIG:
			return new TimeChunk (sig, length);
		case tRNS_HEAD_SIG:
			return new TrnsChunk (sig, length);
		case zTXt_HEAD_SIG:
			return new ZtxtChunk (sig, length);
		default:
			return new UnknownChunk (sig, length);
		}
	}
	
	/** Hand the chunk the NISO metadata object if it needs to
	 *  put information into it. */
	public void setNisoMetadata (NisoImageMetadata nmd) {
		_nisoMetadata = nmd;
	}
	
	/** Hand the main property list to the chunk */
	public void setPropertyList (List<Property> lst) {
		_propList = lst;
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
		processChunkCommon(info);
		_module.eatChunk(this);	// TODO temporary
	}	

	/** Common code to call at the start of every processChunk method. 
	 */
	public void processChunkCommon (RepInfo info) throws PNGException {
		if (ancillary && !duplicateAllowed) {
			if (_module.isChunkSeen(chunkType)) {
				ErrorMessage msg = new ErrorMessage 
						(MessageConstants.PNG_GDM_38, 
								String.format(MessageConstants.PNG_GDM_38_SUB.getMessage(),  
										chunkTypeString()));
				info.setMessage (msg);
				info.setWellFormed (false);
				throw new PNGException (MessageConstants.PNG_GDM_39);
			}
			_module.setChunkSeen (chunkType);
		}
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
			crc.update(b);
		}
		return val;
	}
	
	/** Read a 2-byte unsigned integer and update the CRC */
	public int readUnsignedShort() throws IOException {
		int val = 0;
		for (int i = 0; i < 2; i++) {
			int b = _dstream.readUnsignedByte();
			val = (val << 8) | b;
			crc.update(b);
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
