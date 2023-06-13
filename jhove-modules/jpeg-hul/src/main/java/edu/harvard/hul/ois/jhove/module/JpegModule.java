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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.AgentType;
import edu.harvard.hul.ois.jhove.ByteArrayXMPSource;
import edu.harvard.hul.ois.jhove.Document;
import edu.harvard.hul.ois.jhove.DocumentType;
import edu.harvard.hul.ois.jhove.ErrorMessage;
import edu.harvard.hul.ois.jhove.ExternalSignature;
import edu.harvard.hul.ois.jhove.Identifier;
import edu.harvard.hul.ois.jhove.IdentifierType;
import edu.harvard.hul.ois.jhove.InfoMessage;
import edu.harvard.hul.ois.jhove.InternalSignature;
import edu.harvard.hul.ois.jhove.Message;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.Rational;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.Signature;
import edu.harvard.hul.ois.jhove.SignatureType;
import edu.harvard.hul.ois.jhove.SignatureUseType;
import edu.harvard.hul.ois.jhove.XMPHandler;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.jpeg.ArithConditioning;
import edu.harvard.hul.ois.jhove.module.jpeg.JpegExif;
import edu.harvard.hul.ois.jhove.module.jpeg.JpegStrings;
import edu.harvard.hul.ois.jhove.module.jpeg.MessageConstants;
import edu.harvard.hul.ois.jhove.module.jpeg.QuantizationTable;
import edu.harvard.hul.ois.jhove.module.jpeg.SRS;
import edu.harvard.hul.ois.jhove.module.jpeg.Spiff;
import edu.harvard.hul.ois.jhove.module.jpeg.SpiffDir;
import edu.harvard.hul.ois.jhove.module.jpeg.Tiling;

/**
 * Module for identification and validation of JPEG files.
 *
 * General notes:
 *
 * There is no such thing as a "JPEG file format." There are several commonly
 * used file formats which encapsulate JPEG data and conform to the JPEG stream
 * format. There are also many formats which can encapsulate JPEG data within
 * some larger wrapper; this module does not attempt to recognize them. Only
 * JPEG file formats which are JPEG streams are treated here. A JPEG stream
 * which isn't one of the known file formats will be regarded as well-formed,
 * but not valid. To be valid, a file must conform to one of the following:
 * JFIF, SPIFF, and JPEG/Exif. Other formats may be added in the future.
 *
 * This module uses the JPEG-L method of detecting a marker following a data
 * stream, checking for a 0 high bit rather than an entire 0 byte. So long at no
 * JPEG markers are defined with a value from 0 through 7F, this is valid for
 * all JPEG files.
 *
 * * @author Gary McGath
 */
public class JpegModule extends ModuleBase {
	/******************************************************************
	 * DEBUGGING FIELDS. All debugging fields should be set to false for release
	 * code.
	 ******************************************************************/
	private static final Logger LOGGER = Logger
			.getLogger(JpegModule.class.getName());

	/******************************************************************
	 * PRIVATE CLASS FIELDS.
	 ******************************************************************/
	private static final String NISO_IMAGE_MD = "NisoImageMetadata";
	private static final String NAME = "JPEG-hul";
    private static final String RELEASE = "1.5.4";
    private static final int[] DATE = { 2023, 03, 16 };
	private static final String[] FORMAT = { "JPEG", "ISO/IEC 10918-1:1994",
			"Joint Photographic Experts Group", "JFIF",
			"JPEG File Interchange Format", "SPIFF", "ISO/IEC 10918-3:1997",
			"Still Picture Interchange File Format", "JTIP",
			"ISO/IEC 10918-3:1997", "JPEG Tiled Image Pyramid", "JPEG-LS",
			"ISO/IEC 14495", "Adobe JPEG", "ISO/IEC 10918-6:2013" };
	private static final String COVERAGE = "JPEG (ISO/IEC 10918-1:1994), JFIF 1.02, "
			+ "SPIFF (ISO/IEC 10918-3:1997), "
			+ "Exif 2.0, 2.1 (JEIDA-49-1998), 2.2 (JEITA CP-3451), 2.21 (JEITA CP-3451A), and 2.3 (JEITA CP-3451C), "
			+ "JTIP (ISO/IEC 10918-3:1997), JPEG-LS (ISO/IEC 14495), Adobe JPEG (ISO/IEC 10918-6:2013)";
	private static final String[] MIMETYPE = { "image/jpeg" };
	private static final String WELLFORMED = "A JPEG file is well-formed if "
			+ "the first three bytes are 0xFFD8FF, it consists of one or more "
			+ "correctly formatted segments (using markers 0xC0 through 0xFE), "
			+ "and the data streams following RSTn and SOS markers are correctly "
			+ "terminated";
	private static final String VALIDITY = "A JPEG file is valid if "
			+ "well-formed; the first non-comment segment is APP0 (with "
			+ "identifier 0x4A46494600, indicating JFIF or JTIP, with 1, 3 or 4 components), "
			+ "APP1 (with identifier (0x457869660000, indicating Exif, and only 3 components), "
			+ "APP8 (with identifier 0x545049464600, indicating SPIFF), "
			+ "or JPG7 (or SOF55, indicating JPEG-LS); "
			+ "D8 marker occurs only at the beginning of "
			+ "the file; any DTT segments are preceded by DTI segments; and all "
			+ "DTI segment tiling type have a value of 0, 1, or 2";
	private static final String REPINFO = "Additional representation "
			+ "information includes: NISO Z39.87 Digital Still Image Technical "
			+ "Metadata and segment-specific metadata";
	private static final String NOTE = null;
	private static final String RIGHTS = "Copyright 2003-2007 by JSTOR and "
			+ "the President and Fellows of Harvard College. "
			+ "Released under the GNU Lesser General Public License.";

	/******************************************************************
	 * PRIVATE INSTANCE FIELDS.
	 ******************************************************************/
    private static final int CS_GRAYSCALE = 1;
    private static final int CS_RGB = 2;
    private static final int CS_PALETTE = 3;
    private static final int CS_YCC = 6; // YcbCr
    private static final int CS_CMYK = 5;
    private static final int CS_YCCK = 65535; // not existing !!!

	/*
	 * Profile names. These are just informal identifiers, and probably will be
	 * formalized later.
	 */
	protected String jfifProfileName = "JFIF";
	protected String spiffProfileName = "SPIFF";
	protected String exifProfileName = "Exif";
	protected String jpeglProfileName = "JPEG-L";
    protected String adobeProfileName = "Adobe JPEG";

	/* a NumberFormat for handling the minor part of version numbers */
	protected NumberFormat minorFmt;

	/* Top-level metadata property */
	protected Property _metadata;

	/*
	 * Property for current image. This should go into a list of image
	 * properties, which in turn becomes the "image" property of _metadata.
	 */
	protected Property _imageProp;

	/* Exif property */
	protected Property _exifProp;

	/* XMP property */
	protected Property _xmpProp;

	/* NISO image metadata */
	protected NisoImageMetadata _niso;

	/* Top-level property list */
	protected List<Property> _propList;

	/* List of image properties. */
	protected List<Property> _imageList;

	/* Tiling information, if a DTI has been seen. */
	protected Tiling _tiling;

	/* List of quantization tables. */
	protected List<QuantizationTable> _quantTables;

	/* List of arithmetic conditioning entries */
	protected List<ArithConditioning> _arithCondTables;

	/* List of SRS entries. */
	protected List<SRS> _srsList;

	/* Property list for the primary image. */
	protected List<Property> _primaryImageList;

	/* Number of segments read */
	protected int _numSegments;

	/* Number of scans in the current image */
	protected int _numScans;

	/* Restart interval */
	protected int _restartInterval;

	/* Flag indicating an APP0 JFIF segment has been read */
	protected boolean _seenJFIF;

	/* Flag indicating an APP0 JFIF segment has been read first */
	protected boolean _seenJFIFFirst;

	/* Flag indicating an APP8 SPIFF segment has been read */
	protected boolean _seenSPIFF;

	/*
	 * Flag indicating a JPEG-L SOF55 (aka JPG7) segment has been read
	 */
	protected boolean _seenJPEGL;

	/* Flag to make sure we report signature matching only once. */
	protected boolean _reportedSigMatch;

	/* SPIFF directory information. */
	protected SpiffDir _spiffDir;

	/* Flag indicating an APP1 Exif segment has been read */
	protected boolean _seenExif;

	/* Flag indicating the Exif profile is satisfied */
	protected boolean _exifProfileOK;

	/* Exif profile if one is satisfied, more specific than just Exif */
	protected String _exifProfileText;

    /* Flag indicating an APP14 Adobe segment has been read */
    protected boolean _seenAdobe;

	/* Flag indicating lack of a JFIF segment has been reported */
	protected boolean _reportedJFIF;

	/* Flag indicating the first SOF has been read */
	protected boolean _seenSOF;

	/* List of comment text */
	protected List<String> _commentsList;

	/* List of extensions used */
	protected List<String> _jpegExtsList;

	/* List of application segments used */
	protected List<String> _appSegsList;

	/*
	 * List of expand reference components markers. Members are boolean[2]
	 */
	protected List<boolean[]> _expList;

	/* Capability 0 byte, from VER segment. -1 if none. */
	protected int _capability0;

	/* Capability 1 byte, from VER segment. -1 if none. */
	protected int _capability1;

	/* Fixed value for first 3 bytes */
	private static final int[] sigByte = { 0XFF, 0XD8, 0XFF };

    /* Transform flag from the Adobe marker segment. */
    protected byte _transformFlag = -1;

    /* Keep all the chunks of an ICC profile */
    protected ByteArrayOutputStream _baosIccProfile = null;

	/* Resolution units. */
	protected int _units;

	/* X resolution (or pixel aspect ratio X). */
	protected int _xDensity;

	/* Y resolution (or pixel aspect ration Y). */
	protected int _yDensity;

	/******************************************************************
	 * CLASS CONSTRUCTOR.
	 ******************************************************************/
	/**
	 * Instantiate a <tt>JpegModule</tt> object.
	 */
	public JpegModule() {
		super(NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
				VALIDITY, REPINFO, NOTE, RIGHTS, false);

		// Set up a simple NumberFormat for version reporting
		minorFmt = NumberFormat.getInstance();
		minorFmt.setMinimumIntegerDigits(2);

		// Define HUL vendor agent
		_vendor = Agent.harvardInstance();

		// Define C-Cube JPEG 1.02 doc
		Document doc = new Document(
				"Eric Hamilton, JPEG File Interchange Format, "
						+ "Version 1.02, September 1, 1992",
				DocumentType.WEB);
		Agent agent = new Agent.Builder("C-Cube Microsystems",
				AgentType.COMMERCIAL)
						.address("1778 McCarthy Boulevard, Milipitas, CA 95035")
						.telephone("+1 (408) 944-6314").fax("+1 (408) 944-6314")
						.build();
		doc.setPublisher(agent);
		doc.setDate("1992-09-01");
		doc.setIdentifier(
				new Identifier("http://www.w3.org/Graphics/JPEG/jfif3.pdf",
						IdentifierType.URL));
		_specification.add(doc);

		// Define ISO standard
		doc = new Document(
				"ISO/IEC 10918-1:1994(E), Information technology -- "
						+ "Digital compression and coding of continuous-tone "
						+ "still images: Requirements and guidelines",
				DocumentType.STANDARD);
		Agent isoAgent = Agent.newIsoInstance();
		doc.setPublisher(isoAgent);
		doc.setIdentifier(new Identifier("CCITT REc. T.81 (1992 E)",
				IdentifierType.CCITT));
		_specification.add(doc);

		// Define ISO extensions
		doc = new Document("ISO/IEC 10918-3:1997(E), Digital compression"
				+ "and coding of continuous-tone still-images: " + "Extensions",
				DocumentType.STANDARD);
		doc.setPublisher(isoAgent);
		doc.setIdentifier(new Identifier("ITU-T Rec. T.84 (1996 E)",
				IdentifierType.CCITT));
		_specification.add(doc);

		// Define ISO lossless baseline
		doc = new Document(
				"ISO/IEC 14495-1:1999(E), Information technology -- "
						+ "Lossless and near-lossless compression of "
						+ "continuous-tone still images: Baseline",
				DocumentType.STANDARD);
		doc.setPublisher(isoAgent);
		_specification.add(doc);

		// Define ISO lossless extensions
		doc = new Document(
				"ISO/IEC 14495-2:2003(E), Information technology -- "
						+ "Lossless and near-lossless compression of "
						+ "continuous-tone still images: Extensions",
				DocumentType.STANDARD);
		doc.setPublisher(isoAgent);
		_specification.add(doc);

        // Define ISO Adobe extensions
        doc = new Document(
                "ISO/IEC 10918-6:2013(E), Digital compression"
                        + "and coding of continuous-tone still-images: "
                        + "Application to printing systems",
                DocumentType.STANDARD);
        doc.setPublisher(isoAgent);
        doc.setIdentifier(new Identifier("ITU-T Rec. T.872 (06/12)",
                IdentifierType.CCITT));
        _specification.add(doc);

		// Define JEITA Exif 2.3 doc
		doc = new Document(
				"Exchangeable image file format for digital "
						+ "still cameras: Exif Version 2.3",
				DocumentType.STANDARD);
		Agent jeitaAgent = new Agent.Builder(
				"Japan Electronics and Information Technology "
						+ "Industries Association",
				AgentType.STANDARD)
						.web("http://www.jeita.or.jp/")
						.address("Mitsui Sumitomo Kaijo Building Annex, "
								+ "11, Kanda Surugadai 3-chome, Chiyoda-ku, "
								+ "Tokyo 101-0062, Japan")
						.telephone("+81(03) 3518-6421").fax("+81(03) 3295-8721")
						.build();
		doc.setPublisher(jeitaAgent);
		doc.setDate("2010-04");
		Identifier ident = new Identifier("JEITA CP-3451C",
				IdentifierType.JEITA);
		doc.setIdentifier(ident);
		ident = new Identifier(
				"http://home.jeita.or.jp/tsc/std-pdf/CP3451C.pdf",
				IdentifierType.URL);
		doc.setIdentifier(ident);
		_specification.add(doc);

		// Define JEITA Exif 2.2 doc
		doc = new Document(
				"Exchangeable image file format for digital "
						+ "still cameras: Exif Version 2.2",
				DocumentType.STANDARD);
		doc.setPublisher(jeitaAgent);
		doc.setDate("2002-04");
		ident = new Identifier("JEITA CP-3451", IdentifierType.JEITA);
		doc.setIdentifier(ident);
		ident = new Identifier("http://www.exif.org/Exif2-2.PDF",
				IdentifierType.URL);
		doc.setIdentifier(ident);
		_specification.add(doc);

		// Define Exif 2.1 doc
		doc = new Document("Digital Still Camera Image File Format Standard "
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

		Signature sig = new InternalSignature(sigByte, SignatureType.MAGIC,
				SignatureUseType.MANDATORY, 0, "");
		_signature.add(sig);

		sig = new ExternalSignature(".jpg", SignatureType.EXTENSION,
				SignatureUseType.OPTIONAL);
		_signature.add(sig);

		sig = new ExternalSignature(".jls", SignatureType.EXTENSION,
				SignatureUseType.OPTIONAL,
				"Generally used for JPEG-LS (ISO/IEC 14495)");
		_signature.add(sig);

		sig = new ExternalSignature(".spf", SignatureType.EXTENSION,
				SignatureUseType.OPTIONAL,
				"Generally used for SPIFF (ISO/IEC 10918-3:1997)");
		_signature.add(sig);

		_bigEndian = true;
	}

	/******************************************************************
	 * Parsing methods.
	 ******************************************************************/

	/**
	 * Check if the digital object conforms to this Module's internal signature
	 * information.
	 *
	 * @param file
	 *            A RandomAccessFile, positioned at its beginning, which is
	 *            generated from the object to be parsed
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
		int i;
		int ch;
		_dstream = getBufferedDataStream(stream,
				_je != null ? _je.getBufferSize() : 0);
		for (i = 0; i < 3; i++) {
			try {
				ch = readUnsignedByte(_dstream, this);
			} catch (Exception e) {
				ch = -1;
			}
			if (ch != sigByte[i]) {
				info.setWellFormed(false);
				return;
			}
		}
		info.setModule(this);
		info.setFormat(_format[0]);
		info.setMimeType(_mimeType[0]);
		info.setSigMatch(_name);
	}

	/**
	 * Parse the content of a purported JPEG stream digital object and store the
	 * results in RepInfo.
	 *
	 * This function uses the JPEG-L method of detecting a marker following a
	 * data stream, checking for a 0 high bit rather than an entire 0 byte. So
	 * long at no JPEG markers are defined with a value from 0 through 7F, this
	 * is valid for all JPEG files.
	 *
	 * @param stream
	 *            An InputStream, positioned at its beginning, which is
	 *            generated from the object to be parsed
	 * @param info
     *                   A fresh RepInfo object which will be modified to reflect
     *                   the
	 *            results of the parsing
	 * @param parseIndex
	 *            Must be 0 in first call to <code>parse</code>. If
     *                   <code>parse</code> returns a nonzero value, it must be
     *                   called
     *                   again with <code>parseIndex</code> equal to that return
     *                   value.
	 */
	@Override
	public int parse(InputStream stream, RepInfo info, int parseIndex)
			throws IOException {
		initParse();
		info.setFormat(_format[0]);
		info.setMimeType(_mimeType[0]);
		info.setModule(this);

		/*
		 * We may have already done the checksums while converting a temporary
		 * file.
		 */
		setupDataStream(stream, info);
		_propList = new LinkedList<Property>();
		_metadata = new Property("JPEGMetadata", PropertyType.PROPERTY,
				PropertyArity.LIST, _propList);
		if (!readHeader(info)) {
			return 0;
		}
		_niso = new NisoImageMetadata();
		Property nisoProp = new Property(NISO_IMAGE_MD,
				PropertyType.NISOIMAGEMETADATA, _niso);
		_primaryImageList.add(nisoProp);
		initNiso();

		// Count the number of segments read, exclusive of the APP0 header.
		boolean dataPlowing = false;
		ErrorMessage msg;
		try {
			// When true, we have to go through data till we find a marker
			loop1: for (;;) {
				int dbyt = 0;
				boolean sawFF = false;
				if (dataPlowing) {
					for (;;) {
						dbyt = readUnsignedByte(_dstream, this);
						if (dbyt == 0xFF) {
							sawFF = true;
							// multiple FF's count same as one
						} else if (sawFF) {
							// Note use of JPEG-L check. For a
							// standard JPEG check, we would use
							// (dbyt != 0)
							if ((dbyt & 0x80) != 0) {
								dataPlowing = false;
								break;
							}
							// FF followed by 0 is discarded
							sawFF = false;
						}
					}
				} else {
					dbyt = readUnsignedByte(_dstream, this);
					if (dbyt != 0xFF) {
						info.setMessage(new ErrorMessage(
								MessageConstants.JPEG_HUL_7,
								String.format(MessageConstants.JPEG_HUL_7_SUB.getMessage(), dbyt),
								_nByte));
						info.setWellFormed(false);
						return 0;
					}
					// There can be padding bytes equal to FF,
					// so read till we get one that isn't.
					while (dbyt == 0xFF) {
						dbyt = readUnsignedByte(_dstream, this);
					}
				}
				_numSegments++;
				if (!_seenJFIF && !_seenSPIFF && !_seenExif && !_seenJPEGL
						&& _numSegments >= 2 && !_reportedJFIF) {
					info.setMessage(new ErrorMessage(
							MessageConstants.JPEG_HUL_9,
							_nByte));
					info.setValid(false);
					_reportedJFIF = true;
				}
				if (dbyt >= 0xD0 && dbyt <= 0xD7) {
					// RST[m] -- Restart with modulo 8 count 0-7
					dataPlowing = true;
				} else if (dbyt >= 0xF7 && dbyt <= 0xFD) {
					// JPGn extension
					readJPEGExtension(dbyt, info);
				} else
					switch (dbyt) {
					case 0:
						// Byte stuffing -- ignore
						break;

					case 0xC0:
					case 0xC1:
					case 0xC2:
					case 0xC3:
					case 0xC5:
					case 0xC6:
					case 0xC7:
					case 0xC9:
					case 0xCA:
					case 0xCB:
					case 0xCD:
					case 0xCE:
					case 0xCF:
						// SOF(n) marker; value indicates encoding type
						readSOF(dbyt, info);
						break;

					case 0xC4:
						// DHT -- define Huffman tables
						skipSegment(info);
						break;

					case 0xCC:
						// DAC -- define arithmetic coding conditioning
						readDAC(info);
						break;

					case 0xD9:
						// EOI
						break loop1;

					case 0xDA:
						// SOS -- start of scan. This is followed by data.
						skipSegment(info);
						++_numScans;
						dataPlowing = true;
						break;

					case 0xDB:
						// DQT -- define quantization tables
						readDQT(info);
						break;

					case 0xDC:
						// DNL -- define number of lines
						skipSegment(info);
						break;

					case 0xDD:
						// DRI -- define restart interval
						readDRI(info);
						break;

					case 0xDE:
						// DHP -- define hierarchical progression
						readDHP(info);
						break;

					case 0xDF:
						// EXP -- Expand reference component
						readEXP(info);
						break;

					case 0xE0:
						// APP0 extension
						readAPP0(info);
						break;

					case 0xE1:
						readAPP1(info);
						break;

					case 0xE2:
						readAPP2(info);
						break;

					case 0xE8:
						// APP8 extension
						readAPP8(info);
						break;

                    case 0xEE:
                    	readAPP14(info);
                    	break;

                    case 0xE3:
					case 0xE4:
					case 0xE5:
					case 0xE6:
					case 0xE7:
					case 0xE9:
					case 0xEA:
					case 0xEB:
					case 0xEC:
					case 0xED:
					case 0xEF:
						// Appn extensions which we don't handle specially,
						// but do report the existence thereof
						reportAppExt(dbyt, info);
						skipSegment(info);
						break;

					case 0XF0:
						// VER segment
						readVer(info);
						break;

					case 0XF1:
						// DTI (defined tiled image) segment
						readDTI(info);
						break;

					case 0XF2:
						// DTI (defined tile) segment
						readDTT(info);
						break;

					case 0XF4:
						// SRS (selectively refined scan) segment
						readSRS(info);
						break;

					case 0XFE:
						// comment
						--_numSegments; // don't let comment trigger error
						readComment(info);
						break;

					default:
						// Other values don't belong at the top level.
						msg = new ErrorMessage(
								MessageConstants.JPEG_HUL_6, _nByte);
						info.setMessage(msg);
						info.setValid(false);
						break loop1;
					}
			}

		} catch (EOFException e) {
			msg = new ErrorMessage(MessageConstants.JPEG_HUL_2,
					_nByte);
			info.setMessage(msg);
			info.setWellFormed(false);
			return 0;
		}

		info.setProperty(_metadata);

		if (_units == 0) {
			List<Property> list = new ArrayList<Property>();
			list.add(new Property("PixelAspectRatioX", PropertyType.INTEGER, _xDensity));
			list.add(new Property("PixelAspectRatioY", PropertyType.INTEGER, _yDensity));
			_primaryImageList.add(new Property("PixelAspectRatio",
					PropertyType.PROPERTY, PropertyArity.LIST, list));
		}

		// If there's tiling information, create a property for the
		// primary image list.
		if (_tiling != null) {
			Property tp = buildTilingProp(info);
			if (tp != null) {
				_primaryImageList.add(tp);
			}
		}
		if (_restartInterval >= 0) {
			_primaryImageList.add(new Property("RestartInterval",
					PropertyType.INTEGER, _restartInterval));
		}
		_primaryImageList.add(new Property("Scans", PropertyType.INTEGER, _numScans));
		if (!_quantTables.isEmpty()) {
			List<Property> qpl = new LinkedList<Property>();
			ListIterator<QuantizationTable> iter = _quantTables.listIterator();
			while (iter.hasNext()) {
				QuantizationTable qt = iter.next();
				qpl.add(qt.makeProperty(_je.getShowRawFlag()));
			}
			_primaryImageList.add(new Property("QuantizationTables",
					PropertyType.PROPERTY, PropertyArity.LIST, qpl));
		}

		if (!_arithCondTables.isEmpty()) {
			List<Property> qpl = new LinkedList<Property>();
			ListIterator<ArithConditioning> iter = _arithCondTables
					.listIterator();
			while (iter.hasNext()) {
				ArithConditioning qt = iter.next();
				qpl.add(qt.makeProperty(_je.getShowRawFlag()));
			}
			_primaryImageList.add(new Property("ArithmeticConditioning",
					PropertyType.PROPERTY, PropertyArity.LIST, qpl));
		}
		if (!_srsList.isEmpty()) {
			List<Property> srsl = new LinkedList<Property>();
			ListIterator<SRS> iter = _srsList.listIterator();
			while (iter.hasNext()) {
				SRS s = iter.next();
				srsl.add(s.makeProperty());
			}
			_primaryImageList.add(new Property("SelectivelyRefinedScans",
					PropertyType.PROPERTY, PropertyArity.LIST, srsl));
		}

		if (_ckSummer != null) {
			skipDstreamToEnd(info);
			// Set the checksums in the report if they're calculated
			setChecksums(this._ckSummer, info);
		}

		// Put the primary image in the image list.
		_imageList.add(new Property("Image", PropertyType.PROPERTY,
				PropertyArity.LIST, _primaryImageList));

		// Report profiles.
		if (_seenJFIF && _seenJFIFFirst) {
			info.setProfile(jfifProfileName);
		}
		if (_seenExif && _exifProfileOK) {
			if (_exifProfileText != null) {
				info.setProfile(_exifProfileText);
			} else {
				info.setProfile(exifProfileName);
			}
		}
		if (_seenSPIFF) {
			info.setProfile(spiffProfileName);
			if (_spiffDir != null) {
				// Grab any image properties from the SPIFF directory
				// and add them to the image list.
				_spiffDir.appendThumbnailProps(_imageList);
			}
		}
		if (_seenJPEGL) {
			info.setProfile(jpeglProfileName);
		}
        if (_seenAdobe) {
            info.setProfile(adobeProfileName);
        }
		/*
		 * Create a new property list containing the count of the images and the
		 * list of image properties.
		 */
		List<Property> list = new ArrayList<Property>();
		list.add(new Property("Number", PropertyType.INTEGER,
				PropertyArity.SCALAR, _imageList.size()));
		Iterator<Property> iter = _imageList.iterator();
		while (iter.hasNext()) {
			Property prop = iter.next();
			list.add(prop);
		}
		_propList.add(new Property("Images", PropertyType.PROPERTY,
				PropertyArity.LIST, list));
		// _imageList));
		if (!_commentsList.isEmpty()) {
			_propList.add(new Property("Comments", PropertyType.STRING,
					PropertyArity.LIST, _commentsList));
		}
		if (!_jpegExtsList.isEmpty()) {
			_propList.add(new Property("Extensions", PropertyType.STRING,
					PropertyArity.LIST, _jpegExtsList));
		}
		if (!_appSegsList.isEmpty()) {
			_propList.add(new Property("ApplicationSegments",
					PropertyType.STRING, PropertyArity.LIST, _appSegsList));
		}
		if (!_expList.isEmpty()) {
			_propList.add(buildExpandProp(info));
		}
		if (_exifProp != null) {
			_primaryImageList.add(_exifProp);
		}
		if (_xmpProp != null) {
			_primaryImageList.add(_xmpProp);
		}
		return 0;
	}

	/**
	 * One-argument version of <code>readUnsignedShort</code>. JPEG is always
	 * big-endian, so readUnsignedShort can unambiguously drop its endian
	 * argument.
	 */
	public int readUnsignedShort(DataInputStream stream) throws IOException {
		return readUnsignedShort(stream, true, this);
	}

	/**
	 * One-argument version of <code>readUnsignedInt</code>. JPEG is always
	 * big-endian, so readUnsignedInt can unambiguously drop its endian
	 * argument.
	 */
	public long readUnsignedInt(DataInputStream stream) throws IOException {
		return readUnsignedInt(stream, true, this);
	}

	/**
	 * Initializes the state of the module for parsing.
	 */
	@Override
	protected void initParse() {
		super.initParse();
		_imageList = new LinkedList<Property>();
		_tiling = null;
		_restartInterval = -1;
		_seenSOF = false;
		_seenJFIF = false;
		_seenJFIFFirst = false;
		_seenSPIFF = false;
		_seenJPEGL = false;
		_spiffDir = null;
		_seenExif = false;
		_seenAdobe = false;
		_reportedSigMatch = false;
		_exifProfileOK = false;
		_exifProfileText = null;
		_reportedJFIF = false;
		_numSegments = 0;
		_numScans = 0;
		_commentsList = new LinkedList<String>();
		_jpegExtsList = new LinkedList<String>();
		_appSegsList = new LinkedList<String>();
		_primaryImageList = new LinkedList<Property>();
		_quantTables = new LinkedList<QuantizationTable>();
		_arithCondTables = new LinkedList<ArithConditioning>();
		_srsList = new LinkedList<SRS>();
		_expList = new LinkedList<boolean[]>();
		_exifProp = null;
		_xmpProp = null;
		_capability0 = -1;
		_capability1 = -1;
		_transformFlag = -1;
		_baosIccProfile = new ByteArrayOutputStream();
	}

	/**
	 * Initializes the constant portions of the niso metadata.
	 */
	protected void initNiso() {
		_niso.setMimeType("image/jpeg");
		_niso.setByteOrder("big-endian");
		_niso.setCompressionScheme(6); // JPEG compression
	}

	/* This just reads the initial SOI */
	protected boolean readHeader(RepInfo info) {
		int i;
		int ch;
		boolean valid = true;
		try {
			for (i = 0; i < 2; i++) {
				ch = readUnsignedByte(_dstream, this);
				if (ch != sigByte[i]) {
					valid = false;
					break;
				}
			}
		} catch (IOException e) {
			valid = false;
		}
		if (!valid) {
			info.setMessage(new ErrorMessage(
					MessageConstants.JPEG_HUL_4, 0));
			info.setWellFormed(false);
			return false;
		}
		return true;
	}

	/*
	 * Reads an APP0 marker segment. We have already read the APP0 marker
	 * itself.
	 */
	@SuppressWarnings("fallthrough")
	protected void readAPP0(RepInfo info) throws IOException {
		// Bytes for JFIF extension APP0
		final int jfxxByte[] = { 0X4A, 0X46, 0X58, 0X58, 0X00 };
		// Bytes for JFIF base APP0
		final int jfifByte[] = { 0X4A, 0X46, 0X49, 0X46, 0X00 };

		/* Seeing an APP0 segment counts as seeing a signature. */
		if (!_reportedSigMatch) {
			info.setSigMatch(_name);
			_reportedSigMatch = true;
		}
		reportAppExt(0XE0, info);

		int[] ident = new int[5];
		int length = readUnsignedShort(_dstream);

		// It appears that a meaningless JFIF marker can be included
		// in a valid SPIFF file. Ignore it.
		if (_seenSPIFF) {
			skipBytes(_dstream, length - 2, this);
			return;
		}

		for (int i = 0; i < 5; i++) {
			ident[i] = readUnsignedByte(_dstream, this);
		}
		if (equalArray(ident, jfifByte)) {
			if (_numSegments > 1) {
				if (!_seenExif) {
					LOGGER.fine("Seen Exif " + _seenExif + " exif profile ok "
							+ _exifProfileOK);
					// Apparently this is OK in a exif file
					info.setMessage(new ErrorMessage(
							MessageConstants.JPEG_HUL_5,
							_nByte));
					info.setValid(false);
					skipBytes(_dstream, length - 7, this);
				}
			} else {
				_seenJFIFFirst = true;
			}
			// This is a JFIF APP0 marker. It may come only
			// at the beginning of a file, except for Exif profiles.
			_seenJFIF = true;
			int majorVersion = readUnsignedByte(_dstream, this);
			int minorVersion = readUnsignedByte(_dstream, this);
			// Format version as M.mm
			String vsn = Integer.toString(majorVersion) + "."
					+ minorFmt.format(minorVersion);
			info.setVersion(vsn);
			_units = readUnsignedByte(_dstream, this);
			if (_units >= 0 && _units <= 2) {
				// inches, cm, and no specified unit map linearly
				// to NISO values
				_niso.setSamplingFrequencyUnit(_units + 1);
			}
			_xDensity = readUnsignedShort(_dstream);
			_yDensity = readUnsignedShort(_dstream);
			if (_units != 0) {
				_niso.setXSamplingFrequency(new Rational(_xDensity, 1));
				_niso.setYSamplingFrequency(new Rational(_yDensity, 1));
			}
			int xThumbPix = readUnsignedByte(_dstream, this);
			int yThumbPix = readUnsignedByte(_dstream, this);

			// If there is a thumbnail, create a property for it
			if (xThumbPix > 0 && yThumbPix > 0) {
				NisoImageMetadata thumbNiso = new NisoImageMetadata();
				thumbNiso.setImageWidth(xThumbPix);
				thumbNiso.setImageLength(yThumbPix);
				thumbNiso.setColorSpace(CS_RGB); // RGB
				thumbNiso.setCompressionScheme(1); // uncompressed
				thumbNiso.setPixelSize(8);

				List<Property> thumbPropList = new LinkedList<Property>();
				thumbPropList.add(new Property(NISO_IMAGE_MD,
						PropertyType.NISOIMAGEMETADATA, thumbNiso));
				Property thumbProp = new Property("ThumbImage",
						PropertyType.PROPERTY, PropertyArity.LIST,
						thumbPropList);
				_imageList.add(thumbProp);
			}
			_niso.setColorSpace(CS_YCC); // JFIF header usually implies YCbCr
			skipBytes(_dstream, 3 * xThumbPix * yThumbPix, this);
		} else if (equalArray(ident, jfxxByte)) {
			int extCode = readUnsignedByte(_dstream, this);
			switch (extCode) {
			// The extension codes 0x10, 0x11, and 0x13 indicate
			// different thumbnail formats.

			// 0x10 indicates that the thumbnail is itself a JPEG
			// stream! Yech! Have to call the module recursively?
			// Skip for now.
			case 0x11:
				// thumbnail, palette color, 1 byte/pixel (fall through)
			case 0x13:
				// thumbnail, RGB, 3 bytes/pixel
				// Both of these have the same relevant information, the
				// width and height. We just grab those and skip the rest.
				int xThumbPix = readUnsignedByte(_dstream, this);
				int yThumbPix = readUnsignedByte(_dstream, this);
				skipBytes(_dstream, length - 10, this);
				NisoImageMetadata thumbNiso = new NisoImageMetadata();
				thumbNiso.setImageWidth(xThumbPix);
				thumbNiso.setImageLength(yThumbPix);
				thumbNiso.setColorSpace(extCode == 0x13 ? CS_RGB : CS_PALETTE);
				thumbNiso.setCompressionScheme(1); // uncompressed
				thumbNiso.setPixelSize(8);
				List<Property> thumbPropList = new LinkedList<Property>();
				thumbPropList.add(new Property(NISO_IMAGE_MD,
						PropertyType.NISOIMAGEMETADATA, thumbNiso));
				Property thumbProp = new Property("ThumbImage",
						PropertyType.PROPERTY, PropertyArity.LIST,
						thumbPropList);
				_imageList.add(thumbProp);
				break;

			default:
				skipBytes(_dstream, length - 8, this);
				break;

			// we may want to do stuff with the JFXX APP0
			}
		} else {
			skipBytes(_dstream, length - 7, this);
		}
	}

	/*
	 * Reads an APP1 marker segment. This may contain Exif data, i.e., a whole
	 * TIFF file embedded in the segment.
	 */
	protected void readAPP1(RepInfo info) throws IOException {
		final int[] exifByte = { 0x45, 0x78, 0x69, 0x66, 0x00, 0x00 };
		// First 6 bytes of xmpStr
		final int[] xmpByte = { 0x68, 0x74, 0x74, 0x70, 0x3A, 0x2F };
		final String xmpStr = "http://ns.adobe.com/xap/1.0/";
		reportAppExt(0XE1, info);

		int[] ident = new int[6];
		int length = readUnsignedShort(_dstream);
		if (length < 8) {
			// Guard against pathological short packets.
			skipBytes(_dstream, length - 2, this);
			return;
		}
		for (int i = 0; i < 6; i++) {
			ident[i] = readUnsignedByte(_dstream, this);
		}
		if (equalArray(ident, exifByte)) {
			// Some camera images have only an APP1 segment with
			// Exif information to mark them, so count that as
			// a "signature."
			if (!_reportedSigMatch) {
				info.setSigMatch(_name);
				_reportedSigMatch = true;
			}

			// Theoretically, the TIFF module could be missing,
			// in which case we can't do anything, so check
			// it first.
			_seenExif = true;
			if (!JpegExif.isTiffAvailable()) {
				info.setMessage(new InfoMessage(
						MessageConstants.JPEG_HUL_14,
						_nByte));
				skipBytes(_dstream, (long)length - 8, this);
				return;
			}
			JpegExif je = new JpegExif(this);
			RepInfo exifInfo = je.readExifData(_dstream, _je, length);
			if (exifInfo != null) {
				/* Copy any EXIF messages into the JPEG info object. */
				List<Message> list = exifInfo.getMessage();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					Message msg = list.get(i);
					// Skip message JPEG deprecated in TIFF !!!
					if (msg instanceof InfoMessage) {
						InfoMessage imsg = (InfoMessage)msg;
						if (!"TIFF-HUL-61".equals(imsg.getId())) {
							info.setMessage(imsg);
						}
					} else {
						info.setMessage(msg);
					}
				}

				_exifProp = exifInfo.getProperty("Exif");
				// We may also have extracted NISO metadata.
				Property nisoProp = exifInfo.getProperty(NISO_IMAGE_MD);
				if (nisoProp != null) {
					extractExifNisoData(
							(NisoImageMetadata) nisoProp.getValue());
				}
				// Or there is info from the exif IFD
				if (je.getExifNiso() != null) {
					extractExifNisoData(je.getExifNiso());
				}
			}
			if (_niso.getSamplesPerPixel() == 3) { // EXIF images only have 3 components
				_exifProfileOK = je.isExifProfileOK();
			}
			_exifProfileText = je.getProfileText();
		} else if (equalArray(ident, xmpByte) && length >= 32) {
			// Check if the rest of xmpStr matches
			boolean match = true;
			for (int i = 6; i < 28; i++) {
				int ch = readUnsignedByte(_dstream, this);
				--length;
				if (ch != xmpStr.charAt(i)) {
					match = false;
					break;
				}
			}
			if (!match) {
				skipBytes(_dstream, length - 8, this);
				return;
			}
			// This is an XMP packet, and we are now at the XMP
			readUnsignedByte(_dstream, this); // skip null
			--length;
			byte[] xmpBuf = new byte[length - 8];
			readByteBuf(_dstream, xmpBuf, this);
			_xmpProp = readXMP(xmpBuf);
		} else {
			skipBytes(_dstream, length - 8, this);
		}
	}

	/*
	 * Reads an APP8 marker segment. This indicates a SPIFF file, if it's found
	 * at the beginning of the file. If we're already in a SPIFF file, it's a
	 * directory entry. We have already read the APP8 marker itself.
	 */
	protected void readAPP8(RepInfo info) throws IOException {
		final int[] spiffByte = { 0x53, 0x50, 0x49, 0x46, 0x46, 0x00 };

		/* Seeing an APP8 segment counts as seeing a signature. */
		if (!_reportedSigMatch) {
			info.setSigMatch(_name);
			_reportedSigMatch = true;
		}
		reportAppExt(0xE8, info);

		int length = readUnsignedShort(_dstream);
		int[] ident = new int[6];
		if (_spiffDir != null) {
			// we've already started a SPIFF file, so this
			// should be a directory entry.
			_spiffDir.readDirEntry(_dstream, length);
			return;
		}
		for (int i = 0; i < 6; i++) {
			ident[i] = readUnsignedByte(_dstream, this);
		}
		if (equalArray(ident, spiffByte)) {
			if (_numSegments > 1) {
				info.setMessage(new ErrorMessage(
						MessageConstants.JPEG_HUL_8, _nByte));
				info.setValid(false);
			}
			// This is a SPIFF marker. It may come only
			// at the beginning of a file.
			_seenSPIFF = true;
			_spiffDir = new SpiffDir(this);
			int majorVersion = readUnsignedByte(_dstream, this);
			int minorVersion = readUnsignedByte(_dstream, this);
			// Format version as M.mm
			String vsn = Integer.toString(majorVersion) + "."
					+ minorFmt.format(minorVersion);
			info.setVersion(vsn);
			readUnsignedByte(_dstream, this);
			readUnsignedByte(_dstream, this);
			long height = readUnsignedInt(_dstream);
			_niso.setImageLength(height);
			long width = readUnsignedInt(_dstream);
			_niso.setImageWidth(width);

			int colorSpace = readUnsignedByte(_dstream, this);
			int nisoCS = Spiff.colorSpaceToNiso(colorSpace);
			if (nisoCS >= 0) {
				_niso.setColorSpace(nisoCS);
			}
			@SuppressWarnings("unused")
			int bps = readUnsignedByte(_dstream, this);
			int compType = readUnsignedByte(_dstream, this);
			int nisoCT = Spiff.compressionTypeToNiso(compType);
			if (nisoCT >= 0) {
				_niso.setCompressionScheme(nisoCT);
			}
			int units = readUnsignedByte(_dstream, this);
			if (units > 0 && units <= 2) {
				// inches, cm, and no specified unit map linearly
				// to NISO values
				_niso.setSamplingFrequencyUnit(units + 1);
			}
			@SuppressWarnings("unused")
			long vRes = readUnsignedInt(_dstream);
			@SuppressWarnings("unused")
			long hRes = readUnsignedInt(_dstream);
			// These are fixed point numbers (does it say where the
			// point is?) unless units == 0, in which case there's
			// just an aspect ration of vres/hres.
		} else {
			skipBytes(_dstream, length - 8, this);
		}
	}

	/*
	 * Reads an APP2 marker segment. This may include an ICC_PROFILE.
	 */
	protected void readAPP2(RepInfo info) throws IOException {
		final String iccProfileSequence = "ICC_PROFILE\0";
		final int SEQUENCE_LENGTH = 12;

		reportAppExt(0XE2, info);

		// The length field of a JPEG marker is only two bytes long;
		// the length of the length field is included in the total.
		int length = readUnsignedShort(_dstream);
		byte[] ident = new byte[SEQUENCE_LENGTH];
		for (int i = 0; i < SEQUENCE_LENGTH; i++) {
			ident[i] = (byte) readUnsignedByte(_dstream, this);
		}
		String sIdent = new String(ident, "US-ASCII");
		if (!iccProfileSequence.equalsIgnoreCase(sIdent)) {
			// This is not a APP2 segment containing an ICC_PROFILE
			skipBytes(_dstream, length - SEQUENCE_LENGTH - 2, this);
			return;
		}

		// See http://www.color.org/ICC1-V41.pdf Annex B.4

		// The ICC PROFILE can be on multiple chunks
		int chunkNumber = readUnsignedByte(_dstream, this);
		int numberOfChunks = readUnsignedByte(_dstream, this);
		int profileLength = length - SEQUENCE_LENGTH - 2 - 2;

		// Read the iccprofile data
		byte[] iccProfile = new byte[profileLength];
		readByteBuf(_dstream, iccProfile, this);
		_baosIccProfile.write(iccProfile);
		if (chunkNumber == numberOfChunks) {
			// Last chunk for the ICC Profile
			try {
				// Validate and record the name
				String desc = NisoImageMetadata.extractIccProfileDescription(
						_baosIccProfile.toByteArray());
				if (desc != null) {
					_niso.setProfileName(desc);
				}
			} catch (IllegalArgumentException ie) {
				info.setMessage(new ErrorMessage(
						MessageConstants.JPEG_HUL_11, ie.getMessage(),
						_nByte));
			}
		}
	}

	/* Read the VER marker, and set version information accordingly */
	protected void readVer(RepInfo info) throws IOException {
		int length = readUnsignedShort(_dstream);
		int majVersion = readUnsignedByte(_dstream, this);
		int minVersion = readUnsignedByte(_dstream, this);
		String vsn = Integer.toString(majVersion) + "."
				+ minorFmt.format(minVersion);
		info.setVersion(vsn);

		// The number of capability bytes is equal to
		// majVersion + 1. The current code understands
		// major versions through 1, and per the specs,
		// will ignore these bytes if majVersion is greater
		// than 1.
		int skip = length - 4;
		if (majVersion <= 1) {
			_capability0 = readUnsignedByte(_dstream, this);
			skip--;
			if (majVersion == 1) {
				_capability1 = readUnsignedByte(_dstream, this);
				skip--;
			}
		}
		skipBytes(_dstream, skip, this);
		_seenJPEGL = false; // Not permitted under JPEG-L
	}

	/* Read the DTI segment, and begin setting up the tiling property */
	protected void readDTI(RepInfo info) throws IOException {
		readUnsignedShort(_dstream);
		_tiling = new Tiling();
		_tiling.setTilingType(readUnsignedByte(_dstream, this));
		_tiling.setVertScale(readUnsignedShort(_dstream));
		_tiling.setHorScale(readUnsignedShort(_dstream));
		_tiling.setRefGridHeight(readUnsignedInt(_dstream));
		_tiling.setRefGridWidth(readUnsignedInt(_dstream));
		_seenJPEGL = false; // Not permitted under JPEG-L
	}

	/*
	 * Read the DTT segment. There should already be a tiling property set up.
	 */
	protected void readDTT(RepInfo info) throws IOException {
		readUnsignedShort(_dstream);
		if (_tiling == null) {
			info.setMessage(new ErrorMessage(
					MessageConstants.JPEG_HUL_1, _nByte));
			info.setValid(false);
			return;
		}
		long vertScale = readUnsignedInt(_dstream);
		long horScale = readUnsignedInt(_dstream);
		long vertOffset = readUnsignedInt(_dstream);
		long horOffset = readUnsignedInt(_dstream);
		_tiling.addTile(vertScale, horScale, vertOffset, horOffset);
		_seenJPEGL = false; // Not permitted under JPEG-L
	}

	/*
	 * Read an SRS segment. This provides information about progressive scans
	 * and so on.
	 */
	protected void readSRS(RepInfo info) throws IOException {
		readUnsignedShort(_dstream);
		int vertOffset = readUnsignedShort(_dstream);
		int horOffset = readUnsignedShort(_dstream);
		int vertSize = readUnsignedShort(_dstream);
		int horSize = readUnsignedShort(_dstream);
		_srsList.add(new SRS(vertOffset, horOffset, vertSize, horSize));
	}

    /*
     * Reads an APP14 marker segment. This indicates a Adobe file.
     * We have already read the APP14 marker itself.
     */
    protected void readAPP14(RepInfo info) throws IOException {
        final String ADOBE_SEQUENCE = "Adobe\0";
        final int SEQUENCE_LENGTH = 6;
        reportAppExt(0xEE, info);

        int length = readUnsignedShort(_dstream);
        if (length < 8) {
            // Guard against pathological short packets.
            skipBytes(_dstream, length - 2, this);
            return;
        }

        byte[] ident = new byte[SEQUENCE_LENGTH];
        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
            ident[i] = (byte)readUnsignedByte(_dstream, this);
        }
        String sIdent = new String(ident, "US-ASCII");

        int skip = length - SEQUENCE_LENGTH - 2;
        if (!ADOBE_SEQUENCE.equalsIgnoreCase(sIdent)) {
        	// This is not a APP14 segment containing an Adobe
        	skipBytes(_dstream, skip, this);
        	return;
        }
        // This is a Adobe marker.
        _seenAdobe = true;

        // Skip the head of the segment
        while (skip > 1) {
            readUnsignedByte(_dstream, this); // version (byte), flags0 (int), flags1 (int)
            skip--;
        }
        _transformFlag = (byte)readUnsignedByte(_dstream, this);
        skip--;
        if (_transformFlag > 2) {
            String mess = String.format(MessageConstants.JPEG_HUL_12.getMessage(), _transformFlag);
            JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.JPEG_HUL_12.getId(), mess);
            info.setMessage(new ErrorMessage(message, _nByte));
            info.setValid(false);
        }
    }

    /*
	 * Accumulate reports of APPn segments into a property. It's tempting to
	 * report information about the segment, since many APPn segments have ASCII
	 * identifiers, but there's no guarantee of any content beyond the length
	 * field, so we just report the existence of all APPn segments. No attempt
	 * is made to weed out duplicates, since multiple instances of the same
	 * segment number are legitimate and informative.
	 */
	protected void reportAppExt(int dbyt, RepInfo info) {
		String appStr = "APP";
		if (dbyt <= 0xE9) {
			// 0-9
			appStr += (char) (dbyt - 0xE0 + 0x30);
		} else {
			// 10-15
			appStr += "1" + (char) (dbyt - 0xEA + 0x30);
		}
		_appSegsList.add(appStr);
	}

	/*
	 * Read a SOF segment. The first one is the most interesting, since it
	 * contains the dimensions for the image. No multi-image support right now;
	 * this has to be figured out, including the distinction between images and
	 * frames.
	 */
	protected void readSOF(int dbyt, RepInfo info) throws IOException {
        final int[] UC_RGB = new int[] {82, 71, 66};
        final int[] LC_RGB = new int[] {114, 103, 98};

        int length = readUnsignedShort(_dstream);
        int precision = readUnsignedByte(_dstream, this);
        int nLines = readUnsignedShort(_dstream);
        int samPerLine = readUnsignedShort(_dstream);
        int numComps = readUnsignedByte(_dstream, this);
        int[] componentsId = new int[numComps];
        int skip = length - 8;
        for (int i = 0; i < numComps; i++) {
            componentsId[i] = readUnsignedByte(_dstream, this);
            skip--;
            readUnsignedByte(_dstream); // samplingFactor
            skip--;
            readUnsignedByte(_dstream, this); // qtableSelector
            skip--;
        }
        if (skip > 0) {
            skipBytes(_dstream, skip, this);
        }
		
		if (!_seenSOF) {
			_niso.setImageLength(nLines);
			_niso.setImageWidth(samPerLine);
			int[] bps = new int[numComps];
			for (int i = 0; i < numComps; i++) {
				bps[i] = precision;
			}
			_niso.setBitsPerSample(bps);
			_niso.setSamplesPerPixel(numComps);
			_propList.add(new Property("CompressionType", PropertyType.STRING,
					JpegStrings.COMPRESSION_TYPE[dbyt - 0xC0]));
			_seenSOF = true;

            // Define the colorspace
            switch (numComps) {
                case 1: // 1 component
                _niso.setColorSpace(CS_GRAYSCALE); // grayscale
                break;
                case 3: // 3 components
                if (_seenJFIF && _seenJFIFFirst) {
                    _niso.setColorSpace(CS_YCC); // YCbCr
                } else {
                    switch (_transformFlag) {
                        case -1:
                        if (equalArray(componentsId, UC_RGB) || equalArray(componentsId, LC_RGB)) {
                            _niso.setColorSpace(CS_RGB); // RGB
                        } else {
                            _niso.setColorSpace(CS_YCC); // YCbCr
                        }
                        break;
                        case 0:
                        _niso.setColorSpace(CS_RGB); // RGB
                        break;
                        case 1:
                        default:
                        _niso.setColorSpace(CS_YCC); // YCbCr
                        break;
                    }
                }
                break;
                case 4: // 4 components
                switch (_transformFlag) {
                    case -1:
                    case 0:
                    _niso.setColorSpace(CS_CMYK);
                    break;
                    case 2:
                    default:
                    _niso.setColorSpace(CS_YCCK);
                    break;
                }
                break;
                default: // others ?!?
                String mess = String.format(MessageConstants.JPEG_HUL_13.getMessage(), numComps);
                JhoveMessage message = JhoveMessages.getMessageInstance(MessageConstants.JPEG_HUL_13.getId(), mess);
                info.setMessage(new ErrorMessage(message, _nByte));
                info.setValid(false);
                break;
            }
		}
	}

	/* Read a DHP segment. This has the same format as SOF. */
	protected void readDHP(RepInfo info) throws IOException {
		int length = readUnsignedShort(_dstream);
		int precision = readUnsignedByte(_dstream, this);
		int nLines = readUnsignedShort(_dstream);
		int samPerLine = readUnsignedShort(_dstream);
		int numComps = readUnsignedByte(_dstream, this);
		skipBytes(_dstream, length - 8, this);
		if (!_seenSOF) {
			_niso.setImageLength(nLines);
			_niso.setImageWidth(samPerLine);
			int[] bps = new int[numComps];
			for (int i = 0; i < numComps; i++) {
				bps[i] = precision;
			}
			_niso.setBitsPerSample(bps);
			_niso.setSamplesPerPixel(numComps);
			_seenSOF = true;
		}
	}

	/* Read an EXP segment. */
	protected void readEXP(RepInfo info) throws IOException {
		readUnsignedShort(_dstream);
		int lhlv = readUnsignedByte(_dstream, this);
		boolean arr[] = new boolean[2];
		arr[0] = ((lhlv & 0XF0) != 0);
		arr[1] = ((lhlv & 0X0F) != 0);
		_expList.add(arr);
	}

	/* Read a DRI (Data Restart Interval) segment. */
	protected void readDRI(RepInfo info) throws IOException {
		readUnsignedShort(_dstream);
		_restartInterval = readUnsignedShort(_dstream);
	}

	/*
	 * Read a DQT (Define Quantization Table) segment. (10918-1:1994(E),
	 * B.2.4.1)
	 */
	protected void readDQT(RepInfo info) throws IOException {
		int length = readUnsignedShort(_dstream);
		int pqtq = readUnsignedByte(_dstream, this);
		int pq = pqtq >> 4;
		int tq = pqtq & 0X0F;
		_quantTables.add(new QuantizationTable(pq, tq));
		skipBytes(_dstream, length - 3, this);
		_seenJPEGL = false; // Not permitted under JPEG-L
	}

	/*
	 * Read a DAC (Define Arithmetic Conditioning) segment. (10918-1:1994(E),
	 * B.2.4.1)
	 */
	protected void readDAC(RepInfo info) throws IOException {
		int length = readUnsignedShort(_dstream);
		int pqtq = readUnsignedByte(_dstream, this);
		int pq = pqtq >> 4;
		int tq = pqtq & 0X0F;
		_arithCondTables.add(new ArithConditioning(pq, tq));
		skipBytes(_dstream, length - 3, this);
		_seenJPEGL = false; // Not permitted under JPEG-L
	}

	/* Read a JPGn (JPEG Extension) segment. */
	protected void readJPEGExtension(int dbyt, RepInfo info)
			throws IOException {
		String ext;
		if (dbyt <= 0XF9) {
			// 0-9
			ext = "JPG" + (char) (dbyt - 0XF0 + 0X30);
		} else {
			// 10-15
			ext = "JPG1" + (char) (dbyt - 0XFA + 0X30);
		}
		_jpegExtsList.add(ext);

		// JPEG extensions other than F7 and F8 are not permitted
		// under JPEG-L
		if (dbyt != 0XF7 && dbyt != 0XF8) {
			_seenJPEGL = false;
		}
		if (dbyt == 0XF7 && !_seenSPIFF && !_seenJFIF && !_seenExif && !_seenJPEGL) {
                    // This is probably a JPEG-L file.
                    if (!_reportedSigMatch) {
                        info.setSigMatch(_name);
                        _reportedSigMatch = true;
                    }
                    int length = readUnsignedShort(_dstream);
                    int precision = readUnsignedByte(_dstream, this);
                    int nLines = readUnsignedShort(_dstream);
                    int samPerLine = readUnsignedShort(_dstream);
                    int numComps = readUnsignedByte(_dstream, this);
                    skipBytes(_dstream, length - 8, this);
                    _seenJPEGL = true;
                    _niso.setImageLength(nLines);
                    _niso.setImageWidth(samPerLine);
                    int[] bps = new int[numComps];
                    for (int i = 0; i < numComps; i++) {
                            bps[i] = precision;
                    }
                    _niso.setBitsPerSample(bps);
                    _niso.setSamplesPerPixel(numComps);
                    _seenSOF = true;
                    return;
		}

		int length = readUnsignedShort(_dstream);
		skipBytes(_dstream, length - 2, this);
	}

	/*
	 * Read a JPEG comment, and add its text to the comments list. The JPEG spec
	 * says only that the interpretation of the comment is left to the
	 * application. For a first shot, everything up to but not including the
	 * first null, or the entire comment data (whichever comes first) will be
	 * read into a string.
	 */
	protected void readComment(RepInfo info) throws IOException {
		int length = readUnsignedShort(_dstream);
		StringBuffer buf = new StringBuffer();
		boolean getChars = true;
		for (int i = 0; i < length - 2; i++) {
			int ch = readUnsignedByte(_dstream, this);
			if (ch == 0) {
				getChars = false;
				// but keep reading bytes so we come out right
			}
			if (getChars) {
				buf.append((char) ch);
			}
		}
		if (buf.length() > 0) {
			_commentsList.add(buf.toString());
		}
	}

	/*
	 * Build a property based on the capability0 and capability1 bytes. If these
	 * are both -1 (absent), return null.
	 */
	protected Property buildCapProp(RepInfo info) {
		if (_capability0 < 0) {
			return null;
		}

		try {
			// If we're doing raw output, the capability
			// properties will be numbers. If we're doing
			// verbose output, they will be strings.
			Property cap0Prop;
			List<Property> capList = new ArrayList<Property>(3);
			if (_je.getShowRawFlag()) {
				cap0Prop = new Property("Version0", PropertyType.INTEGER, _capability0);
			} else {
				cap0Prop = new Property("Version0", PropertyType.STRING,
						JpegStrings.CAPABILITY_V0[_capability0]);
			}
			capList.add(cap0Prop);

			if (_capability1 >= 0) {
				if (_je.getShowRawFlag()) {
					Property cap1Prop = new Property("Version1",
							PropertyType.INTEGER, _capability1);
					capList.add(cap1Prop);
				} else {
					// Capability 1 entails 2 strings, one for the
					// basic capability, and one for tiling.
					String[] cap1Str = new String[2];
					cap1Str[0] = JpegStrings.CAPABILITY_V1[_capability1 & 0X1F];
					cap1Str[1] = JpegStrings.TILING_CAPABILITY_V1[_capability1 >> 5];
					Property cap1Prop = new Property("Version1",
							PropertyType.STRING, PropertyArity.ARRAY, cap1Str);
					capList.add(cap1Prop);
				}
			}
			return new Property("CapabilityIndicator", PropertyType.PROPERTY,
					PropertyArity.LIST, capList);
		} catch (Exception e) {
			// If we get caught on an out-of-bounds value,
			// etc., simply don't return the property.
			return null;
		}
	}

	/* Build a property from the tiling information. */
	protected Property buildTilingProp(RepInfo info) {
		if (_tiling == null) {
			return null;
		}
		try {
			Property[] propArr = new Property[6];
			int tilingType = _tiling.getTilingType();
			if (_je.getShowRawFlag()) {
				propArr[0] = new Property("TilingType", PropertyType.INTEGER, tilingType);
			} else {
				propArr[0] = new Property("TilingType", PropertyType.STRING,
						JpegStrings.TILING_TYPE[tilingType]);
			}
			propArr[1] = new Property("VerticalScale", PropertyType.INTEGER, _tiling.getVertScale());
			propArr[2] = new Property("HorizontalScale", PropertyType.INTEGER, _tiling.getHorScale());
			propArr[3] = new Property("RefGridHeight", PropertyType.LONG, _tiling.getRefGridHeight());
			propArr[4] = new Property("RefGridWidth", PropertyType.LONG, _tiling.getRefGridWidth());
			propArr[5] = _tiling.buildTileListProp();
			return new Property("Tiling", PropertyType.PROPERTY,
					PropertyArity.ARRAY, propArr);
		} catch (Exception e) {
			// Out of bounds value -- punt.
			// Should add an error message here.
			info.setMessage(new ErrorMessage(
					MessageConstants.JPEG_HUL_10));
			info.setValid(false);
			return null;
		}
	}

	protected Property buildExpandProp(RepInfo info) {
		List<Property> plist = new LinkedList<Property>();
		Property prop = new Property("ExpansionSegments", PropertyType.PROPERTY,
				PropertyArity.LIST, plist);
		ListIterator<boolean[]> iter = _expList.listIterator();
		while (iter.hasNext()) {
			boolean[] lhlv = iter.next();
			Property[] lhlvProp = new Property[2];
			lhlvProp[0] = new Property("Horizontal", PropertyType.BOOLEAN, lhlv[0]);
			lhlvProp[1] = new Property("Vertical", PropertyType.BOOLEAN, lhlv[1]);
			plist.add(new Property("Expansion", PropertyType.PROPERTY,
					PropertyArity.ARRAY, lhlvProp));
		}
		return prop;
	}

	/* Read XMP data from the tag, and return as a string. */
	protected Property readXMP(byte[] buf) {
		Property xmpProp = null;
		// final String badMetadata = "Invalid or ill-formed XMP metadata";
		try {
			ByteArrayInputStream strm = new ByteArrayInputStream(buf);
			ByteArrayXMPSource src = new ByteArrayXMPSource(strm);

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
				xmpProp = src.makeProperty();
				return xmpProp;
			} catch (SAXException se) {
				String msg = se.getMessage();
				if (msg != null && msg.startsWith("ENC=")) {
					String encoding = msg.substring(5);
					try {
						// Reader rdr = new InputStreamReader (stream,
						// encoding);
						src = new ByteArrayXMPSource(strm, encoding);
						parser.parse(src);
					} catch (UnsupportedEncodingException uee) {
						return null;
					}
				}
				xmpProp = src.makeProperty();
				return xmpProp;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Extract useful information from the Exif NisoImageMetadata, and put it
	 * into our NisoImageMetadata. Not all of the Niso information from the Exif
	 * is meaningful; only that which we think (hope) is is copied. For example,
	 * the MIME type isn't meaningful, but information describing the camera or
	 * scanner is.
	 */
	protected void extractExifNisoData(NisoImageMetadata exifData) {
		int NULL = NisoImageMetadata.NULL; // just a shorthand
		LOGGER.fine("Copying exif nisoImageMD to principal nisoImageMD");
		if (exifData.getExifVersion() != null) {
			_niso.setExifVersion(exifData.getExifVersion());
		}
		if (exifData.getAutoFocus() != NULL) {
			_niso.setAutoFocus(exifData.getAutoFocus());
		}
		if (exifData.getBackLight() != NULL) {
			_niso.setBackLight(exifData.getBackLight());
		}
		if (exifData.getBrightness() != null) {
			_niso.setBrightness(exifData.getBrightness());
		}
		if (exifData.getColorTemp() != NULL) {
			_niso.setColorTemp(exifData.getColorTemp());
		}
		if (exifData.getDeviceSource() != null) {
			_niso.setDeviceSource(exifData.getDeviceSource());
		}
		if (exifData.getDigitalCameraManufacturer() != null) {
			_niso.setDigitalCameraManufacturer(
					exifData.getDigitalCameraManufacturer());
		}
		if (exifData.getDigitalCameraModelName() != null) {
			_niso.setDigitalCameraModelName(
					exifData.getDigitalCameraModelName());
		}
		if (exifData.getDigitalCameraModelNumber() != null) {
			_niso.setDigitalCameraModelNumber(
					exifData.getDigitalCameraModelNumber());
		}
		if (exifData.getDigitalCameraModelSerialNo() != null) {
			_niso.setDigitalCameraModelSerialNo(
					exifData.getDigitalCameraModelSerialNo());
		}
		if (exifData.getOrientation() != NULL) {
			_niso.setOrientation(exifData.getOrientation());
		}
		if (exifData.getExposureBias() != null) {
			_niso.setExposureBias(exifData.getExposureBias());
		}
		if (exifData.getExposureIndex() != NULL) {
			_niso.setExposureIndex(exifData.getExposureIndex());
		}
		if (exifData.getExposureTime() != NULL) {
			_niso.setExposureTime(exifData.getExposureTime());
		}
		if (exifData.getExposureProgram() != NULL) {
			_niso.setExposureProgram(exifData.getExposureProgram());
		}
		if (exifData.getFlash() != NULL) {
			_niso.setFlash(exifData.getFlash());
		}
		if (exifData.getFlashEnergy() != null) {
			_niso.setFlashEnergy(exifData.getFlashEnergy());
		}
		if (exifData.getFlashReturn() != NULL) {
			_niso.setFlashReturn(exifData.getFlashReturn());
		}
		if (exifData.getFNumber() != NULL) {
			_niso.setFNumber(exifData.getFNumber());
		}
		if (exifData.getFocalLength() != NULL) {
			_niso.setFocalLength(exifData.getFocalLength());
		}
		if (exifData.getHostComputer() != null) {
			_niso.setHostComputer(exifData.getHostComputer());
		}
		if (exifData.getImageIdentifier() != null) {
			_niso.setImageIdentifier(exifData.getImageIdentifier());
		}
		if (exifData.getImageProducer() != null) {
			_niso.setImageProducer(exifData.getImageProducer());
		}
		if (exifData.getMaxApertureValue() != null) {
			_niso.setMaxApertureValue(exifData.getMaxApertureValue());
		}
		if (exifData.getMeteringMode() != NULL) {
			_niso.setMeteringMode(exifData.getMeteringMode());
		}
		if (exifData.getOS() != null) {
			_niso.setOS(exifData.getOS());
		}
		if (exifData.getOSVersion() != null) {
			_niso.setOSVersion(exifData.getOSVersion());
		}
		if (exifData.getPerformanceData() != null) {
			_niso.setPerformanceData(exifData.getPerformanceData());
		}
		if (exifData.getProcessingAgency() != null) {
			_niso.setProcessingAgency(exifData.getProcessingAgency());
		}
		if (exifData.getProcessingSoftwareName() != null) {
			_niso.setProcessingSoftwareName(
					exifData.getProcessingSoftwareName());
		}
		if (exifData.getProcessingSoftwareVersion() != null) {
			_niso.setProcessingSoftwareVersion(
					exifData.getProcessingSoftwareVersion());
		}
		if (exifData.getScannerManufacturer() != null) {
			_niso.setScannerManufacturer(exifData.getScannerManufacturer());
		}
		if (exifData.getScannerModelName() != null) {
			_niso.setScannerModelName(exifData.getScannerModelName());
		}
		if (exifData.getScannerModelNumber() != null) {
			_niso.setScannerModelNumber(exifData.getScannerModelNumber());
		}
		if (exifData.getScannerModelSerialNo() != null) {
			_niso.setScannerModelSerialNo(exifData.getScannerModelSerialNo());
		}
		if (exifData.getSceneIlluminant() != NULL) {
			_niso.setSceneIlluminant(exifData.getSceneIlluminant());
		}
		if (exifData.getSubjectDistance() != null) {
			_niso.setSubjectDistance(exifData.getSubjectDistance());
		}
		// Copy information that could come from alternative sources
		if (_niso.getDateTimeCreated() == null
				&& exifData.getDateTimeCreated() != null) {
			_niso.setDateTimeCreated(exifData.getDateTimeCreated());
		}
		if (_niso.getXSamplingFrequency() == null
				&& exifData.getXSamplingFrequency() != null) {
			_niso.setXSamplingFrequency(exifData.getXSamplingFrequency());
			_niso.setSamplingFrequencyUnit(exifData.getSamplingFrequencyUnit());
		}
		if (_niso.getYSamplingFrequency() == null
				&& exifData.getYSamplingFrequency() != null) {
			_niso.setYSamplingFrequency(exifData.getYSamplingFrequency());
			_niso.setSamplingFrequencyUnit(exifData.getSamplingFrequencyUnit());
		}
		if (_niso.getProfileName() == null
				&& exifData.getProfileName() != null) {
			_niso.setProfileName(exifData.getProfileName());
		}

		// If exif FNumber is defined then assume is a camera and not a scanner,
		// migrate Scanner info to DigitalCamera info
		if (_niso.getFNumber() != NULL) {
			if (_niso.getDigitalCameraManufacturer() == null
					&& _niso.getScannerManufacturer() != null) {
				_niso.setDigitalCameraManufacturer(
						_niso.getScannerManufacturer());
			}
			if (_niso.getDigitalCameraModelName() == null
					&& _niso.getScannerModelName() != null) {
				_niso.setDigitalCameraModelName(_niso.getScannerModelName());
			}
			if (_niso.getDigitalCameraModelNumber() == null
					&& _niso.getScannerModelNumber() != null) {
				_niso.setDigitalCameraModelNumber(
						_niso.getScannerModelNumber());
			}
			if (_niso.getDigitalCameraModelSerialNo() == null
					&& _niso.getScannerModelSerialNo() != null) {
				_niso.setDigitalCameraModelSerialNo(
						_niso.getScannerModelSerialNo());
			}
		}
	}

	/*
	 * Skip over a segment without doing anything. When this is called, we have
	 * already read the marker and the stream is ready to read the length.
	 */
	protected boolean skipSegment(RepInfo info) throws IOException {
		int length = readUnsignedShort(_dstream);
		skipBytes(_dstream, length - 2, this);
		return true;
	}

	/*
	 * Compare two arrays of int for equality. They must be the same length.
	 */
	protected static boolean equalArray(int[] a, int[] b) {
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
}
