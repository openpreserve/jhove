package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** Representation of the iTXt (internationalized text) chunk */
public class ItxtChunk extends GeneralTextChunk {

	/** Constructor */
	public ItxtChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	/** Process the data portion of the chunk. */
	public void processChunk(RepInfo info) throws Exception {
		final String badchunk = "Bad iTXt chunk";
		processChunkCommon();
		
		//iTXt chunks may have either compressed or uncompressed values.
		
		// state values:
		// 0 = getting keyword,
		// 1 = compression flag
		// 2 = compression type
		// 3 = language
		// 4 = translated keyword
		// 5 = value
		int state = 0;
		int compressionFlag = 0;
		int compressionType = 0;
		String keyword = null;
		String translatedKeyword = null;
		String language = null;
		byte[] valueData = null;
		StringBuilder sb = new StringBuilder();
		int valueIdx = 0;
		for (int i = 0; i <length; i++) {
			int c = readUnsignedByte();
			switch (state) {
			case 0:		// getting keyword
				if (c == 0) {
					keyword = sb.toString();
					state = 1;
				} else {
					sb.append((char) c);
				}
				break;
			case 1:		// getting compression flag
				compressionFlag = c;
				state = 2;
				break;
			case 2:		// getting compression type
				if (compressionFlag != 0) {
					compressionType = c;
					if (compressionType != 0) {
						ErrorMessage msg = 
								new ErrorMessage ("Unknown compression type " + compressionType  + " in iTXt chunk");
						info.setMessage (msg);
						info.setWellFormed (false);
						throw new PNGException (badchunk);
					}
					state = 3;
					sb = new StringBuilder();	// set up for language
				}
				break;
			case 3:		// getting language
				if (c == 0) {
					if (sb.length() > 0) {
						language = sb.toString();
					}
					state = 4;
					sb = new StringBuilder();	// set up for translated keyword
				}
				break;
			case 4:		// translated keyword
				if (c == 0) {
					if (sb.length() > 0) {
						translatedKeyword = sb.toString();
					}
					state = 5;
					valueData = new byte[(int) length - i];	// set up for value
				}
				break;
			case 5:		// value
			default:
				valueData[valueIdx++] = (byte) c;
				break;
			}
			
			// assemble value, decompressing if necessary
			String value;
			if (compressionFlag != 0) {
				value = inflateToText(valueData);
			} else {
				value = new String (valueData, "ISO-8859-1");
			}
			_module.addKeyword(keyword, translatedKeyword, value,language);
		}
	}

}
