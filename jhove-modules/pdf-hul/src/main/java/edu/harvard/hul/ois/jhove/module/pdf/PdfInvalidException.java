/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

/**
 * Exception subclass used internally by the PDF module.
 * A PdfInvalidException is thrown when a condition indicates
 * that the document is invalid but not necessarily ill-formed.
 */
public final class PdfInvalidException extends PdfException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3224356746816316033L;

	/**
	 * Creates a PdfInvalidException.
	 */
	public PdfInvalidException(final JhoveMessage message) {
		super(message);
	}

	/**
	 * Creates a PdfInvalidException with specified offset.
	 */
	public PdfInvalidException(final JhoveMessage message, final long offset) {
		super(message, offset);
	}


	/**
	 * Creates a PdfInvalidException with specified offset and token.
	 */
	public PdfInvalidException(final JhoveMessage message, final long offset,
			final Token token) {
		super(message, offset, token);
	}

	/**
	 * Performs the appropriate disparagement act on a RepInfo
	 * object. For a PdfInvalidException, this is to call
	 * <code>setValid (false)</code>.
	 */
	@Override
	public void disparage(final RepInfo info) {
		info.setValid(false);
	}
}
