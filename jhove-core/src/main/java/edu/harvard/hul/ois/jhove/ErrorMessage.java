/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

/**
 * This class encapsulates an error message from a Module, representing
 * a problem in the content being analyzed.
 */
public class ErrorMessage extends Message {

	private static final String prefix = "Error";

	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/

	/**
	 * Creates an ErrorMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 */
	public ErrorMessage(JhoveMessage message) {
		super(message);
	}

	/**
	 * Creates an ErrorMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param offset
	 *            The offset in the file at which the problem
	 *            was detected.
	 */
	public ErrorMessage(JhoveMessage message, long offset) {
		super(message, offset);
	}

	/**
	 * Creates an ErrorMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	public ErrorMessage(JhoveMessage message, String subMessage) {
		super(message, subMessage);
	}

	/**
	 * Creates an ErrorMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            The offset in the file at which the problem
	 *            was detected.
	 */
	public ErrorMessage(JhoveMessage message, String subMessage,
			long offset) {
		super(message, subMessage, offset);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}
