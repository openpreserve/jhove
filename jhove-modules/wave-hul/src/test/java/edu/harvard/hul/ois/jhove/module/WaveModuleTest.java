package edu.harvard.hul.ois.jhove.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.RepInfo;

public class WaveModuleTest {

	private WaveModule module;

	@Before
	public void setUp() throws Exception {
		module = new WaveModule();
		JhoveBase je = new JhoveBase();
		module.setBase(je);
	}

	@Test
	public void testSample3() throws IOException {
		File f = new File("src/test/resources/wave/sample3.wav");
		RepInfo info = new RepInfo(f.getName());

		// Parse
		module.parse(new FileInputStream(f), info, 0);

		// Check that JHOVE found this was NOT well formed:
		assertEquals("Should not be well formed. ", info.getWellFormed(),
				RepInfo.FALSE);

		// Check that there is a message:
		assertTrue("There should be at least one message. ", info.getMessage()
				.size() > 0);

		// Go though messages looking for the expected error:
		boolean foundEofMessage = false;
		for( Message m : info.getMessage()) {
			// Use ID to be language independent
			if ("WAVE-HUL-3".equals(m.getId())
					&& m.getOffset() == 96) {
				foundEofMessage = true;
			}
			System.out.println("MESSAGE: (" + m.getId() + ") " + m.getMessage() + " "
					+ m.getSubMessage()
					+ " @" + m.getOffset());
		}
		// Fail if the error was not found.
		assertTrue("The message of id=WAVE-HUL-3 ('Unexpected end of file')@96 was not found. ",
				foundEofMessage);
	}

}
