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
import edu.harvard.hul.ois.jhove.module.ascii.ControlChar;
import edu.harvard.hul.ois.jhove.module.ascii.LineEnding;
import edu.harvard.hul.ois.jhove.module.ascii.MessageConstants;

import java.io.*;
import java.util.*;

/**
 * Module for analysis of content as an ASCII stream.
 */
public class AsciiModule extends ModuleBase {
    public final static int MAX_CHAR = 0x7f;
    public final static int MIN_PRINTABLE = 0x20;
    public final static int MAX_PRINTABLE = 0x7e;

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/
    private static final String NAME = "ASCII-hul";
    private static final String RELEASE = "1.4.2";
    private static final int[] DATE = { 2022, 04, 22 };
    private static final String[] FORMAT = { "ASCII", "US-ASCII", "ANSI X3.4",
            "ISO 646" };
    private static final String COVERAGE = null;
    private static final String[] MIMETYPE = { "text/plain; charset=US-ASCII" };
    private static final String WELLFORMED = "An ASCII object is well-formed "
            + "if each byte is between 0x00 and 0x7F";
    private static final String VALIDITY = null;
    private static final String REPINFO = "Additional representation information includes: line ending and control characters";
    private static final String NOTE = null;
    private static final String RIGHTS = "Copyright 2003-2015 by JSTOR and "
            + "the President and Fellows of Harvard College. "
            + "Copyright 2015-2019 by the Open Preservation Foundation. "
            + "Version 1.4 onwards developed by Open Preservation Foundation. "
            + "Released under the GNU Lesser General Public License.";

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/
    protected Set<ControlChar> usedCtrlChars;
    protected Set<LineEnding> usedLineEndings;

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
        _withTextMD = isParamInDefaults("withtextmd=true");

        initParse();
        initInfo(info);

        ControlChar prevChar = null;
        usedCtrlChars = new HashSet<>();
        usedLineEndings = new HashSet<>();
        _textMD = new TextMDMetadata();

        boolean printableChars = false;

        // Setup the data stream, will determine if we use checksum stream
        setupDataStream(stream, info);

        boolean eof = false;
        _nByte = 0;
        while (!eof) {
            try {
                int ch = readUnsignedByte(_dstream, this);

                /* Only byte values 0x00 through 0x7f are valid. */
                if (ch > MAX_CHAR) {
                    ErrorMessage error = new ErrorMessage(MessageConstants.ASCII_HUL_1,
                            String.format(MessageConstants.ASCII_HUL_1_SUB.getMessage(),
                                    Character.valueOf((char) ch),
                                    Integer.valueOf(ch)),
                            _nByte - 1);
                    info.setMessage(error);
                    info.setWellFormed(RepInfo.FALSE);
                    return 0;
                }
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
                    printableChars = (MIN_PRINTABLE <= ch && ch <= MAX_PRINTABLE);
                }
                if (prevChar == ControlChar.CR && ctrlChar != ControlChar.LF) {
                    // Carry out the line endings test
                    LineEnding le = LineEnding.fromControlChars(ctrlChar, prevChar);
                    if (le != null)
                        this.usedLineEndings.add(le);
                }
                prevChar = ctrlChar;
            } catch (EOFException e) {
                eof = true;
                /* Catch line endings at very end. */
                LineEnding le = LineEnding.fromControlChars(ControlChar.NUL, prevChar);
                if (le != null)
                    usedLineEndings.add(le);
            }
        }

        /* The object is well-formed ASCII. */

        // Set the checksums in the report if they're calculated
        setChecksums(this._ckSummer, info);

        /*
         * Only non-zero-length files are well-formed ASCII.
         */
        if (_nByte == 0) {
            info.setMessage(new ErrorMessage(MessageConstants.ASCII_HUL_2)); // ASCII-HUL-2
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
        List<Property> metadataList = new ArrayList<>(2);

        /* Set property reporting line ending type */
        List<String> propArray = reportLineEndings();
        if (!propArray.isEmpty()) {
            Property property = new Property(LineEnding.PROP_NAME,
                    PropertyType.STRING, PropertyArity.LIST, propArray);
            metadataList.add(property);
        }
        /* Set property reporting control characters used */
        if (!this.usedCtrlChars.isEmpty()) {
            LinkedList<String> propList = new LinkedList<>();
            for (ControlChar ctrlChar : EnumSet.copyOf(this.usedCtrlChars)) {
                propList.add(ctrlChar.mnemonic);
            }
            Property property = new Property(ControlChar.PROP_NAME,
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
            info.setMessage(new InfoMessage(MessageConstants.ASCII_HUL_3));
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
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);
        info.setModule(this);
        int sigBytes = getBase().getSigBytes();
        int bytesRead = 0;
        boolean eof = false;
        DataInputStream dstream = new DataInputStream(stream);
        while (!eof && bytesRead < sigBytes) {
            try {
                int ch = readUnsignedByte(dstream, this);
                ++bytesRead;

                /* Only byte values 0x00 through 0x7f are valid. */

                if (ch > MAX_CHAR) {
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

    /* Set property reporting line ending type */
    private List<String> reportLineEndings() {
        List<String> retVal = new ArrayList<>();
        if (!this.usedLineEndings.isEmpty()) {
            for (LineEnding le : EnumSet.copyOf(this.usedLineEndings)) {
                retVal.add(le.toString());
                _textMD.setLinebreak(le.textMdVal);
            }
        }
        return retVal;
    }
}
