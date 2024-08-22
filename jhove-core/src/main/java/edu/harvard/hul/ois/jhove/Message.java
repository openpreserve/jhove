/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;

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

    protected final JhoveMessage jhoveMessage;

    /** Additional information. */
    protected final String subMessage;

    /** Byte offset to which message applies. */
    protected final long offset;

    protected final String prefix;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Creates a Message with an identifier.
     * This constructor cannot be invoked directly,
     * since Message is abstract. The second argument
     * adds secondary details to the primary message;
     * the message will typically be displayed in the
     * form "message: subMessage".
     * 
     * @param message
     *                   The message text and its identifier.
     * @param subMessage
     *                   Human-readable additional information.
     * @param offset
     *                   Byte offset associated with the message.
     */
    protected Message(final JhoveMessage message, final String subMessage,
            final long offset, final String prefix) {
        super();
        this.jhoveMessage = message;
        this.subMessage = (subMessage.isEmpty()) ? null : subMessage;
        this.offset = offset;
        this.prefix = prefix;
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
        return this.jhoveMessage.getMessage();
    }

    /**
     * Returns the submessage text.
     */
    public String getSubMessage() {
        return this.subMessage;
    }

    /**
     * Returns the offset to which this message is related.
     */
    public long getOffset() {
        return this.offset;
    }

    /**
     * Returns the message's identifier.
     */
    public String getId() {
        return this.jhoveMessage.getId();
    }

    public JhoveMessage getJhoveMessage() {
        return this.jhoveMessage;
    }

    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String toString() {
        return "Message [message=" + jhoveMessage + ", _subMessage=" + subMessage + ", _offset=" + offset + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jhoveMessage == null) ? 0 : jhoveMessage.hashCode());
        result = prime * result + ((subMessage == null) ? 0 : subMessage.hashCode());
        result = prime * result + (int) (offset ^ (offset >>> 32));
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (jhoveMessage == null) {
            if (other.jhoveMessage != null)
                return false;
        } else if (!jhoveMessage.equals(other.jhoveMessage))
            return false;
        if (subMessage == null) {
            if (other.subMessage != null)
                return false;
        } else if (!subMessage.equals(other.subMessage))
            return false;
        if (offset != other.offset)
            return false;
        if (prefix == null) {
            if (other.prefix != null)
                return false;
        } else if (!prefix.equals(other.prefix))
            return false;
        return true;
    }

}
