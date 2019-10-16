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

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated type for GZip supported operating systems.
 */
public final class OperatingSystem {
    /** The list of valid values. */
    private static Map<Integer,OperatingSystem> values = new HashMap<>();

    /** The FAT filesystem (MS-DOS, OS/2, NT/Win32). */
    public static final OperatingSystem FAT_FILESYSTEM = new OperatingSystem(0, "FAT filesystem (MS-DOS, OS/2, NT/Win32)", true);
    /** Amiga. */
    public static final OperatingSystem AMIGA = new OperatingSystem(1, "Amiga", true);
    /** VMS (or OpenVMS). */
    public static final OperatingSystem VMS = new OperatingSystem(2, "VMS (or OpenVMS)", true);
    /** Unix. */
    public static final OperatingSystem UNIX = new OperatingSystem(3, "Unix", true);
    /** VM/CMS. */
    public static final OperatingSystem VM_CMS = new OperatingSystem(4, "VM/CMS", true);
    /** Atari TOS.*/
    public static final OperatingSystem ATARI_TOS = new OperatingSystem(5, "Atari TOS", true);
    /** HPFS filesystem (0S/2, NT). */
    public static final OperatingSystem HPFS = new OperatingSystem(6, "HPFS filesystem (OS/2, NT)", true);
    /** Macintosh. */
    public static final OperatingSystem MACINTOSH = new OperatingSystem(7, "Macintosh)", true);
    /** Z-System. */
    public static final OperatingSystem Z_SYSTEM = new OperatingSystem(8, "Z-System", true);
    /** CP/M. */
    public static final OperatingSystem CP_M = new OperatingSystem(9, "CP/M", true);
    /** TOPS-20. */
    public static final OperatingSystem TOPS_20 = new OperatingSystem(10, "TOPS-20", true);
    /** NTFS filesystem (NT). */
    public static final OperatingSystem NTFS = new OperatingSystem(11, "NTFS filesystem (NT)", true);
    /** QDOS. */
    public static final OperatingSystem QDOS = new OperatingSystem(12, "QDOS", true);
    /** Acorn RISCOS. */
    public static final OperatingSystem ACORN_RISCOS = new OperatingSystem(13, "Acorn RISCOS", true);
    /** Unknown. */
    public static final OperatingSystem UNKNOWN = new OperatingSystem(255, "Unknown", true);
    
    /**
     * Initializes the valid values.
     */
    static {
        values.put(Integer.valueOf(FAT_FILESYSTEM.value), FAT_FILESYSTEM);
        values.put(Integer.valueOf(AMIGA.value), AMIGA);
        values.put(Integer.valueOf(VMS.value), VMS);
        values.put(Integer.valueOf(UNIX.value), UNIX);
        values.put(Integer.valueOf(VM_CMS.value), VM_CMS);
        values.put(Integer.valueOf(ATARI_TOS.value), ATARI_TOS);
        values.put(Integer.valueOf(HPFS.value), HPFS);
        values.put(Integer.valueOf(MACINTOSH.value), MACINTOSH);
        values.put(Integer.valueOf(Z_SYSTEM.value), Z_SYSTEM);
        values.put(Integer.valueOf(CP_M.value), CP_M);
        values.put(Integer.valueOf(TOPS_20.value), TOPS_20);
        values.put(Integer.valueOf(NTFS.value), NTFS);
        values.put(Integer.valueOf(QDOS.value), QDOS);
        values.put(Integer.valueOf(ACORN_RISCOS.value), ACORN_RISCOS);
        values.put(Integer.valueOf(UNKNOWN.value), UNKNOWN);
    }
    
    /** The integer value for the enum instance. */
    public final int value;
    /** The value description. */
    public final String label;
    /** Whether the value is valid. */
    public final boolean valid;
    
    /**  
     * Constructor.
     * @param value The compression type value.
     * @param label The name of the compression type.
     * @param valid If it is a valid compression type.
     */
    protected OperatingSystem(int value, String label, boolean valid) {
        this.value = value;
        this.label = label;
        this.valid = valid;    }

    /**
     * Returns the enumerated value object corresponding to the
     * specified integer value. If the integer value is unknown, an
     * instance marked as not valid is returned.
     * @param  n   the integer value to map.
     *
     * @return a operating system object, valid if <code>n</code> is
     *         one of the defined valid values.
     */
    public static OperatingSystem fromValue(int n) {
        OperatingSystem v = values.get(Integer.valueOf(n));
        if (v == null) {
            v = new OperatingSystem(n, null, false);
        }
        return v;
    }

}
