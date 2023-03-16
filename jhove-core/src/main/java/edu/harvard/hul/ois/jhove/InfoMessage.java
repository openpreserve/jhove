/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

/**
 * This class encapsulates an informational message from a Module, giving
 * information (not necessarily a problem) about the content being analyzed
 * or the way that JHOVE deals with it.
 */
public final class InfoMessage extends Message {
	/**
	 * Creates an InfoMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 */
	public InfoMessage(JhoveMessage message) {
        this(message, NULL);
	}

	/**
	 * Creates an InfoMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described.
	 */
	public InfoMessage(JhoveMessage message, long offset) {
        this(message, message.getSubMessage(), offset);
	}

	/**
	 * Creates an InfoMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	public InfoMessage(JhoveMessage message, String subMessage) {
        this(message, subMessage, NULL);
	}

	/**
	 * Creates an InfoMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described.
	 */
	public InfoMessage(JhoveMessage message, String subMessage, long offset) {
        super(message, subMessage, offset, "Info");
	}
}
