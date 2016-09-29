package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class ConfigHandlerTest {

	@Test
	public void testPresenceOf() {
		final String configSchemaName = "jhoveConfig.xsd";
		InputStream strm = new ConfigHandler().getClass().getResourceAsStream(configSchemaName);
		if(null==strm) {
			fail(configSchemaName+" not found in resources");
		}
	}

}
