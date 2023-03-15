package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jwat.common.DiagnosisType;

import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;

@RunWith(JUnit4.class)
public class GzipModuleTest {
	
    private static final String TXT_GZIP_SAMPLE_FILE = "src/test/resources/gzip/sample.txt.gz";
    private static final String RECORDS = "Records";
    private static final String THREE_FILES_GZIP_SAMPLE_FILE = "src/test/resources/gzip/three-files.gz";
    
    @Test
    public void parseSampleTextGzipFile() throws Exception {
	    File gzipFile = new File(TXT_GZIP_SAMPLE_FILE);
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(Arrays.asList(gzm.getName()), info.getSigMatch());
        
        assertEquals(0, info.getMessage().size());
        
        // Validate that it creates properties for each record
        assertEquals(1, info.getProperty().size());
        assertNotNull(info.getProperty().get(RECORDS));
        assertEquals(PropertyArity.LIST, info.getProperty().get(RECORDS).getArity());
        assertEquals(PropertyType.PROPERTY, info.getProperty().get(RECORDS).getType());
        assertEquals(1, ((List<Property>)info.getProperty().get(RECORDS).getValue()).size());
    }

    @Test
    public void checkSignatureSampleTextGzipFile() throws Exception {
        File gzipFile = new File(TXT_GZIP_SAMPLE_FILE);

        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.checkSignatures(null, new FileInputStream(gzipFile), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(GzipModule.class, info.getModule().getClass());
        assertEquals(Arrays.asList(gzm.getName()), info.getSigMatch());
    }
    
    @Test
    public void parseThreeFilesGzipFile() throws Exception {
        File gzipFile = new File(THREE_FILES_GZIP_SAMPLE_FILE);
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        assertEquals(Arrays.asList(gzm.getName()), info.getSigMatch());
        
        assertEquals(0, info.getMessage().size());
        
        // Validate that it creates properties for each record
        assertEquals(1, info.getProperty().size());
        assertNotNull(info.getProperty().get(RECORDS));
        assertEquals(3, ((List<Property>)info.getProperty().get(RECORDS).getValue()).size());
    }

    @Test
    public void checkSignaturThreeFilesGzipFile() throws Exception {
        File gzipFile = new File(THREE_FILES_GZIP_SAMPLE_FILE);

        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.checkSignatures(null, new FileInputStream(gzipFile), info);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(GzipModule.class, info.getModule().getClass());
        assertEquals(Arrays.asList(gzm.getName()), info.getSigMatch());
    }
    
    @Test
    public void parseInvalidCompressionGzipFile() throws Exception {
        File gzipFile = new File("src/test/resources/gzip/invalid-compression.gz");
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        assertTrue(info.getSigMatch().isEmpty());

        // Validate the failures.
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
    }
    
    @Test
    public void parseInvalidEmptyGzipFile() throws Exception {
        File gzipFile = new File("src/test/resources/gzip/invalid-empty.gzip");
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        assertTrue(info.getSigMatch().isEmpty());

        // Validate the failures.
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.ERROR_EXPECTED.name()).intValue());
    }

    @Test
    public void parseInvalidEntriesGzipFile() throws Exception {
        File gzipFile = new File("src/test/resources/gzip/invalid-entries.gz");
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        assertTrue(info.getSigMatch().isEmpty());

        // Validate the failures.
        assertEquals(5, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(3, messages.size());
        assertEquals(2, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
        assertEquals(2, messages.get(DiagnosisType.RESERVED.name()).intValue());
        assertEquals(1, messages.get(DiagnosisType.UNKNOWN.name()).intValue());
    }

    @Test
    public void parseInvalidMagicGzipFile() throws Exception {
        File gzipFile = new File("src/test/resources/gzip/invalid-magic.gz");
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        assertTrue(info.getSigMatch().isEmpty());

        // Validate the failures.
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractMessages(info.getMessage());
        assertEquals(1, messages.size());
        assertEquals(1, messages.get(DiagnosisType.INVALID_EXPECTED.name()).intValue());
    }

    @Test
    public void parseInvalidTruncatedGzipFile() throws Exception {
        File gzipFile = new File("src/test/resources/gzip/invalid-truncated.gz");
        assertTrue( gzipFile.isFile() );
        
        GzipModule gzm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        gzm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.FALSE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
        assertTrue(info.getSigMatch().isEmpty());

        // Validate the failures.
        assertEquals(1, info.getMessage().size());
        Map<String, Integer> messages = extractSubMessages(info.getMessage());
        for(Map.Entry<String, Integer> e : messages.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
        assertEquals(1, messages.size());
        Iterator<String> messageKeyIterator = messages.keySet().iterator();
        String errorMessage = messageKeyIterator.next();
        assertNotNull(errorMessage);
//        assertTrue("java.util.zip.DataFormatException");
        assertTrue(errorMessage + " should contain " + DataFormatException.class.toString(),
                errorMessage.contains(DataFormatException.class.getCanonicalName()));
        assertFalse(messageKeyIterator.hasNext());
    }
    
    private static Map<String, Integer> extractMessages(Collection<Message> messages) {
		Map<String, Integer> res = new HashMap<>();
		for(Message m : messages) {
			if(res.containsKey(m.getMessage())) {
				res.put(m.getMessage(), Integer.valueOf(res.get(m.getMessage()).intValue() + 1));
			} else {
				res.put(m.getMessage(), Integer.valueOf(1));
			}
		}
		return res;
	}
    private static Map<String, Integer> extractSubMessages(Collection<Message> messages) {
        Map<String, Integer> res = new HashMap<>();
        for (Message m : messages) {
            if (res.containsKey(m.getSubMessage())) {
                res.put(m.getSubMessage(), Integer.valueOf(res.get(m.getSubMessage()).intValue() + 1));
            } else {
                res.put(m.getSubMessage(), Integer.valueOf(1));
            }
        }
        return res;
    }
}
