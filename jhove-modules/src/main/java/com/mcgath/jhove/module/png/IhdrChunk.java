package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
//import edu.harvard.hul.ois.jhove.module.PngModule;

/** Representation of the IHDR (header) chunk */
public class IhdrChunk extends PNGChunk {

	/** Color type constants */
	final private static int COLOR_GRAYSCALE = 0;
	final private static int COLOR_TRUE = 2;		// RGB
	final private static int COLOR_INDEXED = 3;		// Palette
	final private static int COLOR_GRAYSCALE_ALPHA = 4;
	final private static int COLOR_TRUE_ALPHA = 6;
	
	/* Color type names. Follows the spelling in the PNG spec. */
	final private static String[] colorTypeNames = {
		"Greyscale",
		"",		// undefined
		"Truecolour",
		"Indexed-colour",
		"Greyscale with alpha",
		"",		// undefined
		"Truecolour with alpha"
	};
	
	/* Index of allowed bit depths, by color type */
	final private static int[][] allowedBitDepths = {
			{ 1, 2, 4, 8, 16},	// grayscale
			{ },				// invalid
			{ 8, 16},			// truecolour
			{ 1, 2, 4, 8},		// indexed color
			{ 8, 16},			// grayscale with alpha
			{ },				// invalid
			{ 8, 16}			// truecolour with alpha
	};
	
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
	
	/* Interlace method */
	private int interlace;
	
	
	/** Constructor */
	public IhdrChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	/** The IHDR chunk contains image information in a fixed format.
	 *  I don't think the spec says it can't have extra bytes
	 *  which would just be padding. */
	public void processChunk(RepInfo info) throws Exception {
		boolean badChunk = false;
		processChunkCommon();
		_module.setIhdrSeen(true);
		System.out.println("Chunk Type " + chunkTypeString() + " length " + length);
		if (length < 13) {
			ErrorMessage msg = new ErrorMessage ("IHDR chunk too short");
			info.setMessage(msg);
			throw new PNGException ("Bad IHDR chunk, aborting");
		}
		width = readUnsignedInt();
		height = readUnsignedInt();
		bitDepth = readUnsignedByte();
		colorType = readUnsignedByte();
		compression = readUnsignedByte();
		filter = readUnsignedByte();
		interlace = readUnsignedByte();
		
		// Extra bytes (assume they're allowed)
		for (int i = 0; i < length - 13; i++) {
			readUnsignedByte();
		}
		
		
		_nisoMetadata.setImageWidth(width);
		_nisoMetadata.setImageLength(height);
		int[] bits = { bitDepth };
		_nisoMetadata.setBitsPerSample(bits);
		_nisoMetadata.setColorSpace(colorTypeToNiso(colorType));
		if (colorType == 1 || colorType == 5 || colorType > 6) {
			ErrorMessage msg = 
					new ErrorMessage("Invalid color type " + colorType);
			info.setMessage(msg);
			info.setWellFormed(false);
			badChunk = true;
		} else {
			if (!colorAndDepthOK(colorType, bitDepth)) {
				ErrorMessage msg =
						new ErrorMessage("Cannot use color type " + colorType + " with bit depth " + bitDepth);
				info.setMessage(msg);
				info.setWellFormed(false);
				badChunk = true;
			}
			_propList.add(new Property("ColorType", 
					PropertyType.STRING,
					colorTypeNames[colorType]));
		}
		// Deflate is the only defined compression method.
		// Report other methods as numbers.
		String compressionStr;
		if (compression == 0) {
			compressionStr = "Deflate";
		} else {
			compressionStr = Integer.toString(compression);
		}
		_propList.add (new Property("Compression",
				PropertyType.STRING,
				compressionStr));
		
		_propList.add (new Property("Filter type",
				PropertyType.INTEGER,
				filter));
		
		String interlaceStr;
		switch (interlace) {
		case 0:
			interlaceStr = "None";
			break;
		case 1:
			interlaceStr = "Adam7";
			break;
		default:
			interlaceStr = Integer.toString(interlace);
			break;
		}
		_propList.add (new Property("Interlace",
				PropertyType.STRING,
				interlaceStr));
		if (badChunk) {
			throw new PNGException ("Bad IHDR chunk, aborting");
		}
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
	
	/* Check if the combination of color type and bit depth is allowed.
	 * Color must be in the range 0-6. */
	private boolean colorAndDepthOK(int color, int depth) {
		int[] allowedDepths = allowedBitDepths[color];
		boolean ok = false;
		for (int d : allowedDepths) {
			if (d == depth) {
				ok = true;
			}
		}
		return ok;
	}
}
