/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.tiff;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * Exception subclass used internally by the TIFF module.
 */
public final class TiffException extends Exception {
	private final JhoveMessage message;
	private final long offset;     // File offset at which the exception
								     // occurred

	/**
	 * Create a TiffException.
	 */
	public TiffException(final JhoveMessage message) {
		this(message, -1);
	}

	/**
	 * Create a TiffException with specified offset.
	 */
	public TiffException(final JhoveMessage message, long offset) {
		super(message.getMessage());
		this.message = message;
		this.offset = offset;
	}

	/**
	 * @return the JhoveMessage associated with this exception
	 */
	public JhoveMessage getJhoveMessage() {
		return this.message;
	}

	/**
	 * Returns the offset at which the exception occurred.
	 */
	public long getOffset() {
		return this.offset;
	}
}
