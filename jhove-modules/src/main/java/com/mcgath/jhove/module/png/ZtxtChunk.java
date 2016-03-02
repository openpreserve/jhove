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
		
		// The tEXt chunk consists of a keyword, a null, and a value.
		// There needs to be exactly one null in the data.
		StringBuilder sb = new StringBuilder();
		String keyword = null;
		String value = null;
		ErrorMessage msg;
		int cmprsIdx = 0;
		byte[] compressedData = new byte[(int) length];
		for (int i = 0; i <length; i++) {
			int c = readUnsignedByte();
			if (c == 0) {
				if (keyword != null) {
					// We already had a null. This shouldn't be here.
					msg = new ErrorMessage ("Unexpected null in zTXt chunk");
					info.setMessage (msg);
					info.setWellFormed(false);
					throw new PNGException (badchunk);
				}
				keyword = sb.toString();
			} else {
				if (keyword == null) {
					sb.append((char) c);
				} else {
					compressedData[cmprsIdx++] = (byte) c;
				}
			}
		}
		if (keyword != null) {
			// Decompress the value in compressedData
			try {
				value = inflateToText(compressedData);
			} catch (DataFormatException e) {
				msg = new ErrorMessage ("Bad compressed data in tEXt chunk");
				info.setMessage (msg);
				info.setWellFormed (false);
				throw new PNGException (badchunk);
			}
		}
		_module.addKeyword (keyword, value);
	}

}
