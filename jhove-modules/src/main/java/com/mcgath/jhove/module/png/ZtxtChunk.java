package com.mcgath.jhove.module.png;

import java.util.zip.DataFormatException;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** Representation of the zTXt (compressed text) chunk */
public class ZtxtChunk extends GeneralTextChunk {

	/** Constructor */
	public ZtxtChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
	}
	
	public void processChunk(RepInfo info) throws Exception {
		final String badchunk = "Bad zTXt chunk";
		processChunkCommon();
		
		// The tEXt chunk consists of a keyword, a null, a compression type,
		// and a value.
		// There needs to be exactly one null in the data.
		StringBuilder sb = new StringBuilder();
		String keyword = null;
		String value = null;
		ErrorMessage msg;
		int cmprsIdx = 0;
		// State values: 0 = keyword, 1 = compression type, 2 = compressed data
		int state = 0;
		byte[] compressedData = new byte[(int) length];
		for (int i = 0; i <length; i++) {
			int c = readUnsignedByte();
			switch (state) {
			case 0:
				// building keyword
				if (c == 0) {
					keyword = sb.toString();
					state = 1;
				} else {
					sb.append ((char) c);
				}
				break;
			case 1:
				// Picking up compression type, which must be 0
				if (c != 0) {
					msg = new ErrorMessage ("Unrecognized compression type " + c + " in zTXt chunk");
					info.setMessage (msg);
					info.setWellFormed (0);
					throw new PNGException (badchunk);
				}
				state = 2;
				compressedData = new byte [(int) length - i];
				break;
			case 2:
				compressedData[cmprsIdx++] = (byte) c;
				break;
			}
		}
		if (keyword != null) {
			// Decompress the value in compressedData
			try {
				value = inflateToText(compressedData);
			} catch (DataFormatException e) {
				msg = new ErrorMessage ("Bad compressed data in zTXt chunk");
				info.setMessage (msg);
				info.setWellFormed (false);
				throw new PNGException (badchunk);
			}
		}
		_module.addKeyword (keyword, value);
	}

}
