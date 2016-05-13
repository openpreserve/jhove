package com.mcgath.jhove.module.png;

import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;



/** A superclass for TextChunk, ZtxtChunk, and ItxtChunk */
public abstract class GeneralTextChunk extends PNGChunk {

	/** Standard property keywords. */
	protected final static String[] standardKeywords = {
			"Author",
			"Description",
			"Copyright",
			"Creation Time",
			"Software",
			"Disclaimer",
			"Warning",
			"Source"
	};
	
	/** Add a property for the keyword and value. If it isn't one
	 *  of the standard properties, what should we do?
	 */
//	protected void addTextProperty (String keyword, String value, RepInfo info)
//			throws PNGException {
//		boolean badChunk = false;
//		ErrorMessage msg;
//		if (keyword == null) {
//			msg = new ErrorMessage ("No keyword terminator in " + chunkTypeString() + " chunk");
//			info.setMessage(msg);
//			badChunk = true;
//		}
//		else {
//			if (keyword.length() == 0) {
//				msg = new ErrorMessage ("Keyword in " + chunkTypeString() + " chunk is empty");
//				info.setMessage(msg);
//				badChunk = true;
//			}
//			if (value.length() == 0) {
//				msg = new ErrorMessage ("Value in " + chunkTypeString() + " chunk is empty");
//				info.setMessage(msg);
//				badChunk = true;
//			}
//		}
//		if (badChunk) {
//			info.setWellFormed(false);
//			throw new PNGException ("Error in " + chunkTypeString() + "chunk");
//		}
//		for (String kwd : standardKeywords) {
//			if (kwd.equals(keyword)) {
//				_propList.add (new Property (keyword,
//						PropertyType.STRING,
//						value));
//				break;
//			}
//		}
//	}
	
	/** Expand a byte array using the DEFLATE method. */
	protected String inflateToText (byte[] b) throws DataFormatException {
		StringBuilder res = new StringBuilder();
		final int bufSize = 128;
		byte[] deflateBuf = new byte[bufSize];
		// We use the ZIP inflater provided by Java
		Inflater inflater = new Inflater();
		inflater.setInput (b);
		while (!inflater.finished()) {
			int nBytes = inflater.inflate(deflateBuf);
//			for (int i = 0; i < nBytes; i++) {
//				res.append ((char) deflateBuf[i]);
//			}
			try {
				res.append (new String (deflateBuf, 0, nBytes, "ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				// should never happen
			}
			
		}
		return res.toString();
	}
}
