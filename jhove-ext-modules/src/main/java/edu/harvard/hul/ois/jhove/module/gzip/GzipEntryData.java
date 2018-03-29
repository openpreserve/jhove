/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package edu.harvard.hul.ois.jhove.module.gzip;

import java.util.Date;

import org.jwat.gzip.GzipEntry;

/**
 * This class is a wrapper for the information available in a GZip entry.
 * Since the GZip reader is not persistent its data must be moved to a simpler
 * data class which can be persisted instead.
 *
 * @author nicl
 */
public class GzipEntryData {

	/** Boolean indicating whether header is non compliant. */
	protected boolean isNonCompliant;
    /** Offset of entry in input stream. */
    protected long offset;
    /** Compression methods read from header. */
    protected CompressionMethod method;
    /** Compression type read from extra flags in header. */
    protected CompressionType extraFlags;
    /** Optional filename from header. */
    protected String fileName;
    /** Operating System from header converted to a field. */
    protected OperatingSystem os;
    /** Optional comment from header. */
    protected String comment;
    /** Ascii bit set in header. */
    protected boolean asciiFlag;
    /** Optional CRC16 read from header. */
    protected Integer readCrc16;
    /** CRC16 computed as verification to optional value found in header. */
    protected int computedCrc16;

    /** Date read from header. */
    protected Date date;

    /** Uncompressed size of entry data. */
    protected long size  = -1L;
    /** Compressed size of entry data. */
    protected long csize = -1L;
    /** ISIZE read from trailing header. */
    protected long readISize = -1L;
    /** ISIZE computed as verification to value found in trailing header. */
    protected long computedISize = -1;
    /** CRC32 read from trailing header. */
    protected int readCrc32;
    /** CRC32 computed as verification to value found in trailing header. */
    protected int computedCrc32;

    /**
     * Constructor required by the persistence layer.
     */
    public GzipEntryData() {
    }

    /**
     * Given a GZip entry, transfer the data to this object so it can be
     * persisted.
     * @param entry GZip entry data
     */
    public GzipEntryData(GzipEntry entry) {
    	if (entry == null) {
    		throw new IllegalArgumentException("'entry' should never be null");
    	}
    	this.isNonCompliant = !entry.isCompliant();
    	this.offset = entry.getStartOffset();
    	this.method = CompressionMethod.fromValue(entry.cm);
    	this.date = entry.date;
    	this.extraFlags = CompressionType.fromValue(entry.xfl);
    	this.fileName = entry.fname;
    	this.os = OperatingSystem.fromValue(entry.os);
    	this.comment = entry.fcomment;
    	this.asciiFlag = entry.bFText;
    	this.readCrc16 = entry.crc16;
    	this.computedCrc16 = entry.comp_crc16;
    	this.csize = entry.compressed_size;
    	this.size = entry.uncompressed_size;
    	this.readCrc32 = entry.crc32;
    	this.computedCrc32 = entry.comp_crc32;
    	this.readISize = entry.isize;
    	this.computedISize = entry.comp_isize;
    }
}
