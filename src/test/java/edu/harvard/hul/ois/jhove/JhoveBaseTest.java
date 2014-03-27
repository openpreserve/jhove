package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class JhoveBaseTest {

	private static JhoveBase jhove = null;
	
	//FIXME: change to run once only
	@Before
	public void setUp() {
		try {
			jhove = new JhoveBase();
			jhove.init("conf/jhove.conf", JhoveBase.getSaxClassFromProperties());
		} catch (JhoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testGetConfigFile() {
		if(jhove.getConfigFile()!=null){
			if(new File(jhove.getConfigFile()).exists()) {
				return;
			} else {
				fail("Config file does not exist");
			}
		} else {
			fail("Config file is null");
		}
	}

	@Test
	public void testGetHandlerList() {
		if(jhove.getHandlerList()!=null){
			if(jhove.getHandlerList().size()>0) {
				return;
			} else {
				fail("No handlers registered");
			}
		} else {
			fail("getHandlerList() is null");
		}
	}

	@Test
	public void testGetJhoveHome() {
		if(jhove.getJhoveHome()!=null){
			if(new File(jhove.getJhoveHome()).exists()) {
				return;
			} else {
				fail("Jhove home does not exist: "+jhove.getJhoveHome());
			}
		} else {
			fail("Jhove home is null");
		}
	}

	@Test
	public void testGetModuleList() {
		if(jhove.getModuleList()!=null){
			if(jhove.getModuleList().size()>0) {
				return;
			} else {
				fail("No modules registered");
			}
		} else {
			fail("getModuleList() is null");
		}
	}

	@Test
	public void testGetTempDirectory() {
		if(jhove.getTempDirectory()!=null){
			if(new File(jhove.getTempDirectory()).exists()) {
				return;
			} else {
				fail("Temp directory does not exist: "+jhove.getTempDirectory());
			}
		} else {
			fail("Temp directory is null");
		}
	}

	@Test
	public void testNewTempFile() {
		File temp = null;
		try {
			temp = jhove.newTempFile();
			if(temp!=null) {
				if(temp.exists()) {
					if(temp.isFile()) {
						if(temp.canRead()) {
							if(temp.canWrite()) {
								return;
							} else {
								fail("Temp file not writable");							
							}
						} else {
							fail("Temp file not readable");
						}
					} else {
						fail("Temp file not a file");
					}
				} else {
					fail("Temp file already exists");
				}
			} else {
				fail("Temp file is null");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String path = "N/A";
			if(temp!=null) {
				path = temp.getAbsolutePath();
			}
			fail("I/O error creating temp file: "+path+ "; "+e.getMessage());
		}		
	}

}
