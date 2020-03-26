/**********************************************************************
 * JHOVE - JSTOR/Harvard Object Validation Environment
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.wave;

import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.module.WaveModule;
import edu.harvard.hul.ois.jhove.module.iff.Chunk;
import edu.harvard.hul.ois.jhove.module.iff.ChunkHeader;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Implementation of the WAVE Data Size 64 chunk.
 *
 * Stores 64-bit data sizes for fields previously limited to 32-bit lengths.
 */
public class DataSize64Chunk extends Chunk {

    /** The combined length of all mandatory fields. */
    private final static int MINIMUM_CHUNK_LENGTH = 24;

    /**
     * Constructor.
     *
     * @param module   The WaveModule under which this was called
     * @param hdr      The header for this chunk
     * @param dstrm    The stream from which the WAVE data are being read
     */
    public DataSize64Chunk(
            ModuleBase module,
            ChunkHeader hdr,
            DataInputStream dstrm) {
        super(module, hdr, dstrm);
    }

    /**
     * Reads and extracts the file's 64-bit data sizes.
     *
     * @return  <code>false</code> if the chunk is structurally
     *          invalid, otherwise <code>true</code>
     */
    @Override
	public boolean readChunk(RepInfo info) throws IOException {

        WaveModule module = (WaveModule) _module;

        long riffSize = module.readSignedLong(_dstream);
        module.setExtendedRiffSize(riffSize);

        long dataSize = module.readSignedLong(_dstream);
        module.addExtendedChunkSize("data", dataSize);

        long sampleCount = module.readSignedLong(_dstream);
        module.setExtendedSampleLength(sampleCount);

        if (chunkSize > MINIMUM_CHUNK_LENGTH) {
            long tableSize = module.readUnsignedInt(_dstream);
            for (int i = 0; i < tableSize; i++) {
                String chunkId = module.read4Chars(_dstream);
                long chunkSize = module.readSignedLong(_dstream);
                module.addExtendedChunkSize(chunkId, chunkSize);
            }
        }

        return true;
    }
}
