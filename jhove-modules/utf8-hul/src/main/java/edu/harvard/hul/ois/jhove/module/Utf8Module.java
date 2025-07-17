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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.AgentType;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.DocumentType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.IdentifierType;
import edu.harvard.hul.ois.jhove.InfoMessage;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.TextMDMetadata;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.module.ascii.ControlChar;
import edu.harvard.hul.ois.jhove.module.ascii.LineEnding;
import edu.harvard.hul.ois.jhove.module.utf8.MessageConstants;
import edu.harvard.hul.ois.jhove.module.utf8.Utf8BlockMarker;

/**
 * Module for analysis of content as a UTF-8 stream.
 */
public class Utf8Module extends ModuleBase {

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/
    private static final String NAME = "UTF8-hul";
    private static final String RELEASE = "1.7.5";
    private static final int[] DATE = { 2025, 06, 25 };
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

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    protected Set<ControlChar> usedCtrlChars;
    protected Set<LineEnding> usedLineEndings;
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

        this._vendor = Agent.harvardInstance();

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
        this._specification.add(doc);

        doc = new Document("Information technology -- Universal "
                + "Multiple-Octet Coded Character Set (UCS) -- "
                + "Part 1: Architecture and Basic Multilingual "
                + "Plane. Appendix R, Amendment 2", DocumentType.STANDARD);
        doc.setPublisher(Agent.newIsoInstance());
        doc.setDate("1991");
        doc.setIdentifier(new Identifier("ISO/IEC 10646-1 Amendment 2",
                IdentifierType.ISO));
        this._specification.add(doc);

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
        this._specification.add(doc);

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
     *                   An InputStream, positioned at its beginning, which is
     *                   generated from the object to be parsed. If multiple calls
     *                   to
     *                   <code>parse</code> are made on the basis of a nonzero value
     *                   being returned, a new InputStream must be provided each
     *                   time.
     *
     * @param info
     *                   A fresh (on the first call) RepInfo object which will be
     *                   modified to reflect the results of the parsing If multiple
     *                   calls to <code>parse</code> are made on the basis of a
     *                   nonzero
     *                   value being returned, the same RepInfo object should be
     *                   passed
     *                   with each call.
     *
     * @param parseIndex
     *                   Must be 0 in first call to <code>parse</code>. If
     *                   <code>parse</code> returns a nonzero value, it must be
     *                   called
     *                   again with <code>parseIndex</code> equal to that return
     *                   value.
     *
     */
    @Override
    public final int parse(InputStream stream, RepInfo info, int parseIndex)
            throws IOException {
        // Test if textMD is to be generated
        _withTextMD = isParamInDefaults("withtextmd=true");

        initParse();
        initInfo(info);
        this.initialBytes = new int[4];

        // No line end types have been discovered.
        ControlChar prevChar = null;
        this.usedCtrlChars = new HashSet<>();
        this.usedLineEndings = new HashSet<>();
        this._textMD = new TextMDMetadata();

        boolean printableChars = false;

        // TODO: Why here and not ASCII
        info.setNote("Additional representation information includes "
                + "the line endings: CR, LF, or CRLF");
        this._nByte = 0;
        long nChar = 0;

        // Setup the data stream, will determine if we use checksum stream
        setupDataStream(stream, info);
        this.blockMarker = new Utf8BlockMarker();

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

                b[0] = readUnsignedByte(this._dstream, this);
                if (this._nByte < 4) {
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
                    ErrorMessage error = new ErrorMessage(MessageConstants.UTF8_HUL_2,
                            "Value = " + ((char) b[0]) + " (0x"
                                    + Integer.toHexString(b[0]) + ")",
                            this._nByte);
                    info.setMessage(error);
                    info.setWellFormed(false);
                    return 0;
                }

                for (int i = 1; i < nBytes; i++) {
                    b[i] = readUnsignedByte(this._dstream, this);
                    if (this._nByte < 4) {
                        isMark = checkMark(b[i], info);
                    }
                    if (info.getWellFormed() == RepInfo.FALSE) {
                        return 0;
                    }

                    if (0x80 > b[i] || b[i] > 0xbf) {
                        String subMessage = "Value = " + ((char) b[i]) + " (0x" + Integer.toHexString(b[i]) + ")";
                        JhoveMessage errMessage = null;
                        switch (i) { // max(nBytes) is 4
                            case 1:
                                errMessage = MessageConstants.UTF8_HUL_3;
                                break;
                            case 2:
                                errMessage = MessageConstants.UTF8_HUL_4;
                                break;
                            case 3:
                                errMessage = MessageConstants.UTF8_HUL_5;
                                break;
                            default:
                                break;
                        }
                        ErrorMessage error = new ErrorMessage(errMessage, subMessage, this._nByte);
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
                    this.blockMarker.markBlock(ch);
                }

                /* Character values U+000..U+001f,U+007f aren't printable. */
                /* Only byte values 0x20 through 0x7e are printable. */
                if (!printableChars) {
                    printableChars = (ch > 0x001f && ch != 0x7f);
                }

                /* Determine the line ending type(s). */
                ControlChar ctrlChar = ControlChar.asciiFromInt(ch);
                if (ControlChar.isLineEndChar(ctrlChar)) {
                    // Carry out the line endings test
                    LineEnding le = LineEnding.fromControlChars(ctrlChar, prevChar);
                    if (le != null)
                        this.usedLineEndings.add(le);
                } else if (ctrlChar != null) {
                    // The passed char is a control char and not a line ending
                    this.usedCtrlChars.add(ctrlChar);
                } else if (!printableChars) {
                    // Only byte values 0x20 through 0x7e are printable.
                    printableChars = (0x001f < ch);
                }
                if (prevChar == ControlChar.CR && ctrlChar != ControlChar.LF) {
                    // Carry out the line endings test
                    LineEnding le = LineEnding.fromControlChars(ctrlChar, prevChar);
                    if (le != null)
                        this.usedLineEndings.add(le);
                }
                prevChar = ctrlChar;

                nChar++;
            } catch (EOFException e) {
                eof = true;
                /* Catch line endings at very end. */
                LineEnding le = LineEnding.fromControlChars(ControlChar.NUL, prevChar);
                if (le != null)
                    this.usedLineEndings.add(le);
            }
        }

        /* Object is well-formed UTF-8. */

        // Set the checksums in the report if they're calculated
        setChecksums(this._ckSummer, info);

        /*
         * Only non-zero-length files are well-formed UTF-8.
         */
        if (this._nByte == 0) {
            info.setMessage(new ErrorMessage(MessageConstants.UTF8_HUL_6));
            info.setWellFormed(RepInfo.FALSE);
            return 0;
        }

        /* Add the textMD information */
        this._textMD.setCharset(TextMDMetadata.CHARSET_UTF8);
        this._textMD.setByte_order(this._bigEndian ? TextMDMetadata.BYTE_ORDER_BIG
                : TextMDMetadata.BYTE_ORDER_LITTLE);
        this._textMD.setByte_size("8");
        this._textMD.setCharacter_size("variable");

        /*
         * Create a metadata property for the module-specific info. (4-Feb-04)
         */
        List<Property> metadataList = new ArrayList<>(4);
        info.setProperty(new Property("UTF8Metadata", PropertyType.PROPERTY,
                PropertyArity.LIST, metadataList));

        Property property = new Property("Characters", PropertyType.LONG,
                new Long(nChar));
        metadataList.add(property);

        property = this.blockMarker.getBlocksUsedProperty("UnicodeBlocks");
        if (property != null) {
            metadataList.add(property);
        }

        /* Set property reporting line ending type */
        List<String> propArray = reportLineEndings();
        if (!propArray.isEmpty()) {
            property = new Property(LineEnding.PROP_NAME,
                    PropertyType.STRING, PropertyArity.LIST, propArray);
            metadataList.add(property);
        }
        /* Set property reporting control characters used */
        if (!this.usedCtrlChars.isEmpty()) {
            LinkedList<String> propList = new LinkedList<>();
            for (ControlChar ctrlChar : EnumSet.copyOf(this.usedCtrlChars)) {
                propList.add(ctrlChar.mnemonic);
            }
            property = new Property(ControlChar.PROP_NAME,
                    PropertyType.STRING, PropertyArity.LIST, propList);
            metadataList.add(property);
        }

        if (this._withTextMD) {
            property = new Property("TextMDMetadata",
                    PropertyType.TEXTMDMETADATA, PropertyArity.SCALAR, this._textMD);
            metadataList.add(property);
        }

        if (!printableChars) {
            info.setMessage(new InfoMessage(MessageConstants.UTF8_HUL_10));
        }

        return 0;
    }

    /**
     * Check if the digital object conforms to this Module's internal signature
     * information. Try to read the BOM if it's present, and check the beginning
     * of the file.
     *
     * @param file
     *               A File object for the object being parsed
     * @param stream
     *               An InputStream, positioned at its beginning, which is
     *               generated from the object to be parsed
     * @param info
     *               A fresh RepInfo object which will be modified to reflect the
     *               results of the test
     */
    @Override
    public void checkSignatures(File file, InputStream stream, RepInfo info)
            throws IOException {
        info.setFormat(this._format[0]);
        info.setMimeType(this._mimeType[0]);
        info.setModule(this);
        this.initialBytes = new int[4];
        JhoveBase jb = getBase();
        int sigBytes = jb.getSigBytes();
        int bytesRead = 0;
        this.blockMarker = new Utf8BlockMarker();
        boolean eof = false;
        this._nByte = 0;
        DataInputStream dstream = new DataInputStream(stream);
        while (!eof && bytesRead < sigBytes) {
            int[] b = new int[4];
            try {
                b[0] = readUnsignedByte(dstream, this);
                ++bytesRead;
                if (this._nByte < 4) {
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
                    if (this._nByte < 4) {
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
            info.setSigMatch(this._name);
        } else {
            // Don't match an empty file
            info.setWellFormed(false);
        }
    }

    /******************************************************************
     * PRIVATE INSTANCE METHODS.
     ******************************************************************/

    protected boolean checkMark(int byt, RepInfo info) {
        ErrorMessage msg;
        this.initialBytes[(int) this._nByte - 1] = byt;
        if (this._nByte == 3) {
            // Check for UTF-8 byte order mark in 1st 3 bytes
            if (this.initialBytes[0] == 0xEF && this.initialBytes[1] == 0xBB
                    && this.initialBytes[2] == 0xBF) {
                InfoMessage im = new InfoMessage(MessageConstants.UTF8_HUL_1, 0); // UTF8-HUL-1
                info.setMessage(im);
                // If we've found a non-character header, clear
                // all usage blocks
                this.blockMarker.reset();
                return true;
            }

            if (this.initialBytes[0] == 0xFF && this.initialBytes[1] == 0xFE) {
                if (this.initialBytes[2] == 0 && this.initialBytes[3] == 0) {
                    msg = new ErrorMessage(MessageConstants.UTF8_HUL_7);
                } else {
                    msg = new ErrorMessage(MessageConstants.UTF8_HUL_8);
                }
                info.setMessage(msg);
                info.setWellFormed(false);
                return false;
            } else if (this.initialBytes[0] == 0xFE && this.initialBytes[1] == 0xFF) {
                msg = new ErrorMessage(MessageConstants.UTF8_HUL_9);
                info.setMessage(msg);
                info.setWellFormed(false);
                return false;
            }
        }
        return false;
    }

    /* Set property reporting line ending type */
    private List<String> reportLineEndings() {
        List<String> retVal = new ArrayList<>();
        if (!this.usedLineEndings.isEmpty()) {
            for (LineEnding le : EnumSet.copyOf(this.usedLineEndings)) {
                retVal.add(le.toString());
                this._textMD.setLinebreak(le.textMdVal);
            }
        }
        return retVal;
    }
}
