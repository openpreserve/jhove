package edu.harvard.hul.ois.jhove.module.iff;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 1.0
 *
 * Created 31 Oct 2017:15:54:36
 */
public enum MessageConstants {
	INSTANCE;
	private static final JhoveMessageFactory messageFactory = JhoveMessages.getInstance("edu.harvard.hul.ois.jhove.module.iff.ErrorMessages"); //$NON-NLS-1$

	public static final JhoveMessage IFF_HUL_1 = messageFactory.getMessage("IFF-HUL-1"); //$NON-NLS-1$
	public static final JhoveMessage IFF_HUL_1_SUB = messageFactory.getMessage("IFF-HUL-1-SUB"); //$NON-NLS-1$
	public static final JhoveMessage IFF_HUL_2 = messageFactory.getMessage("IFF-HUL-2"); //$NON-NLS-1$
}
