package com.mcgath.jhove.module.png;

import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.RepInfo;

/**
 * Representation of the iTXt (internationalized text) chunk
 *
 * @see <a href="https://www.w3.org/TR/PNG/#11iTXt">https://www.w3.org/TR/PNG/#11iTXt</a>
 **/
public class ItxtChunk extends GeneralTextChunk {

	/** Constructor */
	public ItxtChunk(int sig, long leng) {
		chunkType = sig;
		length = leng;
		ancillary = true;
		duplicateAllowed = true;
	}

	/** Process the data portion of the chunk. */
	@Override
	public void processChunk(RepInfo info) throws Exception {
		processChunkCommon(info);

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
							new ErrorMessage(MessageConstants.PNG_GDM_29,
							String.format(MessageConstants.PNG_GDM_29_SUB.getMessage(),compressionType));
							info.setMessage (msg);
							info.setWellFormed (false);
						throw new PNGException (MessageConstants.PNG_GDM_30);
					}
				}
				sb = new StringBuilder(); // set up for language
				state = 3;
				break;
			case 3:		// getting language
				if (c == 0) {
					language = sb.toString();
					state = 4;
					sb = new StringBuilder(); // set up for translated keyword
				} else {
					sb.append((char) c);
				}
				break;
			case 4:		// translated keyword
				if (c == 0) {
						translatedKeyword = sb.toString();
						state = 5;
						// set up for text value. (i+1) because we started at 0
						valueData = new byte[(int) length - (i + 1)];
				} else {
						sb.append((char) c);
				}
				break;
			case 5: // value of text
			default:
					valueData[valueIdx++] = (byte) c;
			break;
			}
		}

		// assemble value, decompressing if necessary
		String value;
		if (compressionFlag != 0) {
			value = inflateToText(valueData);
		} else {
			value = new String(valueData, "ISO-8859-1");
		}
		_module.addKeyword(keyword, translatedKeyword, value, language);
	}

}
