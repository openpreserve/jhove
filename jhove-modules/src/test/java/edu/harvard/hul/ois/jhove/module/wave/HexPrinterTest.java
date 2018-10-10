package edu.harvard.hul.ois.jhove.module.wave;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HexPrinterTest {

    @Test
    public void testHexPrinter() {
        assertEquals("DEADBEEF1099", HexPrinter.printHexBinary(makeByteArray(new int[] { 0xDe, 0xAd, 0xBe, 0xEf, 0x10, 0x99 })));
    }

    @Test
    public void testHexPrinterLeadingZeros() {
        assertEquals("00001001", HexPrinter.printHexBinary(makeByteArray(new int[] { 0x00, 0x00, 0x10, 0x01 })));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHexPrinterWhenValIsNull() {
        HexPrinter.printHexBinary(null);
    }

    private byte[] makeByteArray(int[] values) {
        final byte[] bytes = new byte[values.length];
        for(int i=0;i<values.length;i++) {
            bytes[i] = (byte)values[i];
        }
        return bytes;
    }
}
