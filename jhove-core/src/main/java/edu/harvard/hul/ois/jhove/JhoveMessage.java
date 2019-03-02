package edu.harvard.hul.ois.jhove;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 *          Created 20 Feb 2019:16:46:19
 */

public final class JhoveMessage {
	public final String id;
	public final String message;

	/**
	 * 
	 */
	private JhoveMessage(final String id, final String message) {
		this.id = id;
		this.message = message;
	}

	public static JhoveMessage getInstance(final String id, final String message) {
		if (id == null | id.isEmpty())
			throw new IllegalArgumentException(
					"id cannot be null or an empty string.");
		if (message == null | message.isEmpty())
			throw new IllegalArgumentException(
					"message cannot be null or an empty string.");
		return new JhoveMessage(id, message);
	}
}
