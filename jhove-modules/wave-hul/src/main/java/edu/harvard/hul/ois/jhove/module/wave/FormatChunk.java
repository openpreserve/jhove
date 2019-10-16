/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.wave;

import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.module.WaveModule;
import edu.harvard.hul.ois.jhove.module.iff.Chunk;
import edu.harvard.hul.ois.jhove.module.iff.ChunkHeader;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Implementation of the WAVE Format Chunk.
 *
 * @author Gary McGath
 */
public class FormatChunk extends Chunk {

    /** Compression code for original Microsoft PCM */
    public final static int WAVE_FORMAT_PCM = 0x0001;

    /** Compression code for MPEG */
    public final static int WAVE_FORMAT_MPEG = 0x0050;

    /** Compression code for Microsoft Extensible Wave Format */
    public final static int WAVE_FORMAT_EXTENSIBLE = 0xFFFE;

    /** Chunk size for PCMWAVEFORMAT files */
    private final static int PCMWAVEFORMAT_LENGTH = 16;

    /** Minimum extra chunk bytes for WAVEFORMATEXTENSIBLE files */
    private final static int EXTRA_WAVEFORMATEXTENSIBLE_LENGTH = 22;

    /** Suffix given to GUIDs migrated from Format Tags */
    private final static String FORMAT_TAG_GUID_SUFFIX =
            "-0000-0010-8000-00AA00389B71";

    /** Table of losslessly compressed codecs */
    private final static int[] losslessCodecs = {
            0x0163,     // WMA lossless
            0x1971,     // Sonic foundry lossless
            0xF1AC      // FLAC
    };

    /**
     * Constructor.
     * 
     * @param module   The WaveModule under which this was called
     * @param hdr      The header for this chunk
     * @param dstrm    The stream from which the WAVE data are being read
     */
    public FormatChunk(
            WaveModule module,
            ChunkHeader hdr,
            DataInputStream dstrm) {
        super(module, hdr, dstrm);
    }

    /**
     * Reads a chunk and puts appropriate Properties into the RepInfo object.
     * 
     * @return   <code>false</code> if the chunk is structurally invalid,
     *           otherwise <code>true</code>
     */
    @Override
	public boolean readChunk(RepInfo info) throws IOException {

        WaveModule module = (WaveModule) _module;

        int validBitsPerSample = -1;
        byte[] extraBytes = null;
        String subformat = null;
        long channelMask = -1;

        int waveCodec = module.readUnsignedShort(_dstream);
        module.setWaveCodec(waveCodec);
        int numChannels = module.readUnsignedShort(_dstream);
        long sampleRate = module.readUnsignedInt(_dstream);
        module.setSampleRate(sampleRate);
        long bytesPerSecond = module.readUnsignedInt(_dstream);
        int blockAlign = module.readUnsignedShort(_dstream);
        module.setBlockAlign(blockAlign);
        int bitsPerSample = module.readUnsignedShort(_dstream);

        bytesLeft -= PCMWAVEFORMAT_LENGTH;

        if (bytesLeft > 0) {
            int extraFormatBytes = module.readUnsignedShort(_dstream);
            extraBytes = new byte[extraFormatBytes];
            if (waveCodec == WAVE_FORMAT_EXTENSIBLE
                    && extraFormatBytes >= EXTRA_WAVEFORMATEXTENSIBLE_LENGTH) {
                // This is -- or should be -- WAVEFORMATEXTENSIBLE.
                // Need to do some additional checks on profile satisfaction.
                boolean wfe = true;     // Accept tentatively
                // The next word may be valid bits per sample, samples
                // per block, or merely "reserved".  Which one it is
                // apparently depends on the compression format.  I really
                // can't figure out how to tell which it is without
                // exhaustively researching all compression formats.
                validBitsPerSample = module.readUnsignedShort(_dstream);
                channelMask = module.readUnsignedInt(_dstream);

                // The Subformat field is a Microsoft GUID
                byte[] guidBytes = new byte[16];
                ModuleBase.readByteBuf(_dstream, guidBytes, module);
                subformat = formatGUID(guidBytes);

                // If the Subformat GUID was generated from a Format Tag value,
                // extract the old Format Tag value from its GUID so we can
                // properly identify the codec and treat it the same way.
                if (subformat.endsWith(FORMAT_TAG_GUID_SUFFIX)) {
                    waveCodec = Integer.parseInt(subformat.substring(4, 8), 16);
                    module.setWaveCodec(waveCodec);
                }

                // Nitpicking profile requirements
                if ((((bitsPerSample + 7) / 8) * numChannels) != blockAlign) {
                    wfe = false;
                }
                if ((bitsPerSample % 8) != 0) {
                    // So why was that fancy ceiling arithmetic needed?
                    // So it can be the same calculation as with WaveFormatEx.
                    wfe = false;
                }
                if (validBitsPerSample > bitsPerSample) {
                    wfe = false;
                }
                if (wfe) {
                    module.setWaveFormatExtensible(true);
                }
            }
            else {
                if (waveCodec != WAVE_FORMAT_PCM ||
                    (((bitsPerSample + 7) / 8) * numChannels) == blockAlign) {
                    module.setWaveFormatEx(true);
                }
                ModuleBase.readByteBuf(_dstream, extraBytes, module);
            }
            
            // Possible pad to maintain even alignment
            if ((extraFormatBytes & 1) != 0) {
                _module.skipBytes(_dstream, 1, module);
            }
        }
        else {
            // No extra bytes signifies the PCM profile. In this case,
            // the wave codec also needs to be Microsoft PCM.
            if (waveCodec == WAVE_FORMAT_PCM &&
                    (((bitsPerSample + 7) / 8) * numChannels) == blockAlign) {
                module.setPCMWaveFormat(true);
            }
        }

        // Add extended MIME type information when available
        if (waveCodec != WAVE_FORMAT_EXTENSIBLE) {
            String codecID = Integer.toHexString(waveCodec).toUpperCase();
            String extendedMIMEType = _module.getMimeType()[0]
                    + "; codec=" + codecID;
            info.setMimeType(extendedMIMEType);
        }

        Property prop = module.addIntegerProperty("CompressionCode", waveCodec,
                    WaveStrings.COMPRESSION_FORMAT, WaveStrings.COMPRESSION_INDEX);
        module.addWaveProperty(prop);
        String compName = (String)prop.getValue();

        AESAudioMetadata aes = module.getAESMetadata();
        aes.setAudioDataEncoding(compName);
        aes.setNumChannels(numChannels);
        setChannelLocations(aes, numChannels);
        aes.setSampleRate(sampleRate);
        aes.setBitDepth(bitsPerSample);

        // Check which codecs are losslessly compressed
        String qual = "LOSSY";
        for (int losslessCodec : losslessCodecs) {
            if (waveCodec == losslessCodec) {
                qual = "CODE_REGENERATING";
            }
        }
        if (waveCodec == WAVE_FORMAT_PCM) {
            aes.clearBitrateReduction();
        }
        else {
            aes.setBitrateReduction(compName, "", "", "",
                qual, Long.toString(bytesPerSecond), "FIXED");
        }

        module.addWaveProperty(new Property("AverageBytesPerSecond",
                    PropertyType.LONG,
                    Long.valueOf(bytesPerSecond)));
        module.addWaveProperty(new Property("BlockAlign",
                    PropertyType.INTEGER,
                    Integer.valueOf(blockAlign)));
        if (extraBytes != null) {
            module.addWaveProperty(new Property("ExtraFormatBytes",
                    PropertyType.BYTE,
                    PropertyArity.ARRAY,
                    extraBytes));
        }
        if (validBitsPerSample != -1) {
            // Should this property be called something like
            // ValidBitsPersampleOrSamplesPerBlock?
            module.addWaveProperty(new Property("ValidBitsPerSample",
                    PropertyType.INTEGER,
                    Integer.valueOf(validBitsPerSample)));
        }
        if (channelMask != -1) {
            module.addWaveProperty(new Property("ChannelMask",
                    PropertyType.LONG,
                    Long.valueOf(channelMask)));
        }
        if (subformat != null) {
            module.addWaveProperty(new Property("Subformat",
                    PropertyType.STRING,
                    subformat));
        }

        return true;
    }

    /**
     * Set default channel assignments. This is fairly simple,
     * but it's helpful to keep the same structure as the equivalent
     * CommonChunk.setChannelLocations function.
     */
    private static void setChannelLocations(AESAudioMetadata aes, int numChannels) {

        String[] mapLoc = new String[numChannels];
        switch (numChannels) {
            case 2:
                mapLoc[0] = "LEFT";
                mapLoc[1] = "RIGHT";
                break;

            // If we get some other number of channels, punt.
            default:
                for (int i = 0; i < numChannels; i++) {
                    mapLoc[i] = "UNKNOWN";
                }
        }
        aes.setMapLocations(mapLoc);
    }

    /**
     * Returns the string representation of a Microsoft GUID.
     *
     * @param guidBytes  a 16-byte array with GUID values in little-endian order
     * @return           a String of the form 00000001-0000-0010-8000-00AA00389B71
     */
    private static String formatGUID(byte[] guidBytes) {

        StringBuilder guid = new StringBuilder(36);

        byte[] doubleWord = reverseBytes(Arrays.copyOf(guidBytes, 4));
        guid.append(DatatypeConverter.printHexBinary(doubleWord));
        guid.append("-");

        byte[] word = reverseBytes(Arrays.copyOfRange(guidBytes, 4, 6));
        guid.append(DatatypeConverter.printHexBinary(word));
        guid.append("-");

        word = reverseBytes(Arrays.copyOfRange(guidBytes, 6, 8));
        guid.append(DatatypeConverter.printHexBinary(word));
        guid.append("-");

        byte[] bytes = Arrays.copyOfRange(guidBytes, 8, 10);
        guid.append(DatatypeConverter.printHexBinary(bytes));
        guid.append("-");

        bytes = Arrays.copyOfRange(guidBytes, 10, 16);
        guid.append(DatatypeConverter.printHexBinary(bytes));

        return guid.toString();
    }

    /** Reverses a byte array in place */
    private static byte[] reverseBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length / 2; i++) {
            byte valueToSwap = bytes[i];
            bytes[i] = bytes[bytes.length - 1 - i];
            bytes[bytes.length - 1 - i] = valueToSwap;
        }
        return bytes;
    }
}
