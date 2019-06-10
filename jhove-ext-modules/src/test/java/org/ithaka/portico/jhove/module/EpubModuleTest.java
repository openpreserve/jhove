package org.ithaka.portico.jhove.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    // TODO: consider whether validity should actually be UNDETERMINED when checking
    // signature since it causes mismatches when compared to parse results? Also,
    // should validity be UNDETERMINED when parsing encrypted since can't validate?
    // Consider whether e.g. RSC-006 and other missing resource errors should be
    // fatal?

    private static final String EPUBMETADATA_KEY = "EPUBMetadata";

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
        File epubFile = new File("src/test/resources/epub/epub3-valid-childrens-literature.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        assertEquals(0, info.getMessage().size()); // no errors
        assertEquals("EPUB", info.getFormat());
        assertEquals("application/epub+zip", info.getMimeType());
        assertEquals("3.2", info.getVersion());
        // these may change, so just check they aren't null
        assertNotNull(info.getCreated());
        assertNotNull(info.getLastModified());

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        assertEquals(PropertyArity.LIST, metadata.getArity());
        Map<String, Object> props = toMap(metadata);

        assertEquals(324066L, props.get("CharacterCount"));
        assertEquals("en", props.get("Language"));
        assertEquals(true, props.get("hasScripts"));
        assertEquals(null, props.get("hasAudio"));
        assertEquals(null, props.get("hasVideo"));

        Set<String> mediaTypes = new HashSet<String>(Arrays.asList((String[]) props.get("MediaTypes")));
        assertEquals(4, mediaTypes.size());
        assertTrue(mediaTypes.contains("image/png"));
        assertTrue(mediaTypes.contains("text/css"));
        assertTrue(mediaTypes.contains("application/xhtml+xml"));
        assertTrue(mediaTypes.contains("application/x-dtbncx+xml"));

        Set<String> resources = new HashSet<String>(Arrays.asList((String[]) props.get("LocalResources")));
        assertEquals(5, resources.size());
        assertTrue(resources.contains("EPUB/images/cover.png"));
        assertTrue(resources.contains("EPUB/css/nav.css"));

        Set<Property> infoPropsSet = (Set<Property>) props.get("Info");

        Map<String, Object> infoProps = new HashMap<String, Object>();
        infoPropsSet.forEach(p -> infoProps.put(p.getName(), p.getValue()));

        assertEquals("http://www.gutenberg.org/ebooks/25545", infoProps.get("Identifier"));
        assertEquals("2008-05-20", infoProps.get("Date"));
        assertEquals("Public domain in the USA.", infoProps.get("Rights"));

        Set<String> titles = new HashSet<String>(Arrays.asList((String[]) infoProps.get("Title")));
        assertEquals(2, titles.size());
        assertTrue(titles.contains("Children's Literature"));
        assertTrue(titles.contains("A Textbook of Sources for Teachers and Teacher-Training Classes"));

        Set<String> creators = new HashSet<String>(Arrays.asList((String[]) infoProps.get("Creator")));
        assertEquals(2, creators.size());
        assertTrue(creators.contains("Charles Madison Curry"));
        assertTrue(creators.contains("Erle Elsworth Clippinger"));

        Set<String> subjects = new HashSet<String>(Arrays.asList((String[]) infoProps.get("Subject")));
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
        File epubFile = new File("src/test/resources/epub/epub3-valid-childrens-literature.epub");

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
        assertEquals(3, info.getChecksum().size());
    }

    /**
     * Check signatures for valid EPUB3 file. Test file source:
     * https://github.com/IDPF/epub3-samples/tree/master/30/childrens-literature
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesValidEpub3Test() throws Exception {
        File epubFile = new File("src/test/resources/epub/epub3-valid-childrens-literature.epub");
        checkSignatureMatch(epubFile, RepInfo.TRUE);
    }

    /**
     * Valid EPUB3 file with remote and local multimedia embedded. Test file source:
     * https://github.com/IDPF/epub-testsuite/tree/master/content/30/epub30-test-0100
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub3WithRemoteResourcesTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/epub3-valid-multimedia.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);

        assertEquals(true, props.get("hasAudio"));
        assertEquals(true, props.get("hasVideo"));
        assertEquals(true, props.get("hasScripts"));

        Set<String> remoteResources = new HashSet<String>(Arrays.asList((String[]) props.get("RemoteResources")));
        assertEquals(2, remoteResources.size());
        assertTrue(remoteResources.contains("http://epubtest.org/media/remote/allison64-remote.mp4"));
        assertTrue(remoteResources.contains("http://epubtest.org/media/remote/allison64-remote.mp3"));

        Set<String> references = new HashSet<String>(Arrays.asList((String[]) props.get("References")));
        assertEquals(8, references.size());
        // spot check a few
        assertTrue(references.contains("http://idpf.org/"));
        assertTrue(
                references.contains("http://idpf.org/epub/30/spec/epub30-contentdocs.html#sec-xhtml-content-switch"));
        assertTrue(references.contains("http://epubtest.org/media/remote/allison64-remote.mp3"));

        Set<String> localResources = new HashSet<String>(Arrays.asList((String[]) props.get("LocalResources")));
        assertEquals(49, localResources.size());
        // spot check a few
        assertTrue(localResources.contains("EPUB/img/mathml-01-020-styling.png"));
        assertTrue(localResources.contains("EPUB/img/check.jpg"));
    }

    /**
     * Basic EPUB2 check. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub2PropertiesTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/epub2-minimal.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);

        assertEquals(0, info.getMessage().size()); // no errors
        assertEquals("application/epub+zip", info.getMimeType());
        assertEquals("application/epub+zip", info.getMimeType());
        assertEquals("2.0.1", info.getVersion());
        // may change, so just check it isn't null
        assertNotNull(info.getCreated());

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);

        assertEquals(4520L, props.get("CharacterCount"));
        assertEquals("en", props.get("Language"));

        Set<String> mediaTypes = new HashSet<String>(Arrays.asList((String[]) props.get("MediaTypes")));
        assertEquals(4, mediaTypes.size());
        assertTrue(mediaTypes.contains("image/png"));
        assertTrue(mediaTypes.contains("image/jpeg"));
        assertTrue(mediaTypes.contains("application/xhtml+xml"));
        assertTrue(mediaTypes.contains("application/x-dtbncx+xml"));

        Set<String> resources = new HashSet<String>(Arrays.asList((String[]) props.get("LocalResources")));
        assertEquals(4, resources.size());
        assertTrue(resources.contains("OEBPS/Text/pdfMigration.html"));
        assertTrue(resources.contains("OEBPS/Text/cover.xhtml"));

        Set<String> references = new HashSet<String>(Arrays.asList((String[]) props.get("References")));
        assertEquals(7, references.size());
        assertTrue(references.contains("http://acroeng.adobe.com/PDFReference/ISO32000/PDF32000-Adobe.pdf"));
        assertTrue(references.contains(
                "http://qanda.digipres.org/19/what-are-the-benefits-and-risks-of-using-the-pdf-a-file-format?show=21#a21"));

        Set<Property> infoPropsSet = (Set<Property>) props.get("Info");

        Map<String, Object> infoProps = new HashMap<String, Object>();
        infoPropsSet.forEach(p -> infoProps.put(p.getName(), p.getValue()));

        assertEquals("urn:uuid:f930f4b3-cba2-42ba-ab26-d49438ab00d6", infoProps.get("Identifier"));
        assertEquals("2015-03-03", infoProps.get("Date"));
        assertEquals("When (not) to migrate a PDF to PDF/A", infoProps.get("Title"));
        assertEquals("Johan van der Knijff", infoProps.get("Creator"));

    }

    /**
     * Valid EPUB2 file and the checksum. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void parseValidEpub2ChecksumTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/epub2-minimal.epub");

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
        assertEquals(3, info.getChecksum().size());
    }

    /**
     * Check signatures for valid EPUB2 file. Test file source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesValidEpub2Test() throws Exception {
        File epubFile = new File("src/test/resources/epub/epub2-minimal.epub");
        checkSignatureMatch(epubFile, RepInfo.TRUE);
    }

    /**
     * Attempt to parse a file with an epub extension that is actually an empty file
     * and not a zip at all.
     *
     * @throws Exception
     */
    @Test
    public void parseEmptyFileTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/empty.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        assertEquals(2, info.getMessage().size());
        assertEquals("application/octet-stream", info.getMimeType());
    }

    /**
     * Attempt to check signature of a file with an epub extension that is actually
     * an empty file and not a zip at all.
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesEmptyFileTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/empty.epub");
        checkSignatureMatch(epubFile, RepInfo.FALSE);
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
        File epubFile = new File("src/test/resources/epub/epub3-wrong-ext-childrens-literature.wrong");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        assertEquals(0, info.getMessage().size());
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
        File epubFile = new File("src/test/resources/epub/epub3-wrong-ext-childrens-literature.wrong");
        checkSignatureMatch(epubFile, RepInfo.TRUE);
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
        File epubFile = new File("src/test/resources/epub/epub3-zipped-childrens-literature.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        assertEquals("application/epub+zip", info.getMimeType());
        assertEquals("3.2", info.getVersion());
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
        File epubFile = new File("src/test/resources/epub/epub3-zipped-childrens-literature.epub");
        checkSignatureMatch(epubFile, RepInfo.FALSE);
    }

    /**
     * Parse shows not well formed / invalid when file is not an EPUB
     *
     * @throws Exception
     */
    @Test
    public void parseNonEpubTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/not-an-epub.docx");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        List<Message> msgs = info.getMessage();
        assertEquals(1, msgs.size());
    }

    /**
     * Check signature shows not well formed / invalid when file is not an EPUB
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesNonEpubTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/not-an-epub.docx");
        checkSignatureMatch(epubFile, RepInfo.FALSE);
    }

    /**
     * File is not an EPUB even though it has the correct extension
     *
     * @throws Exception
     */
    @Test
    public void parseNonEpubWithEpubExtensionTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/not-an-epub.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);
        assertEquals("application/octet-stream", info.getMimeType());
        List<Message> msgs = info.getMessage();
        assertEquals(3, msgs.size());
    }

    /**
     * Check signature shows not well formed / invalid when file is not an EPUB
     *
     * @throws Exception
     */
    @Test
    public void checkSignaturesNonEpubWithEpubExtensionTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/not-an-epub.epub");
        checkSignatureMatch(epubFile, RepInfo.FALSE);
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
        File epubFile = new File("src/test/resources/epub/epub2-with-warning-minimal.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        // check for the one warning error:
        assertEquals(1, info.getMessage().size());
        Message msg = info.getMessage().get(0);
        assertEquals("PKG-010", msg.getId());
        assertTrue(msg.getMessage().contains("WARN"));
        assertTrue(msg.getMessage().contains("Filename contains spaces"));
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
        File epubFile = new File("src/test/resources/epub/epub2-missing-fontresource.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.FALSE);
        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        Set<Property> fonts = (Set<Property>) props.get("Fonts");
        assertEquals(1, fonts.size());

        Set<Property> font = (Set<Property>) fonts.iterator().next().getValue();
        Map<String, Object> fontinfo = new HashMap<String, Object>();
        font.forEach(f -> fontinfo.put(f.getName(), f.getValue()));

        // only one font in this file, listed but missing.
        assertEquals("Courier", fontinfo.get("FontName"));
        assertEquals(true, fontinfo.get("FontFile"));

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
        File epubFile = new File("src/test/resources/epub/epub3-font-obfuscated-wasteland.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        assertEquals(true, props.get("hasEncryption"));

        Set<Property> fonts = (Set<Property>) props.get("Fonts");
        assertEquals(3, fonts.size());
        Set<String> fontNames = new HashSet<String>();
        for (Property font : fonts) {
            Set<Property> fontinfo = (Set<Property>) font.getValue();
            Map<String, Object> map = new HashMap<String, Object>();
            fontinfo.forEach(f -> map.put(f.getName(), f.getValue()));
            assertEquals(true, map.get("FontFile"));
            fontNames.add(map.get("FontName").toString());
        }
        assertEquals(3, fontNames.size());
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
        File epubFile = new File("src/test/resources/epub/epub2-with-error-minimal.epub");
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
        File epubFile = new File("src/test/resources/epub/epub2-no-opf-minimal.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.FALSE, RepInfo.FALSE);

        assertEquals("application/octet-stream", info.getMimeType());

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
        File epubFile = new File("src/test/resources/epub/epub3-valid-fixedlayout-page-blanche.epub");
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.TRUE);
        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        assertEquals(true, props.get("hasFixedLayout"));
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
        File epubFile = new File("src/test/resources/epub/epub2-valid-minimal-encryption.epub");
        // well formed but not valid because can't parse encrypted file to confirm
        RepInfo info = parseAndCheckValidity(epubFile, RepInfo.TRUE, RepInfo.FALSE);

        Property metadata = info.getProperty(EPUBMETADATA_KEY);
        Map<String, Object> props = toMap(metadata);
        assertEquals(true, props.get("hasEncryption"));

        for (Message msg : info.getMessage()) {
            if (msg.getId().equals("RSC-004")) {
                assertTrue(msg.getMessage().contains("could not be decrypted"));
                return;
            }
        }
        fail("RSC-004 error was not found");
    }

    /**
     * EPUB file simulates encryption. This is a case where signature will show it
     * is well formed and valid but parsing will reveal encryption. Test file
     * source:
     * https://github.com/KBNLresearch/epubPolicyTests/tree/master/content/epub20_minimal_encryption
     *
     * @throws IOException
     * @throws Exception
     */
    @Test
    public void checkSignaturesEpub2WithEncryptionTest() throws Exception {
        File epubFile = new File("src/test/resources/epub/epub2-valid-minimal-encryption.epub");
        checkSignatureMatch(epubFile, RepInfo.TRUE);
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
    private void checkSignatureMatch(File epubFile, int expectedWellFormedValue) throws Exception {
        RepInfo info = new RepInfo(epubFile.getAbsolutePath());
        EpubModule em = new EpubModule();
        em.checkSignatures(epubFile, new FileInputStream(epubFile), info);
        assertEquals(expectedWellFormedValue, info.getWellFormed());
        assertEquals(expectedWellFormedValue, info.getValid());
        if (expectedWellFormedValue == RepInfo.TRUE) {
            List<String> sigmatch = info.getSigMatch();
            assertEquals("EPUB-ptc", sigmatch.get(0));
        } else {
            assertEquals(0, info.getSigMatch().size());
        }
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
        if (!(metadata.getValue() instanceof java.util.List)) {
            throw new IllegalArgumentException(
                    "\"metadata\" property must be a List<Property> in order for it to be converted to a map");
        }
        Map<String, Object> propertymap = new HashMap<String, Object>();
        List<Property> propList = (List<Property>) metadata.getValue();
        propList.forEach(p -> propertymap.put(p.getName(), p.getValue()));
        return propertymap;
    }

}
