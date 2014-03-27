package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ChecksumInputStreamTest {

	final static String RESDIR_GOVDOCS = "src/test/resources/govdocs/jpg/";
	
	private static Checksummer initChecksummer(File pFile) {
		try {
			Checksummer c = new Checksummer();
			ChecksumInputStream cis = new ChecksumInputStream(new FileInputStream(pFile), c);
			while(cis.available()>0) {
				cis.read();
			}
			cis.close();
			return cis.getChecksummer();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void testChecksumPass_1() {
		File input = new File(RESDIR_GOVDOCS+"000245.jpg");
		Checksummer c = initChecksummer(input);
		if(!c.getCRC32().equals("698b750e")) fail("CRC32 checksums don't match: "+c.getCRC32());
		if(!c.getMD5().equals("23bee9ce971fe4268ecd15816d61c3d9")) fail("MD5 checksums don't match: "+c.getMD5());
		if(!c.getSHA1().equals("653b1360dbf06c56d18088720044a485f5d180d1")) fail("SHA1 checksums don't match: "+c.getSHA1());
	}
	
	@Test
	public void testChecksumPass_2() {
		File input = new File(RESDIR_GOVDOCS+"000515.jpg");
		Checksummer c = initChecksummer(input);
		if(!c.getCRC32().equals("360a48f3")) fail("CRC32 checksums don't match: "+c.getCRC32());
		if(!c.getMD5().equals("a6b154498868a0570faccd8a9dbb7425")) fail("MD5 checksums don't match: "+c.getMD5());
		if(!c.getSHA1().equals("1d3a7ad729a39eba80277ad132564c2aa35572f4")) fail("SHA1 checksums don't match: "+c.getSHA1());
	}

}
