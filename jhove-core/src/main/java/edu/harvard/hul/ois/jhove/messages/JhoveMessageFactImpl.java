package edu.harvard.hul.ois.jhove.messages;

import java.util.ResourceBundle;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 *          Created 1 Mar 2019:16:52:25
 */

final class JhoveMessageFactImpl implements JhoveMessageFactory {
	private final ResourceBundle messageBundle;

	private JhoveMessageFactImpl(final ResourceBundle messageBundle) {
		this.messageBundle = messageBundle;
	}

	@Override
	public JhoveMessage getMessage(final String id) {
		String message = this.messageBundle.getString(id);
		return JhoveMessages.getMessageInstance(id, message);
	}

	static JhoveMessageFactImpl getInstance(
			final ResourceBundle messageBundle) throws IllegalArgumentException {
		if (messageBundle == null)
			throw new IllegalArgumentException("messageBundle cannot be null");
		return new JhoveMessageFactImpl(messageBundle);
	}
}
