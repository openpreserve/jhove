/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.wave;

import edu.harvard.hul.ois.jhove.ModuleBase;
import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyType;
import edu.harvard.hul.ois.jhove.RepInfo;
import edu.harvard.hul.ois.jhove.module.WaveModule;
import edu.harvard.hul.ois.jhove.module.iff.Chunk;
import edu.harvard.hul.ois.jhove.module.iff.ChunkHeader;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Implementation of the WAVE Fact Chunk.
 *
 * The Fact chunk contains information specific to the compression scheme.
 *
 * @author Gary McGath
 */
public class FactChunk extends Chunk {

    private final static int BASE_CHUNK_SIZE = 4;

    /**
     * Constructor.
     * 
     * @param module   The WaveModule under which this was called
     * @param hdr      The header for this chunk
     * @param dstrm    The stream from which the WAVE data are being read
     */
    public FactChunk(
            ModuleBase module,
            ChunkHeader hdr,
            DataInputStream dstrm) {
        super(module, hdr, dstrm);
    }

    /**
     * Reads a chunk and puts a Fact Property into the RepInfo object.
     *
     * @return   <code>false</code> if the chunk is structurally invalid,
     *           otherwise <code>true</code>
     */
    @Override
	public boolean readChunk(RepInfo info) throws IOException {

        WaveModule module = (WaveModule) _module;

        long sampleLength = module.readUnsignedInt(_dstream);
        if (module.hasExtendedDataSizes()
                && sampleLength == WaveModule.LOOKUP_EXTENDED_DATA_SIZE) {
            sampleLength = module.getExtendedSampleLength();
        }

        module.addSamples(sampleLength);
        bytesLeft -= BASE_CHUNK_SIZE;

        module.skipBytes(_dstream, bytesLeft, module);

        Property sizeProp = new Property("Size",
                PropertyType.LONG, Long.valueOf(chunkSize));
        module.addWaveProperty(new Property("Fact",
                PropertyType.PROPERTY, sizeProp));

        return true;
    }
}
