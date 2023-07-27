package org.ithaka.portico.jhove.module.epub;

import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessageFactory;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;

public enum MessageConstants {

    INSTANCE;

    private static JhoveMessageFactory messageFactory = JhoveMessages
            .getInstance(
                    "org.ithaka.portico.jhove.module.epub.ErrorMessages");

    /**
     * Error messages
     */
    public static final JhoveMessage EPUB_PTC_1 = messageFactory
            .getMessage("EPUB-PTC-1");
    public static final JhoveMessage EPUB_PTC_1_SUB = messageFactory
            .getMessage("EPUB-PTC-1-SUB");
    public static final JhoveMessage EPUB_PTC_2 = messageFactory
            .getMessage("EPUB-PTC-2");
}
