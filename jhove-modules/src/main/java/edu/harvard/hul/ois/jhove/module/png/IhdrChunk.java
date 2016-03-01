package edu.harvard.hul.ois.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.PngModule;

public class IhdrChunk extends PNGChunk {

	/** Color type constants */
	final private static int COLOR_GRAYSCALE = 0;
	final private static int COLOR_TRUE = 2;		// RGB
	final private static int COLOR_INDEXED = 3;		// Palette
	final private static int COLOR_GRAYSCALE_ALPHA = 4;
	final private static int COLOR_TRUE_ALPHA = 6;
	
	/* Image width */
	private long width;
	
	/* Image height */
	private long height;
	
	/* Bit depth */
	private int bitDepth;
	
	/* Colour (sic) type, should be one of the color type constants */
	private int colorType;
	
	/* Compression method */
	private int compression;
	
	/* Filter method */
	private int filter;
	
	/* Interface method */
	private int interfac;
	
	
	/** Constructor */
	public IhdrChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	/** The IHDR chunk contains image information in a fixed format.
	 *  I don't think the spec says it can't have extra bytes
	 *  which would just be padding. */
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon();
		_module.setIhdrSeen(true);
		System.out.println("Chunk Type " + chunkTypeString() + " length " + length);
		if (length < 13) {
			ErrorMessage msg = new ErrorMessage ("IHDR chunk too short");
			info.setMessage(msg);
			throw new PNGException ("Bad IHDR chunk, aborting");
		}
		System.out.println ("Initial CRC = " + crc.getValue()); // make sure CRC started clean
		width = readUnsignedInt();
		System.out.println("width = " + width);
		height = readUnsignedInt();
		System.out.println("height = " + height);
		bitDepth = readUnsignedByte();
		System.out.println("bitDepth = " + bitDepth);
		colorType = readUnsignedByte();
		compression = readUnsignedByte();
		filter = readUnsignedByte();
		interfac = readUnsignedByte();
		
		// Extra bytes (assume they're allowed)
		for (int i = 0; i < length - 13; i++) {
			readUnsignedByte();
		}
		
		
		_nisoMetadata.setImageWidth(width);
		_nisoMetadata.setImageLength(height);
		int[] bits = { bitDepth };
		_nisoMetadata.setBitsPerSample(bits);
		_nisoMetadata.setColorSpace(colorTypeToNiso(colorType));
	}
	
	/* Convert PNG colour type to NISO color space */
	int colorTypeToNiso (int typ) {
		int val = 0;
		switch (typ) {
		case COLOR_GRAYSCALE:
		case COLOR_GRAYSCALE_ALPHA:
			val = 0;
			break;
		case COLOR_TRUE:
		case COLOR_TRUE_ALPHA:
			val = 2;
			break;
		case COLOR_INDEXED:
			val = 3;
			break;
		default:
			// This is an error, should report it TODO
			break;
		}
		return val;
	}
}
