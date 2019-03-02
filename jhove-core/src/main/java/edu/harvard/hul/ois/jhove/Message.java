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

	public static final String NO_ID = "NO-ID";

	/******************************************************************
	 * PRIVATE INSTANCE FIELDS.
	 ******************************************************************/

	protected final JhoveMessage message;

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
	protected Message(final String message) {
		this(JhoveMessage.getInstance(NO_ID, message));
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
	protected Message(final JhoveMessage message) {
		this(message, null, NULL);
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
	protected Message(final String message, final String subMessage) {
		this(JhoveMessage.getInstance(NO_ID, message), subMessage);
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
	protected Message(final JhoveMessage message, final String subMessage) {
		this(message, subMessage, NULL);
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
	protected Message(final String message, final long offset) {
		this(JhoveMessage.getInstance(NO_ID, message), offset);
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
	protected Message(final JhoveMessage message, final long offset) {
		this(message, null, offset);
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
	protected Message(final String message, final String subMessage, final long offset) {
		this(JhoveMessage.getInstance(NO_ID, message), subMessage, offset);
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
	protected Message(final JhoveMessage message, String subMessage, long offset) {
		this.message = message;
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
		return this.message.message;
	}

	/**
	 * Get the submessage string.
	 */
	public String getSubMessage() {
		return this._subMessage;
	}

	/**
	 * Return the offset to which the information is related.
	 */
	public long getOffset() {
		return this._offset;
	}

	public String getId() {
		return this.message.id;
	}

	public JhoveMessage getJhoveMessage() {
		return this.message;
	}

	@SuppressWarnings("static-method")
	public String getPrefix() {
		return "";
	}
}
