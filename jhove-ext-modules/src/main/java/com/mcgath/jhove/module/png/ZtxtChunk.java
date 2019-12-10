package com.mcgath.jhove.module.png;

import java.util.zip.DataFormatException;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/** Representation of the zTXt (compressed text) chunk */
public class ZtxtChunk extends GeneralTextChunk {

	/** Constructor
         * @param sig: int representing chunktype
         * @param leng: long representing length
         */
	public ZtxtChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = true;
	}

	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);

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
					msg = new ErrorMessage (MessageConstants.PNG_GDM_63,
							String.format(MessageConstants.PNG_GDM_63_SUB.getMessage(),
									c));
					info.setMessage (msg);
					info.setWellFormed (0);
					throw new PNGException (MessageConstants.PNG_GDM_64);
				}
				state = 2;
				compressedData = new byte [(int) length - i];
				break;
			case 2:
				compressedData[cmprsIdx++] = (byte) c;
				break;
			default :
		    		break;
			}
		}
		if (keyword != null) {
			// Decompress the value in compressedData
			try {
				value = inflateToText(compressedData);
			} catch (DataFormatException e) {
				msg = new ErrorMessage (MessageConstants.PNG_GDM_65);
				info.setMessage (msg);
				info.setWellFormed (false);
				throw new PNGException (MessageConstants.PNG_GDM_64);
			}
		}
		_module.addKeyword (keyword, value);
	}

}
