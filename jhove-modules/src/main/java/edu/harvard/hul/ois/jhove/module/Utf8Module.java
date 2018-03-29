/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment Copyright 2003-2007 by
 * JSTOR and the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.*;

import java.io.*;
import java.util.*;

/**
 * Module for analysis of content as a UTF-8 stream.
 */
public class Utf8Module extends ModuleBase {

    public final static String INF_PRINT_CHAR_MISS = "No printable characters";

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/
    private static final String NAME = "UTF8-hul";
    private static final String RELEASE = "1.6";
    private static final int[] DATE = { 2014, 7, 18 };
    private static final String[] FORMAT = { "UTF-8" };
    private static final String COVERAGE = "Unicode 7.0.0";
    private static final String[] MIMETYPE = { "text/plain; charset=UTF-8" };
    private static final String WELLFORMED = "An UTF-8 object is well-formed "
            + "if each character is correctly encoded as a one-to-four byte "
            + "sequence, as defined in the specifications";
    private static final String VALIDITY = null;
    private static final String REPINFO = "Additional representation "
            + "information includes: number of characters and Unicode 7.0.0 code "
            + "blocks";
    private static final String NOTE = null;
    private static final String RIGHTS = "Copyright 2003-2011 by JSTOR and "
            + "the President and Fellows of Harvard College. "
            + "Released under the GNU Lesser General Public License.";

    private static final int CR = 0x0d;
    private static final int LF = 0x0a;

    /* Mnemonics for control characters (0-1F) */
    private static final String[] controlCharMnemonics = { "NUL (0x00)",
            "SOH (0x01)", "STX (0x02)", "ETX (0x03)", "EOT (0x04)",
            "ENQ (0x05)", "ACK (0x06)", "BEL (0x07)", "BS (0x08)",
            "TAB (0x09)", "LF (0x0A)", "VT (0x0B)", "FF (0x0C)", "CR (0x0D)",
            "SO (0x0E)", "SI (0x0F)", "DLE (0x10)", "DC1 (0x11)", "DC2 (0x12)",
            "DC3 (0x13)", "DC4 (0x14)", "NAK (0x15)", "SYN (0x16)",
            "ETB (0x17)", "CAN (0x18)", "EM (0x19)", "SUB (0x1A)",
            "ESC (0x1B)", "FS (0x1C)", "GS (0x1D)", "RS (0x1E)", "US (0x1F)" };

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    /* Input stream wrapper which handles checksums */
    protected ChecksumInputStream _cstream;

    /* Data input stream wrapped around _cstream */
    protected DataInputStream _dstream;

    protected boolean _lineEndCR;
    protected boolean _lineEndLF;
    protected boolean _lineEndCRLF;
    protected int _prevChar;
    protected Map<Integer, String> _controlCharMap;
    protected int initialBytes[];
    protected Utf8BlockMarker blockMarker;

    /* Flag to know if the property TextMDMetadata is to be added */
    protected boolean _withTextMD = false;
    /* Hold the information needed to generate a textMD metadata fragment */
    protected TextMDMetadata _textMD;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Creates a Utf8Module.
     */
    public Utf8Module() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);

        _vendor = Agent.harvardInstance();

        Document doc = new Document("The Unicode Standard, Version 6.0",
                DocumentType.BOOK);
        Agent agent = new Agent.Builder("The Unicode Consortium",
                AgentType.NONPROFIT)
                .web("http://www.unicode.org/versions/Unicode7.0.0/")
                .address("Mountain View, California").build();
        doc.setAuthor(agent);
        agent = new Agent.Builder("Addison-Wesley", AgentType.COMMERCIAL).address("Boston, Massachusetts").build();
        doc.setPublisher(agent);
        doc.setDate("2011");
        doc.setIdentifier(new Identifier("978-1-936213-01-6",
                IdentifierType.ISBN));
        _specification.add(doc);

        doc = new Document("Information technology -- Universal "
                + "Multiple-Octet Coded Character Set (UCS) -- "
                + "Part 1: Architecture and Basic Multilingual "
                + "Plane. Appendix R, Amendment 2", DocumentType.STANDARD);
        doc.setPublisher(Agent.newIsoInstance());
        doc.setDate("1991");
        doc.setIdentifier(new Identifier("ISO/IEC 10646-1 Amendment 2",
                IdentifierType.ISO));
        _specification.add(doc);

        doc = new Document("UTF-8, a transformation format of ISO 10646",
                DocumentType.RFC);
        agent = new Agent.Builder("F. Yergeau", AgentType.OTHER).build();
        doc.setAuthor(agent);
        agent = new Agent.Builder("IETF", AgentType.NONPROFIT).web("http://www.ietf.org/").build();
        doc.setPublisher(agent);
        doc.setDate("1998-01");
        doc.setIdentifier(new Identifier("RFC 2279", IdentifierType.RFC));
        doc.setIdentifier(new Identifier("http://www.ietf.org/rfc/rfc2279.txt",
                IdentifierType.URL));
        _specification.add(doc);

    }

    /******************************************************************
     * PUBLIC INSTANCE METHODS.
     *
     * Parsing methods.
     ******************************************************************/

    /**
     * Parse the content of a stream digital object and store the results in
     * RepInfo.
     *
     * @param stream
     *            An InputStream, positioned at its beginning, which is
     *            generated from the object to be parsed. If multiple calls to
     *            <code>parse</code> are made on the basis of a nonzero value
     *            being returned, a new InputStream must be provided each time.
     *
     * @param info
     *            A fresh (on the first call) RepInfo object which will be
     *            modified to reflect the results of the parsing If multiple
     *            calls to <code>parse</code> are made on the basis of a nonzero
     *            value being returned, the same RepInfo object should be passed
     *            with each call.
     *
     * @param parseIndex
     *            Must be 0 in first call to <code>parse</code>. If
     *            <code>parse</code> returns a nonzero value, it must be called
     *            again with <code>parseIndex</code> equal to that return value.
     *
     */
    @Override
    public final int parse(InputStream stream, RepInfo info, int parseIndex)
            throws IOException {
        // Test if textMD is to be generated
        if (_defaultParams != null) {
            Iterator<String> iter = _defaultParams.iterator();
            while (iter.hasNext()) {
                String param = iter.next();
                if ("withtextmd=true".equalsIgnoreCase(param)) {
                    _withTextMD = true;
                }
            }
        }

        initParse();
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        info.setModule(this);
        initialBytes = new int[4];

        // No line end types have been discovered.
        _lineEndCR = false;
        _lineEndLF = false;
        _lineEndCRLF = false;
        _prevChar = 0;
        _controlCharMap = new HashMap<Integer, String>();
        _textMD = new TextMDMetadata();

        boolean printableChars = false;

        info.setNote("Additional representation information includes "
                + "the line endings: CR, LF, or CRLF");
        _nByte = 0;
        long nChar = 0;
        /*
         * We may have already done the checksums while converting a temporary
         * file.
         */
        Checksummer ckSummer = null;
        if (_je != null && _je.getChecksumFlag()
                && info.getChecksum().isEmpty()) {
            ckSummer = new Checksummer();
            _cstream = new ChecksumInputStream(stream, ckSummer);
            _dstream = getBufferedDataStream(_cstream,
                    _je != null ? _je.getBufferSize() : 0);
        } else {
            _dstream = getBufferedDataStream(stream,
                    _je != null ? _je.getBufferSize() : 0);
        }
        blockMarker = new Utf8BlockMarker();

        boolean eof = false;
        while (!eof) {
            try {
                boolean isMark = false;
                int[] b = new int[4];
                int ch = -1;

                /* Byte values must be valid UTF-8 encodings: */
                /* Unicode value Byte 1 Byte 2 Byte 3 Byte 4 */
                /* 000000000xxxxxxx 0xxxxxxx */
                /* 00000yyyyyxxxxxx 110yyyyy 10xxxxxx */
                /* zzzzyyyyyyxxxxxx 1110zzzz 10yyyyyy 10yyyyyy */
                /* uuuuuzzzzyyyyyyxxxxxx 11110uuu 10uuzzzz 10yyyyyy 10xxxxxx */

                b[0] = readUnsignedByte(_dstream, this);
                if (_nByte < 4) {
                    isMark = checkMark(b[0], info);
                    if (info.getWellFormed() == RepInfo.FALSE) {
                        return 0;
                    }
                    if (isMark) {
                        nChar = 0;
                    }
                }

                int nBytes = 1;
                if (0xc0 <= b[0] && b[0] <= 0xdf) {
                    nBytes = 2;
                } else if (0xe0 <= b[0] && b[0] <= 0xef) {
                    nBytes = 3;
                } else if (0xf0 <= b[0] && b[0] <= 0xf7) {
                    nBytes = 4;
                } else if ((0x80 <= b[0] && b[0] <= 0xbf)
                        || (0xf8 <= b[0] && b[0] <= 0xff)) {
                    ErrorMessage error = new ErrorMessage(Utf8MessageConstants.ERR_INVALID_FIRST_BYTE_ENCODING,
                            "Value = " + ((char) b[0]) + " (0x"
                                    + Integer.toHexString(b[0]) + ")", _nByte);
                    info.setMessage(error);
                    info.setWellFormed(false);
                    return 0;
                }

                for (int i = 1; i < nBytes; i++) {
                    b[i] = readUnsignedByte(_dstream, this);
                    if (_nByte < 4) {
                        isMark = checkMark(b[i], info);
                    }
                    if (info.getWellFormed() == RepInfo.FALSE) {
                        return 0;
                    }

                    if (0x80 > b[i] || b[i] > 0xbf) {
                        String subMessage = "Value = " + ((char) b[i]) + " (0x" + Integer.toHexString(b[i]) + ")";
                        String errMessage = "";
                        switch (i) { // max(nBytes) is 4
                            case 1: errMessage = Utf8MessageConstants.ERR_INVALID_SECOND_BYTE_ENCODING; break;
                            case 2: errMessage = Utf8MessageConstants.ERR_INVALID_THIRD_BYTE_ENCODING; break;
                            case 3: errMessage = Utf8MessageConstants.ERR_INVALID_FOURTH_BYTE_ENCODING; break;
                            default: break;
                        }
                        ErrorMessage error = new ErrorMessage(errMessage, subMessage , _nByte);
                        info.setMessage(error);
                        info.setWellFormed(false);
                        return 0;
                    }
                }

                if (nBytes == 1) {
                    ch = b[0];
                } else if (nBytes == 2) {
                    ch = ((b[0] & 0x1f) << 6) + (b[1] & 0x3f);
                } else if (nBytes == 3) {
                    ch = ((b[0] & 0x0f) << 12) + ((b[1] & 0x3f) << 6)
                            + (b[2] & 0x3f);
                } else if (nBytes == 4) {
                    ch = ((b[0] & 0x07) << 18) + ((b[1] & 0x3f) << 12)
                            + ((b[2] & 0x3f) << 6) + (b[3] & 0x3f);
                }

                if (!isMark) {
                    blockMarker.markBlock(ch);
                }

                /* Track what control characters are used. */
                if (ch < 0x20 && ch != 0x0D && ch != 0x0A) {
                    _controlCharMap.put(new Integer(ch),
                            controlCharMnemonics[ch]);
                } else if (ch == 0x7F) {
                    _controlCharMap.put(new Integer(ch), "DEL (0x7F)");
                }

                /* Character values U+000..U+001f,U+007f aren't printable. */
                if (ch > 0x001f && ch != 0x7f) {
                    printableChars = true;
                }

                /* Determine the line ending type(s). */
                checkLineEnd(ch);
                _prevChar = ch;

                nChar++;
            } catch (EOFException e) {
                eof = true;
                /* Catch line endings at very end. */
                checkLineEnd(0);
            }
        }

        /* Object is well-formed UTF-8. */

        if (ckSummer != null) {
            info.setSize(_cstream.getNBytes());
            info.setChecksum(new Checksum(ckSummer.getCRC32(),
                    ChecksumType.CRC32));
            String value = ckSummer.getMD5();
            if (value != null) {
                info.setChecksum(new Checksum(value, ChecksumType.MD5));
            }
            if ((value = ckSummer.getSHA1()) != null) {
                info.setChecksum(new Checksum(value, ChecksumType.SHA1));
            }
        }

        /*
         * Only non-zero-length files are well-formed UTF-8.
         */
        if (_nByte == 0) {
            info.setMessage(new ErrorMessage(Utf8MessageConstants.ERR_ZERO_LENGTH_FILE));
            info.setWellFormed(RepInfo.FALSE);
            return 0;
        }

        /* Add the textMD information */
        _textMD.setCharset(TextMDMetadata.CHARSET_UTF8);
        _textMD.setByte_order(_bigEndian ? TextMDMetadata.BYTE_ORDER_BIG
                : TextMDMetadata.BYTE_ORDER_LITTLE);
        _textMD.setByte_size("8");
        _textMD.setCharacter_size("variable");

        /*
         * Create a metadata property for the module-specific info. (4-Feb-04)
         */
        List<Property> metadataList = new ArrayList<Property>(4);
        info.setProperty(new Property("UTF8Metadata", PropertyType.PROPERTY,
                PropertyArity.LIST, metadataList));

        Property property = new Property("Characters", PropertyType.LONG,
                new Long(nChar));
        metadataList.add(property);

        property = blockMarker.getBlocksUsedProperty("UnicodeBlocks");
        if (property != null) {
            metadataList.add(property);
        }

        /* Set property reporting line ending type */
        if (_lineEndCR || _lineEndLF || _lineEndCRLF) {
            ArrayList<String> propArray = new ArrayList<String>(3);
            if (_lineEndCR) {
                propArray.add("CR");
                _textMD.setLinebreak(TextMDMetadata.LINEBREAK_CR);
            }
            if (_lineEndLF) {
                propArray.add("LF");
                _textMD.setLinebreak(TextMDMetadata.LINEBREAK_LF);
            }
            if (_lineEndCRLF) {
                propArray.add("CRLF");
                _textMD.setLinebreak(TextMDMetadata.LINEBREAK_CRLF);
            }
            property = new Property("LineEndings", PropertyType.STRING,
                    PropertyArity.LIST, propArray);
            metadataList.add(property);
        }
        /* Set property reporting control characters used */
        if (!_controlCharMap.isEmpty()) {
            LinkedList<String> propList = new LinkedList<String>();
            String mnem;
            for (int i = 0; i < 0x20; i++) {
                mnem = _controlCharMap.get(new Integer(i));
                if (mnem != null) {
                    propList.add(mnem);
                }
            }
            /* need to check separately for DEL */
            mnem = _controlCharMap.get(new Integer(0x7F));
            if (mnem != null) {
                propList.add(mnem);
            }
            property = new Property("ControlCharacters", PropertyType.STRING,
                    PropertyArity.LIST, propList);
            metadataList.add(property);
        }

        if (_withTextMD) {
            property = new Property("TextMDMetadata",
                    PropertyType.TEXTMDMETADATA, PropertyArity.SCALAR, _textMD);
            metadataList.add(property);
        }

        if (!printableChars) {
            info.setMessage(new InfoMessage(INF_PRINT_CHAR_MISS));
        }

        return 0;
    }

    /**
     * Check if the digital object conforms to this Module's internal signature
     * information. Try to read the BOM if it's present, and check the beginning
     * of the file.
     *
     * @param file
     *            A File object for the object being parsed
     * @param stream
     *            An InputStream, positioned at its beginning, which is
     *            generated from the object to be parsed
     * @param info
     *            A fresh RepInfo object which will be modified to reflect the
     *            results of the test
     */
    @Override
    public void checkSignatures(File file, InputStream stream, RepInfo info)
            throws IOException {
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        info.setModule(this);
        initialBytes = new int[4];
        JhoveBase jb = getBase();
        int sigBytes = jb.getSigBytes();
        int bytesRead = 0;
        blockMarker = new Utf8BlockMarker();
        boolean eof = false;
        _nByte = 0;
        DataInputStream dstream = new DataInputStream(stream);
        while (!eof && bytesRead < sigBytes) {
            int[] b = new int[4];
            try {
                b[0] = readUnsignedByte(dstream, this);
                ++bytesRead;
                if (_nByte < 4) {
                    checkMark(b[0], info);
                    if (info.getWellFormed() == RepInfo.FALSE) {
                        return;
                    }
                }
                int nBytes = 1;
                if (0xc0 <= b[0] && b[0] <= 0xdf) {
                    nBytes = 2;
                } else if (0xe0 <= b[0] && b[0] <= 0xef) {
                    nBytes = 3;
                } else if (0xf0 <= b[0] && b[0] <= 0xf7) {
                    nBytes = 4;
                } else if ((0x80 <= b[0] && b[0] <= 0xbf)
                        || (0xf8 <= b[0] && b[0] <= 0xff)) {
                    info.setWellFormed(false);
                    return;
                }
                for (int i = 1; i < nBytes; i++) {
                    b[i] = readUnsignedByte(dstream, this);
                    if (_nByte < 4) {
                        checkMark(b[i], info);
                    }
                    if (info.getWellFormed() == RepInfo.FALSE) {
                        return;
                    }

                    if (0x80 > b[i] || b[i] > 0xbf) {
                        // Not a valid UTF-8 character
                        info.setWellFormed(false);
                        return;
                    }
                }

            } catch (EOFException e) {
                eof = true;
            }
        }
        if (bytesRead > 0) {
            info.setSigMatch(_name);
        } else {
            // Don't match an empty file
            info.setWellFormed(false);
        }
    }

    /******************************************************************
     * PRIVATE INSTANCE METHODS.
     ******************************************************************/

    /**
     * Accumulate information about line endings.
     *
     * @param ch
     *            Current character
     */
    protected void checkLineEnd(int ch) {
        if (ch == LF) {
            if (_prevChar == CR) {
                _lineEndCRLF = true;
            } else {
                _lineEndLF = true;
            }
        } else if (_prevChar == CR) {
            _lineEndCR = true;
        }
    }

    protected boolean checkMark(int byt, RepInfo info) {
        ErrorMessage msg;
        initialBytes[(int) _nByte - 1] = byt;
        if (_nByte == 3) {
            // Check for UTF-8 byte order mark in 1st 3 bytes
            if (initialBytes[0] == 0xEF && initialBytes[1] == 0xBB
                    && initialBytes[2] == 0xBF) {
                InfoMessage im = new InfoMessage(Utf8MessageConstants.INF_BOM_MARK_PRESENT, 0);
                info.setMessage(im);
                // If we've found a non-character header, clear
                // all usage blocks
                blockMarker.reset();
                return true;
            }

            if (initialBytes[0] == 0xFF && initialBytes[1] == 0xFE) {
                if (initialBytes[2] == 0 && initialBytes[3] == 0) {
                    msg = new ErrorMessage(Utf8MessageConstants.ERR_UCS4_NOT_UTF8);
                } else {
                    msg = new ErrorMessage(Utf8MessageConstants.ERR_UTF16LE_NOT_UTF8);
                }
                info.setMessage(msg);
                info.setWellFormed(false);
                return false;
            } else if (initialBytes[0] == 0xFE && initialBytes[1] == 0xFF) {
                msg = new ErrorMessage(Utf8MessageConstants.ERR_UTF16BE_NOT_UTF8);
                info.setMessage(msg);
                info.setWellFormed(false);
                return false;
            }
        }
        return false;
    }
}
