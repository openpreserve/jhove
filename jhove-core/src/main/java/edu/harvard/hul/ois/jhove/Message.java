/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * This class encapsulates a String to be displayed.
 */
public abstract class Message {
	/******************************************************************
	 * PUBLIC CLASS FIELDS.
	 ******************************************************************/

	/** Value indicating a null offset. */
	public static final long NULL = -1;

	public static final int UNKNOWN_ID = -1;

	/******************************************************************
	 * PRIVATE INSTANCE FIELDS.
	 ******************************************************************/

	/** Error message ID number */
	protected final int _id;

	/** Message text. */
	protected final String _message;

	/** Additional information. */
	protected final String _subMessage;

	/** Byte offset to which message applies. */
	protected final long _offset;

	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/

	/**
	 * Create a Message with an unknown id (for backward compatibility).
	 * This constructor cannot be invoked directly,
	 * since Message is abstract.
	 * 
	 * @param message
	 *            Human-readable string.
	 */
	protected Message(String message) {
		this(message, UNKNOWN_ID);
	}

	/**
	 * Create a Message with an id. This constructor cannot be invoked directly,
	 * since Message is abstract.
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string.
	 */
	protected Message(int id, String message) {
		this(id, message, null, NULL);
	}

	/**
	 * Create a Message. This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message:subMessage".
	 * 
	 * @param message
	 *            Human-readable string.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	protected Message(String message, String subMessage) {
		this(UNKNOWN_ID, message, subMessage);
	}

	/**
	 * Create a Message. This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message:subMessage".
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	protected Message(int id, String message, String subMessage) {
		this(id, message, subMessage, NULL);
	}

	/**
	 * Create a Message. This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message:subMessage".
	 * 
	 * @param message
	 *            Human-readable string.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(String message, long offset) {
		this(UNKNOWN_ID, message, offset);
	}

	/**
	 * Create a Message. This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message:subMessage".
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(int id, String message, long offset) {
		this(id, message, null, offset);
	}

	/**
	 * Create a Message. This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message:subMessage".
	 * 
	 * @param message
	 *            Human-readable string.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(String message, String subMessage, long offset) {
		this(UNKNOWN_ID, message, subMessage, offset);
	}

	/**
	 * Create a Message. This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message:subMessage".
	 * 
	 * @param id
	 *            Unique ID (within the module) for the message
	 * @param message
	 *            Human-readable string.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(int id, String message, String subMessage, long offset) {
		this._id = id;
		this._message = message;
		this._subMessage = subMessage;
		this._offset = offset;
	}

	/******************************************************************
	 * PUBLIC INSTANCE METHODS.
	 *
	 * Accessor methods.
	 ******************************************************************/

	/**
	 * Get the message string.
	 */
	public String getMessage() {
		return _message;
	}

	/**
	 * Get the submessage string.
	 */
	public String getSubMessage() {
		return _subMessage;
	}

	/**
	 * Return the offset to which the information is related.
	 */
	public long getOffset() {
		return _offset;
	}

	public int getId() {
		return this._id;
	}
}
