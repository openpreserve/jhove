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
import java.util.LinkedHashMap;
import java.util.Map;

import org.jwat.gzip.GzipEntry;


/**
 * A GZip file entry.
 * Based on the property elements from the GZIP module of JHOVE2.
 */
public class GzipEntryProperties {

    /** WARC record data container. */
    protected GzipEntryData data;
    /** The map for the properties of the GZIP entry.*/
    private Map<String, String> properties;

    /**
     * Construct GZip entry base property instance with the supplied data.
     * @param entry GZip entry data
     */
    public GzipEntryProperties(GzipEntry entry) {
        this.data = new GzipEntryData(entry);
    }
    
    /**
     * Retrieves the GZIP entry properties as a map.
     * @return A map of the properties of the GZIP entry.
     */
    public Map<String, String> getProperties() {
        properties = new LinkedHashMap<>();
        setProperty(Boolean.valueOf(data.isNonCompliant), "Is non compliant.");
        setProperty(Long.valueOf(data.offset), "Offset value.");
        setProperty(data.fileName, "GZip entry name.");
        setProperty(data.comment, "GZip entry comment.");
        setProperty(data.date, "GZip entry date.");
        setProperty(data.method.label, "GZip entry compression method.");
        setProperty(data.os.label, "GZip entry operating system.");
        setProperty(getCrc16(), "GZip entry header crc16.");
        setProperty("0x" + Integer.toHexString(data.readCrc32), "GZip entry crc32.");
        setProperty(Long.valueOf(data.readISize), "GZip entry extracted size (ISIZE) value.");
        setProperty(Long.valueOf(data.size), "GZip entry (computed) uncompressed size, in bytes.");
        setProperty(Long.valueOf(data.csize), "GZip entry (computed) compressed size, in bytes.");
        setProperty(getCompressionRatio(), "GZip entry (computed) compression ratio.");
        return properties;
    }

    /**
     * @return The crc16 element, converted into a HEX. Or null, if it not set.
     */
    private String getCrc16() {
    	String crc16;
    	if (data.readCrc16 != null) {
            crc16 = "0x" + Integer.toHexString(data.readCrc16.intValue() & 0xffff);
    	} else {
    		crc16 = null;
    	}
        return crc16;
    }

    /**
     * @return The calculated compressionRatio.
     */
    private String getCompressionRatio() {
        Double ratio = Double.valueOf(-1.0);
        long size  = data.size;
        long csize = data.csize;
        if ((size > 0L) && (csize > 0L)) {
            // Compute compression ratio with 2 decimals only.
            long l = ((size - csize) * 10000L) / size;
            ratio = Double.valueOf(l / 100.00);
        }
        return ratio.toString();
    }

    /**
     * Sets the given string property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(String variable, String description) {
        if(variable != null && !variable.isEmpty()) {
            properties.put(description, variable);
        }
    }

    /**
     * Sets the given long property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(Long variable, String description) {
        if(variable != null ) {
            properties.put(description, variable.toString());
        }
    }

    /**
     * Sets the given boolean property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(Boolean variable, String description) {
        if(variable != null) {
            properties.put(description, variable.toString());
        }
    }

    /**
     * Sets the given date property, if it has a valid value (not null and not empty).
     * @param variable The value for the property.
     * @param description The description and key of the property.
     */
    private void setProperty(Date variable, String description) {
        if(variable != null) {
            properties.put(description, variable.toString());
        }
    }
}
