/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.wave;

import java.io.DataInputStream;
import java.io.IOException;

import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.WaveModule;
import edu.harvard.hul.ois.jhove.module.iff.*;

/**
 * The associated data list ('list') chunk, which is different from
 * the RIFF 'LIST' chunk, ListInfoTextChunk. It can contain
 * several different types of informational chunks.
 *
 * @author Gary McGath
 */
public class AssocDataListChunk extends Superchunk {

	private static final int TYPE_LENGTH = 4;

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            The WaveModule under which this was called
	 * @param hdr
	 *            The header for this chunk
	 * @param dstrm
	 *            The stream from which the WAVE data are being read
	 * @param info
	 *            RepInfo object for error reporting
	 */
	public AssocDataListChunk(ModuleBase module, ChunkHeader hdr,
			DataInputStream dstrm, RepInfo info) {
		super(module, hdr, dstrm, info);
	}

	/**
	 * Reads the chunk and its nested chunks, and puts appropriate
	 * properties into the RepInfo object.
	 * 
	 * @return <code>false</code> if the chunk or a nested chunk
	 *         is structurally
	 *         invalid, otherwise <code>true</code>
	 */
	@Override
	public boolean readChunk(RepInfo info) throws IOException {
		WaveModule module = (WaveModule) _module;

		// The chunk has a type ID, which is always "adtl". Presumably
		// this was intended to allow other list structures (don't ask
		// why), but any others will be considered non-conforming.
		String typeID = module.read4Chars(_dstream);
		bytesLeft -= TYPE_LENGTH;
		if (!"adtl".equals(typeID)) {
			info.setMessage(new ErrorMessage(MessageConstants.WAVE_HUL_9,
					String.format(MessageConstants.WAVE_HUL_9_SUB.getMessage(),
							typeID),
					_module.getNByte() - TYPE_LENGTH));
			info.setWellFormed(false);
			return false;
		}
		for (;;) {
			ChunkHeader chunkh = getNextChunkHeader();
			if (chunkh == null) {
				break;
			}
			Chunk chunk = null;
			// The chunk list can include Labels, Notes, and
			// Labelled Text.
			String id = chunkh.getID();
			int chunkSize = (int) chunkh.getSize();
			if ("labl".equals(id)) {
				chunk = new LabelChunk(_module, chunkh, _dstream);
			} else if ("note".equals(id)) {
				chunk = new NoteChunk(_module, chunkh, _dstream);
			} else if ("ltxt".equals(id)) {
				chunk = new LabeledTextChunk(_module, chunkh, _dstream);
			}

			if (chunk == null) {
				_module.skipBytes(_dstream, chunkSize, _module);
				JhoveMessage message = JhoveMessages.getMessageInstance(
						MessageConstants.WAVE_HUL_10.getId(), String.format(
								MessageConstants.WAVE_HUL_10.getMessage(), id));
				info.setMessage(new InfoMessage(message));
			} else {
				if (!chunk.readChunk(info)) {
					return false;
				}
			}
		}
		return true;
	}
}
