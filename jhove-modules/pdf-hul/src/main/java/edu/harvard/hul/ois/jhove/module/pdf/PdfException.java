/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.pdf;

import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

/**
 * Abstract exception subclass used internally by the PDF module.
 * Throwing a PDFException indicates that the document is
 * ill-formed or invalid; use the appropriate subclass to
 * indicate which.
 */
public abstract class PdfException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 972230109944524397L;
	/*
	 * Note 25-Feb-2004: Previously PdfException indicated
	 * a not-well-formed condition, and PdfInvalidException
	 * was a subclass of PdfException that indicated an
	 * invalid condition. This is a bad class hierarchy,
	 * since the role of PdfException was ambiguous,
	 * so PdfMalformedException was added, and PdfException
	 * was made abstract.
	 */
	private final JhoveMessage message;
	private final long _offset;     // File offset at which the exception
								     // occurred
	private final Token _token;     // Token associated with the exception

	/**
	 * Create a PdfException.
	 */
	public PdfException(final JhoveMessage message) {
		this(message, -1);
	}

	/**
	 * Create a PdfException with specified offset.
	 */
	public PdfException(final JhoveMessage message, final long offset) {
		this(message, offset, null);
	}

	/**
	 * Create a PdfException with specified offset and token.
	 */
	public PdfException(final JhoveMessage message, final long offset, final Token token) {
		super(message.getMessage());
		this.message = message;
		this._offset = offset;
		this._token = token;
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
		return this._offset;
	}

	/**
	 * Return the token associated with the exception.
	 */
	public Token getToken() {
		return this._token;
	}

	/**
	 * Performs the appropriate disparagement act on a RepInfo
	 * object, such as setting the valid or well-formed
	 * flag to <code>false</code>.
	 */
	public abstract void disparage(final RepInfo info);
}
