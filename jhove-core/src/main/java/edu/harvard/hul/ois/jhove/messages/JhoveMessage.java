package edu.harvard.hul.ois.jhove.messages;

/**
 * Interface that defines behaviour of JhoveMessages.
 * These messages have a unique string identifier as well as
 * the previous message and sub-message strings.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public interface JhoveMessage {
	/**
	 * Get the unique, persistent message identifier.
	 * 
	 * @return the String message id.
	 */
	public String getId();

	/**
	 * Get the main message
	 * 
	 * @return the String message
	 */
	public String getMessage();

	/**
	 * Test whether the message has a sub-message
	 * 
	 * @return true if the message has a sub-message
	 */
	public boolean hasSubMessage();

	/**
	 * Get the sub-message
	 * 
	 * @return the String sub-message
	 */
	public String getSubMessage();

}
