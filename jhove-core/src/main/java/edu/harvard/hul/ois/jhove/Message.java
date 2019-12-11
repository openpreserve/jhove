/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * This class encapsulates a message to be displayed.
 */
public abstract class Message {

	/******************************************************************
	 * PUBLIC CLASS FIELDS.
	 ******************************************************************/

	/** Value indicating a null offset. */
	public static final long NULL = -1;

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
	 * Creates a Message with an unknown identifier for backward compatibility.
	 * This constructor cannot be invoked directly, since Message is abstract.
	 * 
	 * @param message
	 *            Human-readable message text.
	 */
	protected Message(final String message) {
		this(JhoveMessages.getMessageInstance(JhoveMessages.NO_ID, message));
	}

	/**
	 * Creates a Message with an identifier.
	 * This constructor cannot be invoked directly, since Message is abstract.
	 * 
	 * @param message
	 *            The message text and its identifier.
	 */
	protected Message(final JhoveMessage message) {
		this(message, message.getSubMessage(), NULL);
	}

	/**
	 * Creates a Message.
	 * This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message: subMessage".
	 * 
	 * @param message
	 *            Human-readable message text.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	protected Message(final String message, final String subMessage) {
		this(JhoveMessages.getMessageInstance(JhoveMessages.NO_ID, message),
				subMessage);
	}

	/**
	 * Creates a Message with an identifier.
	 * This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message: subMessage".
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param subMessage
	 *            Human-readable additional information.
	 */
	protected Message(final JhoveMessage message, final String subMessage) {
		this(message, subMessage, NULL);
	}

	/**
	 * Creates a Message.
	 * This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message: subMessage".
	 * 
	 * @param message
	 *            Human-readable message text.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(final String message, final long offset) {
		this(JhoveMessages.getMessageInstance(JhoveMessages.NO_ID, message),
				offset);
	}

	/**
	 * Creates a Message with an identifier.
	 * This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message: subMessage".
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(final JhoveMessage message, final long offset) {
		this(message, message.getSubMessage(), offset);
	}

	/**
	 * Creates a Message.
	 * This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message: subMessage".
	 * 
	 * @param message
	 *            Human-readable message text.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(final String message, final String subMessage,
			final long offset) {
		this(JhoveMessages.getMessageInstance(JhoveMessages.NO_ID, message),
				subMessage, offset);
	}

	/**
	 * Creates a Message with an identifier.
	 * This constructor cannot be invoked directly,
	 * since Message is abstract. The second argument
	 * adds secondary details to the primary message;
	 * the message will typically be displayed in the
	 * form "message: subMessage".
	 * 
	 * @param message
	 *            The message text and its identifier.
	 * @param subMessage
	 *            Human-readable additional information.
	 * @param offset
	 *            Byte offset associated with the message.
	 */
	protected Message(final JhoveMessage message, String subMessage,
			long offset) {
		super();
		this.message = message;
		this._subMessage = (subMessage.isEmpty()) ? null : subMessage;
		this._offset = offset;
	}

	/******************************************************************
	 * PUBLIC INSTANCE METHODS.
	 *
	 * Accessor methods.
	 ******************************************************************/

	/**
	 * Returns the message text.
	 */
	public String getMessage() {
		return this.message.getMessage();
	}

	/**
	 * Returns the submessage text.
	 */
	public String getSubMessage() {
		return this._subMessage;
	}

	/**
	 * Returns the offset to which this message is related.
	 */
	public long getOffset() {
		return this._offset;
	}

	/**
	 * Returns the message's identifier.
	 */
	public String getId() {
		return this.message.getId();
	}

	public JhoveMessage getJhoveMessage() {
		return this.message;
	}

	@SuppressWarnings("static-method")
	public String getPrefix() {
		return "";
	}
}
