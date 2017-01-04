package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jwat.common.DiagnosisType;

import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.RepInfo;

@RunWith(JUnit4.class)
public class WarcModuleTest {
	@Test
    public void parseValidUTF8File() throws Exception {
	    File warcFile = new File("src/test/resources/warc/valid-warcfile-utf8.warc");
        assertTrue( warcFile.isFile() );
        
        WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new FileInputStream(warcFile), info, 0);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        
        assertEquals(0, info.getMessage().size());
    }

	@Test
    public void checkSignaturValidUTF8File() throws Exception {
	    File warcFile = new File("src/test/resources/warc/valid-warcfile-utf8.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.checkSignatures(null, new FileInputStream(warcFile), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(WarcModule.class, info.getModule().getClass());
        assertEquals(Arrays.asList(wm.getName()), info.getSigMatch());
	}
	
	@Test
    public void parseInvalidEmptyFile() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-empty.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new FileInputStream(warcFile), info, 0);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void checkSignaturInvalidEmptyFile() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-empty.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.checkSignatures(null, new FileInputStream(warcFile), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(WarcModule.class, info.getModule().getClass());
	}
	
	@Test
    public void parseInvalidWarcFileContentTypeRecommended() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-contenttype-recommended.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(7, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(7, messages.get(DiagnosisType.RECOMMENDED_MISSING.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileContentTypeWarcInfoRecommended() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-contenttype-warcinfo-recommended.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileDigestField() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-digest-fields.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(8, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(8, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileDuplicateField() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-duplicate-fields.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(6, messages.get(DiagnosisType.DUPLICATE.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileFieldsEmpty() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-fields-empty.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(16, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(4, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(9, messages.get(DiagnosisType.EMPTY.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileFieldsInvalidFormat() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-fields-invalidformat.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(15, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(9, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.INVALID.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileFieldsMissing() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-fields-missing.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(24, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(19, messages.get(DiagnosisType.EMPTY.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileLonelyContinuation() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-lonely-continuation.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(6, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileLonelyMonkeysLfLineEndings() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-lonely-monkeys-lf-line-endings.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(3, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileLonelyRequestResponseResourceConversion() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-lonely-request-response-resource-conversion.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(16, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(16, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileLonelyRevisit() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-lonely-revisit.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(5, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileLonelyWarcInfoMetadata() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-lonely-warcinfo-metadata.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(6, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileSegmentNumberContinuation() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-segment-number-continuation.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcFileSegmentNumberResponse() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcfile-segment-number-response.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(3, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderFieldPolicy1() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-1.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(3, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy2() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-2.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy3() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-3.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderFieldPolicy4() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-4.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(5, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy5() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-5.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED_MISSING.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy6() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-6.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy7() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-7.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(4, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(4, messages.size());
        assertEquals(1, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED_MISSING.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy8() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-8.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(5, messages.size());
        assertEquals(1, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.EMPTY.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED_MISSING.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy9() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-9.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy10() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-10.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy11() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-11.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy12() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-12.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy13() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-13.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(4, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy14() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-14.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy15() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-15.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy16() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-16.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderFieldPolicy17() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderfieldpolicy-17.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcHeaderVersion1() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-1.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion2() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-2.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion3() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-3.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion4() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-4.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion5() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-5.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion6() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-6.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion7() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-7.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion8() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-8.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion9() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-9.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion10() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-10.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion11() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-11.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion12() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-12.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion13() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-13.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion14() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-14.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion15() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-15.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion16() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-16.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion17() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-17.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion18() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-18.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion19() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-19.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion20() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-20.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion21() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-21.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion22() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-22.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion23() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-23.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion24() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-24.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion25() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-25.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        
        assertEquals(6, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.EMPTY.name()).intValue());
	}

	@Test
    public void parseInvalidWarcHeaderVersion26() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcheaderversion-26.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(4, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcReaderDiagnosis1() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcreader-diagnosis-1.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(3, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(2, messages.size());
        assertEquals(2, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcRecord1() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcrecord-1.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(7, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(5, messages.size());
        assertEquals(2, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.REQUIRED_INVALID.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED_MISSING.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.RECOMMENDED.name()).intValue());
	}

	@Test
    public void parseInvalidWarcRecord2() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcrecord-2.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(2, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(2, messages.get(DiagnosisType.ERROR.name()).intValue());
	}
	
	@Test
    public void parseInvalidWarcRecord3() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcrecord-3.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(8, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(4, messages.get(DiagnosisType.INVALID.name()).intValue());
        assertEquals(3, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.INVALID_DATA.name()).intValue());
	}

	@Test
    public void parseInvalidWarcRecordDigests1() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcrecorddigests-1.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(12, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(12, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
	}

	@Test
    public void parseInvalidWarcRecordDigests2() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcrecorddigests-2.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(4, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(4, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
	}

	@Test
    public void parseInvalidWarcRecordDigests3() throws Exception {
		File warcFile = new File("src/test/resources/warc/invalid-warcrecorddigests-3.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());

        assertEquals(4, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(4, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
	}

	@Test
    public void parseValidWarcFileContentTypeContinuation() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-contenttype-continuation.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcFileDiplicateConcurrentTo() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-duplicate-concurrentto.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcFileFieldsContinuation() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-fields-continuation.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcFileFieldsMetaInfo() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-fields-metainfo.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcFileFieldsWarcInfo() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-fields-warcinfo.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcFileNonWarcHeaders() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-non-warc-headers.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcFileUpperLowerCase() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcfile-upper-lower-case.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcRecordDigests1() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcrecorddigests-1.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}

	@Test
    public void parseValidWarcRecordDigests2() throws Exception {
		File warcFile = new File("src/test/resources/warc/valid-warcrecorddigests-2.warc");

		WarcModule wm = new WarcModule();
        RepInfo info = new RepInfo(warcFile.getAbsolutePath());
        wm.parse(new RandomAccessFile(warcFile, "r"), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(0, info.getMessage().size());
	}


	private Map<String, Integer> extractMessages(Collection<Message> messages) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		for(Message m : messages) {
			if(res.containsKey(m.getMessage())) {
				res.put(m.getMessage(), res.get(m.getMessage()) + 1);
			} else {
				res.put(m.getMessage(), 1);
			}
		}
		return res;
	}
}
