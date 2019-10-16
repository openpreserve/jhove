/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.wave;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import edu.harvard.hul.ois.jhove.AESAudioMetadata;
import edu.harvard.hul.ois.jhove.InfoMessage;
import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.WaveModule;
import edu.harvard.hul.ois.jhove.module.iff.Chunk;
import edu.harvard.hul.ois.jhove.module.iff.ChunkHeader;

/**
 * Implementation of the WAVE Broadcast Audio Extension Chunk.
 *
 * @author Gary McGath
 */
public class BroadcastExtChunk extends Chunk {

	private final static int BASE_CHUNK_SIZE = 602;
	private final static int VER_0_RESERVED_LENGTH = 254;
	private final static int VER_1_RESERVED_LENGTH = 190;
	private final static int VER_2_RESERVED_LENGTH = 180;
	private final static int UNUSED_LOUDNESS_FIELD = 0x7FFF;

	/**
	 * Constructor.
	 *
	 * @param module
	 *            The WaveModule under which this was called
	 * @param hdr
	 *            The header for this chunk
	 * @param dstrm
	 *            The stream from which the WAVE data are being read
	 */
	public BroadcastExtChunk(ModuleBase module, ChunkHeader hdr,
			DataInputStream dstrm) {
		super(module, hdr, dstrm);
	}

	/**
	 * Reads a chunk and puts a BroadcastAudioExtension Property into
	 * the RepInfo object.
	 *
	 * @return <code>false</code> if the chunk is structurally
	 *         invalid, otherwise <code>true</code>
	 */
	@Override
	public boolean readChunk(RepInfo info) throws IOException {

		WaveModule module = (WaveModule) _module;

		byte[] buf256 = new byte[256];
		ModuleBase.readByteBuf(_dstream, buf256, module);
		String description = byteBufString(buf256);

		byte[] buf32 = new byte[32];
		ModuleBase.readByteBuf(_dstream, buf32, module);
		String originator = byteBufString(buf32);

		ModuleBase.readByteBuf(_dstream, buf32, module);
		String originatorRef = byteBufString(buf32);

		byte[] buf10 = new byte[10];
		ModuleBase.readByteBuf(_dstream, buf10, module);
		String originationDate = byteBufString(buf10);

		byte[] buf8 = new byte[8];
		ModuleBase.readByteBuf(_dstream, buf8, module);
		String originationTime = byteBufString(buf8);

		long timeReference = module.readSignedLong(_dstream);
		int version = module.readUnsignedShort(_dstream);

		String umid = "";
		if (version >= 1) {
			byte[] buf64 = new byte[64];
			ModuleBase.readByteBuf(_dstream, buf64, module);
			umid = formatUmid(buf64);
		}

		String loudnessValue = "";
		String loudnessRange = "";
		String maxTruePeakLevel = "";
		String maxMomentaryLoudness = "";
		String maxShortTermLoudness = "";
		if (version >= 2) {
			loudnessValue = formatLoudness(module.readSignedShort(_dstream));
			loudnessRange = formatLoudness(module.readSignedShort(_dstream));
			maxTruePeakLevel = formatLoudness(module.readSignedShort(_dstream));
			maxMomentaryLoudness = formatLoudness(
					module.readSignedShort(_dstream));
			maxShortTermLoudness = formatLoudness(
					module.readSignedShort(_dstream));
		}

		if (version == 0) {
			module.skipBytes(_dstream, VER_0_RESERVED_LENGTH, module);
		} else if (version == 1) {
			module.skipBytes(_dstream, VER_1_RESERVED_LENGTH, module);
		} else if (version == 2) {
			module.skipBytes(_dstream, VER_2_RESERVED_LENGTH, module);
		} else {
			// If it's a higher version, we can't read its fields,
			// so skip any remaining reserved bytes anyway.
			module.skipBytes(_dstream, VER_2_RESERVED_LENGTH, module);
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.WAVE_HUL_27.getId(),
					String.format(MessageConstants.WAVE_HUL_27.getMessage(),
							Integer.valueOf(version)));
			info.setMessage(new InfoMessage(message));
		}

		String codingHistory = "";
		int codingHistorySize = (int) chunkSize - BASE_CHUNK_SIZE;
		if (codingHistorySize > 0) {
			byte[] bufCodingHistory = new byte[codingHistorySize];
			ModuleBase.readByteBuf(_dstream, bufCodingHistory, module);
			codingHistory = byteBufString(bufCodingHistory);
		}

		// Whew -- we've read the whole thing. Now make that into a
		// list of Properties.
		List<Property> plist = new ArrayList<>(14);

		if (!description.isEmpty()) {
			plist.add(new Property("Description", PropertyType.STRING,
					description));
		}
		if (!originator.isEmpty()) {
			plist.add(new Property("Originator", PropertyType.STRING,
					originator));
		}
		if (!originatorRef.isEmpty()) {
			plist.add(new Property("OriginatorReference", PropertyType.STRING,
					originatorRef));
		}
		if (!originationDate.isEmpty()) {
			plist.add(new Property("OriginationDate", PropertyType.STRING,
					originationDate));
		}
		if (!originationTime.isEmpty()) {
			plist.add(new Property("OriginationTime", PropertyType.STRING,
					originationTime));
		}

		plist.add(new Property("TimeReference", PropertyType.LONG,
				Long.valueOf(timeReference)));
		plist.add(new Property("Version", PropertyType.INTEGER,
				Integer.valueOf(version)));

		if (!umid.isEmpty()) {
			plist.add(new Property("UMID", PropertyType.STRING, umid));
		}

		if (!loudnessValue.isEmpty()) {
			plist.add(new Property("LoudnessValue", PropertyType.STRING,
					loudnessValue));
		}
		if (!loudnessRange.isEmpty()) {
			plist.add(new Property("LoudnessRange", PropertyType.STRING,
					loudnessRange));
		}
		if (!maxTruePeakLevel.isEmpty()) {
			plist.add(new Property("MaxTruePeakLevel", PropertyType.STRING,
					maxTruePeakLevel));
		}
		if (!maxMomentaryLoudness.isEmpty()) {
			plist.add(new Property("MaxMomentaryLoudness", PropertyType.STRING,
					maxMomentaryLoudness));
		}
		if (!maxShortTermLoudness.isEmpty()) {
			plist.add(new Property("MaxShortTermLoudness", PropertyType.STRING,
					maxShortTermLoudness));
		}

		if (!codingHistory.isEmpty()) {
			plist.add(new Property("CodingHistory", PropertyType.STRING,
					codingHistory));
		}

		module.addWaveProperty(new Property("BroadcastAudioExtension",
				PropertyType.PROPERTY, PropertyArity.LIST, plist));

		AESAudioMetadata aes = module.getAESMetadata();
		aes.setStartTime(timeReference);

		return true;
	}

	/**
	 * Returns a UMID formatted as a hexadecimal string.
	 *
	 * @return an empty String if no UMID is found; a basic UMID if the
	 *         extension space is empty; and an extended UMID if it isn't
	 */
	private static String formatUmid(byte[] umid) {

		String formattedUmid = "";

		byte[] basicUmid = Arrays.copyOfRange(umid, 0, 32);
		byte[] umidExtension = Arrays.copyOfRange(umid, 32, 64);

		boolean basicUmidExists = hasValue(basicUmid);
		boolean extendedUmidExists = false;

		if (basicUmidExists) {
			extendedUmidExists = hasValue(umidExtension);
		}

		if (extendedUmidExists) {
			formattedUmid = DatatypeConverter.printHexBinary(umid);
		} else if (basicUmidExists) {
			formattedUmid = DatatypeConverter.printHexBinary(basicUmid);
		}

		return formattedUmid;
	}

	/** Checks for a non-zero value in an array of bytes. */
	private static boolean hasValue(byte[] byteArray) {

		for (byte b : byteArray) {
			if (b != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a properly formatted loudness value.
	 *
	 * Loudness fields store integer values accurate to two decimal places by
	 * multiplying the original value by 100, and rounding away any remainder.
	 * To recover the original value, we do the reverse.
	 *
	 * Unused fields should contain the value 0x7FFF.
	 */
	private static String formatLoudness(int value) {

		String formattedValue = "";

		if (value != UNUSED_LOUDNESS_FIELD) {
			formattedValue = String.format("%.2f", Float.valueOf(value / 100f));
		}

		return formattedValue;
	}
}
