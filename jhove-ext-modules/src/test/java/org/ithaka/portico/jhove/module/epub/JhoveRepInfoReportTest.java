package org.ithaka.portico.jhove.module.epub;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.adobe.epubcheck.api.EPUBLocation;
import com.adobe.epubcheck.messages.Message;
import com.adobe.epubcheck.messages.MessageId;
import com.adobe.epubcheck.messages.Severity;
import com.adobe.epubcheck.reporting.CheckMessage;
import com.adobe.epubcheck.util.FeatureEnum;

/**
 * Unit tests for JhoveRepInfoReport
 *
 * @author Karen Hanson
 */
@RunWith(JUnit4.class)
public class JhoveRepInfoReportTest {

    private static final String FAKE_EPUB_FILEPATH = "/fake/filepath.epub";

    private static final MessageId FATAL_MSG_ID_1 = MessageId.OPF_001;
    private static final String FATAL_MSG_1 = "Houston, we've had a problem here.";
    private static final String FATAL_MSG_SUGGEST_1 = "Close the hatch.";

    private static final MessageId FATAL_MSG_ID_2 = MessageId.OPF_002;
    private static final String FATAL_MSG_2 = "It's a fatal error.";
    private static final String FATAL_MSG_SUGGEST_2 = "Needs to be fixed.";

    private static final MessageId ERROR_MSG_ID = MessageId.ACC_001;
    private static final String ERROR_MSG = "There has been an non-fatal error";
    private static final String ERROR_MSG_SUGGEST = "Report it";

    private static final MessageId WARN_MSG_ID = MessageId.CHK_001;
    private static final String WARN_MSG = "Consider yourself warned";
    private static final String WARN_MSG_SUGGEST = "Don't do it again!";
    
    private EPUBLocation messageLoc = EPUBLocation.of(new File("epub.opf"));
    private EPUBLocation messageLoc2 = EPUBLocation.of(new File("content.xhtml"));
    
    private String messageArg = "fakearg";


    /**
     * Sets a single fatal message then confirms the properties are
     * correct when you getAllMessages()
     * @throws Exception
     */
    @Test
    public void setAndGetMessageTest() throws Exception {
        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);
        report.message(fatalMsg1(), messageLoc, messageArg);
        List<CheckMessage> allMsgs = report.getAllMessages();
        assertEquals(1, allMsgs.size());
        CheckMessage fatalmsg = allMsgs.get(0);
        assertEquals(FATAL_MSG_ID_1.toString(), fatalmsg.getID());
        assertEquals(Severity.FATAL, fatalmsg.getSeverity());
        assertEquals(FATAL_MSG_1, fatalmsg.getMessage());
        assertEquals(FATAL_MSG_SUGGEST_1, fatalmsg.getSuggestion());
        assertEquals(1, fatalmsg.getLocations().size());
        assertEquals(messageLoc, fatalmsg.getLocations().get(0));
        assertEquals(1, report.getFatalErrorCount());
        assertEquals(0, report.getErrorCount());
        assertEquals(0, report.getWarningCount());
    }

    /**
     * Adds 2 different messages to report and confirms they are returned by
     * getAllMessages()
     * @throws Exception
     */
    @Test
    public void setAndGet2DifferentMessagesTest() throws Exception {
        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);
        report.message(fatalMsg1(), messageLoc, messageArg);
        report.message(warnMsg(), messageLoc, messageArg);
        List<CheckMessage> allMsgs = report.getAllMessages();
        assertEquals(2, allMsgs.size());

        CheckMessage fatalmsg = allMsgs.get(0);
        CheckMessage warnmsg = allMsgs.get(1);
        //make sure these are assigned correctly, if not switch them
        if (allMsgs.get(0).getSeverity().equals(Severity.WARNING)) {
            fatalmsg = allMsgs.get(1);
            warnmsg = allMsgs.get(0);
        }

        assertEquals(FATAL_MSG_ID_1.toString(), fatalmsg.getID());
        assertEquals(Severity.FATAL, fatalmsg.getSeverity());
        assertEquals(FATAL_MSG_1, fatalmsg.getMessage());
        assertEquals(FATAL_MSG_SUGGEST_1, fatalmsg.getSuggestion());
        assertEquals(1, fatalmsg.getLocations().size());
        assertEquals(messageLoc, fatalmsg.getLocations().get(0));

        assertEquals(WARN_MSG_ID.toString(), warnmsg.getID());
        assertEquals(Severity.WARNING, warnmsg.getSeverity());
        assertEquals(WARN_MSG, warnmsg.getMessage());
        assertEquals(WARN_MSG_SUGGEST, warnmsg.getSuggestion());
        assertEquals(1, warnmsg.getLocations().size());
        assertEquals(messageLoc, warnmsg.getLocations().get(0));

        assertEquals(1, report.getFatalErrorCount());
        assertEquals(0, report.getErrorCount());
        assertEquals(1, report.getWarningCount());
    }

    /**
     * Adds 2 messages to report that are the same ID but have different locations
     * and confirms they are returned as 1 message by getAllMessages()
     * @throws Exception
     */
    @Test
    public void setAndGet2MessagesWithSameIdTest() throws Exception {
        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);
        report.message(errorMsg(), messageLoc, messageArg);
        report.message(errorMsg(), messageLoc2, messageArg);
        List<CheckMessage> allMsgs = report.getAllMessages();
        assertEquals(1, allMsgs.size());

        CheckMessage errormsg = allMsgs.get(0);
        assertEquals(ERROR_MSG_ID.toString(), errormsg.getID());
        assertEquals(Severity.ERROR, errormsg.getSeverity());
        assertEquals(ERROR_MSG, errormsg.getMessage());
        assertEquals(ERROR_MSG_SUGGEST, errormsg.getSuggestion());
        assertEquals(2, errormsg.getLocations().size());
        assertTrue(errormsg.getLocations().contains(messageLoc));
        assertTrue(errormsg.getLocations().contains(messageLoc2));

        assertEquals(0, report.getFatalErrorCount());
        assertEquals(1, report.getErrorCount());
        assertEquals(0, report.getWarningCount());
    }


    /**
     * Adds multiple messages and confirms the count is accurate afterwards
     * @throws Exception
     */
    @Test
    public void setAndGetMultipleMessagesTest() throws Exception {
        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);
        report.message(fatalMsg1(), messageLoc, messageArg);
        report.message(fatalMsg2(), messageLoc, messageArg);
        report.message(fatalMsg2(), messageLoc2, messageArg);
        report.message(errorMsg(), messageLoc, messageArg);
        report.message(warnMsg(), messageLoc, messageArg);
        //added 5 messages, but two are the same message id, so there should be 4 total
        List<CheckMessage> allMsgs = report.getAllMessages();
        final int expectedMessagesSize = 4;
        assertEquals(expectedMessagesSize, allMsgs.size());
        assertEquals(2, report.getFatalErrorCount());
        assertEquals(1, report.getErrorCount());
        assertEquals(1, report.getWarningCount());
    }

    /**
     * Checks all default values for report in which no properties were populated
     * @throws Exception
     */
    @Test
    public void reportWithNoInfoTest() throws Exception {
        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);
        assertEquals(0, report.getAllMessages().size());
        assertEquals(0, report.getCharacterCount());
        assertArrayEquals(null, report.getContributors());
        assertEquals(null, report.getCreationDate());
        assertArrayEquals(null, report.getCreators());
        assertEquals(null, report.getDate());
        assertEquals(0, report.getEmbeddedFonts().size());
        assertEquals(FAKE_EPUB_FILEPATH, report.getEpubFileName());
        assertEquals(0, report.getFatalErrorCount());
        assertEquals(0, report.getFeatures().size());
        assertEquals("application/octet-stream", report.getFormat());
        assertEquals(null, report.getIdentifier());
        assertEquals(0, report.getInfoCount());
        assertEquals(null, report.getLanguage());
        assertEquals(null, report.getLastModifiedDate());
        assertArrayEquals(null, report.getMediaTypes());
        assertEquals(0, report.getPageCount());
        assertEquals(null, report.getPublisher());
        assertArrayEquals(null, report.getReferences());
        assertEquals(0, report.getRefFonts().size());
        assertArrayEquals(null, report.getRights());
        assertArrayEquals(null, report.getSubjects());
        assertArrayEquals(null, report.getTitles());
        assertEquals(0, report.getUsageCount());
        assertEquals(null, report.getVersion());
        assertEquals(0, report.getWarningCount());
    }

    /**
     * Checks that lists of local and remote resources are correctly managed
     *
     * @throws Exception
     */
    @Test
    public void remoteAndLocalResourcesTest() throws Exception {
        final String localResource1 = "EPUB/image.jpg";
        final String localResource2 = "EPUB/code.xhtml";
        final String localResource3 = "EPUB/somefile.xhtml";
        final String localResource4 = "EPUB/image2.jpg";
        final String remoteResource1 = "https://example.com/remotepath1";
        final String remoteResource2 = "https://example.com/remotepath2";
        final String remoteResource3 = "https://example.com/remotepath3";
        final String reference1 = "https://example.com/referencepath1";
        final String reference2 = "https://example.com/referencepath2";
        final String reference3 = "https://example.com/referencepath3";

        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);

        // references consist of both citation type references / links
        // but also any embedded remote resources that appear to be part of the epub.
        report.info(null, FeatureEnum.REFERENCE, reference1);
        report.info(null, FeatureEnum.REFERENCE, reference2);
        report.info(null, FeatureEnum.REFERENCE, reference3);

        // resources are what appear to be part of the EPUB and may be local or remote
        report.info(null, FeatureEnum.RESOURCE, localResource1);
        report.info(null, FeatureEnum.RESOURCE, localResource2);
        report.info(null, FeatureEnum.RESOURCE, localResource3);
        report.info(null, FeatureEnum.RESOURCE, localResource4);
        report.info(null, FeatureEnum.RESOURCE, remoteResource1);
        report.info(null, FeatureEnum.RESOURCE, remoteResource2);
        report.info(null, FeatureEnum.RESOURCE, remoteResource3);

        final int expectedNumReferences = 3;
        assertEquals(expectedNumReferences, report.getReferences().length);
        String[] expectedReferences = new String[] { reference1, reference2, reference3 };
        assertTrue(arraysSame(expectedReferences, report.getReferences()));

        final int expectedNumResources = 7;
        assertEquals(expectedNumResources, report.getResources().length);
        String[] expectedResources = new String[] { localResource1, localResource2, localResource3, localResource4, remoteResource1,
                remoteResource2, remoteResource3 };
        assertTrue(arraysSame(expectedResources, report.getResources()));
    }

    /**
     * Checks all report fields are correctly parsed
     *
     * @throws Exception
     */
    @Test
    public void reportWithAllInfoTest() throws Exception {
        final String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        final String formatName = "EPUB";
        final String formatVersion = "3.2";
        final String creationDate = "2018-08-12T12:00:12Z";
        final String modifiedDate = "2018-08-12T13:00:13Z";
        final Date dCreationDate = new SimpleDateFormat(DATEFORMAT).parse(creationDate);
        final Date dModifiedDate = new SimpleDateFormat(DATEFORMAT).parse(modifiedDate);
        final String pagesCount = "22";
        final String charsCount = "77777";
        final String mimetype1 = "text/xml";
        final String mimetype2 = "audio/mpeg";
        final String mimetype3 = "video/ogg";
        final String fontEmbedded = "Arial";
        final String fontReference = "Verdana";
        final String resource1 = "EPUB/imageA.jpg";
        final String resource2 = "EPUB/codeA.xhtml";
        final String reference = "https://example.com/referencepath1A";
        final String language = "en-us";
        final String title = "A Very Good Book";
        final String creator1 = "J Lee";
        final String creator2 = "B Singh";
        final String contributor = "D Rodríguez";
        final String publisher = "Simple Publishing Inc";
        final String subject1 = "Information science--Sociological aspects";
        final String subject2 = "Communication";
        final String rights = "CC0";
        final String sdate = "2018";
        final String identifier = "doi:10.0000/abcez123";
        final String sTrue = "true";

        JhoveRepInfoReport report = new JhoveRepInfoReport(FAKE_EPUB_FILEPATH);


        // populate all values
        report.info(null, FeatureEnum.CHARS_COUNT, charsCount);
        report.info(null, FeatureEnum.CREATION_DATE, creationDate);
        report.info(null, FeatureEnum.DC_CONTRIBUTOR, contributor);
        report.info(null, FeatureEnum.DC_CREATOR, creator1);
        report.info(null, FeatureEnum.DC_CREATOR, creator2);
        report.info(null, FeatureEnum.DC_DATE, sdate);
        report.info(null, FeatureEnum.DC_LANGUAGE, language);
        report.info(null, FeatureEnum.DC_PUBLISHER, publisher);
        report.info(null, FeatureEnum.DC_RIGHTS, rights);
        report.info(null, FeatureEnum.DC_SUBJECT, subject1);
        report.info(null, FeatureEnum.DC_SUBJECT, subject2);
        report.info(null, FeatureEnum.DC_TITLE, title);
        report.info(null, FeatureEnum.DECLARED_MIMETYPE, mimetype1);
        report.info(null, FeatureEnum.DECLARED_MIMETYPE, mimetype2);
        report.info(null, FeatureEnum.DECLARED_MIMETYPE, mimetype3);
        report.info(null, FeatureEnum.FONT_EMBEDDED, fontEmbedded);
        report.info(null, FeatureEnum.FONT_REFERENCE, fontReference);
        report.info(null, FeatureEnum.FORMAT_NAME, formatName);
        report.info(null, FeatureEnum.FORMAT_VERSION, formatVersion);
        report.info(null, FeatureEnum.HAS_ENCRYPTION, sTrue);
        report.info(null, FeatureEnum.HAS_FIXED_LAYOUT, sTrue);
        report.info(null, FeatureEnum.HAS_SCRIPTS, sTrue);
        report.info(null, FeatureEnum.HAS_SIGNATURES, sTrue);
        report.info(null, FeatureEnum.MODIFIED_DATE, modifiedDate);
        report.info(null, FeatureEnum.PAGES_COUNT, pagesCount);
        report.info(null, FeatureEnum.REFERENCE, reference);
        report.info(null, FeatureEnum.RESOURCE, resource1);
        report.info(null, FeatureEnum.RESOURCE, resource2);
        report.info(null, FeatureEnum.UNIQUE_IDENT, identifier);

        assertEquals(FAKE_EPUB_FILEPATH, report.getEpubFileName());
        assertEquals(Long.parseLong(charsCount), report.getCharacterCount());
        assertEquals(dCreationDate, report.getCreationDate());

        assertEquals(1, report.getContributors().length);
        assertEquals(contributor, report.getContributors()[0]);

        assertEquals(2, report.getCreators().length);
        assertTrue(arraysSame(new String[] { creator1, creator2 }, report.getCreators()));

        assertEquals(sdate, report.getDate());
        assertEquals(language, report.getLanguage());
        assertEquals(publisher, report.getPublisher());

        assertEquals(1, report.getRights().length);
        assertEquals(rights, report.getRights()[0]);

        assertEquals(2, report.getSubjects().length);
        assertTrue(arraysSame(new String[] { subject1, subject2 }, report.getSubjects()));

        assertEquals(1, report.getTitles().length);
        assertEquals(title, report.getTitles()[0]);

        final int expectedNumMediaTypes = 3;
        assertEquals(expectedNumMediaTypes, report.getMediaTypes().length);
        assertTrue(arraysSame(new String[] { mimetype1, mimetype2, mimetype3 }, report.getMediaTypes()));

        assertEquals(1, report.getEmbeddedFonts().size());
        assertEquals(fontEmbedded, report.getEmbeddedFonts().iterator().next());

        assertEquals(1, report.getRefFonts().size());
        assertEquals(fontReference, report.getRefFonts().iterator().next());

        assertEquals(formatName, report.getFormat());
        assertEquals(formatVersion, report.getVersion());

        final int expectedNumFeatures = 6;
        assertEquals(expectedNumFeatures, report.getFeatures().size());
        assertTrue(report.getFeatures().contains("hasEncryption"));
        assertTrue(report.getFeatures().contains("hasSignatures"));
        assertTrue(report.getFeatures().contains("hasAudio"));
        assertTrue(report.getFeatures().contains("hasVideo"));
        assertTrue(report.getFeatures().contains("hasFixedLayout"));
        assertTrue(report.getFeatures().contains("hasScripts"));

        assertEquals(dModifiedDate, report.getLastModifiedDate());
        assertEquals(Long.parseLong(pagesCount), report.getPageCount());

        assertEquals(1, report.getReferences().length);
        assertEquals(reference, report.getReferences()[0]);

        assertEquals(2, report.getResources().length);
        assertTrue(arraysSame(new String[] { resource1, resource2 }, report.getResources()));

        assertEquals(identifier, report.getIdentifier());

        // shouldn't affect message counts
        assertEquals(0, report.getFatalErrorCount());
        assertEquals(0, report.getInfoCount());
        assertEquals(0, report.getUsageCount());
        assertEquals(0, report.getWarningCount());
    }

    private static Message fatalMsg1() {
        return new Message(FATAL_MSG_ID_1, Severity.FATAL, FATAL_MSG_1, FATAL_MSG_SUGGEST_1);
    }

    private static Message fatalMsg2() {
        return new Message(FATAL_MSG_ID_2, Severity.FATAL, FATAL_MSG_2, FATAL_MSG_SUGGEST_2);
    }

    private static Message errorMsg() {
        return new Message(ERROR_MSG_ID, Severity.ERROR, ERROR_MSG, ERROR_MSG_SUGGEST);
    }

    private static Message warnMsg() {
        return new Message(WARN_MSG_ID, Severity.WARNING, WARN_MSG, WARN_MSG_SUGGEST);
    }

    /**
     * Compares arrays independent of order
     *
     * @param arr1
     * @param arr2
     * @return
     */
    private static boolean arraysSame(String[] arr1, String[] arr2) {
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }


}
