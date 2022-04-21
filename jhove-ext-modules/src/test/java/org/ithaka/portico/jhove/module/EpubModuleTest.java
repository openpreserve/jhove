package org.ithaka.portico.jhove.module;

import static org.ithaka.portico.jhove.module.epub.ReportPropertyNames.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.harvard.hul.ois.jhove.ChecksumType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.RepInfo;

/**
 * <p>
 * Tests for EpubModule. Although EPUBCheck has not been mocked for these tests,
 * tests focus on functionality in the EpubModule as it generates the JHOVE info
 * rather than testing the functionality of EPUBCheck. Sample EPUBs sourced
 * from:
 * <ul>
 * <li>IDPF's EPUB 3 samples and test suite on GitHub</li>
 * <li>National Library of the Netherlands / Research epubPolicyTests on
 * GitHub</li>
 * </ul>
 * Some of the sample EPUBs were modified slightly for specific tests.
 * </p>
 *
 * @see <a href=
 *      "https://github.com/IDPF/epub3-samples">https://github.com/IDPF/epub3-samples</a>
 * @see <a href=
 *      "https://github.com/IDPF/epub-testsuite">https://github.com/IDPF/epub-testsuite</a>
 * @see <a href=
 *      "https://github.com/KBNLresearch/epubPolicyTests">https://github.com/KBNLresearch/epubPolicyTests</a>
 *
 * @author Karen Hanson
 */
@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class EpubModuleTest {

    private static final String EPUBMETADATA_KEY = "EPUBMetadata";

    private static final String CHILDRENSLIT_SRC_FILEPATH = "src/test/resources/epub/epub3-valid-childrens-literature.epub";
    private static final String MINIMAL_EPUB_FILEPATH = "src/test/resources/epub/epub2-minimal.epub";
    private static final String WRONG_EXT_NOT_AN_EPUB_FILEPATH = "src/test/resources/epub/not-an-epub.docx";
    private static final String RIGHT_EXT_NOT_AN_EPUB_FILEPATH = "src/test/resources/epub/not-an-epub.epub";
    private static final String ZIPPED_EPUB_FILEPATH = "src/test/resources/epub/epub3-zipped-childrens-literature.epub";
    private static final String EPUB2_WITH_WARNING_FILEPATH = "src/test/resources/epub/epub2-with-warning-minimal.epub";
    private static final String EPUB3_WITH_MULTIMEDIA_FILEPATH = "src/test/resources/epub/epub3-valid-multimedia.epub";
    private static final String EMPTY_EPUB_FILEPATH = "src/test/resources/epub/empty.epub";
    private static final String EPUB_WRONG_EXT_FILEPATH = "src/test/resources/epub/epub3-wrong-ext-childrens-literature.wrong";
    private static final String EPUB_MISSING_FONT_FILEPATH = "src/test/resources/epub/epub2-missing-fontresource.epub";
    private static final String EPUB_OBFUSCATED_FONT_FILEPATH = "src/test/resources/epub/epub3-font-obfuscated-wasteland.epub";
    private static final String EPUB2_WITH_ERROR_FILEPATH = "src/test/resources/epub/epub2-with-error-minimal.epub";
    private static final String EPUB2_MISSING_OPF_FILEPATH = "src/test/resources/epub/epub2-no-opf-minimal.epub";
    private static final String EPUB3_FIXED_LAYOUT_FILEPATH = "src/test/resources/epub/epub3-valid-fixedlayout-page-blanche.epub";
    private static final String EPUB2_ENCRYPTION = "src/test/resources/epub/epub2-valid-minimal-encryption.epub";
    private static final String EPUB3_TITLE_ENCODING = "src/test/resources/epub/epub3-multiple-renditions.epub";

    private static final String EXPECTED_MEDIATYPE = "application/epub+zip";
    private static final String EXPECTED_VERSION_3_2 = "3.2";
    private static final String PNG_MIMETYPE = "image/png";
    private static final String XHTML_MIMETYPE = "application/xhtml+xml";
    private static final String NCX_MIMETYPE = "application/x-dtbncx+xml";
    private static final String JPG_MIMETYPE = "image/jpeg";
    private static final String CSS_MIMETYPE = "text/css";
    private static final String OCTET_MIMETYPE = "application/octet-stream";
    
    private static final String EN_LANGUAGE = "en";

    /**
     * Test parses a valid EPUB3 file does thorough verification that properties are
     * as expected. Properties not checked here will be covered in other tests. Test
     * file source:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub3PropertiesTest() throws Exception {
        File epubFile = new File(CHILDRENSLIT_SRC_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        assertEquals(0, info.getMessage().size()); // no errors
        assertEquals("EPUB", info.getFormat());
        assertEquals(EXPECTED_MEDIATYPE, info.getMimeType());
        assertEquals(EXPECTED_VERSION_3_2, info.getVersion());
        // these may change, so just check they aren't null
        assertNotNull(info.getCreated());
        assertNotNull(info.getLastModified());

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        assertEquals(PropertyArity.LIST, metadata.getArity());
        Map<String, Object> props = toMap(metadata);

        final long charcount = 324066L;
        assertEquals(charcount, props.get(PROPNAME_CHARCOUNT));
        assertEquals(EN_LANGUAGE, props.get(PROPNAME_LANGUAGE));
        assertEquals(true, props.get(FEATURE_HASSCRIPTS));
        assertEquals(null, props.get(FEATURE_HASAUDIO));
        assertEquals(null, props.get(FEATURE_HASVIDEO));

        Set<String> mediaTypes = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_MEDIATYPES)));
        final int expectedNumMediaTypes = 4;
        assertEquals(expectedNumMediaTypes, mediaTypes.size());
        assertTrue(mediaTypes.contains(PNG_MIMETYPE));
        assertTrue(mediaTypes.contains(CSS_MIMETYPE));
        assertTrue(mediaTypes.contains(XHTML_MIMETYPE));
        assertTrue(mediaTypes.contains(NCX_MIMETYPE));

        Set<String> resources = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_RESOURCES)));
        final int expectedNumResources = 5;
        assertEquals(expectedNumResources, resources.size());
        assertTrue(resources.contains("EPUB/images/cover.png"));
        assertTrue(resources.contains("EPUB/css/nav.css"));

        Set<Property> infoPropsSet = (Set<Property>) props.get(PROPNAME_INFO);

        Map<String, Object> infoProps = new HashMap<String, Object>();
        infoPropsSet.forEach(p -> infoProps.put(p.getName(), p.getValue()));

        assertEquals("http://www.gutenberg.org/ebooks/25545", infoProps.get(PROPNAME_IDENTIFIER));
        assertEquals("2008-05-20", infoProps.get(PROPNAME_DATE));
        assertEquals("Public domain in the USA.", infoProps.get(PROPNAME_RIGHTS));

        Set<String> titles = new HashSet<String>(Arrays.asList((String[]) infoProps.get(PROPNAME_TITLE)));
        assertEquals(2, titles.size());
        assertTrue(titles.contains("Children's Literature"));
        assertTrue(titles.contains("A Textbook of Sources for Teachers and Teacher-Training Classes"));

        Set<String> creators = new HashSet<String>(Arrays.asList((String[]) infoProps.get(PROPNAME_CREATOR)));
        assertEquals(2, creators.size());
        assertTrue(creators.contains("Charles Madison Curry"));
        assertTrue(creators.contains("Erle Elsworth Clippinger"));

        Set<String> subjects = new HashSet<String>(Arrays.asList((String[]) infoProps.get(PROPNAME_SUBJECTS)));
        assertEquals(2, subjects.size());
        assertTrue(subjects.contains("Children -- Books and reading"));
        assertTrue(subjects.contains("Children's literature -- Study and teaching"));

    }

    /**
     * Valid EPUB3 file and the checksum. Test file source:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub3ChecksumTest() throws Exception {
        File epubFile = new File(CHILDRENSLIT_SRC_FILEPATH);

        EpubModule em = new EpubModule();
        JhoveBase je = new JhoveBase();
        je.setChecksumFlag(true);
        em.setBase(je);
        RepInfo info = new RepInfo(epubFile.getAbsolutePath());
        em.parse(new FileInputStream(epubFile), info, 0);

        Map<ChecksumType, String> checksums = new HashMap<ChecksumType, String>();
        info.getChecksum().forEach(cs -> checksums.put(cs.getType(), cs.getValue()));
        assertEquals("da79cb9b", checksums.get(ChecksumType.CRC32));
        assertEquals("4c2dee43162e40690ba05926b9f42522", checksums.get(ChecksumType.MD5));
        assertEquals("72bcf1b71f4dd9b902a5fcd614601d5e488003a0", checksums.get(ChecksumType.SHA1));
        assertEquals("a1da72ef94de43a70a97538d5e789f74bf7c29bd99f8059697ae75a6b85a75f7", checksums.get(ChecksumType.SHA256));
        final int expectedNumChecksums = 4;
        assertEquals(expectedNumChecksums, info.getChecksum().size());
    }

    /**
     * Check signatures for valid EPUB3 file. Test file source:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesValidEpub3Test() throws Exception {
        File epubFile = new File(CHILDRENSLIT_SRC_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.TRUE));
    }

    /**
     * Valid EPUB3 file with remote and local multimedia embedded. Test file source:
     * https://github.com/IDPF/epub-testsuite/tree/master/content/30/epub30-test-0100
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub3WithRemoteResourcesTest() throws Exception {
        String remoteMp3Url = "http://epubtest.org/media/remote/allison64-remote.mp3";
        String remoteMp4Url = "http://epubtest.org/media/remote/allison64-remote.mp4";

        File epubFile = new File(EPUB3_WITH_MULTIMEDIA_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);

        assertEquals(true, props.get(FEATURE_HASAUDIO));
        assertEquals(true, props.get(FEATURE_HASVIDEO));
        assertEquals(true, props.get(FEATURE_HASSCRIPTS));

        Set<String> references = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_REFERENCES)));
        final int expectedNumReferences = 8;
        assertEquals(expectedNumReferences, references.size());
        // spot check a few
        assertTrue(references.contains("http://idpf.org/"));
        assertTrue(
                references.contains("http://idpf.org/epub/30/spec/epub30-contentdocs.html#sec-xhtml-content-switch"));
        assertTrue(references.contains(remoteMp3Url));

        Set<String> resources = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_RESOURCES)));
        final int expectedNumResources = 51;
        assertEquals(expectedNumResources, resources.size());
        // spot check a few
        assertTrue(resources.contains(remoteMp4Url));
        assertTrue(resources.contains(remoteMp3Url));
        assertTrue(resources.contains("EPUB/img/mathml-01-020-styling.png"));
        assertTrue(resources.contains("EPUB/img/check.jpg"));
    }

    /**
     * Basic EPUB2 check. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub2PropertiesTest() throws Exception {
        File epubFile = new File(MINIMAL_EPUB_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        assertEquals(0, info.getMessage().size()); // no errors
        assertEquals(EXPECTED_MEDIATYPE, info.getMimeType());
        assertEquals("2.0.1", info.getVersion());
        // may change, so just check it isn't null
        assertNotNull(info.getCreated());

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);

        final long expectedCharCount = 4520L;
        assertEquals(expectedCharCount, props.get(PROPNAME_CHARCOUNT));
        assertEquals(EN_LANGUAGE, props.get(PROPNAME_LANGUAGE));

        Set<String> mediaTypes = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_MEDIATYPES)));
        final int expectedNumMediaTypes = 4;
        assertEquals(expectedNumMediaTypes, mediaTypes.size());
        assertTrue(mediaTypes.contains(PNG_MIMETYPE));
        assertTrue(mediaTypes.contains(JPG_MIMETYPE));
        assertTrue(mediaTypes.contains(XHTML_MIMETYPE));
        assertTrue(mediaTypes.contains(NCX_MIMETYPE));

        Set<String> resources = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_RESOURCES)));
        final int expectedNumResources = 4;
        assertEquals(expectedNumResources, resources.size());
        assertTrue(resources.contains("OEBPS/Text/pdfMigration.html"));
        assertTrue(resources.contains("OEBPS/Text/cover.xhtml"));

        Set<String> references = new HashSet<String>(Arrays.asList((String[]) props.get(PROPNAME_REFERENCES)));
        final int expectedNumReferences = 7;
        assertEquals(expectedNumReferences, references.size());
        assertTrue(references.contains("http://acroeng.adobe.com/PDFReference/ISO32000/PDF32000-Adobe.pdf"));
        assertTrue(references.contains(
                "http://qanda.digipres.org/19/what-are-the-benefits-and-risks-of-using-the-pdf-a-file-format?show=21#a21"));

        Set<Property> infoPropsSet = (Set<Property>) props.get(PROPNAME_INFO);

        Map<String, Object> infoProps = new HashMap<String, Object>();
        infoPropsSet.forEach(p -> infoProps.put(p.getName(), p.getValue()));

        assertEquals("urn:uuid:f930f4b3-cba2-42ba-ab26-d49438ab00d6", infoProps.get(PROPNAME_IDENTIFIER));
        assertEquals("2015-03-03", infoProps.get(PROPNAME_DATE));
        assertEquals("When (not) to migrate a PDF to PDF/A", infoProps.get(PROPNAME_TITLE));
        assertEquals("Johan van der Knijff", infoProps.get(PROPNAME_CREATOR));

    }

    /**
     * Valid EPUB2 file and the checksum. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub2ChecksumTest() throws Exception {
        File epubFile = new File(MINIMAL_EPUB_FILEPATH);

        EpubModule em = new EpubModule();
        JhoveBase je = new JhoveBase();
        je.setChecksumFlag(true);
        em.setBase(je);
        RepInfo info = new RepInfo(epubFile.getAbsolutePath());
        em.parse(new FileInputStream(epubFile), info, 0);

        Map<ChecksumType, String> checksums = new HashMap<ChecksumType, String>();
        info.getChecksum().forEach(cs -> checksums.put(cs.getType(), cs.getValue()));
        assertEquals("8b80b526", checksums.get(ChecksumType.CRC32));
        assertEquals("b2110219d62c3c6ef1683c645636fd38", checksums.get(ChecksumType.MD5));
        assertEquals("79f20f6a499a640019a9bb0334652edbb954c3c9", checksums.get(ChecksumType.SHA1));
        assertEquals("4776bb33b1cce8598b31996ed0b4daf36e4b74e379b811a27e51efa15315744a", checksums.get(ChecksumType.SHA256));
        final int expectedNumChecksum = 4;
        assertEquals(expectedNumChecksum, info.getChecksum().size());
    }

    /**
     * Check signatures for valid EPUB2 file. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesValidEpub2Test() throws Exception {
        File epubFile = new File(MINIMAL_EPUB_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.TRUE));
    }

    /**
     * Attempt to parse a file with an epub extension that is actually an empty file
     * and not a zip at all.
     *
     * @throws Exception
     */
    @Test
    public void parseEmptyFileTest() throws Exception {
        File epubFile = new File(EMPTY_EPUB_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        assertEquals(2, info.getMessage().size());
        assertEquals(OCTET_MIMETYPE, info.getMimeType());
    }

    /**
     * Attempt to check signature of a file with an epub extension that is actually
     * an empty file and not a zip at all.
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesEmptyFileTest() throws Exception {
        File epubFile = new File(EMPTY_EPUB_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.FALSE));
    }

    /**
     * It's a valid EPUB but the file extension is wrong - this is not considered an
     * issue by the EPUBCheck. Test file derived from:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void parseEpubWithWrongExtensionTest() throws Exception {
        File epubFile = new File(EPUB_WRONG_EXT_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        Message msg = info.getMessage().get(0);
        assertEquals("PKG-024", msg.getId());
        assertTrue(msg.getMessage().contains("INFO"));
        assertEquals(1, info.getMessage().size());
    }

    /**
     * Attempt to check signature of a file that is a valid epub with a non-standard
     * extension This should pass the signature test, since .epub extension not
     * mandatory.
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesWrongExtensionTest() throws Exception {
        File epubFile = new File(EPUB_WRONG_EXT_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.TRUE));
    }

    /**
     * Contents of the EPUB are valid but it was not compressed properly - it was
     * zipped and renamed. Test file derived from:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void parseImproperlyCompressedEpubTest() throws Exception {
        File epubFile = new File(ZIPPED_EPUB_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        assertEquals(EXPECTED_MEDIATYPE, info.getMimeType());
        assertEquals(EXPECTED_VERSION_3_2, info.getVersion());
        assertEquals(1, info.getMessage().size());
        assertEquals("PKG-006", info.getMessage().get(0).getId());
    }

    /**
     * Contents of the EPUB are valid but it was not compressed properly - it was
     * zipped and renamed. Test file derived from:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesImproperlyCompressedEpubTest() throws Exception {
        File epubFile = new File(ZIPPED_EPUB_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.FALSE));
    }

    /**
     * Parse shows not well formed / invalid when file is not an EPUB
     *
     * @throws Exception
     */
    @Test
    public void parseNonEpubTest() throws Exception {
        File epubFile = new File(WRONG_EXT_NOT_AN_EPUB_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        List<Message> msgs = info.getMessage();
        final int expectedNumMessages = 3;
        assertEquals(expectedNumMessages, msgs.size());
    }

    /**
     * Check signature shows not well formed / invalid when file is not an EPUB
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesNonEpubTest() throws Exception {
        File epubFile = new File(WRONG_EXT_NOT_AN_EPUB_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.FALSE));
    }

    /**
     * File is not an EPUB even though it has the correct extension
     *
     * @throws Exception
     */
    @Test
    public void parseNonEpubWithEpubExtensionTest() throws Exception {
        File epubFile = new File(RIGHT_EXT_NOT_AN_EPUB_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        assertEquals(OCTET_MIMETYPE, info.getMimeType());
        List<Message> msgs = info.getMessage();
        final int expectedNumMessages = 3;
        assertEquals(expectedNumMessages, msgs.size());
    }

    /**
     * Check signature shows not well formed / invalid when file is not an EPUB
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesNonEpubWithEpubExtensionTest() throws Exception {
        File epubFile = new File(RIGHT_EXT_NOT_AN_EPUB_FILEPATH);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.FALSE));
    }


    /**
     * This epub has been modified to generate a single warning. Should be well
     * formed and valid. File derived from:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * throws @Exception
     */
    @Test
    public void parseEpubWithWarningNoErrorsTest() throws Exception {
        File epubFile = new File(EPUB2_WITH_WARNING_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        // check for the one warning error:
        assertEquals(1, info.getMessage().size());
        Message msg = info.getMessage().get(0);
        assertEquals("PKG-010", msg.getId());
        assertTrue(msg.getMessage().contains("WARN"));
        // Do NOT compare strings because of translated messages
        // assertTrue(msg.getMessage().contains("Filename contains spaces"));
        assertTrue(msg instanceof ErrorMessage);
    }

    /**
     * This epub has missing fonts. Should be well formed but not valid. File
     * derived from:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_missingfontresource
     *
     * throws @Exception
     */
    @Test
    public void parseEpubWithMissingFontsTest() throws Exception {
        File epubFile = new File(EPUB_MISSING_FONT_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.FALSE);
        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        Set<Property> fonts = (Set<Property>) props.get(PROPNAME_FONTS);
        assertEquals(1, fonts.size());

        Set<Property> font = (Set<Property>) fonts.iterator().next().getValue();
        Map<String, Object> fontinfo = new HashMap<String, Object>();
        font.forEach(f -> fontinfo.put(f.getName(), f.getValue()));

        // only one font in this file, listed but missing.
        assertEquals("Courier", fontinfo.get(PROPNAME_FONTNAME));
        assertEquals(true, fontinfo.get(PROPNAME_FONTFILE));

        // check for could not find referenced resource error.
        assertEquals("RSC-007", info.getMessage().get(0).getId());
    }

    /**
     * This EPUB3 has obfuscated fonts. It is valid, but there should be a flag
     * signaling there is an encrypted file. Source of EPUB:
     * https://github.com/IDPF/epub3-samples/tree/master/30/wasteland-woff-obf
     *
     */
    @Test
    public void parseEpubWithObfuscatedFontsTest() throws Exception {
        File epubFile = new File(EPUB_OBFUSCATED_FONT_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        assertEquals(true, props.get(FEATURE_HASENCRYPTION));

        Set<Property> fonts = (Set<Property>) props.get(PROPNAME_FONTS);
        final int expectedNumFonts = 3;
        assertEquals(expectedNumFonts, fonts.size());
        Set<String> fontNames = new HashSet<String>();
        for (Property font : fonts) {
            Set<Property> fontinfo = (Set<Property>) font.getValue();
            Map<String, Object> map = new HashMap<String, Object>();
            fontinfo.forEach(f -> map.put(f.getName(), f.getValue()));
            assertEquals(true, map.get(PROPNAME_FONTFILE));
            fontNames.add(map.get(PROPNAME_FONTNAME).toString());
        }
        assertEquals(expectedNumFonts, fontNames.size());
        assertTrue(fontNames.contains("OldStandard"));
        assertTrue(fontNames.contains("OldStandard,bold"));
        assertTrue(fontNames.contains("OldStandard,italic"));
    }

    /**
     * This EPUB has been modified to have a single non-fatal error. The error
     * complains about a remote resource not being listed in the OCF (RSC-006). Test
     * file derived from:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void parseEpubWithNonFatalErrorTest() throws Exception {
        File epubFile = new File(EPUB2_WITH_ERROR_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.FALSE);
        assertEquals(1, info.getMessage().size());
        Message msg = info.getMessage().get(0);
        assertEquals("RSC-006", msg.getId());
        assertTrue(msg instanceof ErrorMessage);
    }

    /**
     * EPUBs require an OPF file. In this test EPUB it has been removed. The test
     * confirms that the result is a FATAL error that flags the file as not well
     * formed. EPUBCheck also returns a non-fatal ERROR with similar text. File
     * derived from:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void parseEpubMissingOpfTest() throws Exception {
        File epubFile = new File(EPUB2_MISSING_OPF_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);

        assertEquals(OCTET_MIMETYPE, info.getMimeType());

        Set<String> msgCodes = new HashSet<String>();
        assertEquals(2, info.getMessage().size());
        Message msg1 = info.getMessage().get(0);
        Message msg2 = info.getMessage().get(1);
        assertTrue(msg1 instanceof ErrorMessage);
        msgCodes.add(msg1.getId());
        assertTrue(msg2 instanceof ErrorMessage);
        msgCodes.add(msg2.getId());
        assertTrue(msgCodes.contains("OPF-002"));
        assertTrue(msgCodes.contains("RSC-001"));
    }

    /**
     * This EPUB3 has a fixed layout. This test confirms that the hasFixedLayout
     * flag is present in the JHOVE properties section. File source:
     * https://github.com/IDPF/epub3-samples/tree/master/30/page-blanche
     *
     * @throws Exception
     */
    @Test
    public void parseEpubWithFixedLayoutTest() throws Exception {
        File epubFile = new File(EPUB3_FIXED_LAYOUT_FILEPATH);
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        assertEquals(true, props.get(FEATURE_HASFIXEDLAYOUT));
        assertEquals(0, info.getMessage().size()); // no issues
    }

    /**
     * EPUB file simulates encryption - should return hasEncryption. Note that
     * checkSignatures returns well formed and valid, while parse on same file
     * returns invalid. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal_encryption
     *
     * @throws Exception
     */
    @Test
    public void parseEpub2WithEncryptionTest() throws Exception {
        File epubFile = new File(EPUB2_ENCRYPTION);
        // well formed but not valid because can't parse encrypted file to confirm
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.FALSE);

        Property metadata = info.getProperty(PROPNAME_EPUB_METADATA);
        Map<String, Object> props = toMap(metadata);
        assertEquals(true, props.get(FEATURE_HASENCRYPTION));

        boolean bFoundRsc4 = false; 
        for (Message msg : info.getMessage()) {
            if ("RSC-004".equals(msg.getId())) {
                // Do NOT compare strings because of translations...
                // assertTrue(msg.getMessage().contains("could not be decrypted"));
                bFoundRsc4 = true;
                break;
            }
        }
        assertEquals("RSC-004 error was not found", true, bFoundRsc4);
    }

    /**
     * EPUB file simulates encryption. This is a case where signature will show it
     * is well formed and valid but parsing will reveal encryption. Test file
     * source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal_encryption
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesEpub2WithEncryptionTest() throws Exception {
        File epubFile = new File(EPUB2_ENCRYPTION);
        assertTrue(checkSignatureMatch(epubFile, RepInfo.TRUE));
    }

    /**
     * EPUB file has title sensitive to incorrect encoding. This tests parses and
     * confirms title is properly encoded in output source:
     * https://github.com/w3c/epubcheck/blob/master/src/test/resources/30/epub/valid/edupub-multiple-renditions.epub
     *
     * @throws Exception
     */
    @Test
    public void parseEpub3TitleEncodingTest() throws Exception {
        File epubFile = new File(EPUB3_TITLE_ENCODING);
        String expectedTitle = "महाभारत";
        // well formed and valid
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);

        Property metadata = info.getProperty(PROPNAME_EPUB_METADATA);
        Map<String, Object> props = toMap(metadata);
        Set<Property> infoPropsSet = (Set<Property>) props.get(PROPNAME_INFO);
        Map<String, Object> infoProps = new HashMap<String, Object>();
        infoPropsSet.forEach(p -> infoProps.put(p.getName(), p.getValue()));
        String title = (String) infoProps.get(PROPNAME_TITLE);
        assertEquals(expectedTitle, title);
    }


    /**
     * Passes file to checkSignatures(), checks wellformed, valid, and sigmatch
     * values are as expected.
     *
     * @param epubFile
     * @param expectedWellFormedValue RepInfo.TRUE/FALSE/UNDETERMINED.
     * @return
     * @throws Exception
     */
    private boolean checkSignatureMatch(File epubFile, int expectedWellFormedValue) throws Exception {
        RepInfo info = new RepInfo(epubFile.getAbsolutePath());
        EpubModule em = new EpubModule();
        em.checkSignatures(epubFile, new FileInputStream(epubFile), info);
        assertEquals(expectedWellFormedValue, info.getWellFormed());
        // valid should always be undetermined in sig match
        assertEquals(RepInfo.UNDETERMINED, info.getValid());
        if (expectedWellFormedValue == RepInfo.TRUE) {
            List<String> sigmatch = info.getSigMatch();
            assertEquals("EPUB-ptc", sigmatch.get(0));
        } else {
            assertEquals(0, info.getSigMatch().size());
        }
        // all fine? return true.
        return true;
    }

    /**
     * Passes file to parse() checks well formed and valid status against expected
     * values then returns RepInfo for further analy
     *
     * @param epubFile                file to parse
     * @param expectedWellFormedValue RepInfo.TRUE/FALSE/UNDETERMINED.
     * @param expectedValidityValue   RepInfo.TRUE/FALSE/UNDETERMINED.
     * @return
     * @throws Exception
     */
    private RepInfo parseAndCheckValidity(File epubFile, int expectedWellFormedValue, int expectedValidityValue)
            throws Exception {
        RepInfo info = new RepInfo(epubFile.getAbsolutePath());
        EpubModule em = new EpubModule();
        em.parse(new FileInputStream(epubFile), info, 0);
        assertEquals(expectedWellFormedValue, info.getWellFormed());
        assertEquals(expectedValidityValue, info.getValid());
        return info;
    }

    /**
     * Utility to convert metadata property to a Map. Property passed in must have a
     * list as its value.
     *
     * @param propertymap
     * @return
     */
    private static Map<String, Object> toMap(Property metadata) {
        if (!(metadata.getValue() instanceof List)) {
            throw new IllegalArgumentException(
                    "\"metadata\" property must be a List<Property> in order for it to be converted to a map");
        }
        Map<String, Object> propertymap = new HashMap<String, Object>();
        List<Property> propList = (List<Property>) metadata.getValue();
        propList.forEach(p -> propertymap.put(p.getName(), p.getValue()));
        return propertymap;
    }

}
