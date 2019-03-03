package edu.harvard.hul.ois.jhove.messages;

import java.util.NoSuchElementException;

/**
 * Factory interface for JhoveMessage creation
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public interface JhoveMessageFactory {

	/**
	 * Retrieve JhoveMessage by unique persistent id
	 * 
	 * @param id
	 *            the id of the message to be retrieved
	 * @return the message with persistent id equal to id
	 * @throws NoSuchElementException
	 *             if no message with id can be retrieved
	 */
	public JhoveMessage getMessage(final String id)
			throws NoSuchElementException;
}
