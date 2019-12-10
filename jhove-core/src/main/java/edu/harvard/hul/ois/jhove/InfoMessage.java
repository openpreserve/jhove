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
public class InfoMessage extends Message {

	private static final String prefix = "Info";

	/**
	 * Creates an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable message giving the information.
	 */
	public InfoMessage(String message) {
		super(message);
	}

	/**
	 * Creates an InfoMessage with an identifier.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 */
	public InfoMessage(JhoveMessage message) {
		super(message);
	}

	/**
	 * Creates an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable message giving the information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described.
	 */
	public InfoMessage(String message, long offset) {
		super(message, offset);
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
		super(message, offset);
	}

	/**
	 * Creates an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable message giving the information.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	public InfoMessage(String message, String subMessage) {
		super(message, subMessage);
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
		super(message, subMessage);
	}

	/**
	 * Creates an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable message giving the information.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described.
	 */
	public InfoMessage(String message, String subMessage, long offset) {
		super(message, subMessage, offset);
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
		super(message, subMessage, offset);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}
