/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.tiff;

import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * Encapsulation of an Exif Interoperability IFD (for Exif).
 */
public class InteroperabilityIFD extends IFD {
	/******************************************************************
	 * PRIVATE CLASS FIELDS.
	 ******************************************************************/

	/** InteroperabilityIndex tag. */
	private static final int INTEROPERABILITYINDEX = 1;

	/******************************************************************
	 * PRIVATE INSTANCE FIELDS.
	 ******************************************************************/

	/** Interoperability identification tag (1). */
	private String _interoperabilityIndex;

	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/

	/**
	 * Instantiate an <code>InteroperabilityIFD</code> object.
	 * 
	 * @param offset
	 *            IFD offset
	 * @param info
	 *            the RepInfo object
	 * @param raf
	 *            TIFF file
	 * @param bigEndian
	 *            True if big-endian file
	 */
	public InteroperabilityIFD(long offset, RepInfo info, RandomAccessFile raf,
			boolean bigEndian) {
		super(offset, info, raf, bigEndian);
	}

	/******************************************************************
	 * PUBLIC INSTANCE METHODS.
	 ******************************************************************/

	/** Get the InteroperabilityIndex tag (1). */
	public String getInteroperabilityIndex() {
		return _interoperabilityIndex;
	}

	/** Get the IFD properties. */
	@Override
	public Property getProperty(boolean rawOutput) {
		List entries = new LinkedList();
		entries.add(new Property("Index", PropertyType.STRING,
				_interoperabilityIndex));

		return propertyHeader("Exif Interoperability", entries);
	}

	/** Lookup an IFD tag. */
	@Override
	public void lookupTag(int tag, int type, long count, long value)
			throws TiffException {
		try {
			if (tag == INTEROPERABILITYINDEX) {
				checkType(tag, type, ASCII);
				_interoperabilityIndex = readASCII(count, value);
			} else {
				String subMessage = MessageFormat.format(
						MessageConstants.TIFF_HUL_15_SUB.getMessage(),
						Integer.valueOf(tag));
				_info.setMessage(new ErrorMessage(MessageConstants.TIFF_HUL_15,
						subMessage, value));
			}
		} catch (IOException e) {
			String mess = MessageFormat.format(
					MessageConstants.TIFF_HUL_16.getMessage(),
					Integer.valueOf(tag));
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.TIFF_HUL_16.getId(), mess);
			throw new TiffException(message, value);
		}
	}
}
