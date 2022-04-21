package org.ithaka.portico.jhove.module.epub;

import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.FEATURE_HASAUDIO;
import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.FEATURE_HASENCRYPTION;
import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.FEATURE_HASFIXEDLAYOUT;
import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.FEATURE_HASSCRIPTS;
import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.FEATURE_HASSIGNATURES;
import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.FEATURE_HASVIDEO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.adobe.epubcheck.api.EPUBLocation;
import com.adobe.epubcheck.api.MasterReport;
import com.adobe.epubcheck.messages.Message;
import com.adobe.epubcheck.messages.Severity;
import com.adobe.epubcheck.reporting.CheckMessage;
import com.adobe.epubcheck.util.FeatureEnum;
import com.adobe.epubcheck.util.PathUtil;

/**
 * This is a custom report to extend the EPUBCheck {@link MasterReport}. Its
 * purpose is to collect the properties needed to producing a JHOVE RepInfo
 * object while the EPUB is being validated.
 *
 * @author Karen Hanson
 *
 */
public class JhoveRepInfoReport extends MasterReport {

    protected String generationDate;

    protected String creationDate;
    protected String lastModifiedDate;
    protected String identifier;
    protected Set<String> titles = new TreeSet<String>();
    protected Set<String> creators = new TreeSet<String>();
    protected Set<String> contributors = new TreeSet<String>();
    protected Set<String> subjects = new TreeSet<String>();
    protected String publisher;
    protected Set<String> rights = new TreeSet<String>();
    protected String date;
    protected Set<String> mediaTypes = new TreeSet<String>();

    protected String formatName;
    protected String formatVersion;
    protected long pagesCount;
    protected long charsCount;
    protected String language;
    protected Set<String> embeddedFonts = new TreeSet<String>();
    protected Set<String> refFonts = new TreeSet<String>();
    protected Set<String> references = new TreeSet<String>();
    protected Set<String> resources = new TreeSet<String>();
    protected boolean hasEncryption;
    protected boolean hasSignatures;
    protected boolean hasAudio;
    protected boolean hasVideo;
    protected boolean hasFixedLayout;
    protected boolean hasScripts;

    protected List<CheckMessage> warns = new ArrayList<CheckMessage>();
    protected List<CheckMessage> errors = new ArrayList<CheckMessage>();
    protected List<CheckMessage> fatalErrors = new ArrayList<CheckMessage>();
    protected List<CheckMessage> usageMsgs = new ArrayList<CheckMessage>();
    protected List<CheckMessage> infoMsgs = new ArrayList<CheckMessage>();

    protected static final String ISO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    protected static final String FALLBACK_FORMAT = "application/octet-stream";

    public JhoveRepInfoReport(String ePubName) {
        this.setEpubFileName(PathUtil.removeWorkingDirectory(ePubName));

    }

    @Override
    public void message(Message message, EPUBLocation location, Object... args) {
        Severity s = message.getSeverity();
        switch (s) {
        case FATAL:
            CheckMessage.addCheckMessage(fatalErrors, message, location, args);
            break;
        case ERROR:
            CheckMessage.addCheckMessage(errors, message, location, args);
            break;
        case WARNING:
            CheckMessage.addCheckMessage(warns, message, location, args);
            break;
        case USAGE:
            CheckMessage.addCheckMessage(usageMsgs, message, location, args);
            break;
        case INFO:
            CheckMessage.addCheckMessage(infoMsgs, message, location, args);
            break;
        case SUPPRESSED:
            break;
        default:
            break;
        }
    }

    @Override
    public void info(String resource, FeatureEnum feature, String value) {
        // Dont store 'null' values
        if (value == null)
            return;

        switch (feature) {
        case FORMAT_NAME:
            this.formatName = value;
            break;
        case FORMAT_VERSION:
            this.formatVersion = value;
            break;
        case CREATION_DATE:
            this.creationDate = value;
            break;
        case MODIFIED_DATE:
            this.lastModifiedDate = value;
            break;
        case PAGES_COUNT:
            this.pagesCount = Long.parseLong(value);
            break;
        case CHARS_COUNT:
            this.charsCount += Long.parseLong(value);
            break;
        case DECLARED_MIMETYPE:
            mediaTypes.add(value);
            if (value != null && value.startsWith("audio/")) {
                this.hasAudio = true;
            } else if (value != null && value.startsWith("video/")) {
                this.hasVideo = true;
            }
            break;
        case FONT_EMBEDDED:
            this.embeddedFonts.add(value);
            break;
        case FONT_REFERENCE:
            this.refFonts.add(value);
            break;
        case REFERENCE:
            this.references.add(value);
            break;
        case RESOURCE:
            this.resources.add(value);
            break;
        case DC_LANGUAGE:
            this.language = value;
            break;
        case DC_TITLE:
            this.titles.add(value);
            break;
        case DC_CREATOR:
            this.creators.add(value);
            break;
        case DC_CONTRIBUTOR:
            this.contributors.add(value);
            break;
        case DC_PUBLISHER:
            this.publisher = value;
            break;
        case DC_SUBJECT:
            this.subjects.add(value);
            break;
        case DC_RIGHTS:
            this.rights.add(value);
            break;
        case DC_DATE:
            this.date = value;
            break;
        case UNIQUE_IDENT:
            if (resource == null) {
                this.identifier = value;
            }
            break;
        case HAS_SIGNATURES:
            this.hasSignatures = true;
            break;
        case HAS_ENCRYPTION:
            this.hasEncryption = true;
            break;
        case HAS_FIXED_LAYOUT:
            this.hasFixedLayout = true;
            break;
        case HAS_SCRIPTS:
            this.hasScripts = true;
            break;
        case SPINE_INDEX:
            break;
        default:
            break;
        }
    }

    /**
     * Get EPUB file creation date
     * @return
     */
    public Date getCreationDate() {
        return toDate(this.creationDate);
    }

    /**
     * Get EPUB file last modified date
     * @return
     */
    public Date getLastModifiedDate() {
        return toDate(this.lastModifiedDate);
    }

    /**
     * Get format of file validated (application/octet-stream if not an EPUB)
     * @return
     */
    public String getFormat() {
        if (formatName == null) {
            return FALLBACK_FORMAT;
        } else {
            return formatName; // application/epub+zip
        }
    }

    /**
     * Get EPUB version validated against - EPUBcheck will use the most recent minor
     * release version for the EPUB's major version e.g. for EPUBCheck 4.2.0, all
     * EPUB 2s will be validated against the 2.0.1 spec, all EPUB 3s will be
     * validated against the EPUB 3.2 spec.
     *
     * @return version validated as
     */
    public String getVersion() {
        return formatVersion;
    }

    /**
     * Get all messages generated during validation
     * @return
     */
    public List<CheckMessage> getAllMessages() {
        List<CheckMessage> messages = new ArrayList<CheckMessage>();
        messages.addAll(fatalErrors);
        messages.addAll(errors);
        messages.addAll(warns);
        messages.addAll(usageMsgs);
        messages.addAll(infoMsgs);
        return messages;
    }

    /**
     * Get EPUB page count.
     * @return
     */
    public long getPageCount() {
        return pagesCount;
    }

    /**
     * Get EPUB character count.
     * @return
     */
    public long getCharacterCount() {
        return charsCount;
    }

    /**
     * Get EPUB language.
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get EPUB identifier.
     * @return
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get EPUB titles as string array
     * @return
     */
    public String[] getTitles() {
        return toStringArray(titles);
    }

    /**
     * Get EPUB creators as string array
     * @return
     */
    public String[] getCreators() {
        return toStringArray(creators);
    }

    /**
     * Get EPUB contributors as string array.
     * @return
     */
    public String[] getContributors() {
        return toStringArray(contributors);
    }

    /**
     * Get EPUB publication date
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Get EPUB publisher
     * @return
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Get EPUB subject headings
     * @return
     */
    public String[] getSubjects() {
        return toStringArray(subjects);
    }

    /**
     * Get EPUB rights statements as string array
     * @return
     */
    public String[] getRights() {
        return toStringArray(rights);
    }

    /**
     * Get EPUB embedded fonts
     * @return
     */
    public Set<String> getEmbeddedFonts() {
        return embeddedFonts;
    }

    /**
     * Get EPUB referenced fonts - these fonts are not embedded
     * @return
     */
    public Set<String> getRefFonts() {
        return refFonts;
    }

    /**
     * Get all EPUB references - this array includes both referenced and embedded
     * material.
     *
     * @return
     */
    public String[] getReferences() {
        return toStringArray(references);
    }

    /**
     * Get a list of all EPUB resources - files that are either stored in the EPUB
     * package or else stored outside of the EPUB but used as a key component e.g.
     * embedded video or audio.
     *
     * @return
     */
    public String[] getResources() {
        return toStringArray(resources);
    }

    /**
     * Get list of media types present in the EPUB package
     *
     * @return
     */
    public String[] getMediaTypes() {
        return toStringArray(mediaTypes);
    }

    /**
     * Get set of features identified in the EPUB
     * @return
     */
    public Set<String> getFeatures() {
        Set<String> features = new TreeSet<String>();
        if (hasEncryption)
            features.add(FEATURE_HASENCRYPTION);
        if (hasSignatures)
            features.add(FEATURE_HASSIGNATURES);
        if (hasAudio)
            features.add(FEATURE_HASAUDIO);
        if (hasVideo)
            features.add(FEATURE_HASVIDEO);
        if (hasFixedLayout)
            features.add(FEATURE_HASFIXEDLAYOUT);
        if (hasScripts)
            features.add(FEATURE_HASSCRIPTS);
        return features;
    }

    private String[] toStringArray(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            String[] arr = new String[set.size()];
            return set.toArray(arr);
        } else {
            return null;
        }
    }

    private static Date toDate(String isoDate) {
        if (isoDate == null || isoDate.length() == 0) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_PATTERN);
        Date date;
        try {
            date = simpleDateFormat.parse(isoDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid ISO date provided: " + isoDate, e);
        }
        return date;
    }

    /*
     * Because of the way message(MessageId, EPUBLocation, Args) is overridden in
     * MasterReport and message(Message, EPUBLocation, Args) is overridden in this
     * report, messages that are passed as a Message rather than a MessageId may not
     * get included in the message count. These overrides count the number of
     * messages listed instead since all messages travel through message(Message,
     * EPUBLocation, Args)
     */

    @Override
    public int getErrorCount() {
        return errors.size();
    }

    @Override
    public int getWarningCount() {
        return warns.size();
    }

    @Override
    public int getFatalErrorCount() {
        return fatalErrors.size();
    }

    @Override
    public int getUsageCount() {
        return usageMsgs.size();
    }

    @Override
    public int getInfoCount() {
        return infoMsgs.size();
    }

    /*
     * This report has no output, it is used to harvest properties for a JHOVE
     * report
     */
    @Override
    public int generate() {
        return 0;
    }

    @Override
    public void initialize() {
        // no initialization code required
    }
}
