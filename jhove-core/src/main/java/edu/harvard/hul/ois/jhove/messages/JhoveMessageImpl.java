package edu.harvard.hul.ois.jhove.messages;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 *          Created 20 Feb 2019:16:46:19
 */

public final class JhoveMessageImpl implements JhoveMessage {
	private final String id;
	private final String message;
	private final String subMessage;

	private JhoveMessageImpl(final String id, final String message) {
		this(id, message, "");
	}

	private JhoveMessageImpl(final String id, final String message, final String subMessage) {
		this.id = id;
		this.message = message;
		this.subMessage = subMessage;
	}

	static JhoveMessage getInstance(final String id, final String message, final String subMessage) {
		return new JhoveMessageImpl(id, message);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public boolean hasSubMessage() {
		return (this.subMessage == null || this.subMessage.isEmpty());
	}

	@Override
	public String getSubMessage() {
		return this.subMessage;
	}
}
