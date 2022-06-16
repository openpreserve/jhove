/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2003-2007 by JSTOR and the President and Fellows of Harvard College
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.Agent.Builder;
import edu.harvard.hul.ois.jhove.AgentType;
import edu.harvard.hul.ois.jhove.ByteArrayXMPSource;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.DocumentType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.ExternalSignature;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.IdentifierType;
import edu.harvard.hul.ois.jhove.InternalSignature;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.Signature;
import edu.harvard.hul.ois.jhove.SignatureType;
import edu.harvard.hul.ois.jhove.SignatureUseType;
import edu.harvard.hul.ois.jhove.XMPHandler;
import edu.harvard.hul.ois.jhove.module.gif.GifStrings;
import edu.harvard.hul.ois.jhove.module.gif.MessageConstants;

/**
 * Module for identification and validation of GIF files.
 *
 * @author Gary McGath
 *
 */
public class GifModule extends ModuleBase {
    /******************************************************************
     * DEBUGGING FIELDS.
     * All debugging fields should be set to false for release code.
     ******************************************************************/

    /* Set to true to allow application identifiers to be case-insensitive. */
    private static final boolean debug_appIdentCaseInsens = false;

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    private static final String NAME = "GIF-hul";
    private static final String RELEASE = "1.4.3";
    private static final int[] DATE = { 2022, 04, 22 };
    private static final String[] FORMAT = { "GIF",
            "Graphics Interchange Format" };
    private static final String COVERAGE = "GIF87a, GIF89a";
    private static final String[] MIMETYPE = { "image/gif" };
    private static final String WELLFORMED = "A GIF file is well-formed if " +
            "it has a header block; a sequence of properly formed control, " +
            "graphic-rendering, and special purpose blocks; and a trailer block";
    private static final String VALIDITY = "A GIF file is valid if " +
            "well-formed, has at most one global color map, and at most one " +
            "graphic control extension preceding an image descriptor or a plain " +
            "text extension";
    private static final String REPINFO = "Additional representation " +
            "information includes: NISO Z39.87 Digital Still Image Technical " +
            "Metadata, and block-specific metadata";
    private static final String NOTE = "'GIF' and 'Graphics Interchange " +
            "Format' are trademarks of " +
            "Compuserve Interactive Services Inc.";
    private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and " +
            "the President and Fellows of Harvard College. " +
            "Released under the GNU Lesser General Public License.";

    /* Block type values */
    private static final int EXT_BLOCK = 0X21,
            APPLICATION_EXT = 0XFF,
            COMMENT_EXT = 0XFE,
            GRAPHIC_CONTROL_EXT = 0XF9,
            IMAGE_DESC = 0X2C,
            PLAIN_TEXT_EXT = 0X01,
            TRAILER = 0X3B;

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    /* First 6 bytes of file */
    protected byte _sig[];

    /* XMP property */
    protected Property _xmpProp;

    /* Flag for presence of global color table */
    protected boolean _globalColorTableFlag;

    /* Size of global color table */
    protected int _globalColorTableSize;

    /*
     * Count of graphic control extensions preceding
     * something to modify
     */
    protected int _gceCounter;

    /* Top-level metadata property */
    protected Property _metadata;

    /* Blocks list property */
    protected List _blocksList;

    /* Total count of graphic and plain text extension blocks */
    protected int _numGraphicBlocks;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/
    /**
     * Instantiate a <tt>GifModule</tt> object.
     */
    public GifModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, false);

        _vendor = Agent.harvardInstance();

        Document doc = new Document("GIF (Graphics Interchange Format): A " +
                "standard defining a mechanism for the " +
                "storage and transmission of raster-" +
                "based graphics information",
                DocumentType.REPORT);
        Builder builder = new Agent.Builder("Compuserve Interactive Services Inc.",
                AgentType.COMMERCIAL).address("5000 Arlington Centre Blvd., Columbus, OS 43220")
                .telephone("(614) 457-8600").web("http://www.compuserve.com/");
        Agent cmpsrvAgent = builder.build();
        doc.setAuthor(cmpsrvAgent);
        doc.setDate("1987-06-15");
        doc.setIdentifier(new Identifier("http://www.w3.org/Graphics/GIF/spec-gif87.txt",
                IdentifierType.URL));
        _specification.add(doc);

        doc = new Document("Graphics Interchange Format",
                DocumentType.REPORT);
        doc.setEdition("Version 89a");
        doc.setAuthor(cmpsrvAgent);
        doc.setDate("1987-06-15");
        doc.setIdentifier(new Identifier("http://www.w3.org/Graphics/GIF/spec-gif89a.txt",
                IdentifierType.URL));
        _specification.add(doc);

        Signature sig = new InternalSignature("GIF", SignatureType.MAGIC,
                SignatureUseType.MANDATORY, 0);
        _signature.add(sig);
        sig = new InternalSignature("87a", SignatureType.MAGIC,
                SignatureUseType.MANDATORY_IF_APPLICABLE,
                3, "For version 87a");
        _signature.add(sig);
        sig = new InternalSignature("89a", SignatureType.MAGIC,
                SignatureUseType.MANDATORY_IF_APPLICABLE,
                3, "For version 89a");
        _signature.add(sig);

        sig = new ExternalSignature(".gif", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL);
        _signature.add(sig);

        _bigEndian = false;
    }

    /******************************************************************
     * Parsing methods.
     ******************************************************************/

    /**
     * Check if the digital object conforms to this Module's
     * internal signature information.
     *
     * @param file   A File object for the object being parsed
     * @param stream An InputStream, positioned at its beginning,
     *               which is generated from the object to be parsed
     * @param info   A fresh RepInfo object which will be modified
     *               to reflect the results of the test
     */
    @Override
    public void checkSignatures(File file,
            InputStream stream,
            RepInfo info) {
        int[] sigBytes = { 'G', 'I', 'F', '8', '*', 'a' };
        int i;
        int ch;
        try {
            _dstream = null;
            _dstream = getBufferedDataStream(stream, _je != null ? _je.getBufferSize() : 0);
            for (i = 0; i < 4; i++) {
                ch = readUnsignedByte(_dstream, this);
                if (ch != sigBytes[i]) {
                    info.setWellFormed(false);
                    return;
                }
            }
            /* Byte 4 can be either 7 or 9 */
            ch = readUnsignedByte(_dstream, this);
            if (ch != '7' && ch != '9') {
                info.setWellFormed(false);
                return;
            }
            ch = readUnsignedByte(_dstream, this);
            if (ch != sigBytes[5]) {
                info.setWellFormed(false);
                return;
            }
            info.setModule(this);
            info.setFormat(_format[0]);
            info.setMimeType(_mimeType[0]);
            info.setSigMatch(_name);
        } catch (Exception e) {
            // Reading a very short file may take us here.
            info.setWellFormed(false);
            return;
        }
    }

    /**
     * Parse the content of a purported GIF stream digital object and store the
     * results in RepInfo.
     *
     * @param stream     An InputStream, positioned at its beginning,
     *                   which is generated from the object to be parsed
     * @param info       A fresh RepInfo object which will be modified
     *                   to reflect the results of the parsing
     * @param parseIndex Must be 0 in first call to <code>parse</code>. If
     *                   <code>parse</code> returns a nonzero value, it must be
     *                   called again with <code>parseIndex</code>
     *                   equal to that return value.
     */
    @Override
    public int parse(InputStream stream, RepInfo info, int parseIndex)
            throws IOException {
        initParse();
        info.setModule(this);
        info.setFormat(_format[0]);
        info.setMimeType(_mimeType[0]);

        _blocksList = new LinkedList();

        Property _blocks = new Property("Blocks",
                PropertyType.PROPERTY,
                PropertyArity.LIST,
                _blocksList);

        setupDataStream(stream, info);

        if (!readSig(info)) {
            return 0;
        }

        /* If we got this far, take note that the signature is OK. */
        info.setSigMatch(_name);

        if (!readLSD(info)) {
            return 0;
        }

        boolean moreToCome = true;
        while (moreToCome) {
            moreToCome = readBlock(info);
            if (info.getWellFormed() == RepInfo.FALSE) {
                return 0;
            }
        }

        if (_ckSummer != null) {
            skipDstreamToEnd(info);
            // Set the checksums in the report if they're calculated
            setChecksums(this._ckSummer, info);
        }
        Property[] metaArray;
        if (_xmpProp != null) {
            // Making this an array rather than a list is a pain, but it's policy
            metaArray = new Property[3];
        } else {
            metaArray = new Property[2];
        }
        _metadata = new Property("GIFMetadata",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                metaArray);
        metaArray[1] = _blocks; // this comes after GraphicRenderingBlocks,
                                // which we can't calculate yet
        metaArray[0] = new Property("GraphicRenderingBlocks",
                PropertyType.INTEGER,
                new Integer(_numGraphicBlocks));
        if (_xmpProp != null) {
            metaArray[2] = _xmpProp;
        }
        info.setProperty(_metadata);
        return 0;
    }

    /**
     * Initializes the state of the module for parsing.
     */
    @Override
    protected void initParse() {
        super.initParse();
        _sig = new byte[6];
        _globalColorTableFlag = false;
        _globalColorTableSize = 0;
        _gceCounter = 0;
        _numGraphicBlocks = 0;
    }

    /* Read the 6-byte signature. */
    protected boolean readSig(RepInfo info) throws IOException {
        int nbyt = 0;
        while (nbyt < 6) {
            try {
                int ch = readUnsignedByte(_dstream, this);
                if (nbyt < 6) {
                    _sig[nbyt] = (byte) ch;
                }
                nbyt++;
                // if (_ckSummer != null) {
                // _ckSummer.update (ch);
                // }
            } catch (EOFException e) {
                info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_1, 0));
                info.setWellFormed(RepInfo.FALSE);
                return false;
            }
        }
        String sigStr = new String(_sig);
        if ("GIF89a".equals(sigStr)) {
            info.setVersion("89a");
            info.setProfile("GIF 89a");
        } else if ("GIF87a".equals(sigStr)) {
            info.setVersion("87a");
            info.setProfile("GIF 87a");
        } else {
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_4, 0));
            info.setWellFormed(RepInfo.FALSE);
            return false;
        }
        return true;
    }

    /* Read the Logical Screen Descriptor. */
    protected boolean readLSD(RepInfo info) throws IOException {
        Vector propVec = new Vector(8);
        /* GIF data is always little-endian. */
        int width = readUnsignedShort(_dstream);
        propVec.add(new Property("LogicalScreenWidth",
                PropertyType.INTEGER,
                new Integer(width)));
        int height = readUnsignedShort(_dstream);
        propVec.add(new Property("LogicalScreenHeight",
                PropertyType.INTEGER,
                new Integer(height)));
        int packedFields = readUnsignedByte(_dstream, this);
        _globalColorTableFlag = (packedFields & 0X80) != 0;
        int bitsPerColor = ((packedFields & 0X70) >> 4) + 1;
        propVec.add(new Property("ColorResolution",
                PropertyType.INTEGER,
                new Integer(bitsPerColor)));
        boolean sortFlag = (packedFields & 0X8) != 0;
        int rawGlobalColorTableSize = packedFields & 0X7;
        if (_globalColorTableFlag) {
            _globalColorTableSize = 3 *
                    (1 << (rawGlobalColorTableSize + 1));
        }

        int bgColorIndex = readUnsignedByte(_dstream, this);
        propVec.add(new Property("BackgroundColorIndex",
                PropertyType.INTEGER,
                new Integer(bgColorIndex)));
        int pixAspectRatio = readUnsignedByte(_dstream, this);
        // The pixel aspect ratio is turned into a real aspect
        // ratio by a formula, but we just report the raw number.
        propVec.add(new Property("PixelAspectRatio",
                PropertyType.SHORT,
                new Short((short) pixAspectRatio)));
        propVec.add(addByteProperty("GlobalColorTableFlag",
                _globalColorTableFlag ? 1 : 0,
                GifStrings.GLOBAL_COLOR_TABLE_FLAG));
        propVec.add(addByteProperty("GlobalColorTableSortFlag",
                sortFlag ? 1 : 0,
                GifStrings.COLOR_TABLE_SORT_FLAG));
        propVec.add(new Property("GlobalColorTableSize",
                PropertyType.SHORT,
                new Short((short) rawGlobalColorTableSize)));
        Property prop = new Property("LogicalScreenDescriptor",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                vectorToPropArray(propVec));
        _blocksList.add(prop);

        // Make a property with the global color table, if present
        if (_globalColorTableFlag) {
            short[] gctArray = new short[_globalColorTableSize];
            for (int i = 0; i < _globalColorTableSize; i++) {
                gctArray[i] = (short) readUnsignedByte(_dstream, this);
            }
            _blocksList.add(new Property("GlobalColorTable",
                    PropertyType.SHORT,
                    PropertyArity.ARRAY,
                    gctArray));
        }
        return true;
    }

    /*
     * Read Graphic blocks, Special blocks, and the trailer.
     * Return false if we get an error that prevents further
     * progress, or if we encounter the Trailer.
     */
    protected boolean readBlock(RepInfo info) throws IOException {
        int type;
        try {
            type = readUnsignedByte(_dstream, this);
        } catch (EOFException e) {
            // The spec isn't fully clear on whether a trailer is
            // required, but seems to imply it is.
            info.setWellFormed(RepInfo.FALSE);
            info.setMessage(new ErrorMessage(
                    MessageConstants.GIF_HUL_9, _nByte));
            return false;
        }
        try {
            switch (type) {
                case EXT_BLOCK:
                    return readExtBlock(info);
                case IMAGE_DESC:
                    return readImage(info);
                case TRAILER:
                    return false; // end of file
                default:
                    info.setWellFormed(RepInfo.FALSE);
                    info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_2,
                            "Type = " + type, _nByte));
                    return false;
            }
        } catch (EOFException e) {
            // An EOF in the middle of a block is definitely a problem
            info.setWellFormed(RepInfo.FALSE);
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_10, _nByte));
            return false;
        }
    }

    /*
     * Read an extension block.
     */
    protected boolean readExtBlock(RepInfo info) throws IOException {
        int subtype = readUnsignedByte(_dstream, this);
        switch (subtype) {
            case APPLICATION_EXT:
                return readAppExtension(info);
            case COMMENT_EXT:
                return readCommentExtension(info);
            case GRAPHIC_CONTROL_EXT:
                return readGraphicsCtlBlock(info);
            case PLAIN_TEXT_EXT:
                return readPlainTextExtension(info);
            default:
                info.setWellFormed(RepInfo.FALSE);
                info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_3,
                        "Type = " + subtype,
                        _nByte));
                return false;
        }
    }

    /*
     * Read an image descriptor and the subsequent data.
     * We are positioned just after the type byte of the
     * image descriptor.
     */
    protected boolean readImage(RepInfo info) throws IOException {
        ++_numGraphicBlocks;
        Vector propVec = new Vector(7);
        NisoImageMetadata niso = new NisoImageMetadata();
        Property nisoProp = new Property("NisoImageMetadata",
                PropertyType.NISOIMAGEMETADATA, niso);

        // GIF doesn't have a lot of options, so several
        // NISO properties are constants.
        niso.setMimeType("image/gif");
        niso.setByteOrder("little-endian");
        niso.setCompressionScheme(5); // LZW
        niso.setColorSpace(3); // palette color
        niso.setOrientation(1); // normal
        niso.setBitsPerSample(new int[] { 8 });

        _gceCounter = 0;
        int leftPos = readUnsignedShort(_dstream);
        propVec.add(new Property("ImageLeftPosition",
                PropertyType.INTEGER,
                new Integer(leftPos)));
        int topPos = readUnsignedShort(_dstream);
        propVec.add(new Property("ImageTopPosition",
                PropertyType.INTEGER,
                new Integer(topPos)));
        int width = readUnsignedShort(_dstream);
        niso.setImageWidth(width);
        int height = readUnsignedShort(_dstream);
        niso.setImageLength(height);
        int packedFields = readUnsignedByte(_dstream, this);
        int interlaceFlag = (packedFields & 0X40) >> 6;
        propVec.add(addByteProperty("InterlaceFlag",
                interlaceFlag,
                GifStrings.INTERLACE_FLAG));
        int localColorTableFlag = (packedFields & 0X80) >> 7;
        propVec.add(addByteProperty("LocalColorTableFlag",
                localColorTableFlag,
                GifStrings.LOCAL_COLOR_TABLE_FLAG));
        int sortFlag = (packedFields & 0X20) >> 5;
        propVec.add(addByteProperty("LocalColorTableSortFlag",
                sortFlag,
                GifStrings.COLOR_TABLE_SORT_FLAG));
        int localColorTableSize = 0;
        int rawLocalColorTableSize = packedFields & 0X7;
        propVec.add(new Property("LocalColorTableSize",
                PropertyType.SHORT,
                new Short((short) rawLocalColorTableSize)));
        propVec.add(nisoProp);
        if (localColorTableFlag != 0) {
            localColorTableSize = 3 * (1 << (rawLocalColorTableSize + 1));
            skipBytes(_dstream, localColorTableSize, this);
        }
        Property prop = new Property("ImageDescriptor",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                vectorToPropArray(propVec));
        _blocksList.add(prop);

        // Skip over the LZW minimum code size
        readUnsignedByte(_dstream, this);
        // Now read sub-blocks till we get one of zero size.
        for (;;) {
            int blockSize = readUnsignedByte(_dstream, this);
            if (blockSize == 0) {
                break;
            }
            skipBytes(_dstream, blockSize, this);
        }
        return true;
    }

    /*
     * Read an application extension block and fill in the appropriate
     * properties
     */
    protected boolean readAppExtension(RepInfo info)
            throws IOException {
        int blockSize = readUnsignedByte(_dstream, this);
        if (blockSize != 11) {
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_1,
                    _nByte));
            info.setWellFormed(RepInfo.FALSE);
            return false;
        }
        Vector propVec = new Vector(3);
        StringBuffer appIdent = new StringBuffer();
        int i;
        for (i = 0; i < 8; i++) {
            appIdent.append((char) readUnsignedByte(_dstream, this));
        }
        propVec.add(new Property("ApplicationIdentifier",
                PropertyType.STRING,
                appIdent.toString()));

        short[] appAuth = new short[3];
        for (i = 0; i < 3; i++) {
            appAuth[i] = (short) readUnsignedByte(_dstream, this);
        }
        propVec.add(new Property("ApplicationAuthenticationCode",
                PropertyType.SHORT,
                PropertyArity.ARRAY,
                appAuth));

        int appDataSize = 0;
        // We are interested in the application extension for XMP.
        if (("XMP Data".equals(appIdent.toString()) ||
                (debug_appIdentCaseInsens &&
                        "xmp data".equalsIgnoreCase(appIdent.toString())))
                &&
                appAuth[0] == (short) 'X' &&
                appAuth[1] == (short) 'M' &&
                appAuth[2] == (short) 'P') {
            appDataSize = readXMP();
        } else {
            // Zip through the application data blocks, totalling their size
            for (;;) {
                int subBlockSize = readUnsignedByte(_dstream, this);
                appDataSize += subBlockSize + 1;
                if (subBlockSize == 0) {
                    break;
                }
                skipBytes(_dstream, subBlockSize, this);
            }
        }
        propVec.add(new Property("ApplicationDataSize",
                PropertyType.INTEGER,
                new Integer(appDataSize)));

        Property prop = new Property("ApplicationExtension",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                vectorToPropArray(propVec));
        _blocksList.add(prop);
        return true;
    }

    /*
     * Read an application extension block and fill in the appropriate
     * properties. A comment extension should, by recommendation,
     * contain ASCII, but actually can contain anything. Nulls
     * are skipped, but everything else is included as is.
     */
    protected boolean readCommentExtension(RepInfo info)
            throws IOException {
        StringBuffer buf = new StringBuffer();
        for (;;) {
            int subBlockSize = readUnsignedByte(_dstream, this);
            if (subBlockSize == 0) {
                break;
            }
            for (int i = 0; i < subBlockSize; i++) {
                int ch = readUnsignedByte(_dstream, this);
                if (ch != 0) {
                    buf.append((char) ch);
                }
            }
        }
        Property prop = new Property("CommentExtension",
                PropertyType.STRING,
                buf.toString());
        return true;
    }

    /*
     * Read an application extension block and fill in the appropriate
     * properties
     */
    protected boolean readPlainTextExtension(RepInfo info)
            throws IOException {
        ++_numGraphicBlocks;
        _gceCounter = 0;
        int blockSize = readUnsignedByte(_dstream, this);
        if (blockSize != 12) {
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_7,
                    _nByte));
            info.setWellFormed(RepInfo.FALSE);
            return false;
        }

        // A plain text extension requires a global color table
        if (!_globalColorTableFlag) {
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_8,
                    _nByte));
            info.setValid(false);
        }
        Vector propVec = new Vector(9);
        int textLeft = readUnsignedShort(_dstream);
        propVec.add(new Property("TextGridLeftPosition",
                PropertyType.INTEGER,
                new Integer(textLeft)));

        int textTop = readUnsignedShort(_dstream);
        propVec.add(new Property("TextGridTopPosition",
                PropertyType.INTEGER,
                new Integer(textTop)));

        int textGWidth = readUnsignedShort(_dstream);
        propVec.add(new Property("TextGridWidth",
                PropertyType.INTEGER,
                new Integer(textGWidth)));

        int textGHeight = readUnsignedShort(_dstream);
        propVec.add(new Property("TextGridHeight",
                PropertyType.INTEGER,
                new Integer(textGHeight)));

        int charCWidth = readUnsignedByte(_dstream, this);
        propVec.add(new Property("CharacterCellWidth",
                PropertyType.SHORT,
                new Short((short) charCWidth)));

        int charCHeight = readUnsignedByte(_dstream, this);
        propVec.add(new Property("CharacterCellHeight",
                PropertyType.SHORT,
                new Short((short) charCHeight)));

        int textFgIdx = readUnsignedByte(_dstream, this);
        propVec.add(new Property("TextForegroundColorIndex",
                PropertyType.SHORT,
                new Short((short) textFgIdx)));

        int textBgIdx = readUnsignedByte(_dstream, this);
        propVec.add(new Property("TextBackgroundColorIndex",
                PropertyType.SHORT,
                new Short((short) textBgIdx)));

        // Read the text data. The GIF recommendation states that
        // characters less than 0X20 or greater than 0XF7 (why F7?
        // It's on at least 2 independent copies of the spec, so
        // apparently it's not a typo, or else is a well-entrenched one)
        // should be represented as spaces.
        StringBuffer buf = new StringBuffer();
        for (;;) {
            int subBlockSize = readUnsignedByte(_dstream, this);
            if (subBlockSize == 0) {
                break;
            }
            for (int i = 0; i < subBlockSize; i++) {
                int ch = readUnsignedByte(_dstream, this);
                if (ch >= 0X20 || ch <= 0XF7) {
                    buf.append((char) ch);
                } else {
                    buf.append(' ');
                }
            }
        }
        propVec.add(new Property("PlainTextData",
                PropertyType.STRING,
                buf.toString()));

        Property prop = new Property("PlainTextExtension",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                vectorToPropArray(propVec));
        _blocksList.add(prop);
        return true;
    }

    /*
     * Read a graphics control block and fill in the
     * relevant properties
     */
    protected boolean readGraphicsCtlBlock(RepInfo info)
            throws IOException {
        Vector propVec = new Vector(5);
        if (++_gceCounter > 1) {
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_5,
                    _nByte));
            info.setWellFormed(RepInfo.FALSE);
        }
        int blockSize = readUnsignedByte(_dstream, this);
        if (blockSize != 4) {
            info.setMessage(new ErrorMessage(MessageConstants.GIF_HUL_6,
                    _nByte));
            info.setWellFormed(RepInfo.FALSE);
            return false;
        }
        int packedFields = readUnsignedByte(_dstream, this);
        int dispMethod = (packedFields & 0X1C) >> 3;
        propVec.add(addByteProperty("DisposalMethod",
                dispMethod,
                GifStrings.GCE_DISPOSAL_METHOD));
        int userInputFlag = (packedFields & 2) >> 1;
        propVec.add(addByteProperty("UserInputFlag",
                userInputFlag,
                GifStrings.GCE_USER_INPUT_FLAG));
        int transparencyFlag = packedFields & 1;
        propVec.add(addByteProperty("TransparencyFlag",
                transparencyFlag,
                GifStrings.GCE_TRANSPARENCY_FLAG));
        int delayTime = readUnsignedShort(_dstream);
        propVec.add(new Property("DelayTime",
                PropertyType.INTEGER,
                new Integer(delayTime)));
        int transIndex = readUnsignedByte(_dstream, this);
        propVec.add(new Property("TransparentColorIndex",
                PropertyType.SHORT,
                new Short((short) transIndex)));
        // Skip the block terminator.
        readUnsignedByte(_dstream, this);

        Property prop = new Property("GraphicControlExtension",
                PropertyType.PROPERTY,
                PropertyArity.ARRAY,
                vectorToPropArray(propVec));
        _blocksList.add(prop);
        return true;
    }

    /*
     * Read and process an XMP block and return the number of
     * bytes read. When we reach a 0 byte, we've hit the
     * end of the "magic" trailer.
     */
    protected int readXMP() throws IOException {
        // Read bytes till we get to the trailer. Annoyingly,
        // we don't know how big the byte buffer has to be,
        // so we build a List of fixed-size buffers. We don't add
        // curBuf to bufList till it's full.
        List bufList = new LinkedList();
        final int bufsiz = 4096;
        byte[] curBuf = new byte[bufsiz];
        int curBufOff = 0;

        // Fill up buffers till we hit a null.
        for (;;) {
            int ch = readUnsignedByte(_dstream, this);
            if (ch == 0) {
                // Read past second null, which concludes trailer
                readUnsignedByte(_dstream, this);
                break;
            }
            if (curBufOff == bufsiz) {
                bufList.add(curBuf);
                curBuf = new byte[bufsiz];
                curBufOff = 0;
            }
            curBuf[curBufOff++] = (byte) ch;
        }

        // Consolidate the buffers into one big buffer.
        // The magic trailer is 258 bytes long, of which 256
        // bytes were actually read into the buffers, so we
        // set our target to 256 bytes less than the total.
        int appDataSize = bufList.size() * bufsiz + curBufOff + 2;
        int totalSize = appDataSize - 258;
        byte[] bigBuf = new byte[totalSize];
        int bigBufOff = 0;
        ListIterator iter = bufList.listIterator();
        int i;
        l1: while (iter.hasNext()) {
            byte[] buf = (byte[]) iter.next();
            for (i = 0; i < bufsiz; i++) {
                bigBuf[bigBufOff++] = buf[i];
                if (bigBufOff >= totalSize) {
                    break l1;
                }
            }
        }
        // Finally curBuf gets added
        for (i = 0; i < curBufOff; i++) {
            if (bigBufOff >= totalSize) {
                break;
            }
            bigBuf[bigBufOff++] = curBuf[i];
        }

        // OK. All that was just to get the XMP into one big byte
        // buffer. Now process it.
        final String badMetadata = "Invalid or ill-formed XMP metadata";
        try {
            ByteArrayInputStream strm = new ByteArrayInputStream(bigBuf);
            ByteArrayXMPSource src = new ByteArrayXMPSource(strm, "UTF-8");

            // Create an InputSource to feed the parser.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XMLReader parser = factory.newSAXParser().getXMLReader();
            XMPHandler handler = new XMPHandler();
            parser.setContentHandler(handler);
            parser.setErrorHandler(handler);
            // We have to parse twice. The first time, we may get
            // an encoding change as part of an exception thrown. If this
            // happens, we create a new InputSource with the encoding, and
            // continue.
            try {
                parser.parse(src);
                _xmpProp = src.makeProperty();
                return appDataSize;
            } catch (SAXException se) {
                String msg = se.getMessage();
                if (msg != null && msg.startsWith("ENC=")) {
                    String encoding = msg.substring(5);
                    try {
                        // The only permitted encoding is UTF-8, but
                        // that may come under various aliased names,
                        // so we assume the encoding is legitimate.
                        src = new ByteArrayXMPSource(strm, encoding);
                        parser.parse(src);
                    } catch (UnsupportedEncodingException uee) {
                        return appDataSize;
                    }
                }
                _xmpProp = src.makeProperty();
                return appDataSize;
            }
        } catch (Exception e) {
            return appDataSize;
        }

    }

    protected Property addByteProperty(String name, int value,
            String[] labels) {
        if (!_je.getShowRawFlag()) {
            try {
                return new Property(name, PropertyType.STRING, labels[value]);
            } catch (Exception e) {
                // fall through
            }
        }
        return new Property(name, PropertyType.BYTE, new Byte((byte) value));
    }

    /*
     * GIF is always little-endian, so readUnsignedShort can
     * unambiguously drop its endian argument
     */
    protected int readUnsignedShort(DataInputStream stream)
            throws IOException {
        return readUnsignedShort(stream, false, this);
    }
}
