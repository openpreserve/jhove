package edu.harvard.hul.ois.jhove;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 *          Created 1 Mar 2019:16:52:25
 */

public final class JhoveMessageFactory {
	private final ResourceBundle messageBundle;

	/**
	 * 
	 */
	private JhoveMessageFactory(final ResourceBundle messageBundle) {
		this.messageBundle = messageBundle;
	}

	public JhoveMessage getMessage(final String id) {
		String message = this.messageBundle.getString(id);
		return JhoveMessage.getInstance(id, message);
	}

	public static JhoveMessageFactory getInstance(
			final ResourceBundle messageBundle) {
		if (messageBundle == null)
			throw new IllegalArgumentException("messageBundle cannot be null");
		return new JhoveMessageFactory(messageBundle);
	}

	public static JhoveMessageFactory getInstance(final String bundleName) {
		if (bundleName == null | bundleName.isEmpty())
			throw new IllegalArgumentException(
					"bundleName cannot be null or empty");
		ResourceBundle messageBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());
		return new JhoveMessageFactory(messageBundle);
	}
}
