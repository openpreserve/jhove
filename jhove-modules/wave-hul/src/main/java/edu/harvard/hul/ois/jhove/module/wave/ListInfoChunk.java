/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.wave;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

import edu.harvard.hul.ois.jhove.*;
import edu.harvard.hul.ois.jhove.messages.JhoveMessage;
import edu.harvard.hul.ois.jhove.messages.JhoveMessages;
import edu.harvard.hul.ois.jhove.module.WaveModule;
import edu.harvard.hul.ois.jhove.module.iff.*;

/**
 * Implementation of the WAVE LIST chunk.
 * 
 * Two chunk types, 'exif' and 'INFO', are supported;
 * other list types will be reported as unknown
 * and treated as an error.
 *
 * @author Gary McGath
 */
public class ListInfoChunk extends Superchunk {

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
	public ListInfoChunk(ModuleBase module, ChunkHeader hdr,
			DataInputStream dstrm, RepInfo info) {
		super(module, hdr, dstrm, info);
	}

	/**
	 * Reads a chunk and puts appropriate information into
	 * the RepInfo object.
	 * 
	 * @return <code>false</code> if the chunk is structurally
	 *         invalid, otherwise <code>true</code>
	 */
	@Override
	public boolean readChunk(RepInfo info) throws IOException {
		String typeID = ((WaveModule) _module).read4Chars(_dstream);
		bytesLeft -= TYPE_LENGTH;
		if ("INFO".equals(typeID)) {
			return readInfoChunk(info);
		} else if ("exif".equals(typeID)) {
			return readExifChunk(info);
		} else if ("adtl".equals(typeID)) {
			return readAdtlChunk(info);
		} else {
			// Skip unrecognized list types
			JhoveMessage message = JhoveMessages.getMessageInstance(
					MessageConstants.WAVE_HUL_15.getId(), String.format(
							MessageConstants.WAVE_HUL_15.getMessage(), typeID));
			info.setMessage(new InfoMessage(message, chunkOffset));
			_module.skipBytes(_dstream, bytesLeft, _module);
			return true;
		}
	}

	private boolean readInfoChunk(RepInfo info) throws IOException {
		List listInfoProps = new LinkedList();
		WaveModule module = (WaveModule) _module;
		// The set of subchunks is somewhat
		// open-ended, but apparently all are identical in format, consisting
		// of a null-terminated string. These are subsumed under
		// ListInfoTextChunk. We accumulate them into a List of Properties.
		for (;;) {
			ChunkHeader chunkh = getNextChunkHeader();
			if (chunkh == null) {
				break;
			}
			Chunk chunk = null;
			int chunkSize = (int) chunkh.getSize();
			chunk = new ListInfoTextChunk(_module, chunkh, _dstream,
					listInfoProps, this);

			if (!chunk.readChunk(info)) {
				return false;
			}
			if ((chunkSize & 1) != 0) {
				// Must come out to an even byte boundary
				_module.skipBytes(_dstream, 1, _module);
				--bytesLeft;
			}
		}
		if (!listInfoProps.isEmpty()) {
			module.addListInfo(listInfoProps);
		}
		return true;
	}

	/**
	 * The Exif chunk, unlike the Info chunk, has subchunks which aren't
	 * homogeneous.
	 */
	private boolean readExifChunk(RepInfo info) throws IOException {
		WaveModule module = (WaveModule) _module;
		module.setExifInfo(new ExifInfo());
		for (;;) {
			ChunkHeader chunkh = getNextChunkHeader();
			if (chunkh == null) {
				break;
			}
			Chunk chunk = null;
			String id = chunkh.getID();
			int chunkSize = (int) chunkh.getSize();

			if ("ever".equals(id)) {
				chunk = new ExifVersionChunk(_module, chunkh, _dstream);
			} else if ("erel".equals(id) || "etim".equals(id)
					|| "ecor".equals(id) || "emdl".equals(id)) {
				chunk = new ExifStringChunk(_module, chunkh, _dstream);
			} else if ("emnt".equals(id)) {

			} else if ("eucm".equals(id)) {

			}
			if (chunk == null) {
				_module.skipBytes(_dstream, chunkSize, _module);
				JhoveMessage message = JhoveMessages.getMessageInstance(
						MessageConstants.WAVE_HUL_17.getId(), String.format(
								MessageConstants.WAVE_HUL_17.getMessage(), id));
				info.setMessage(new InfoMessage(message));
			} else {
				if (!chunk.readChunk(info)) {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Reads the chunk and its nested chunks, and puts appropriate
	 * properties into the RepInfo object.
	 *
	 * @return <code>false</code> if the chunk or a nested chunk
	 *         is structurally
	 *         invalid, otherwise <code>true</code>
	 */
	public boolean readAdtlChunk(RepInfo info) throws IOException {
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
						MessageConstants.WAVE_HUL_18.getId(), String.format(
								MessageConstants.WAVE_HUL_18.getMessage(), id));
				info.setMessage(new InfoMessage(message));
			} else {
				if (!chunk.readChunk(info)) {
					return false;
				}
			}
			if ((chunkSize & 1) != 0) {
				// Must come out to an even byte boundary
				_module.skipBytes(_dstream, 1, _module);
				--bytesLeft;
			}
		}
		return true;
	}
}
