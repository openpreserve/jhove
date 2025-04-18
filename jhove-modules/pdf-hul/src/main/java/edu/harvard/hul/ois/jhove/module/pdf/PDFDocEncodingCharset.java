package edu.harvard.hul.ois.jhove.module.pdf;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class PDFDocEncodingCharset extends Charset {
    public PDFDocEncodingCharset() {
        super("PDFDocEncoding", new String[] {});
    }

    @Override
    public boolean contains(Charset cs) {
        return cs.name().equals("PDFDocEncoding");
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new CharsetDecoder(this, 1F, 1F) {
            @Override
            protected CoderResult decodeLoop(ByteBuffer from, CharBuffer to) {
                while (from.hasRemaining()) {
                    if (!to.hasRemaining())
                        return CoderResult.OVERFLOW;
                    byte c = from.get();
                    char d = PDFDOCENCODING[c & 0xFF];
                    to.put(d);
                }

                return CoderResult.UNDERFLOW;
            }
        };
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new CharsetEncoder(this, 1F, 1F) {
            @Override
            protected CoderResult encodeLoop(CharBuffer from, ByteBuffer to) {
                while (from.hasRemaining()) {
                    if (!to.hasRemaining())
                        return CoderResult.OVERFLOW;
                    char d = from.get();
                    Byte v = LOOKUP.get(d);
                    if (v == null) {
                        // 'un'consume the character we consumed
                        from.position(from.position() - 1);
                        return CoderResult.unmappableForLength(1);
                    }
                    to.put(v.byteValue());
                }

                return CoderResult.UNDERFLOW;
            }
        };
    }

    /** Mapping between PDFDocEncoding and Unicode code points. */
    public static char[] PDFDOCENCODING = {
            '\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
            '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f',
            '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017',
            '\u02d8', '\u02c7', '\u02c6', '\u02d9', '\u02dd', '\u02db', '\u02da', '\u02dc',
            '\u0020', '\u0021', '\"', '\u0023', '\u0024', '\u0025', '\u0026', '\'',
            '\u0028', '\u0029', '\u002a', '\u002b', '\u002c', '\u002d', '\u002e', '\u002f',
            '\u0030', '\u0031', '\u0032', '\u0033', '\u0034', '\u0035', '\u0036', '\u0037',
            '\u0038', '\u0039', '\u003a', '\u003b', '\u003c', '\u003d', '\u003e', '\u003f',
            '\u0040', '\u0041', '\u0042', '\u0043', '\u0044', '\u0045', '\u0046', '\u0047',
            '\u0048', '\u0049', '\u004a', '\u004b', '\u004c', '\u004d', '\u004e', '\u004f',
            '\u0050', '\u0051', '\u0052', '\u0053', '\u0054', '\u0055', '\u0056', '\u0057',
            '\u0058', '\u0059', '\u005a', '\u005b', '\\', '\u005d', '\u005e', '\u005f',
            '\u0060', '\u0061', '\u0062', '\u0063', '\u0064', '\u0065', '\u0066', '\u0067',
            '\u0068', '\u0069', '\u006a', '\u006b', '\u006c', '\u006d', '\u006e', '\u006f',
            '\u0070', '\u0071', '\u0072', '\u0073', '\u0074', '\u0075', '\u0076', '\u0077',
            '\u0078', '\u0079', '\u007a', '\u007b', '\u007c', '\u007d', '\u007e', '\u007f',
            '\u2022', '\u2020', '\u2021', '\u2026', '\u2003', '\u2002', '\u0192', '\u2044',
            '\u2039', '\u203a', '\u2212', '\u2030', '\u201e', '\u201c', '\u201d', '\u2018',
            '\u2019', '\u201a', '\u2122', '\ufb01', '\ufb02', '\u0141', '\u0152', '\u0160',
            '\u0178', '\u017d', '\u0131', '\u0142', '\u0153', '\u0161', '\u017e', '\u009f',
            '\u20ac', '\u00a1', '\u00a2', '\u00a3', '\u00a4', '\u00a5', '\u00a6', '\u00a7',
            '\u00a8', '\u00a9', '\u00aa', '\u00ab', '\u00ac', '\u00ad', '\u00ae', '\u00af',
            '\u00b0', '\u00b1', '\u00b2', '\u00b3', '\u00b4', '\u00b5', '\u00b6', '\u00b7',
            '\u00b8', '\u00b9', '\u00ba', '\u00bb', '\u00bc', '\u00bd', '\u00be', '\u00bf',
            '\u00c0', '\u00c1', '\u00c2', '\u00c3', '\u00c4', '\u00c5', '\u00c6', '\u00c7',
            '\u00c8', '\u00c9', '\u00ca', '\u00cb', '\u00cc', '\u00cd', '\u00ce', '\u00cf',
            '\u00d0', '\u00d1', '\u00d2', '\u00d3', '\u00d4', '\u00d5', '\u00d6', '\u00d7',
            '\u00d8', '\u00d9', '\u00da', '\u00db', '\u00dc', '\u00dd', '\u00de', '\u00df',
            '\u00e0', '\u00e1', '\u00e2', '\u00e3', '\u00e4', '\u00e5', '\u00e6', '\u00e7',
            '\u00e8', '\u00e9', '\u00ea', '\u00eb', '\u00ec', '\u00ed', '\u00ef', '\u00ef',
            '\u00f0', '\u00f1', '\u00f2', '\u00f3', '\u00f4', '\u00f5', '\u00f6', '\u00f7',
            '\u00f8', '\u00f9', '\u00fa', '\u00fb', '\u00fc', '\u00fd', '\u00fe', '\u00ff'
    };

    private static final Map<Character, Byte> LOOKUP;
    static {
        Map<Character, Byte> map = new HashMap<>();
        for (int i = 0; i < PDFDOCENCODING.length; i++)
            map.put(PDFDOCENCODING[i], (byte) i);
        LOOKUP = Collections.unmodifiableMap(map);
    }
}