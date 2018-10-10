package edu.harvard.hul.ois.jhove.module.wave;

import java.util.Objects;

/**
 * This class implements functionality that is removed in java9+ jres
 * -> javax.xml.bind.DatatypeConverter
 */
class HexPrinter {

    private HexPrinter() {}

    public static String printHexBinary(byte[] value) {
        if(Objects.isNull(value)) {
            throw new IllegalArgumentException("value was null!");
        }
        String ret = "";
        for(byte v:value) {
            ret += String.format("%02X", v);
        }
        return ret;
    }
}
