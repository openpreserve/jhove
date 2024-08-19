/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2007 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.harvard.hul.ois.jhove.AESAudioMetadata;
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
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.Signature;
import edu.harvard.hul.ois.jhove.SignatureType;
import edu.harvard.hul.ois.jhove.SignatureUseType;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.iff.Chunk;
import edu.harvard.hul.ois.jhove.module.iff.ChunkHeader;
import edu.harvard.hul.ois.jhove.module.wave.AssocDataListChunk;
import edu.harvard.hul.ois.jhove.module.wave.BroadcastExtChunk;
import edu.harvard.hul.ois.jhove.module.wave.CartChunk;
import edu.harvard.hul.ois.jhove.module.wave.CueChunk;
import edu.harvard.hul.ois.jhove.module.wave.DataChunk;
import edu.harvard.hul.ois.jhove.module.wave.DataSize64Chunk;
import edu.harvard.hul.ois.jhove.module.wave.ExifInfo;
import edu.harvard.hul.ois.jhove.module.wave.FactChunk;
import edu.harvard.hul.ois.jhove.module.wave.FormatChunk;
import edu.harvard.hul.ois.jhove.module.wave.InstrumentChunk;
import edu.harvard.hul.ois.jhove.module.wave.LabelChunk;
import edu.harvard.hul.ois.jhove.module.wave.LinkChunk;
import edu.harvard.hul.ois.jhove.module.wave.ListInfoChunk;
import edu.harvard.hul.ois.jhove.module.wave.MessageConstants;
import edu.harvard.hul.ois.jhove.module.wave.MpegChunk;
import edu.harvard.hul.ois.jhove.module.wave.NoteChunk;
import edu.harvard.hul.ois.jhove.module.wave.PeakEnvelopeChunk;
import edu.harvard.hul.ois.jhove.module.wave.SampleChunk;

/**
 * Module for identification and validation of WAVE sound files.
 *
 * There is no published specification for WAVE files; this module is based on
 * several Internet sources.
 *
 * WAVE format is a type of RIFF format. RIFF, in turn, is a variant on EA IFF
 * 85.
 *
 * @author Gary McGath
 */
public class WaveModule extends ModuleBase {

    /* Module metadata */
    private static final String NAME = "WAVE-hul";
    private static final String RELEASE = "1.8.3";
	private static final int [] DATE = { 2024, 03, 05 };
    private static final String[] FORMATS = { "WAVE", "Audio for Windows",
            "EBU Technical Specification 3285", "Broadcast Wave Format", "BWF",
            "EBU Technical Specification 3306", "RF64" };
    private static final String COVERAGE = "WAVE (PCMWAVEFORMAT, WAVEFORMATEX, WAVEFORMATEXTENSIBLE); "
            + "Broadcast Wave Format (BWF) version 0, 1 and 2; RF64";
    private static final String[] MIMETYPES = { "audio/vnd.wave", "audio/wav",
            "audio/wave", "audio/x-wav", "audio/x-wave" };
    private static final String WELLFORMED = null;
    private static final String VALIDITY = null;
    private static final String REPINFO = null;
    private static final String NOTE = "There is no published standard for WAVE files. This module regards "
            + "a file as valid if it conforms to common usage practices.";
    private static final String RIGHTS = "Copyright 2004-2007 by JSTOR and the "
            + "President and Fellows of Harvard College. "
            + "Released under the GNU Lesser General Public License.";
    
    private static final String WAVE_FORM_TYPE = "WAVE";
    private static final String EBU_TECH_SPEC_3285 = "EBU Technical Specification 3285";
    private static final String EBU_TECH_SPEC_3306 = "EBU Technical Specification 3306";
    private static final String MSLIB_URL = "http://msdn.microsoft.com/library/default.asp?url=/library/en-us/";
    private static final String PCMWAVEFORMAT_PROFILE = "PCMWAVEFORMAT";
    private static final String WAVEFORMATEX_PROFILE = "WAVEFORMATEX";
    private static final String WAVEFORMATEXTENSIBLE_PROFILE = "WAVEFORMATEXTENSIBLE";

    /** Fixed value for first four bytes of WAVE files */
    private static final String RIFF_SIGNATURE = "RIFF";

    /** Fixed value for first four bytes of RF64 files */
    private static final String RF64_SIGNATURE = "RF64";

    /** Length of the RIFF form type field in bytes */
    private static final int RIFF_FORM_TYPE_LENGTH = 4;

    /** Value indicating a required 64-bit data size lookup */
    public static final long LOOKUP_EXTENDED_DATA_SIZE = 0xFFFFFFFFL;

	/**
	 * Map of 64-bit chunk sizes found in the Data Size 64 chunk.
	 * <code>Long</code> size values should be treated as unsigned.
	 */
	protected Map<String, Long> extendedChunkSizes;

	/** Top-level metadata property */
	protected Property _metadata;

	/** Top-level property list */
	protected List<Property> _propList;

	/** List of Note properties */
	protected List<Property> _notes;

	/** List of Label properties */
	protected List<Property> _labels;

	/** List of Labeled Text properties */
	protected List<Property> _labeledText;

	/** List of Sample properties */
	protected List<Property> _samples;

	/** AES audio metadata to go into WAVE metadata */
	protected AESAudioMetadata _aesMetadata;

	/** RIFF size as found in the RIFF chunk header. */
	protected long riffSize;

	/** Bytes needed to store a file */
	protected int _blockAlign;

	/** Exif data from file */
	protected ExifInfo _exifInfo;

	/** WAVE codec, used for profile verification */
	protected int waveCodec;

	/** Extended (and unsigned) RIFF size as found in Data Size 64 chunk */
	protected long extendedRiffSize;

	/** Extended (and unsigned) sample length as found in Data Size 64 chunk */
	protected long extendedSampleLength;

	/**
	 * Number of samples in the file. Obtained from the Data chunk for
	 * uncompressed files, and the Fact chunk for compressed ones. Value
	 * should be treated as unsigned.
	 */
	protected long sampleCount;

	/** Sample rate from file */
	protected long sampleRate;

	/** Flag to check for exactly one Format chunk */
	protected boolean formatChunkSeen;

	/** Flag to check for presence of a Fact chunk */
	protected boolean factChunkSeen;

	/** Flag to check for not more than one Data chunk */
	protected boolean dataChunkSeen;

	/** Flag to check for a Data Size 64 chunk */
	protected boolean dataSize64ChunkSeen;

	/** Flag to check for not more than one Instrument chunk */
	protected boolean instrumentChunkSeen;

	/** Flag to check for not more than one MPEG chunk */
	protected boolean mpegChunkSeen;

	/** Flag to check for not more than one Cart chunk */
	protected boolean cartChunkSeen;

	/** Flag to check for not more than one Broadcast Audio Extension chunk */
	protected boolean broadcastExtChunkSeen;

	/** Flag to check for not more than one Peak Envelope chunk */
	protected boolean peakChunkSeen;

	/** Flag to check for not more than one Link chunk */
	protected boolean linkChunkSeen;

	/** Flag to check for not more than one Cue chunk */
	protected boolean cueChunkSeen;

	/** Profile flag for PCMWAVEFORMAT */
	protected boolean flagPCMWaveFormat;

	/** Profile flag for WAVEFORMATEX */
	protected boolean flagWaveFormatEx;

	/** Profile flag for WAVEFORMATEXTENSIBLE */
	protected boolean flagWaveFormatExtensible;

	/** Profile flag for RF64 */
	protected boolean flagRF64;

	/** Flag to note that first sample offset has been recorded */
	protected boolean firstSampleOffsetMarked;

	/**
	 * Class constructor.
	 *
	 * Instantiates a <code>WaveModule</code> object.
	 */
	public WaveModule() {
		super(NAME, RELEASE, DATE, FORMATS, COVERAGE, MIMETYPES, WELLFORMED,
				VALIDITY, REPINFO, NOTE, RIGHTS, false);
		_vendor = Agent.harvardInstance();

		Agent msAgent = new Agent.Builder("Microsoft Corporation",
				AgentType.COMMERCIAL)
						.address("One Microsoft Way, Redmond, WA 98052-6399")
						.telephone("+1 (800) 426-9400")
						.web("http://www.microsoft.com").build();

		Document doc = new Document(PCMWAVEFORMAT_PROFILE, DocumentType.WEB);
		doc.setIdentifier(new Identifier(
				MSLIB_URL + "multimed/htm/_win32_pcmwaveformat_str.asp",
				IdentifierType.URL));
		doc.setPublisher(msAgent);
		_specification.add(doc);

		doc = new Document(WAVEFORMATEX_PROFILE, DocumentType.WEB);
		doc.setIdentifier(new Identifier(
				MSLIB_URL + "multimed/htm/_win32_waveformatex_str.asp",
				IdentifierType.URL));
		doc.setPublisher(msAgent);
		_specification.add(doc);

		doc = new Document(WAVEFORMATEXTENSIBLE_PROFILE, DocumentType.WEB);
		doc.setIdentifier(new Identifier(
				MSLIB_URL + "multimed/htm/_win32_waveformatextensible_str.asp",
				IdentifierType.URL));
		doc.setPublisher(msAgent);
		_specification.add(doc);

		Agent ebuAgent = new Agent.Builder("European Broadcasting Union",
				AgentType.COMMERCIAL)
						.address("Casa postale 45, Ancienne Route 17A, "
								+ "CH-1218 Grand-Saconex, Geneva, Switzerland")
						.telephone("+41 (0)22 717 2111")
						.fax("+41 (0)22 747 4000").email("techreview@ebu.ch")
						.web("http://www.ebu.ch").build();

		doc = new Document("Specification of the Broadcast Wave Format (BWF)",
				DocumentType.REPORT);
		doc.setIdentifier(new Identifier(EBU_TECH_SPEC_3285,
				IdentifierType.OTHER));
		doc.setIdentifier(
				new Identifier("https://tech.ebu.ch/docs/tech/tech3285.pdf",
						IdentifierType.URL));
		doc.setPublisher(ebuAgent);
		doc.setDate("2011-05");
		_specification.add(doc);

		doc = new Document("MBWF / RF64: An Extended File Format for Audio",
				DocumentType.REPORT);
		doc.setIdentifier(new Identifier(EBU_TECH_SPEC_3306,
				IdentifierType.OTHER));
		doc.setIdentifier(new Identifier(
				"https://tech.ebu.ch/docs/tech/tech3306-2009.pdf",
				IdentifierType.URL));
		doc.setPublisher(ebuAgent);
		doc.setDate("2009-07");
		_specification.add(doc);

		Agent ietfAgent = new Agent.Builder("IETF", AgentType.STANDARD)
				.web("https://www.ietf.org").build();

		doc = new Document("WAVE and AVI Codec Registries", DocumentType.RFC);
		doc.setPublisher(ietfAgent);
		doc.setDate("1998-06");
		doc.setIdentifier(new Identifier("RFC 2361", IdentifierType.RFC));
		doc.setIdentifier(new Identifier("https://www.ietf.org/rfc/rfc2361.txt",
				IdentifierType.URL));
		_specification.add(doc);

		Signature sig = new ExternalSignature(".wav", SignatureType.EXTENSION,
				SignatureUseType.OPTIONAL);
		_signature.add(sig);

		sig = new ExternalSignature(".bwf", SignatureType.EXTENSION,
				SignatureUseType.OPTIONAL, "For BWF profile");
		_signature.add(sig);

		sig = new ExternalSignature(".rf64", SignatureType.EXTENSION,
				SignatureUseType.OPTIONAL, "For RF64 profile");
		_signature.add(sig);

		sig = new InternalSignature(RIFF_SIGNATURE, SignatureType.MAGIC,
				SignatureUseType.MANDATORY_IF_APPLICABLE, 0);
		_signature.add(sig);

		sig = new InternalSignature("RF64", SignatureType.MAGIC,
				SignatureUseType.MANDATORY_IF_APPLICABLE, 0);
		_signature.add(sig);

		sig = new InternalSignature(WAVE_FORM_TYPE, SignatureType.MAGIC,
				SignatureUseType.MANDATORY, 8);
		_signature.add(sig);

		_bigEndian = false;
	}

	/**
	 * Parses the content of a purported WAVE digital object and stores the
	 * results in RepInfo.
	 *
	 * @param stream
	 *            An InputStream, positioned at its beginning, which is
	 *            generated from the object to be parsed
	 * @param info
	 *            A fresh RepInfo object which will be modified to reflect the
	 *            results of the parsing
	 * @param parseIndex
	 *            Must be 0 in first call to <code>parse</code>. If
	 *            <code>parse</code> returns a nonzero value, it must be called
	 *            again with <code>parseIndex</code> equal to that return value.
	 */
	@Override
	public int parse(InputStream stream, RepInfo info, int parseIndex) {
		initParse();
		info.setModule(this);

		_aesMetadata.setPrimaryIdentifier(info.getUri());
		if (info.getURLFlag()) {
			_aesMetadata.setOtherPrimaryIdentifierType("URI");
		} else {
			_aesMetadata.setPrimaryIdentifierType(AESAudioMetadata.FILE_NAME);
		}

		// We may have already done the checksums
		// while converting a temporary file.
		setupDataStream(stream, info);

		try {
			// Check the start of the file for the right opening bytes
			String firstFourChars = read4Chars(_dstream);
			if (firstFourChars.equals(RF64_SIGNATURE)) {
				info.setProfile("RF64");
				flagRF64 = true;
			} else if (!firstFourChars.equals(RIFF_SIGNATURE)) {
				info.setMessage(
						new ErrorMessage(MessageConstants.WAVE_HUL_1, 0));
				info.setWellFormed(false);
				return 0;
			}

			// Get the length of the Form chunk. This includes all
			// subsequent form fields and form subchunks, but excludes
			// the form chunk's header (its ID and the its length).
			riffSize = readUnsignedInt(_dstream);

			// Read the RIFF form type
			String formType = read4Chars(_dstream);
			if (!WAVE_FORM_TYPE.equals(formType)) {
				info.setMessage(new ErrorMessage(
						MessageConstants.WAVE_HUL_2,
						_nByte - RIFF_FORM_TYPE_LENGTH));
				info.setWellFormed(false);
				return 0;
			}

			// If we get this far, the signature is OK.
			info.setSigMatch(_name);
			info.setFormat(_format[0]);
			info.setMimeType(_mimeType[0]);

			if (flagRF64) {
				// For RF64 files the first chunk should be a Data Size 64 chunk
				// containing the extended data sizes for a number of elements.
				if (readChunk(info) && dataSize64ChunkSeen) {
					if (riffSize == LOOKUP_EXTENDED_DATA_SIZE) {
						// Even though RF64 can support files larger than
						// Long.MAX_VALUE, this module currently does not.
						if (Long.compareUnsigned(extendedRiffSize,
								Long.MAX_VALUE) > 0) {
							info.setMessage(new InfoMessage(
									MessageConstants.WAVE_HUL_22));
							info.setWellFormed(RepInfo.UNDETERMINED);
							return 0;
						}
					}
				} else {
					info.setMessage(new ErrorMessage(
							MessageConstants.WAVE_HUL_23,
							Chunk.HEADER_LENGTH + RIFF_FORM_TYPE_LENGTH));
					info.setWellFormed(false);
					return 0;
				}
			}

			while (getBytesRemaining() > 0) {
				if (!readChunk(info)) {
					break;
				}
			}

			if (getBytesRemaining() > 0) {
				// The file has been truncated or there
				// remains unexpected chunk data to skip
				remainingDataInfo(_dstream, info, getBytesRemaining(),
						firstFourChars);
			}

		} catch (EOFException eofe) {
			info.setWellFormed(false);
			String subMessage = String.format(
					MessageConstants.WAVE_HUL_3_SUB.getMessage(),
					getBytesRemaining());
			if (eofe.getMessage() != null) {
				subMessage += "; " + eofe.getMessage();
			}
			info.setMessage(new ErrorMessage(MessageConstants.WAVE_HUL_3,
					subMessage, _nByte));
		} catch (Exception e) { // TODO make this more specific
			e.printStackTrace();
			info.setWellFormed(false);
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.WAVE_HUL_4.getId(),
					String.format(MessageConstants.WAVE_HUL_4.getMessage(),
							e.getClass().getName() + ", " + e.getMessage()));
			info.setMessage(new ErrorMessage(message, _nByte));
			return 0;
		}

		// Set duration from number of samples and rate.
		if (sampleCount > 0) {
			_aesMetadata.setDuration(sampleCount);
		}

		// Add note and label properties, if there's anything to report.
		if (!_labels.isEmpty()) {
			_propList.add(new Property("Labels", PropertyType.PROPERTY,
					PropertyArity.LIST, _labels));
		}
		if (!_labeledText.isEmpty()) {
			_propList.add(new Property("LabeledText", PropertyType.PROPERTY,
					PropertyArity.LIST, _labeledText));
		}
		if (!_notes.isEmpty()) {
			_propList.add(new Property("Notes", PropertyType.PROPERTY,
					PropertyArity.LIST, _notes));
		}
		if (!_samples.isEmpty()) {
			_propList.add(new Property("Samples", PropertyType.PROPERTY,
					PropertyArity.LIST, _samples));
		}
		if (_exifInfo != null) {
			_propList.add(_exifInfo.buildProperty());
		}

		if (!formatChunkSeen) {
			info.setMessage(new ErrorMessage(MessageConstants.WAVE_HUL_5));
			info.setWellFormed(false);
			return 0;
		}
		if (!dataChunkSeen) {
			info.setMessage(new ErrorMessage(MessageConstants.WAVE_HUL_24));
			info.setWellFormed(false);
			return 0;
		}

		// This file looks OK.
		if (_ckSummer != null) {
			skipDstreamToEnd(info);
			// Set the checksums in the report if they're calculated
			setChecksums(this._ckSummer, info);
		}

		info.setProperty(_metadata);

		// Indicate satisfied profiles.
		if (flagPCMWaveFormat) {
			info.setProfile(PCMWAVEFORMAT_PROFILE);
		}
		if (flagWaveFormatEx) {
			info.setProfile(WAVEFORMATEX_PROFILE);
		}
		if (flagWaveFormatExtensible) {
			info.setProfile(WAVEFORMATEXTENSIBLE_PROFILE);
		}
		if (broadcastExtChunkSeen && (
                        (waveCodec == FormatChunk.WAVE_FORMAT_MPEG && factChunkSeen)
                        || waveCodec == FormatChunk.WAVE_FORMAT_PCM)) {
                    info.setProfile("BWF");
		}
                return 0;
	}

	/**
	 * Marks the first sample offset as the current byte position, if it hasn't
	 * already been marked.
	 */
	public void markFirstSampleOffset() {
		if (!firstSampleOffsetMarked) {
			firstSampleOffsetMarked = true;
			_aesMetadata.setFirstSampleOffset(_nByte);
		}
	}

	/** Sets an ExifInfo object for the module. */
	public void setExifInfo(ExifInfo exifInfo) {
		_exifInfo = exifInfo;
	}

	/** Sets the number of bytes that holds an aligned sample. */
	public void setBlockAlign(int align) {
		_blockAlign = align;
	}

	/**
	 * Returns the ExifInfo object.
	 *
	 * If no ExifInfo object has been set, returns <code>null</code>.
	 */
	public ExifInfo getExifInfo() {
		return _exifInfo;
	}

	/** Returns the WAVE codec value. */
	public int getWaveCodec() {
		return waveCodec;
	}

	/** Returns the number of bytes needed per aligned sample. */
	public int getBlockAlign() {
		return _blockAlign;
	}

	/** Adds a Property to the WAVE metadata. */
	public void addWaveProperty(Property prop) {
		_propList.add(prop);
	}

	/** Adds a Label property */
	public void addLabel(Property p) {
		_labels.add(p);
	}

	/** Adds a LabeledText property */
	public void addLabeledText(Property p) {
		_labeledText.add(p);
	}

	/** Adds a Sample property */
	public void addSample(Property p) {
		_samples.add(p);
	}

	/** Adds a Note string */
	public void addNote(Property p) {
		_notes.add(p);
	}

	/** Adds the ListInfo property, which is a List of String Properties. */
	public void addListInfo(List l) {
		_propList.add(new Property("ListInfo", PropertyType.PROPERTY,
				PropertyArity.LIST, l));
	}

	/**
	 * One-argument version of <code>readSignedLong</code>. WAVE is always
	 * little-endian, so we can unambiguously drop its endian argument.
	 */
	public long readSignedLong(DataInputStream stream) throws IOException {
		return readSignedLong(stream, false, this);
	}

	/**
	 * One-argument version of <code>readUnsignedInt</code>. WAVE is always
	 * little-endian, so we can unambiguously drop its endian argument.
	 */
	public long readUnsignedInt(DataInputStream stream) throws IOException {
		return readUnsignedInt(stream, false, this);
	}

	/**
	 * One-argument version of <code>readSignedInt</code>. WAVE is always
	 * little-endian, so we can unambiguously drop its endian argument.
	 */
	public int readSignedInt(DataInputStream stream) throws IOException {
		return readSignedInt(stream, false, this);
	}

	/**
	 * One-argument version of <code>readUnsignedShort</code>. WAVE is always
	 * little-endian, so we can unambiguously drop its endian argument.
	 */
	public int readUnsignedShort(DataInputStream stream) throws IOException {
		return readUnsignedShort(stream, false, this);
	}

	/**
	 * One-argument version of <code>readSignedShort</code>. WAVE is always
	 * little-endian, so we can unambiguously drop its endian argument.
	 */
	public int readSignedShort(DataInputStream stream) throws IOException {
		return readSignedShort(stream, false, this);
	}

	/**
	 * Reads 4 bytes and concatenates them into a String. This pattern is used
	 * for ID's of various kinds.
	 */
	public String read4Chars(DataInputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder(4);
		for (int i = 0; i < 4; i++) {
			int ch = readUnsignedByte(stream, this);
			// Omit nulls
			if (ch != 0) {
				sb.append((char) ch);
			}
		}
		return sb.toString();
	}

	/** Sets the WAVE codec. */
	public void setWaveCodec(int value) {
		waveCodec = value;
	}

	/**
	 * Adds to the number of data bytes. This may be called multiple times to
	 * give a cumulative total.
	 */
	public void addSamples(long samples) {
		sampleCount += samples;
	}

	/** Sets the sample rate. */
	public void setSampleRate(long rate) {
		sampleRate = rate;
	}

	/** Sets the profile flag for PCMWAVEFORMAT. */
	public void setPCMWaveFormat(boolean b) {
		flagPCMWaveFormat = b;
	}

	/** Sets the profile flag for WAVEFORMATEX. */
	public void setWaveFormatEx(boolean b) {
		flagWaveFormatEx = b;
	}

	/** Sets the profile flag for WAVEFORMATEXTENSIBLE. */
	public void setWaveFormatExtensible(boolean b) {
		flagWaveFormatExtensible = b;
	}

	/** Initializes the state of the module for parsing. */
	@Override
	protected void initParse() {
		super.initParse();
		_propList = new LinkedList<>();
		_notes = new LinkedList<>();
		_labels = new LinkedList<>();
		_labeledText = new LinkedList<>();
		_samples = new LinkedList<>();
		firstSampleOffsetMarked = false;
		waveCodec = -1;
		sampleCount = 0;
		riffSize = 0;
		extendedRiffSize = 0;
		extendedSampleLength = 0;
		extendedChunkSizes = new HashMap<>();

		_metadata = new Property("WAVEMetadata", PropertyType.PROPERTY,
				PropertyArity.LIST, _propList);
		_aesMetadata = new AESAudioMetadata();
		_aesMetadata.setByteOrder(AESAudioMetadata.LITTLE_ENDIAN);
		_aesMetadata.setAnalogDigitalFlag("FILE_DIGITAL");
		_aesMetadata.setFormat(WAVE_FORM_TYPE);
		_aesMetadata.setUse("OTHER", "JHOVE_validation");
		_aesMetadata.setDirection("NONE");

		_propList.add(new Property("AESAudioMetadata",
				PropertyType.AESAUDIOMETADATA, _aesMetadata));

		// Most chunk types are allowed to occur only once,
		// and a few must occur exactly once.
		// Clear flags for whether they have been seen.
		formatChunkSeen = false;
		dataChunkSeen = false;
		dataSize64ChunkSeen = false;
		instrumentChunkSeen = false;
		cartChunkSeen = false;
		mpegChunkSeen = false;
		broadcastExtChunkSeen = false;
		peakChunkSeen = false;
		linkChunkSeen = false;
		cueChunkSeen = false;

		// Initialize profile flags
		flagPCMWaveFormat = false;
		flagWaveFormatEx = false;
		flagWaveFormatExtensible = false;
		flagRF64 = false;
	}

	/** Reads a WAVE chunk. */
	protected boolean readChunk(RepInfo info) throws IOException {

		Chunk chunk = null;
		ChunkHeader chunkh = new ChunkHeader(this, info);
		if (!chunkh.readHeader(_dstream)) {
			return false;
		}

		String chunkId = chunkh.getID();
		long chunkSize = chunkh.getSize();
		if (hasExtendedDataSizes() && chunkSize == LOOKUP_EXTENDED_DATA_SIZE) {
			Long extendedSize = extendedChunkSizes.get(chunkId);
			if (extendedSize != null) {
				chunkh.setSize(extendedSize);
				chunkSize = extendedSize;
			}
		}

		// Check if the chunk size is greater than the RIFF's remaining length
		if (Long.compareUnsigned(getBytesRemaining(), chunkSize) < 0) {
			info.setMessage(new ErrorMessage(
					MessageConstants.WAVE_HUL_6, _nByte - Chunk.SIZE_LENGTH));
			info.setWellFormed(false);
			return false;
		}

		if ("fmt ".equals(chunkId)) {
			if (formatChunkSeen) {
				dupChunkError(info, "Format");
			}
			chunk = new FormatChunk(this, chunkh, _dstream);
			formatChunkSeen = true;
		} else if ("data".equals(chunkId)) {
			if (!formatChunkSeen) {
				info.setMessage(new ErrorMessage(
						MessageConstants.WAVE_HUL_25, chunkh.getOffset()));
				info.setValid(false);
			}
			if (dataChunkSeen) {
				dupChunkError(info, "Data");
			}
			chunk = new DataChunk(this, chunkh, _dstream);
			dataChunkSeen = true;
		} else if ("ds64".equals(chunkId)) {
			chunk = new DataSize64Chunk(this, chunkh, _dstream);
			dataSize64ChunkSeen = true;
		} else if ("fact".equals(chunkId)) {
			chunk = new FactChunk(this, chunkh, _dstream);
			factChunkSeen = true;
			// Are multiple 'fact' chunks allowed?
		} else if ("note".equals(chunkId)) {
			chunk = new NoteChunk(this, chunkh, _dstream);
			// Multiple note chunks are allowed
		} else if ("labl".equals(chunkId)) {
			chunk = new LabelChunk(this, chunkh, _dstream);
			// Multiple label chunks are allowed
		} else if ("list".equals(chunkId)) {
			chunk = new AssocDataListChunk(this, chunkh, _dstream, info);
			// Are multiple chunks allowed? Who knows?
		} else if ("LIST".equals(chunkId)) {
			chunk = new ListInfoChunk(this, chunkh, _dstream, info);
			// Multiple list chunks must be OK, since there can
			// be different types, e.g., an INFO list and an exif list.
		} else if ("smpl".equals(chunkId)) {
			chunk = new SampleChunk(this, chunkh, _dstream);
			// Multiple sample chunks are allowed -- I think
		} else if ("inst".equals(chunkId)) {
			if (instrumentChunkSeen) {
				dupChunkError(info, "Instrument");
			}
			chunk = new InstrumentChunk(this, chunkh, _dstream);
			// Only one instrument chunk is allowed
			instrumentChunkSeen = true;
		} else if ("mext".equals(chunkId)) {
			if (mpegChunkSeen) {
				dupChunkError(info, "MPEG Audio Extension");
			}
			chunk = new MpegChunk(this, chunkh, _dstream);
			// I think only one MPEG chunk is allowed
			mpegChunkSeen = true;
		} else if ("cart".equals(chunkId)) {
			if (cartChunkSeen) {
				dupChunkError(info, "Cart");
			}
			chunk = new CartChunk(this, chunkh, _dstream);
			cartChunkSeen = true;
		} else if ("bext".equals(chunkId)) {
			if (broadcastExtChunkSeen) {
				dupChunkError(info, "Broadcast Audio Extension");
			}
			chunk = new BroadcastExtChunk(this, chunkh, _dstream);
			broadcastExtChunkSeen = true;
		} else if ("levl".equals(chunkId)) {
			if (peakChunkSeen) {
				dupChunkError(info, "Peak Envelope");
			}
			chunk = new PeakEnvelopeChunk(this, chunkh, _dstream);
			peakChunkSeen = true;
		} else if ("link".equals(chunkId)) {
			if (linkChunkSeen) {
				dupChunkError(info, "Link");
			}
			chunk = new LinkChunk(this, chunkh, _dstream);
			linkChunkSeen = true;
		} else if ("cue ".equals(chunkId)) {
			if (cueChunkSeen) {
				dupChunkError(info, "Cue Points");
			}
			chunk = new CueChunk(this, chunkh, _dstream);
			cueChunkSeen = true;
		} else {
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.WAVE_HUL_7.getId(),
					String.format(MessageConstants.WAVE_HUL_7.getMessage(),
							chunkId));
			info.setMessage(new InfoMessage(message, chunkh.getOffset()));
		}

		long dataRead = _nByte;
		if (chunk != null) {
			if (!chunk.readChunk(info)) {
				return false;
			}
		} else {
			// Other chunk types are legal, just skip over them
			skipBytes(_dstream, chunkSize, this);
		}
		dataRead = _nByte - dataRead;

		if (dataRead < chunkSize) {
			// The file has been truncated or there
			// remains unexpected chunk data to skip
			remainingDataInfo(_dstream, info, chunkSize - dataRead, chunkId);
		}

		if ((chunkSize & 1) != 0) {
			// Must come out to an even byte boundary
			skipBytes(_dstream, 1, this);
		}

		return true;
	}

	/**
	 * Reports and passes over any data still remaining following an attempt
	 * at reading a chunk. This ensures we align with the start of any
	 * subsequent chunk.
	 */
	private void remainingDataInfo(DataInputStream stream, RepInfo info,
								   long bytesToProcess, String chunkId)
			throws IOException {

		if (stream.available() > 0) {

			boolean nullData = true;
			long bytesProcessed = 0;

			// Check for non-null data
			while (nullData && bytesProcessed < bytesToProcess) {
				int b = readUnsignedByte(stream, this);
				if (b != 0) nullData = false;
				bytesProcessed++;
			}

			// Skip any remaining data
			bytesProcessed += skipBytes(stream,
					bytesToProcess - bytesProcessed, this);

			info.setMessage(new InfoMessage(
					MessageConstants.WAVE_HUL_26,
					String.format(MessageConstants.WAVE_HUL_26_SUB.getMessage(),
							chunkId, bytesProcessed, nullData),
					_nByte - bytesProcessed));

		} else {
			throw new EOFException(String.format(
					MessageConstants.WAVE_HUL_3_SUB_2.getMessage(), chunkId));
		}
	}

	/** Returns the number of RIFF bytes remaining to be read. */
	private long getBytesRemaining() {

		long totalBytes = Chunk.HEADER_LENGTH;

		if (hasExtendedDataSizes()) {
			totalBytes += extendedRiffSize;
		} else {
			totalBytes += riffSize;
		}

		return totalBytes - _nByte;
	}

	/** Returns the module's AES metadata. */
	public AESAudioMetadata getAESMetadata() {
		return _aesMetadata;
	}

	/** Reports a duplicate chunk. */
	protected void dupChunkError(RepInfo info, String chunkName) {
		JhoveMessage message = JhoveMessages.getMessageInstance(
				MessageConstants.WAVE_HUL_8.getId(), String.format(
						MessageConstants.WAVE_HUL_8.getMessage(), chunkName));
		info.setMessage(new ErrorMessage(
				message, _nByte - Chunk.HEADER_LENGTH));
		info.setValid(false);
	}

	/**
	 * General function for adding a property with a 32-bit value, with two
	 * arrays of Strings to interpret 0 and 1 values as a bitmask.
	 *
	 * @param val
	 *            The bitmask
	 * @param name
	 *            The name for the Property
	 * @param oneValueNames
	 *            Array of names to use for '1' values
	 * @param zeroValueNames
	 *            Array of names to use for '0' values
	 */
	public Property buildBitmaskProperty(int val, String name,
			String[] oneValueNames, String[] zeroValueNames) {
		if (_je != null && _je.getShowRawFlag()) {
			return new Property(name, PropertyType.INTEGER, val);
		}
		List<String> slist = new LinkedList<>();
		try {
			for (int i = 0; i < oneValueNames.length; i++) {
				String s;
				if ((val & (1 << i)) != 0) {
					s = oneValueNames[i];
				} else {
					s = zeroValueNames[i];
				}
				if (s != null && s.length() > 0) {
					slist.add(s);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return new Property(name, PropertyType.STRING, PropertyArity.LIST,
				slist);
	}

	/**
	 * Returns whether or not the module has parsed the chunks required to
	 * provide extended data sizes, namely RF64's Data Size 64 chunk.
	 */
	public boolean hasExtendedDataSizes() {
		return flagRF64 && dataSize64ChunkSeen;
	}

	/** Sets the extended RIFF size. */
	public void setExtendedRiffSize(long size) {
		extendedRiffSize = size;
	}

	/** Sets the extended sample length. */
	public void setExtendedSampleLength(long length) {
		extendedSampleLength = length;
	}

	/** Returns the extended sample length. */
	public long getExtendedSampleLength() {
		return extendedSampleLength;
	}

	/**
	 * Adds a chunk's extended chunk size to the map of extended sizes.
	 * If a chunk has previously been mapped, its chunk size will be replaced.
	 */
	public void addExtendedChunkSize(String chunkId, Long chunkSize) {
		extendedChunkSizes.put(chunkId, chunkSize);
	}
}
