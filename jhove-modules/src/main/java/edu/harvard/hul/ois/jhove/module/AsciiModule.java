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
import edu.harvard.hul.ois.jhove.Agent.Builder;

import java.io.*;
import java.util.*;

/**
 * Module for analysis of content as an ASCII stream.
 */
public class AsciiModule extends ModuleBase {

    public final static String ERR_CHAR_INV = "Invalid character";
    public final static String INF_PRINT_CHAR_MISS = "No printable characters";

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/
    private static final String NAME = "ASCII-hul";
    private static final String RELEASE = "1.3";
    private static final int[] DATE = { 2006, 9, 5 };
    private static final String[] FORMAT = { "ASCII", "US-ASCII", "ANSI X3.4",
            "ISO 646" };
    private static final String COVERAGE = null;
    private static final String[] MIMETYPE = { "text/plain; charset=US-ASCII" };
    private static final String WELLFORMED = "An ASCII object is well-formed "
            + "if each byte is between 0x00 and 0x7F";
    private static final String VALIDITY = null;
    private static final String REPINFO = "Additional representation information includes: line ending and control characters";
    private static final String NOTE = null;
    private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and "
            + "the President and Fellows of Harvard College. "
            + "Released under the GNU Lesser General Public License.";

    private static final int CR = 0x0d; // '\r'
    private static final int LF = 0x0a; // '\n'

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

    /* Flag to know if the property TextMDMetadata is to be added */
    protected boolean _withTextMD = false;
    /* Hold the information needed to generate a textMD metadata fragment */
    protected TextMDMetadata _textMD;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Creates an AsciiModule.
     */
    public AsciiModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);

        _vendor = Agent.harvardInstance();

        Document doc = new Document("Information technology -- ISO 7-bit "
                + "coded character set for information " + "interchange",
                DocumentType.STANDARD);
        doc.setPublisher(Agent.newIsoInstance());
        doc.setDate("1991");
        doc.setIdentifier(new Identifier("ISO/IEC 646:1991", IdentifierType.ISO));
        _specification.add(doc);

        doc = new Document("Information Systems -- Coded Character Sets "
                + "7-Bit American National Standard Code for "
                + "Information Interchange (7-Bit ASCII)",
                DocumentType.STANDARD);
        Builder builder = new Agent.Builder("ANSI", AgentType.STANDARD)
                .address("1819 L Street, NW, Washington, DC 20036")
                .telephone("+1 (202) 293-8020").fax("+1 (202) 293-9287")
                .email("info@ansi.org").web("http://www.ansi.org/");
        doc.setPublisher(builder.build());
        doc.setDate("1986-12-30");
        doc.setIdentifier(new Identifier("ANSI X3.4-1986", IdentifierType.ANSI));
        _specification.add(doc);

        doc = new Document("7-Bit coded Character Set", DocumentType.STANDARD);
        doc.setEdition("6th");
        doc.setDate("1991-12");
        builder = new Agent.Builder("ECMA", AgentType.STANDARD)
                .address("114 Rue du Rhone, CH-1204 Geneva, Switzerland")
                .telephone("+41 22 849.60.00").fax("+41 22 849.60.01")
                .email("helpdesk@ecma.ch")
                .web("http://www.ecma-international.org/");
        doc.setPublisher(builder.build());
        doc.setIdentifier(new Identifier("ECMA-6", IdentifierType.ECMA));
        doc.setIdentifier(new Identifier("http://www.ecma-international."
                + "org/publications/files/ecma-st/" + "Ecma-006.pdf",
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
     */
    @Override
    public final int parse(InputStream stream, RepInfo info, int parseIndex)
            throws IOException {
        // Test if textMD is to be generated
        if (_defaultParams != null) {
            Iterator iter = _defaultParams.iterator();
            while (iter.hasNext()) {
                String param = (String) iter.next();
                if ("withtextmd=true".equalsIgnoreCase(param)) {
                    _withTextMD = true;
                }
            }
        }

        initParse();
        info.setModule(this);

        // No line end types have been discovered.
        _lineEndCR = false;
        _lineEndLF = false;
        _lineEndCRLF = false;
        _prevChar = 0;
        _controlCharMap = new HashMap<Integer, String>();
        _textMD = new TextMDMetadata();

        boolean printableChars = false;

        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);

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
        boolean eof = false;
        _nByte = 0;
        while (!eof) {
            try {
                int ch = readUnsignedByte(_dstream, this);

                /* Only byte values 0x00 through 0x7f are valid. */

                if (ch > 0x7f) {
                    ErrorMessage error = new ErrorMessage("Invalid character",
                            "Character = " + ((char) ch) + " (0x"
                                    + Integer.toHexString(ch) + ")", _nByte - 1);
                    info.setMessage(error);
                    info.setWellFormed(RepInfo.FALSE);
                    return 0;
                }
                /* Track what control characters are used. */
                if (ch < 0x20 && ch != 0x0D && ch != 0x0A) {
                    _controlCharMap.put(Integer.valueOf(ch),
                            controlCharMnemonics[ch]);
                } else if (ch == 0x7F) {
                    _controlCharMap.put(Integer.valueOf(ch), "DEL (0x7F)");
                }

                /* Determine the line ending type(s). */
                checkLineEnd(ch);

                /* Only byte values 0x20 through 0x7e are printable. */
                if (0x20 <= ch && ch <= 0x7e) {
                    printableChars = true;
                }

                _prevChar = ch;
            } catch (EOFException e) {
                eof = true;
                /* Catch line endings at very end. */
                checkLineEnd(0);
            }
        }

        /* The object is well-formed ASCII. */

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
         * Only non-zero-length files are well-formed ASCII.
         */
        if (_nByte == 0) {
            info.setMessage(new ErrorMessage(ERR_CHAR_INV));
            info.setWellFormed(RepInfo.FALSE);
            return 0;
        }

        /* Add the textMD information */
        _textMD.setCharset(TextMDMetadata.CHARSET_ASCII);
        _textMD.setByte_order(_bigEndian ? TextMDMetadata.BYTE_ORDER_BIG
                : TextMDMetadata.BYTE_ORDER_LITTLE);
        _textMD.setByte_size("8");
        _textMD.setCharacter_size("1");

        /*
         * Create a metadata property for the module-specific info. (4-Feb-04)
         */
        List<Property> metadataList = new ArrayList<Property>(2);

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
            Property property = new Property("LineEndings",
                    PropertyType.STRING, PropertyArity.LIST, propArray);
            metadataList.add(property);
        }
        /* Set property reporting control characters used */
        if (!_controlCharMap.isEmpty()) {
            LinkedList<String> propList = new LinkedList<String>();
            String mnem;
            for (int i = 0; i < 0x20; i++) {
                mnem = _controlCharMap.get(Integer.valueOf(i));
                if (mnem != null) {
                    propList.add(mnem);
                }
            }
            /* need to check separately for DEL */
            mnem = _controlCharMap.get(Integer.valueOf(0x7F));
            if (mnem != null) {
                propList.add(mnem);
            }
            Property property = new Property("ControlCharacters",
                    PropertyType.STRING, PropertyArity.LIST, propList);
            metadataList.add(property);
        }

        if (_withTextMD) {
            Property property = new Property("TextMDMetadata",
                    PropertyType.TEXTMDMETADATA, PropertyArity.SCALAR, _textMD);
            metadataList.add(property);
        }

        /* Add the ASCII-specific metadata, if it exists. */
        if (!metadataList.isEmpty()) {
            info.setProperty(new Property("ASCIIMetadata",
                    PropertyType.PROPERTY, PropertyArity.LIST, metadataList));
        }

        if (!printableChars) {
            info.setMessage(new InfoMessage(INF_PRINT_CHAR_MISS));
        }

        return 0;
    }

    /**
     * Check if the digital object conforms to this Module's internal signature
     * information. An ASCII file has no "signature," so in cases like this we
     * just check the beginning of the file as a plausible guess. This really
     * proves nothing, since a text file could have a single accented character
     * dozens of kilobytes into it. But oh well.
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
        JhoveBase jb = getBase();
        int sigBytes = jb.getSigBytes();
        int bytesRead = 0;
        boolean eof = false;
        DataInputStream dstream = new DataInputStream(stream);
        while (!eof && bytesRead < sigBytes) {
            try {
                int ch = readUnsignedByte(dstream, this);
                ++bytesRead;

                /* Only byte values 0x00 through 0x7f are valid. */

                if (ch > 0x7f) {
                    info.setWellFormed(false);
                    return;
                }
            } catch (EOFException e) {
                eof = true;
            }
        }
        // Reject an empty file.
        if (bytesRead == 0) {
            info.setWellFormed(false);
            return;
        }
        // Do this only after being sure it's OK, as this property
        // is sticky.
        info.setSigMatch(_name);

    }

    /******************************************************************
     * PRIVATE INSTANCE METHODS.
     ******************************************************************/

    /*
     * Accumulate information about line endings. ch is the current character,
     * and _prevChar the one before it.
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

}
