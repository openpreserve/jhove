package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.harvard.hul.ois.jhove.RepInfo;

@RunWith(JUnit4.class)
public class GzipModuleTest {
	@Test
    public void parseValidUTF8File() throws Exception {
	    File gzipFile = new File("src/test/resources/gzip/sample.txt.gz");
        assertTrue( gzipFile.isFile() );
        
        GzipModule wm = new GzipModule();
        RepInfo info = new RepInfo(gzipFile.getAbsolutePath());
        wm.parse(new FileInputStream(gzipFile), info, 0);
        
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
        
        assertEquals(0, info.getMessage().size());
    }

//	private Map<String, Integer> extractMessages(Collection<Message> messages) {
//		Map<String, Integer> res = new HashMap<String, Integer>();
//		for(Message m : messages) {
//			if(res.containsKey(m.getMessage())) {
//				res.put(m.getMessage(), res.get(m.getMessage()) + 1);
//			} else {
//				res.put(m.getMessage(), 1);
//			}
//		}
//		return res;
//	}
}
