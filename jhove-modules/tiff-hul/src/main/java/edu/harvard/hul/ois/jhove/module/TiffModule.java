/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment Copyright 2003-2012 by
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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.AgentType;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.DocumentType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.ExternalSignature;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.IdentifierType;
import edu.harvard.hul.ois.jhove.InfoMessage;
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
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.tiff.ExifIFD;
import edu.harvard.hul.ois.jhove.module.tiff.GPSInfoIFD;
import edu.harvard.hul.ois.jhove.module.tiff.GlobalParametersIFD;
import edu.harvard.hul.ois.jhove.module.tiff.IFD;
import edu.harvard.hul.ois.jhove.module.tiff.InteroperabilityIFD;
import edu.harvard.hul.ois.jhove.module.tiff.MessageConstants;
import edu.harvard.hul.ois.jhove.module.tiff.TiffException;
import edu.harvard.hul.ois.jhove.module.tiff.TiffIFD;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfile;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassB;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassG;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITBL;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITBLP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITBP;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITBPP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITBPP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITCT;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITCTP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITCTP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITFP;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITFPP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITFPP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITHC;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITHCP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITHCP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITLW;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITLWP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITLWP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITMP;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITMPP1;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITMPP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITSD;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassITSDP2;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassP;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassR;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileClassY;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileDLFBW;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileDLFColor;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileDLFGray;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileDNG;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileDNGThumb;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileEP;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileExif;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileExifThumb;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileFXC;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileFXF;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileFXJ;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileFXL;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileFXM;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileFXS;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileGeoTIFF;
import edu.harvard.hul.ois.jhove.module.tiff.TiffProfileRFC1314;

/**
 * Module for identification and validation of TIFF files.
 */
public class TiffModule extends ModuleBase {

    /**
     * Value to write as module params to the default config file.
     */
    public static final String[] defaultConfigParams = { "byteoffset=true" };

    /******************************************************************
     * PRIVATE CLASS FIELDS.
     ******************************************************************/

    /** Logger for this class. */
    protected Logger _logger;

    private static final String NAME = "TIFF-hul";
    private static final String RELEASE = "1.9.4";
    private static final int[] DATE = { 2023, 03, 16 };
    private static final String[] FORMAT = { "TIFF", "Tagged Image File Format" };
    private static final String COVERAGE = "TIFF 4.0, 5.0, and 6.0; "
            + "TIFF/IT (ISO/DIS 12639:2003), including file types CT, LW, HC, MP, "
            + "BP, BL, and FP, and conformance levels P1 and P2; TIFF/EP "
            + "(ISO 12234-2:2001); "
            + "Exif 2.0, 2.1 (JEIDA-49-1998), 2.2 (JEITA CP-3451), 2.21 (JEITA CP-3451A), and 2.3 (JEITA CP-3451C); "
            + "Baseline GeoTIFF "
            + "1.0; Baseline 6.0 bilevel (known in TIFF 5.0 as Class B), "
            + "grayscale (Class G), palette-color (Class P), and RGB (Class R); "
            + "6.0 extension YCbCr (Class Y); DLF Benchmark for Faithful Digital "
            + "Reproductions of Monographs and Serials; TIFF-FX (RFC 2301), "
            + "Class F (RFC 2306); RFC 1314; " + "and DNG (Digital Negative)";

    /***
     * These profiles are not reported anymore (SLA, 2004-01-06)
     * "Adobe PageMaker 6.0; Adobe Photoshop 'Advanced TIFF'; " +
     ***/

    private static final String[] MIMETYPE = { "image/tiff", "image/tiff-fx",
            "image/ief" };
    private static final String WELLFORMED = "A TIFF file is well-formed if "
            + "it has a big-endian or little-endian header; at least one IFD; all "
            + "IFDs are 16-bit word aligned; all IFDs have at least one entry; "
            + "all IFD entries are sorted in ascending order by tag number; all "
            + "IFD entries specify the correct type and count; all value offsets "
            + "are 16-bit word aligned; all value offsets reference locations "
            + "within the file; and the final IFD is followed by an offset of 0";
    private static final String VALIDITY = "A TIFF file is valid if "
            + "well-formed; ImageLength, ImageWidth, and "
            + "PhotometricInterpretation tags are defined; strip or tile tags "
            + "are defined; tag values are self-consistent (see JHOVE "
            + "documentation); TileWidth and TileLength values are integral "
            + "multiples of 16; and DateTime tag is properly formatted";
    private static final String REPINFO = "Additional representation "
            + "information includes: NISO Z39.87 Digital Still Image Technical "
            + "Metadata and all other tag values";
    private static final String NOTE = null;
    private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and "
            + "the President and Fellows of Harvard College. "
            + "Released under the GNU Lesser General Public License.";

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    /** List of profile checkers. */
    protected List<TiffProfile> _profile;

    /* Exif profile checker for the main IFD. */
    TiffProfileExif _exifMainProfile;

    /* Exif profile checker for the thumbnail IFD. */
    TiffProfileExifThumb _exifThumbnailProfile;

    /* DNG profile checker for Raw IFD. */
    TiffProfileDNG _dngMainProfile;

    /* DNG profile checker for IFD 0. */
    TiffProfileDNGThumb _dngThumbnailProfile;

    /** Special flag for Exif profiles: Is main IFD profile satisfied */
    protected boolean _exifFirstFlag;
    /** Special flag for Exif profiles: Is thumbnail IFD profile satisfied */
    protected boolean _exifThumbnailFlag;

    /** Special flag for DNG profiles; is "thumbnail" (IFD 0) profile satisfied */
    protected boolean _dngThumbnailFlag;
    /** Special flag for DNG profiles; is raw IFD profile satisfied */
    protected boolean _dngRawFlag;

    /** Open TIFF file. */
    protected RandomAccessFile _raf;
    /** TIFF version. */
    protected int _version;

    protected boolean _byteOffsetIsValid;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Instantiate a <code>TiffModule</code> object.
     */
    public TiffModule() {
        super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
                VALIDITY, REPINFO, NOTE, RIGHTS, true);

        _logger = Logger.getLogger("edu.harvard.hul.ois.jhove");

        // Define vendor agent (HUL)
        _vendor = Agent.harvardInstance();

        // Define TIFF 6.0 document with Adobe agent
        Document doc = new Document("TIFF, Revision 6.0", DocumentType.REPORT);
        Agent adobeAgent = Agent.newAdobeInstance();
        doc.setPublisher(adobeAgent);
        doc.setDate("1992-06-03");
        doc.setEdition("Final");
        doc.setIdentifier(new Identifier("http://partners.adobe.com/asn/"
                + "tech/tiff/specification.jsp", IdentifierType.URL));
        _specification.add(doc);

        // Define TIFF 5.0 document reusing Adobe agent
        doc = new Document("TIFF, Revision 5.0", DocumentType.REPORT);
        Agent agent = new Agent.Builder("Aldus Corporation",
                AgentType.COMMERCIAL).build();
        doc.setPublisher(agent);
        doc.setDate("1988-08-08");
        doc.setNote("Aldus was acquired by Adobe Systems, Inc., in 1993");
        _specification.add(doc);

        doc = new Document("Tagged Image File Format, Rev. 4.0",
                DocumentType.REPORT);
        agent = new Agent.Builder("Aldus Corporation", AgentType.COMMERCIAL)
                .build();
        doc.setPublisher(agent);
        doc.setDate("1987-04-30");
        doc.setNote("Aldus was acquired by Adobe Systems, Inc., in 1993");
        _specification.add(doc);

        // Define TIFF/EP document with ISO agent
        doc = new Document("ISO 12234-2:2001, Electronic still-picture "
                + "imaging -- Removable memory -- "
                + "Part 2: TIFF/EP image data format", DocumentType.STANDARD);
        Agent isoAgent = Agent.newIsoInstance();
        doc.setPublisher(isoAgent);
        doc.setDate("2001-10-15");
        Identifier ident = new Identifier("ISO 12234-2:2001(E)",
                IdentifierType.ISO);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define TIFF/IT document, reusing ISO agent
        doc = new Document("ISO/DIS 12639:2003, Graphic technology -- "
                + "Prepress digital data exchange -- "
                + "Tag image file format for image technology " + "(TIFF/IT)",
                DocumentType.STANDARD);
        /* This uses the same agent (ISO) as the prior doc */
        doc.setPublisher(isoAgent);
        doc.setDate("2003-09-04");
        ident = new Identifier("ISO/DIS 12639:2003(E)", IdentifierType.ISO);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define Digital Library Federation doc
        doc = new Document("Benchmark for Faithful Digital Reproductions "
                + "of Monographs and Serials", DocumentType.REPORT);
        agent = new Agent.Builder("Digital Library Federation",
                AgentType.NONPROFIT)
                .address(
                        "1755 Massachusetts Ave., NW, Suite 500, "
                                + "Washington, DC 20036")
                .telephone("+1 (202) 939-4761").fax("+1 (202) 939-4765")
                .email("dlf@clir.org").web("http://www.diglib.org/").build();
        doc.setPublisher(agent);
        doc.setEdition("Version 1");
        doc.setDate("2002-12");
        ident = new Identifier("http://www.diglib.org/standards/bmarkfin.htm",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define PageMaker TIFF doc, reusing Adobe agent
        doc = new Document("Adobe PageMaker TIFF 6.0 Technical Notes",
                DocumentType.REPORT);
        doc.setPublisher(adobeAgent);
        doc.setDate("1995-09-14");
        ident = new Identifier(
                "http://partners.adobe.com/asn/developer/pdfs/tn/TIFFPM6.pdf",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define Photoshop TIFF doc, reusing Adobe agent
        doc = new Document("Adobe Photoshop TIFF Technical Notes",
                DocumentType.REPORT);
        doc.setPublisher(adobeAgent);
        doc.setDate("2002-03-22");
        ident = new Identifier("http://partners.adobe.com/asn/developer/"
                + "pdfs/tn/TIFFphotoshop.pdf", IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define Photoshop file formats doc, reusing Adobe agent
        doc = new Document("Adobe Photoshop 6.0 File Formats Specification",
                DocumentType.REPORT);
        doc.setPublisher(adobeAgent);
        doc.setDate("2000-11");
        doc.setEdition("Version 6.0, Release 2");
        _specification.add(doc);

        // Define TIFF Class F doc
        doc = new Document("TIFF-F Revised Specification: The "
                + "Spirit of TIFF Class F", DocumentType.REPORT);
        agent = new Agent.Builder("Cygnet Technologies", AgentType.COMMERCIAL)
                .build();
        doc.setPublisher(agent);
        doc.setDate("1990-04-28");
        doc.setNote("Cygnet is no longer in business");
        _specification.add(doc);

        // Define IETF Class F doc, with IETF agent Added 2/2/04
        doc = new Document("Tag Image File Format (TIFF) -- F Profile "
                + "for Facsimile", DocumentType.RFC);
        Agent ietfAgent = new Agent.Builder("IETF", AgentType.STANDARD).web(
                "http://www.ietf.org").build();
        doc.setPublisher(ietfAgent);
        doc.setDate("1998-03");
        ident = new Identifier("RFC 2306", IdentifierType.RFC);
        doc.setIdentifier(ident);
        ident = new Identifier(
                "http://hul.harvard.edu/jhove/references.html#rfc2306",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define RFC 1314 doc, reusing IETF agent
        doc = new Document("A File Format for the Exchange of "
                + "Images in the Internet", DocumentType.RFC);
        doc.setPublisher(ietfAgent);
        doc.setDate("1992-04");
        ident = new Identifier("RFC 1314", IdentifierType.RFC);
        doc.setIdentifier(ident);
        ident = new Identifier("http://www.ietf.org/rfc/rfc1314.txt",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define JEITA Exif 2.3 doc
        doc = new Document("Exchangeable image file format for digital "
                + "still cameras: Exif Version 2.3", DocumentType.STANDARD);
        Agent jeitaAgent = new Agent.Builder(
                "Japan Electronics and Information Technology "
                        + "Industries Association",
                AgentType.STANDARD)
                .web("http://www.jeita.or.jp/")
                .address(
                        "Mitsui Sumitomo Kaijo Building Annex, "
                                + "11, Kanda Surugadai 3-chome, Chiyoda-ku, "
                                + "Tokyo 101-0062, Japan")
                .telephone("+81(03) 3518-6421").fax("+81(03) 3295-8721")
                .build();
        doc.setPublisher(jeitaAgent);
        doc.setDate("2010-04");
        ident = new Identifier("JEITA CP-3451C", IdentifierType.JEITA);
        doc.setIdentifier(ident);
        ident = new Identifier("http://home.jeita.or.jp/tsc/std-pdf/CP3451C.pdf",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define JEITA Exif 2.2 doc
        doc = new Document("Exchangeable image file format for digital "
                + "still cameras: Exif Version 2.2", DocumentType.STANDARD);
        doc.setPublisher(jeitaAgent);
        doc.setDate("2002-04");
        ident = new Identifier("JEITA CP-3451", IdentifierType.JEITA);
        doc.setIdentifier(ident);
        ident = new Identifier("http://www.exif.org/Exif2-2.PDF",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define Exif 2.1 doc
        doc = new Document(
                "Digital Still Camera Image File Format Standard "
                        + "(Exchangeable image file format for Digital Still Camera:Exif)",
                DocumentType.STANDARD);
        doc.setPublisher(jeitaAgent);
        doc.setDate("1998-12");
        ident = new Identifier("JEITA JEIDA-49-1998", IdentifierType.JEITA);
        doc.setIdentifier(ident);
        ident = new Identifier("http://www.exif.org/dcf-exif.PDF",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define GeoTIFF doc
        doc = new Document("GeoTIFF Format Specification: "
                + "GeoTIFF Revision 1.0", DocumentType.REPORT);
        agent = new Agent.Builder("Niles Ritter", AgentType.OTHER).build();
        doc.setAuthor(agent);
        agent = new Agent.Builder("Mike Ruth", AgentType.OTHER).build();
        doc.setAuthor(agent);
        agent = new Agent.Builder("GeoTIFF Working Group", AgentType.OTHER).build();
        doc.setPublisher(agent);
        doc.setEdition("Version 1.8.1");
        doc.setDate("1995-10-31");
        ident = new Identifier("http://remotesensing.org/geotiff/spec/"
                + "geotiffhome.html", IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Define RFC 2301 doc (Internet Fax) with IETF agent
        doc = new Document("File Format for Internet Fax", DocumentType.RFC);
        doc.setPublisher(ietfAgent);
        doc.setDate("1998-03");
        ident = new Identifier("RFC 2301", IdentifierType.RFC);
        doc.setIdentifier(ident);
        ident = new Identifier("http://www.ietf.org/rfc/rfc2301.txt",
                IdentifierType.URL);
        doc.setIdentifier(ident);
        _specification.add(doc);

        // Whew -- finally done with docs

        int[] sigbyteI = { 0x49, 0x49, 42, 0 };
        Signature sig = new InternalSignature(sigbyteI, SignatureType.MAGIC,
                SignatureUseType.MANDATORY_IF_APPLICABLE, 0,
                "Little-endian (least significant byte " + "first)");
        _signature.add(sig);

        int[] sigbyteM = { 0x4D, 0x4D, 0, 42 };
        sig = new InternalSignature(sigbyteM, SignatureType.MAGIC,
                SignatureUseType.MANDATORY_IF_APPLICABLE, 0,
                "Big-endian (most significant byte first)");
        _signature.add(sig);

        sig = new ExternalSignature("TIFF", SignatureType.FILETYPE,
                SignatureUseType.OPTIONAL);
        _signature.add(sig);

        sig = new ExternalSignature(".tif", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL);
        _signature.add(sig);

        sig = new ExternalSignature(".tfx", SignatureType.EXTENSION,
                SignatureUseType.OPTIONAL, "For TIFF-FX");
        _signature.add(sig);

        sig = new ExternalSignature("TFX ", SignatureType.FILETYPE,
                SignatureUseType.OPTIONAL, "For TIFF-FX");
        _signature.add(sig);

        buildProfileList();

        _byteOffsetIsValid = false;
    }

    /******************************************************************
     * PUBLIC INSTANCE METHODS.
     *
     * Parsing methods.
     ******************************************************************/

    /**
     * Parse the TIFF for well-formedness and validity, accumulating
     * representation information.
     *
     * @param raf
     *             Open TIFF file
     * @param info
     *             Representation informatino
     */
    @Override
    public final void parse(RandomAccessFile raf, RepInfo info)
            throws IOException {
        if (_defaultParams != null) {
            Iterator<String> iter = _defaultParams.iterator();
            while (iter.hasNext()) {
                String param = iter.next();
                if ("byteoffset=true".equalsIgnoreCase(param)) {
                    _byteOffsetIsValid = true;
                }
            }
        }

        _raf = raf;
        initParse();
        info.setModule(this);
        info.setMimeType(_mimeType[0]);
        info.setFormat(_format[0]);

        Property[] tiffMetadata = new Property[2];
        List<IFD> ifds = null;
        boolean inHeader = true; // Useful for catching empty files
        try {
            /*
             * TIFF header is "II" (little-endian) or "MM" (big-endian),
             * followed by the 16-bit integer value 42.
             */
            raf.seek(0);
            byte ch0 = _raf.readByte();
            byte ch1 = _raf.readByte();
            if (ch0 != ch1 || (ch0 != 0X49 && ch0 != 0X4D)) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_20.getMessage(), (char) ch0, (char) ch1);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_20.getId(), mess);
                throw new TiffException(message, 0);
            }
            inHeader = false;

            _bigEndian = (ch0 == 0X4D);
            tiffMetadata[0] = new Property("ByteOrder", PropertyType.STRING,
                    _bigEndian ? "big-endian" : "little-endian");

            int magic = readUnsignedShort(_raf, _bigEndian);
            if (magic != 42) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_21.getMessage(), magic);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_21.getId(), mess);
                throw new TiffException(message, 2);
            }

            /* If we got this far, take note that the signature is OK. */
            info.setSigMatch(_name);

            /*
             * The offset of the first IFD is found at offset 4. /* The lowest
             * recognized TIFF version is 4. Increment this as features specific
             * to higher versions are recognized.
             */
            _version = 4;
            ifds = parseIFDs(4, info);

            info.setVersion(Integer.toString(_version) + ".0");

            /* Construct IFDs property. */
            List<Property> ifdsList = new LinkedList<Property>();
            Property ifdsProp = new Property("IFDs", PropertyType.PROPERTY,
                    PropertyArity.LIST, ifdsList);
            ifdsList.add(new Property("Number", PropertyType.INTEGER,
                    new Integer(ifds.size())));

            /* Build the IFD property list, for each of the IFDs. */
            ListIterator<IFD> iter = ifds.listIterator();
            while (iter.hasNext()) {
                IFD ifd = iter.next();
                ifdsList.add(ifd.getProperty(_je != null ? _je.getShowRawFlag()
                        : false));

                /*
                 * Check if any messages were generated in constructing the
                 * property. If so, the IFD is invalid.
                 */

                List<ErrorMessage> errors = ifd.getErrors();
                if (!errors.isEmpty()) {
                    info.setValid(false);
                    ListIterator<ErrorMessage> eter = errors.listIterator();
                    while (eter.hasNext()) {
                        info.setMessage(eter.next());
                    }
                }

                /* Check each IFD for profile conformance. */

                ListIterator<TiffProfile> pter = _profile.listIterator();
                while (pter.hasNext()) {
                    TiffProfile prof = pter.next();
                    if (!prof.isAlreadyOK() && prof.satisfiesProfile(ifd)) {
                        info.setProfile(prof.getText());
                    }
                }

                /*
                 * Checking the Exif profile is more complicated, since several
                 * IFD's need to meet their respective requirements. Try
                 * something like this.
                 */
                if (ifd.isFirst()) {
                    _exifFirstFlag = _exifMainProfile.satisfiesProfile(ifd);
                    // We need to compare compression between the main and
                    // thumbnail IFD's.
                    _exifThumbnailProfile.setMainCompression(((TiffIFD) ifd)
                            .getNisoImageMetadata().getCompressionScheme());
                } else if (ifd.isThumbnail()) {
                    _exifThumbnailFlag = _exifThumbnailProfile
                            .satisfiesProfile(ifd);
                }
                /*
                 * The DNG profile is similarly complex, requiring IFD 0 to
                 * satisfy the thumbnail part and some other IFD to satisfy the
                 * main profile. The spec doesn't actually say they can't be the
                 * same profile, so we allow for that possibility.
                 */
                if (ifd.isFirst()) {
                    _dngThumbnailFlag = _dngThumbnailProfile
                            .satisfiesProfile(ifd);
                }
                if (!_dngRawFlag) {
                    _dngRawFlag = _dngMainProfile.satisfiesProfile(ifd);
                }
            }
            tiffMetadata[1] = ifdsProp;

            /*
             * The Exif profile requires coordinating several IFD checks, so we
             * accumulate flags and then set the profile text if they're all
             * set.
             */
            if (_exifFirstFlag && _exifThumbnailFlag) {
                info.setProfile(_exifMainProfile.getText());
            }

            /* Similarly for the DNG profile */
            if (_dngThumbnailFlag && _dngRawFlag) {
                info.setProfile(_dngMainProfile.getText());
            }

            info.setProperty(new Property("TIFFMetadata",
                    PropertyType.PROPERTY, PropertyArity.ARRAY, tiffMetadata));
        } catch (TiffException e) {
            info.setMessage(new ErrorMessage(e.getJhoveMessage(), e.getOffset()));
            info.setWellFormed(false);
            return;
        } catch (IOException e) {
            JhoveMessage msg;
            if (inHeader) {
                msg = MessageConstants.TIFF_HUL_67;
            } else {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_68.getMessage(), e.getClass().getName());
                msg = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_68.getId(), mess);
            }
            info.setMessage(new ErrorMessage(msg));
            info.setWellFormed(false);
            return;
        }

        /* Object is well-formed TIFF. */

        /* Calculate checksums, if necessary. */
        checksumIfRafNotCopied(info, raf);

        info.setMimeType(_mimeType[selectMimeTypeIndex()]);

        /* The document is well-formed; check IFD's for validity. */
        checkValidity(ifds, info);
    }

    /** Allow odd offsets in values */
    public void setByteOffsetValid(boolean v) {
        _byteOffsetIsValid = v;
    }

    /**
     * Special-purpose, limited parser for embedded Exif files.
     *
     * @param raf
     *             Open TIFF file
     * @param info
     *             Representation information
     */
    public final List<IFD> exifParse(RandomAccessFile raf, RepInfo info)
            throws IOException {
        _raf = raf;
        initParse();

        List<IFD> ifds = null;
        boolean inHeader = true; // flag to aid reporting empty file error
        try {
            /*
             * TIFF header is "II" (little-endian) or "MM" (big-endian),
             * followed by the 16-bit integer value 42.
             */
            raf.seek(0);
            byte ch0 = _raf.readByte();
            byte ch1 = _raf.readByte();
            if (ch0 != ch1 || (ch0 != 0X49 && ch0 != 0X4D)) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_22.getMessage(), (char) ch0, (char) ch1);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_22.getId(), mess);
                throw new TiffException(message, 0);
            }
            _bigEndian = (ch0 == 0X4D);

            int magic = readUnsignedShort(_raf, _bigEndian);
            if (magic != 42) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_23.getMessage(), magic);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_23.getId(), mess);
                throw new TiffException(message, 2);
            }
            inHeader = false; // There's SOMETHING in the file

            /*
             * The offset of the first IFD is found at offset 4. The lowest
             * recognized TIFF version is 4. Increment this as features specific
             * to higher versions are recognized.
             * The first IFD is supposed to be normal TIFF IFD, see CP3451C 4.5.2.
             */
            _version = 4;
            ifds = parseIFDs(4, info, true, IFD.TIFF);

            // info.setVersion (Integer.toString (_version) + ".0");

            /* Construct IFDs property. */
            // List ifdsList = new LinkedList ();

            ListIterator<IFD> iter = ifds.listIterator();
            while (iter.hasNext()) {
                IFD ifd = iter.next();

                /*
                 * Check if any messages were generated in constructing the
                 * property. If so, the IFD is invalid.
                 */

                List<ErrorMessage> errors = ifd.getErrors();
                if (!errors.isEmpty()) {
                    info.setValid(false);
                    ListIterator<ErrorMessage> eter = errors.listIterator();
                    while (eter.hasNext()) {
                        info.setMessage(eter.next());
                    }
                }

            }

        } catch (TiffException e) {
            // For parsing EXIF, we don't want to make the enclosing
            // document invalid, so we don't declare the EXIF non-well-formed
            // even though it is.
            info.setMessage(new InfoMessage(e.getJhoveMessage(), e.getOffset()));
            return ifds;
        } catch (IOException e) {
            JhoveMessage msg;
            if (inHeader) {
                msg = MessageConstants.TIFF_HUL_70;
            } else {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_69.getMessage(), e.getClass().getName());
                msg = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_69.getId(), mess);
            }
            info.setMessage(new ErrorMessage(msg));
            info.setWellFormed(false);
            return null;
        }

        // Return the IFD list.
        return ifds;
    }

    /******************************************************************
     * PRIVATE INSTANCE METHODS.
     ******************************************************************/

    /**
     * Build list of profiles to check. Profile checking is, for the most part,
     * done per IFD rather than per file. Exif profile checking is an exception,
     * since it requires the coordination of multiple IFD's. Hence, the Exif
     * profiles aren't added to the list, but treated elsewhere.
     */
    protected void buildProfileList() {
        _profile = new ArrayList<TiffProfile>(30);
        _profile.add(new TiffProfileClassB());
        _profile.add(new TiffProfileClassG());
        _profile.add(new TiffProfileClassP());
        _profile.add(new TiffProfileClassR());
        _profile.add(new TiffProfileClassY());

        _profile.add(new TiffProfileClassITBL());
        _profile.add(new TiffProfileClassITBLP1());
        _profile.add(new TiffProfileClassITBP());
        _profile.add(new TiffProfileClassITBPP1());
        _profile.add(new TiffProfileClassITBPP2());
        _profile.add(new TiffProfileClassITCT());
        _profile.add(new TiffProfileClassITCTP1());
        _profile.add(new TiffProfileClassITCTP2());
        _profile.add(new TiffProfileClassITFP());
        _profile.add(new TiffProfileClassITFPP1());
        _profile.add(new TiffProfileClassITFPP2());
        _profile.add(new TiffProfileClassITHC());
        _profile.add(new TiffProfileClassITHCP1());
        _profile.add(new TiffProfileClassITHCP2());
        _profile.add(new TiffProfileClassITLW());
        _profile.add(new TiffProfileClassITLWP1());
        _profile.add(new TiffProfileClassITLWP2());
        _profile.add(new TiffProfileClassITMP());
        _profile.add(new TiffProfileClassITMPP1());
        _profile.add(new TiffProfileClassITMPP2());
        _profile.add(new TiffProfileClassITSD());
        _profile.add(new TiffProfileClassITSDP2());

        _profile.add(new TiffProfileEP());

        _profile.add(new TiffProfileGeoTIFF());

        _profile.add(new TiffProfileDLFBW());
        _profile.add(new TiffProfileDLFGray());
        _profile.add(new TiffProfileDLFColor());

        _profile.add(new TiffProfileRFC1314());

        // TIFF/FX profiles.
        _profile.add(new TiffProfileFXS());
        _profile.add(new TiffProfileFXF());
        _profile.add(new TiffProfileFXJ());
        _profile.add(new TiffProfileFXL());
        _profile.add(new TiffProfileFXC());
        _profile.add(new TiffProfileFXM());

        _exifMainProfile = new TiffProfileExif();
        _exifThumbnailProfile = new TiffProfileExifThumb();
        _dngMainProfile = new TiffProfileDNG();
        _dngThumbnailProfile = new TiffProfileDNGThumb();
    }

    /**
     * Go through all the IFD's, calling checkIFDValidity on each one that is a
     * standard IFD. (Private IFD's have different requirements, and for the
     * moment aren't checked here.) If any of them are invalid, set info's valid
     * field to false. Validity problems are non-fatal, and more information is
     * better, so we keep going with all IFDs even if we find problems.
     */
    protected void checkValidity(List<IFD> ifds, RepInfo info) {
        _logger.info("TiffModule checking validity of IFDs");
        ListIterator<IFD> iter = ifds.listIterator();
        while (iter.hasNext()) {
            try {
                IFD ifd = iter.next();
                if (ifd instanceof TiffIFD) {
                    checkValidity((TiffIFD) ifd, info);
                }
            } catch (TiffException e) {
                info.setMessage(new ErrorMessage(e.getJhoveMessage(), e.getOffset()));
                info.setValid(false);
            }
        }
    }

    /**
     * Check the validity of the IFD.
     *
     * @param ifd
     *            IFD
     */
    protected void checkValidity(TiffIFD ifd, RepInfo info)
            throws TiffException {
        /* Required fields. */

        NisoImageMetadata niso = ifd.getNisoImageMetadata();
        int photometricInterpretation = niso.getColorSpace();
        if (photometricInterpretation == NisoImageMetadata.NULL) {
            reportInvalid(MessageConstants.TIFF_HUL_63, info);
        }
        long imageWidth = niso.getImageWidth();
        if (imageWidth == NisoImageMetadata.NULL) {
            reportInvalid(MessageConstants.TIFF_HUL_62, info);
        }
        long imageLength = niso.getImageLength();
        if (imageLength == NisoImageMetadata.NULL) {
            reportInvalid(MessageConstants.TIFF_HUL_64, info);
        }

        /* Strips and tiles. */

        long[] stripOffsets = niso.getStripOffsets();
        long[] stripByteCounts = niso.getStripByteCounts();
        boolean stripsDefined = (stripOffsets != null || stripByteCounts != null);

        long tileWidth = niso.getTileWidth();
        long tileLength = niso.getTileLength();
        long[] tileOffsets = niso.getTileOffsets();
        long[] tileByteCounts = niso.getTileByteCounts();
        boolean tilesDefined = (tileWidth != NisoImageMetadata.NULL
                || tileLength != NisoImageMetadata.NULL || tileOffsets != null || tileByteCounts != null);

        if (stripsDefined && tilesDefined) {
            reportInvalid(MessageConstants.TIFF_HUL_24, info);
            throw new TiffException(MessageConstants.TIFF_HUL_24);
        }
        if (!stripsDefined && !tilesDefined) {
            reportInvalid(MessageConstants.TIFF_HUL_25, info);
            throw new TiffException(MessageConstants.TIFF_HUL_25);
        }

        int planarConfiguration = niso.getPlanarConfiguration();
        int samplesPerPixel = niso.getSamplesPerPixel();

        if (stripsDefined) {
            if (stripOffsets == null) {
                reportInvalid(MessageConstants.TIFF_HUL_26, info);
                throw new TiffException(MessageConstants.TIFF_HUL_26);
            }
            if (stripByteCounts == null) {
                reportInvalid(MessageConstants.TIFF_HUL_27, info);
                throw new TiffException(MessageConstants.TIFF_HUL_27);
            }

            int len = stripOffsets.length;
            if (len != stripByteCounts.length) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_28.getMessage(), len,
                        stripByteCounts.length);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_28.getId(), mess);
                reportInvalid(message, info);
            }
            /* Check that all the strips are located within the file */
            try {
                long fileLength = _raf.length();
                for (int i = 0; i < len; i++) {
                    long offset = stripOffsets[i];
                    long count = stripByteCounts[i];
                    if (offset + count > fileLength) {
                        reportInvalid(MessageConstants.TIFF_HUL_29, info);
                    }
                }
            } catch (IOException e) {
            }
        }

        if (tilesDefined) {
            if (tileWidth == NisoImageMetadata.NULL) {
                reportInvalid(MessageConstants.TIFF_HUL_30, info);
            }
            if (tileLength == NisoImageMetadata.NULL) {
                reportInvalid(MessageConstants.TIFF_HUL_31, info);
            }
            if (tileOffsets == null) {
                reportInvalid(MessageConstants.TIFF_HUL_32, info);
            }
            if (tileByteCounts == null) {
                reportInvalid(MessageConstants.TIFF_HUL_33, info);
            }

            if (tileWidth % 16 > 0) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_34.getMessage(), tileWidth);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_34.getId(), mess);
                reportInvalid(message, info);
            }
            if (tileLength % 16 > 0) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_35.getMessage(), tileLength);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_35.getId(), mess);
                reportInvalid(message, info);
            }

            long tilesPerImage = ((imageWidth + tileWidth - 1) / tileWidth)
                    * ((imageLength + tileLength - 1) / tileLength);
            if (planarConfiguration == 2) {
                long spp_tpi = samplesPerPixel * tilesPerImage;
                if (tileOffsets != null && tileOffsets.length < spp_tpi) {
                    String mess = MessageFormat.format(MessageConstants.TIFF_HUL_36.getMessage(), tileOffsets.length,
                            spp_tpi);
                    JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_36.getId(), mess);
                    reportInvalid(message, info);
                }
                if (tileByteCounts != null && tileByteCounts.length < spp_tpi) {
                    String mess = MessageFormat.format(MessageConstants.TIFF_HUL_37.getMessage(), tileByteCounts.length,
                            spp_tpi);
                    JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_37.getId(), mess);
                    reportInvalid(message, info);
                }
            } else {
                if (tileOffsets != null && tileOffsets.length < tilesPerImage) {
                    String mess = MessageFormat.format(MessageConstants.TIFF_HUL_38.getMessage(), tileOffsets.length,
                            tilesPerImage);
                    JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_38.getId(), mess);
                    reportInvalid(message, info);
                }
                if (tileByteCounts != null
                        && tileByteCounts.length < tilesPerImage) {
                    String mess = MessageFormat.format(MessageConstants.TIFF_HUL_39.getMessage(), tileByteCounts.length,
                            tilesPerImage);
                    JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_39.getId(), mess);
                    reportInvalid(message, info);
                }
            }
        }

        /* Transparency mask. */

        int newSubfileType = (int) ifd.getNewSubfileType();
        if ((photometricInterpretation == 4 && (newSubfileType & 4) == 0)
                || (photometricInterpretation != 4 && (newSubfileType & 4) != 0)) {
            reportInvalid(MessageConstants.TIFF_HUL_40,
                    info);
        }
        int[] bitsPerSample = niso.getBitsPerSample();
        if (photometricInterpretation == 4 && (samplesPerPixel < 1 || bitsPerSample[0] != 1)) {
            reportInvalid(MessageConstants.TIFF_HUL_41, info);
        }

        /* Samples per pixel. */

        if ((photometricInterpretation == 0 || photometricInterpretation == 1
                || photometricInterpretation == 3
                || photometricInterpretation == 4) && samplesPerPixel < 1) {
            String mess = MessageFormat.format(MessageConstants.TIFF_HUL_42.getMessage(), samplesPerPixel);
            JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_42.getId(), mess);
            reportInvalid(message, info);
        }
        if ((photometricInterpretation == 2 || photometricInterpretation == 6
                || photometricInterpretation == 8) && samplesPerPixel < 3) {
            String mess = MessageFormat.format(MessageConstants.TIFF_HUL_43.getMessage(), samplesPerPixel);
            JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_43.getId(), mess);
            reportInvalid(message, info);
        }

        /* Palette color. */

        if (photometricInterpretation == 3) {
            int[] colormapBitCodeValue = niso.getColormapBitCodeValue();
            int[] colormapRedValue = niso.getColormapRedValue();
            int[] colormapGreenValue = niso.getColormapGreenValue();
            int[] colormapBlueValue = niso.getColormapBlueValue();
            if (colormapBitCodeValue == null || colormapRedValue == null
                    || colormapGreenValue == null || colormapBlueValue == null) {
                reportInvalid(MessageConstants.TIFF_HUL_44,
                        info);
            }
            if (samplesPerPixel != 1) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_45.getMessage(), samplesPerPixel);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_45.getId(), mess);
                reportInvalid(message, info);
            }
            int len = (1 << bitsPerSample[0]);
            if (colormapBitCodeValue.length < len) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_46.getMessage(),
                        colormapBitCodeValue.length, len);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_46.getId(), mess);
                reportInvalid(message, info);
            }
        }

        /* Cells. */

        if (ifd.getCellLength() != IFD.NULL && ifd.getThreshholding() != 2) {
            reportInvalid(MessageConstants.TIFF_HUL_47, info);
        }

        /* Dot range. */

        int[] dotRange = ifd.getDotRange();
        if (dotRange != null && bitsPerSample != null) {
            int sampleMax = 1 << bitsPerSample[0];
            if (dotRange.length < 2 || dotRange[0] >= sampleMax
                    || dotRange[1] >= sampleMax) {
                reportInvalid(MessageConstants.TIFF_HUL_48, info);
            }
        }

        /* JPEG. */

        if (niso.getCompressionScheme() == 6 && ifd.getJPEGProc() == IFD.NULL) {
            reportInvalid(MessageConstants.TIFF_HUL_49,
                    info);
        }

        /* CIE L*a*b*. */

        if (photometricInterpretation == 8 || photometricInterpretation == 9) {
            int len = 0;
            int[] xs = niso.getExtraSamples();
            if (xs != null) {
                len = niso.getExtraSamples().length;
            }
            int in = samplesPerPixel - len;
            if (in != 1 && in != 3) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_50.getMessage(), samplesPerPixel, len);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_50.getId(), mess);
                reportInvalid(message, info);
            }
            for (int i = 0; i < bitsPerSample.length; i++) {
                if (bitsPerSample[i] != 8 && bitsPerSample[i] != 16) {
                    reportInvalid(MessageConstants.TIFF_HUL_51, info);
                }
            }
        }

        /* Clipping path. */

        if (ifd.getClipPath() != null && ifd.getXClipPathUnits() == IFD.NULL) {
            reportInvalid(MessageConstants.TIFF_HUL_52,
                    info);
        }

        /* Date. */

        String dateTime = ifd.getDateTime();
        if (dateTime != null) {
            if (dateTime.length() != 19) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_53.getMessage(), dateTime);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_53.getId(), mess);
                reportInvalid(message, info);
                return;
            }
            if (dateTime.charAt(4) != ':' || dateTime.charAt(7) != ':'
                    || dateTime.charAt(10) != ' ' || dateTime.charAt(13) != ':'
                    || dateTime.charAt(16) != ':') {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_54.getMessage(), dateTime);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_54.getId(), mess);
                reportInvalid(message, info);
                return;
            }
            try {
                int yyyy = Integer.parseInt(dateTime.substring(0, 4));
                int mm = Integer.parseInt(dateTime.substring(5, 7));
                int dd = Integer.parseInt(dateTime.substring(8, 10));
                int hh = Integer.parseInt(dateTime.substring(11, 13));
                int mn = Integer.parseInt(dateTime.substring(14, 16));
                int ss = Integer.parseInt(dateTime.substring(17));
                if (yyyy < 0 || yyyy > 9999 || mm < 1 || mm > 12 || dd < 1
                        || dd > 31 || hh < 0 || hh > 24 || mn < 0 || mn > 59
                        || ss < 0 || mn > 59) {
                    String mess = MessageFormat.format(MessageConstants.TIFF_HUL_55.getMessage(), dateTime);
                    JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_55.getId(), mess);
                    reportInvalid(message, info);
                }
            } catch (Exception e) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_56.getMessage(), dateTime);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_56.getId(), mess);
                reportInvalid(message, info);
            }
        }
    }

    /** Report an instance of invalidity. */
    protected void reportInvalid(final JhoveMessage message, final RepInfo info) {
        info.setMessage(new ErrorMessage(message));
        info.setValid(false);

    }

    /**
     * Parse all IFDs in the file, accumulating representation information.
     *
     * @param offset
     *               Starting byte offset
     * @param info
     *               Representation information
     */
    protected List<IFD> parseIFDs(long offset, RepInfo info)
            throws TiffException {
        return parseIFDs(offset, info, false, IFD.TIFF);
    }

    /**
     * Parse all IFDs in the file, accumulating representation information.
     *
     * @param offset
     *                       Starting byte offset
     * @param info
     *                       Representation information
     * @param suppressErrors
     *                       If true, use IFD even if it has errors
     */
    protected List<IFD> parseIFDs(long offset, RepInfo info,
            boolean suppressErrors, int ifdType) throws TiffException {
        long next = 0L;
        try {
            _raf.seek(offset);
            next = readUnsignedInt(_raf, _bigEndian);
        } catch (IOException e) {
            throw new TiffException(MessageConstants.TIFF_HUL_57, offset);
        }

        if (next == 0L) {
            throw new TiffException(MessageConstants.TIFF_HUL_58, offset);
        }

        List<IFD> list = new LinkedList<IFD>();
        while (next != 0L) {
            if ((next & 1) != 0) {
                String mess = MessageFormat.format(MessageConstants.TIFF_HUL_59.getMessage(), next);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.TIFF_HUL_59.getId(), mess);
                if (_byteOffsetIsValid) {
                    info.setMessage(new InfoMessage(message));
                } else {
                    info.setMessage(new ErrorMessage(message));
                    info.setWellFormed(false);
                }
            }
            if (list.size() > 50) {
                throw new TiffException(MessageConstants.TIFF_HUL_60);
            }
            _logger.info("Parsing next IFD at offset " + next);
            IFD ifd = parseIFDChain(next, info, ifdType, list, suppressErrors);
            next = ifd.getNext();
        }

        return list;
    }

    protected IFD parseIFDChain(long next, RepInfo info, int type,
            List<IFD> list, boolean suppressErrors) throws TiffException {
        IFD ifd = null;
        switch (type) {
            case IFD.EXIF:
                ifd = new ExifIFD(next, info, _raf, _bigEndian);
                break;
            case IFD.INTEROPERABILITY:
                ifd = new InteroperabilityIFD(next, info, _raf, _bigEndian);
                break;
            case IFD.GPSINFO:
                ifd = new GPSInfoIFD(next, info, _raf, _bigEndian);
                break;
            case IFD.GLOBALPARAMETERS:
                ifd = new GlobalParametersIFD(next, info, _raf, _bigEndian);
                break;
            default:
                ifd = new TiffIFD(next, info, _raf, _bigEndian);
        }
        ifd.parse(_byteOffsetIsValid, suppressErrors);

        /* Update the TIFF version number. */
        int version = ifd.getVersion();
        if (version > _version) {
            _version = version;
        }

        if (list.isEmpty() && type == IFD.TIFF) {
            ifd.setFirst(true);
        } else if (list.size() == 1 && type == IFD.TIFF) {
            // For some profiles, the second IFD is assumed to
            // be the thumbnail. This may not be valid under
            // all circumstances.
            ifd.setThumbnail(true);
        }
        list.add(ifd);
        
        if (list.size() > 50) {
            throw new TiffException(MessageConstants.TIFF_HUL_60);
        }

        if (ifd instanceof TiffIFD) {
            TiffIFD tifd = (TiffIFD) ifd;

            long[] subIFDs = tifd.getSubIFDs();
            if (subIFDs != null) {
                for (int i = 0; i < subIFDs.length; i++) {
                    next = subIFDs[i];
                    while (next != 0) {
                        IFD sub = parseIFDChain(next, info, IFD.TIFF, list,
                                suppressErrors);
                        next = sub.getNext();
                    }
                }
            }

            long offset = tifd.getExifIFD();
            if (offset != IFD.NULL) {
                IFD ex = parseIFDChain(offset, info, IFD.EXIF, list,
                        suppressErrors);
                tifd.setTheExifIFD((ExifIFD) ex);
            }
            if ((offset = tifd.getGPSInfoIFD()) != IFD.NULL) {
                IFD gp = parseIFDChain(offset, info, IFD.GPSINFO, list,
                        suppressErrors);
                tifd.setTheGPSInfoIFD((GPSInfoIFD) gp);
            }
            if ((offset = tifd.getInteroperabilityIFD()) != IFD.NULL) {
                IFD io = parseIFDChain(offset, info, IFD.INTEROPERABILITY,
                        list, suppressErrors);
                tifd.setTheInteroperabilityIFD((InteroperabilityIFD) io);
            }
            if ((offset = tifd.getGlobalParametersIFD()) != IFD.NULL) {
                IFD io = parseIFDChain(offset, info, IFD.GLOBALPARAMETERS,
                        list, suppressErrors);
                tifd.setTheGlobalParametersIFD((GlobalParametersIFD) io);
            }
        }

        return ifd;
    }

    /**
     * Initializes the state of the module for parsing. This overrides the
     * superclass method to reset all the profile flags.
     */
    @Override
    protected void initParse() {
        super.initParse();
        ListIterator<TiffProfile> pter = _profile.listIterator();
        while (pter.hasNext()) {
            TiffProfile prof = pter.next();
            prof.setAlreadyOK(false);
        }
        // Initialize flags for the Exif profile. A thumbnail is not
        // required, so by default we set the thumbnail flag to true.
        // If there is a thumbnail, it must meet the profile.
        _exifFirstFlag = false;
        _exifThumbnailFlag = true;

        // Initialize flags for the DNG profile.
        _dngThumbnailFlag = false;
        _dngRawFlag = false;
    }

    /**
     * Return the index into _mimeType which should be used for the MIME type
     * property. This must be called after all the profiles have been checked.
     * An index of 0 is dominant; if any profiles return 0 from their
     * getMimeClass method, or if conflicting values are returned by different
     * satisfied profiles, then we return 0.
     */
    protected int selectMimeTypeIndex() {
        int trial = -1;
        ListIterator<TiffProfile> pter = _profile.listIterator();
        while (pter.hasNext()) {
            TiffProfile prof = pter.next();
            if (prof.isAlreadyOK()) {
                // Profile was satisfied
                int idx = prof.getMimeClass();
                if (idx == 0) {
                    // 0 beats all others
                    return 0;
                } else if (trial >= 0 && idx != trial) {
                    // any conflict implies 0
                    return 0;
                } else {
                    // Treat idx as the tentative return value
                    trial = idx;
                }
            }
        }
        if (trial == -1) {
            // No profiles at all were satisfied
            return 0;
        }
        // All satisfied profiles returned trial
        return trial;
    }

}
