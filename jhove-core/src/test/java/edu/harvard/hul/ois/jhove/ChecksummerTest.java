package edu.harvard.hul.ois.jhove;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

public class ChecksummerTest {

    @Test
    public void testConvertToHex() throws ReflectiveOperationException {
        Checksummer cs = new Checksummer();
        Method method = cs.getClass().getDeclaredMethod("convertToHex", byte[].class);
        method.setAccessible(true);
        String hex = (String) method.invoke(cs, "\nJHOVE \t rocks!".getBytes());
        assertEquals("0a4a484f5645200920726f636b7321", hex);
    }
    
    @Test
    public void testOutputHexString() {
        String hex = Checksummer.outputHexString("\nJHOVE \t rocks!".getBytes());
        assertEquals("0A4A484F5645200920726F636B7321", hex);
    }

}
