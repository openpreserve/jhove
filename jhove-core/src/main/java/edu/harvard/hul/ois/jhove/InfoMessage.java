/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

/**
 * This class encapsulates an informational message from a Module, giving
 * information (not necessarily a problem)
 * about the content being analyzed or the way that Jhove
 * deals with it.
 */
public class InfoMessage extends Message {
	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/
	private static final String prefix = "Info";
	/**
	 * Create an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable string giving the information.
	 */
	public InfoMessage(String message) {
		super(message);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string giving the information.
	 */
	public InfoMessage(JhoveMessage message) {
		super(message);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable string giving the information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described
	 */
	public InfoMessage(String message, long offset) {
		super(message, offset);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string giving the information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described
	 */
	public InfoMessage(JhoveMessage message, long offset) {
		super(message, offset);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable string giving the information.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	public InfoMessage(String message, String subMessage) {
		super(message, subMessage);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string giving the information.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	public InfoMessage(JhoveMessage message, String subMessage) {
		super(message, subMessage);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param message
	 *            Human-readable string giving the information.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described
	 */
	public InfoMessage(String message, String subMessage, long offset) {
		super(message, subMessage, offset);
	}

	/**
	 * Create an InfoMessage.
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string giving the information.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            The offset in the file relevant to the
	 *            situation being described
	 */
	public InfoMessage(JhoveMessage message, String subMessage, long offset) {
		super(message, subMessage, offset);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}
