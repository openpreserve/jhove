package edu.harvard.hul.ois.jhove.module.utf8;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.TestUtils;
import edu.harvard.hul.ois.jhove.module.Utf8Module;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 */

public class Utf8ByteOrderTests {
	private static final String utf8ResourcePath = "/edu/harvard/hul/ois/jhove/module/utf8/";

	private static final String utf8BomTest = utf8ResourcePath
			+ "bom-test.txt";
	private static final String utf8NoBomTest = utf8ResourcePath
			+ "no-bom-test.txt";
	private Utf8Module module;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.module = new Utf8Module();
		JhoveBase je = new JhoveBase();
		this.module.setBase(je);
	}

	@Test
	public final void testBom() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, utf8BomTest, RepInfo.TRUE,
				RepInfo.TRUE, MessageConstants.UTF8_HUL_1.getMessage(), true);
	}

	@Test
	public final void testNoBom() throws URISyntaxException {
		TestUtils.testValidateResource(this.module, utf8NoBomTest, RepInfo.TRUE,
				RepInfo.TRUE, MessageConstants.UTF8_HUL_1.getMessage(), false);
	}

}
